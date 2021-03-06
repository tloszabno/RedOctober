package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.fasterxml.jackson.databind.JsonNode;
import model.*;
import model.Board;
import model.MovingObject;
import model.Navigation;
import model.Player;
import model.PlayerRepository;
import model.Torpedo;
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
	private static Map<String, Integer> score = new ConcurrentHashMap<String, Integer>();


	private TorpedoRepository torpedoRepository = new TorpedoRepository();
    
	public GameController(){
        system=ActorSystem.create("RedOctober");
        concurrentQueue = new ConcurrentLinkedQueue<Navigation>();
		setPlayers(new PlayerRepository());
        setSockets(new ConcurrentHashMap<TeamSocket, Player>());
        setBoard(new Board());
        setQueue(getSystem().actorOf(Props.create(QueueActor.class,concurrentQueue), "queue"));
        timeconfig = new TimeConfiguration();
		Time time = new Time(concurrentQueue,this,timeconfig);
        time.startTimer();
	}
	
	public void disconnect(TeamSocket teamSocket) {
		MovingObject player = sockets.get(teamSocket);
		sockets.remove(teamSocket);
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

	public void updateScoreByKillerPlayer(Player killed, Player killer){
		// punkty zdobywa drużyna killera
		if( killed.getNick().equals(killer.getNick()) ){
			return;
		}

		if( killed.getTeam().equals(killer.getTeam()) ){
			return;
		}

		String team = killer.getTeam();
		Integer teamScore = this.score.get(team);
		if( teamScore != null ){
			this.score.put(team, teamScore + 1 );
		}else{
			this.score.put(team, 1 );
		}
	}

	public static Map<String, Integer> getScore() {
		return score;
	}
}
