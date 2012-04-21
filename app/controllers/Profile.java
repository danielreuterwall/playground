package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import views.html.profile;

/**
 * Manage projects related operations.
 */
@Security.Authenticated(Secured.class)
public class Profile extends Controller {

	/**
	 * Display the dashboard.
	 */
	public static Result index() {
		return ok(profile.render());
	}
}
