package controllers;

import play.mvc.*;



public class Application extends Controller {
    
    public static Result websocket() {
        return ok(views.html.websocket.render());
    }

    public static Result map(String name, String team) {
        return ok(views.html.map.render(name, team));
    }


}
