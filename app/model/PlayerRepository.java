package model;

import java.util.LinkedList;
import java.util.List;

public class PlayerRepository {

	private List<Player> players = new LinkedList(); 

	public List connectNewPlayer( Player player){
		players.add(player);
		
		return players;
	}
	
	public List getConnectedPlayers(){
		return players;
	}
	
	public void disconnectPlayer(Player player){
		players.remove(player);
	}

}
