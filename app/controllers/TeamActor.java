package controllers;

import model.Player;
import play.mvc.WebSocket;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class TeamActor extends UntypedActor {

	private final ActorRef out;
	private static GameController game = new GameController();

    public TeamActor(ActorRef out) {
        this.out = out;
    }

	public static Props props(ActorRef out) {
        return Props.create(TeamActor.class, out);
    }
	
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
            out.tell("I received your message: " + message, self());
        }
	}

	public void postStop() throws Exception {
	    //someResource.close();
	}
	
	public static WebSocket<String> socket(String name, String team) {
		Player player = new Player(name,team,game.getBoard().getRandomX(),game.getBoard().getRandomY(), 0.0, 0.0);
		System.out.println("connecting:" + player);
		game.getPlayerRepository().connectNewPlayer(player);
	    TeamSocket teamSocket = new TeamSocket(game,player);
		game.getSockets().put(teamSocket, player);
	    game.broadcast();
		return teamSocket;
	}

}
