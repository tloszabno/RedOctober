package model;

import java.util.LinkedList;
import java.util.List;

public class PlayerRepository {

	private List<Player> players = new LinkedList<Player>(); 

	public List<Player> connectNewPlayer( Player player){
		players.add(player);
		
		return players;
	}
	
	public List<Player> getConnectedPlayers(){
		return players;
	}
	
	public void disconnectPlayer(Player player){
		players.remove(player);
	}

}
