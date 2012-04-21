package controllers;

import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.login;

public class Application extends Controller {

	public static Result index() {
		return ok(index.render("Your new application is ready."));
	}

	public static Result login() {
		return ok(login.render(form(Login.class)));
	}

	// -- Authentication

	public static class Login {

		public String mail;
		public String password;

		public String validate() {
			if (User.authenticate(mail, password) == null) {
				return "Invalid user or password";
			}
			return null;
		}
	}

	public static Result authenticate() {
		Form<Login> loginForm = form(Login.class).bindFromRequest();
		if (loginForm.hasErrors()) {
			return badRequest(login.render(loginForm));
		} else {
			session("mail", loginForm.get().mail);
			return redirect(routes.Profile.index());
		}
	}

	public static Result logout() {
	    session().clear();
        return redirect(
            routes.Application.index()
        );
    }
}