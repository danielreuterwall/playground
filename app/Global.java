import java.util.List;
import java.util.Set;

import models.User;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.api.mvc.Controller;

import com.google.code.morphia.logging.MorphiaLoggerFactory;
import com.google.code.morphia.logging.slf4j.SLF4JLogrImplFactory;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;

public class Global extends GlobalSettings {
	private final List<Module> modules = Lists.newArrayList();

	private Injector injector;

	static {
		MorphiaLoggerFactory.reset();
		MorphiaLoggerFactory.registerLogger(SLF4JLogrImplFactory.class);
	}

	@Override
	public void beforeStart(final Application application) {
		final Reflections reflections = new Reflections(
				new ConfigurationBuilder().addUrls(
						ClasspathHelper.forPackage("modules",
								application.classloader())).addScanners(
						new SubTypesScanner(), new TypeAnnotationsScanner()));

		Set<Class<? extends AbstractModule>> guiceModules = reflections
				.getSubTypesOf(AbstractModule.class);
		for (Class<? extends Module> moduleClass : guiceModules) {
			try {
				if (!moduleClass.isAnonymousClass()) {
					modules.add(moduleClass.newInstance());
				}
			} catch (InstantiationException e) {
				throw Throwables.propagate(e);
			} catch (IllegalAccessException e) {
				throw Throwables.propagate(e);
			}
		}

		modules.add(new AbstractModule() {
			@Override
			protected void configure() {
				bind(Application.class).toInstance(application);
				bind(Reflections.class).toInstance(reflections);

				for (Class<? extends Controller> controller : reflections
						.getSubTypesOf(Controller.class)) {
					requestStaticInjection(controller);
				}
			}
		});
	}

	public void onStart(Application app) {
		Logger.info("Creating injector with " + modules.size() + " modules.");
		injector = Guice.createInjector(Stage.PRODUCTION, modules);

		InitialData.insert(app);
	}

	static class InitialData {

		public static void insert(Application app) {
			if (User.find.asList().isEmpty()) {
				User testUser = new User("playground", "playground");
				testUser.save();
				Logger.info("Initialized test data");
			}
		}

	}

}