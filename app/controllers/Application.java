package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
    public static Result websocket() {
        return ok(views.html.websocket.render());
    }
	
	public static Result testParameters(String name, String team) {
		return ok(index.render("passing:"+name+":"+team));
	}

}
