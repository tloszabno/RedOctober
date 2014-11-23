package controllers;

import java.util.HashMap;
import java.util.Map;

import model.Board;
import model.Player;
import model.PlayerRepository;
import play.mvc.WebSocket;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class TeamActor extends UntypedActor {

    private final ActorRef out;
    private static final PlayerRepository players;
    private static final Map<TeamSocket, Player> sockets;
    private static final Board board;
    
    static {
        players = new PlayerRepository();
        sockets = new HashMap<TeamSocket, Player>();
        board = new Board();
    }

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
		Player player = new Player(name,team,board.getRandomX(),board.getRandomY());
		System.out.println("connecting:" + player);
		players.connectNewPlayer(player);
	    TeamSocket teamSocket = new TeamSocket(players,player,board);
		sockets.put(teamSocket, player);
	    broadcast();
		return teamSocket;
	}

	public static void disconnect(TeamSocket teamSocket) {
		Player player = sockets.get(teamSocket);
		sockets.remove(teamSocket);
		System.out.println("disconnecting:" + player);
		players.disconnectPlayer(player);
		broadcast();
	}
	
	private static void broadcast(){
		for (TeamSocket sock: sockets.keySet()){
			sock.sendUsers();
		}
	}
}
