import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "playground"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
	    // Guice
	    "com.google.inject" % "guice" % "3.0",
	    "com.google.inject.extensions" % "guice-assistedinject" % "3.0",
	    "com.google.inject.extensions" % "guice-multibindings" % "3.0",
	    "com.google.inject.extensions" % "guice-throwingproviders" % "3.0",

	    // Morphia
	    "com.google.code.morphia" % "morphia" % "0.99.1-SNAPSHOT", // checkout Morphia manually and execute 'mvn install'
	    "com.google.code.morphia" % "morphia-logging-slf4j" % "0.99"
	)

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
    	// Add your own project settings here      
    	resolvers += "Morphia Repository" at "http://morphia.googlecode.com/svn/mavenrepo/"
    )

}
