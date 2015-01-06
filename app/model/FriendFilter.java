package model;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FriendFilter {
	
	private Player my;
	private List<Player> all_players;
	private double intervals;
	private double radar_range;

	enum Filter{
		FRIENDS,ENEMIES;
	}

	public FriendFilter(Player my, List<Player> all, double intervals, double radar_range){
		this.my = my;
		this.all_players = all;
		this.intervals = intervals;
		this.radar_range = radar_range;
	}
	
	@JsonProperty("type")
	public String getType(){
		return "position";
	}
	
	@JsonProperty("intervals")
	public double getIntervals(){
		return  intervals;
	}
	
	@JsonProperty("friendly")
	public List<Player> friendly(){
		return extract(Filter.FRIENDS);
	}
	
	@JsonProperty("enemy")
	public List<Player> enemy(){
		List<Player> enemies = extract(Filter.ENEMIES);
		return filterRange(enemies, radar_range);
	}
	
	@JsonProperty("my")
	public MovingObject my(){
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
	
	private <T extends MovingObject> List<T> filterRange(List<T> objects, double range){
		LinkedList<T> accepted = new LinkedList<T>();
		for (T pickedup : objects) {
			double dx = pickedup.getX()-this.my.getX();
			double dy = pickedup.getY()-this.my.getY();
			System.out.println(String.format("%s, %s - %s",dx,dy,range));
			if (Math.sqrt((dx*dx)+(dy*dy))<=range) {
				accepted.add(pickedup);
			}
		}
		return accepted;
	}
}
