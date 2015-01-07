package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.fasterxml.jackson.databind.JsonNode;
import model.*;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import play.libs.Json;

public class GameController {
	
	private ActorSystem system;
	private ActorRef queue;
    private PlayerRepository players;
    private Map<TeamSocket, Player> sockets;
    private Board board;
    private ConcurrentLinkedQueue<Navigation> concurrentQueue;
    private TimeConfiguration timeconfig;

	private TorpedoRepository torpedoRepository = new TorpedoRepository();
    
	public GameController(){
		system=ActorSystem.create("RedOctober");
        concurrentQueue = new ConcurrentLinkedQueue<Navigation>();
		setPlayers(new PlayerRepository());
        setSockets(new HashMap<TeamSocket, Player>());
        setBoard(new Board());
        setQueue(getSystem().actorOf(Props.create(QueueActor.class,concurrentQueue), "queue"));
        timeconfig = new TimeConfiguration();
		Time time = new Time(concurrentQueue,this,timeconfig);
        time.startTimer();
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

	public PlayerRepository getPlayerRepository() {
		return players;
	}

	private void setPlayers(PlayerRepository players) {
		this.players = players;
	}

	public Map<TeamSocket, Player> getSockets() {
		return sockets;
	}

	private void setSockets(Map<TeamSocket, Player> sockets) {
		this.sockets = sockets;
	}

	public Board getBoard() {
		return board;
	}

	private void setBoard(Board board) {
		this.board = board;
	}

	public ActorSystem getSystem() {
		return system;
	}

	public TorpedoRepository getTorpedoRepository() {
		return torpedoRepository;
	}

	public ActorRef getQueue() {
		return queue;
	}

	private void setQueue(ActorRef queue) {
		this.queue = queue;
	}

	public List<Player> getPlayers() {
		return this.players.getConnectedPlayers();
	}

	public Player searchPlayer(String user_nick) {
		for (Player p : this.getPlayers()){
			if (p.getNick().equalsIgnoreCase(user_nick)){
				return p;
			}
		}
		return null;
	}

	public double getIntervals() {
		return timeconfig.getRate()/1000.0;
	}

}
