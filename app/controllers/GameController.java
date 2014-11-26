package controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import model.Board;
import model.Player;
import model.PlayerRepository;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;

public class GameController {
	
	private ActorSystem system;
	private ActorRef queue;
    private PlayerRepository players;
    private Map<TeamSocket, Player> sockets;
    private Board board;
    
	public GameController(){
		setPlayers(new PlayerRepository());
        setSockets(new HashMap<TeamSocket, Player>());
        setBoard(new Board());
        system=ActorSystem.create("RedOctober");
        setQueue(getSystem().actorOf(Props.create(QueueActor.class), "queue"));    
        startTimer();
	}

	private void startTimer() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Inbox in = Inbox.create(getSystem());
				in.send(getQueue(), "getAll");
				try {
					Thread.sleep(200);
					in.send(getQueue(), "stopProcessing");
				} catch (InterruptedException ex){
					ex.printStackTrace();
				}
			}
		}, 2000, 1000);
	}
	
	public void disconnect(TeamSocket teamSocket) {
		Player player = sockets.get(teamSocket);
		sockets.remove(teamSocket);
		System.out.println("disconnecting:" + player);
		players.disconnectPlayer(player);
		broadcast();
	}
	
	public void broadcast(){
		for (TeamSocket sock: sockets.keySet()){
			sock.sendUsers();
		}
	}

	public PlayerRepository getPlayers() {
		return players;
	}

	public void setPlayers(PlayerRepository players) {
		this.players = players;
	}

	public Map<TeamSocket, Player> getSockets() {
		return sockets;
	}

	public void setSockets(Map<TeamSocket, Player> sockets) {
		this.sockets = sockets;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public ActorSystem getSystem() {
		return system;
	}

	public ActorRef getQueue() {
		return queue;
	}

	public void setQueue(ActorRef queue) {
		this.queue = queue;
	}

}
