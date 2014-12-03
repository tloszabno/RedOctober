package model;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FriendFilter {
	
	private Player my;
	private List<Player> all_players;
	
	enum Filter{
		FRIENDS,ENEMIES;
	}
	
	public FriendFilter(Player my, List<Player> all){
		this.my = my;
		this.all_players = all;
	}
	
	@JsonProperty("type")
	public String getType(){
		return "position";
	}
	
	@JsonProperty("intervals")
	public double getIntervals(){
		return  0.2;
	}
	
	@JsonProperty("friendly")
	public List<Player> friendly(){
		return extract(Filter.FRIENDS);
	}
	
	@JsonProperty("enemy")
	public List<Player> enemy(){
		return extract(Filter.ENEMIES);
	}
	
	@JsonProperty("my")
	public Player my(){
		return my;
	}

	private List<Player> extract(Filter filter) {
		List<Player> result = new LinkedList<Player>();
		for (Player p : all_players){
			boolean isInMyTeam = p.getTeam().equalsIgnoreCase(my.getTeam());
			boolean isNotMy = !p.equals(my);
			boolean wantToGetFriends = filter==Filter.FRIENDS;
			if ((wantToGetFriends == isInMyTeam)&&isNotMy){
				result.add(p);
			}
		}
		return result;
	}
}
