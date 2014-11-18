package controllers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import models.UsersConnected;
import play.mvc.Result;
import play.mvc.WebSocket;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import play.libs.F.*;
import views.html.index;

public class TeamActor extends UntypedActor {

    private final ActorRef out;
    private static final UsersConnected players;
    private static final Map<TeamSocket, String> sockets;
    
    static {
        players = new UsersConnected();
        sockets = new HashMap<TeamSocket, String>();
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
		String user = name+":"+team;
		System.out.println("connecting:" + user);
		players.connectNew(user);
	    TeamSocket teamSocket = new TeamSocket(players);
		sockets.put(teamSocket, user);
	    broadcast();
		return teamSocket;
	}

	public static void disconnect(TeamSocket teamSocket) {
		String user = sockets.get(teamSocket);
		sockets.remove(teamSocket);
		System.out.println("disconnecting:" + user);
		players.disconnect(user);
		broadcast();
	}
	
	private static void broadcast(){
		for (TeamSocket sock: sockets.keySet()){
			sock.sendUsers();
		}
	}
}
