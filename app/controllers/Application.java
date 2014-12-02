package controllers;

import play.mvc.*;

public class Application extends Controller {
    
    public static Result websocket() {
        return ok(views.html.websocket.render());
    }

}
