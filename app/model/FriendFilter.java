package model;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FriendFilter {
	
	private Player my;
	private List<Player> all_players;
	private double intervals;
	private double radar_range;
	private List<Torpedo> torpedoes;

	enum Filter{
		FRIENDS,ENEMIES;
	}

	public FriendFilter(Player my, List<Player> all, List<Torpedo> torpedos, double intervals, double radar_range){
		this.my = my;
		this.all_players = all;
		this.intervals = intervals;
		this.radar_range = radar_range;
		this.torpedoes = torpedos;
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
	
	@JsonProperty("torpedoes")
	public List<Torpedo> torpedoes(){
		LinkedList<Torpedo> accepted = new LinkedList<Torpedo>();
		for (Torpedo t : torpedoes) {
			Player striker = findPlayerByName(t.getUserNick());
			boolean isFriend = classifyPlayer(striker, Filter.FRIENDS);
			if (isFriend){
				accepted.add(t);
			} else if (isNear(t, radar_range)){
				accepted.add(t);
			}
		}
		return accepted;
	}
	
	@JsonProperty("shots")
	public List<ShotInfo> getShots(){
		return CollisionDetector.getShotInfos();
	}

	private Player findPlayerByName(String userNick) {
		for (Player p : all_players) {
			if (p.getNick().equalsIgnoreCase(userNick)) {
				return p;
			}
		}
		return null;
	}

	private List<Player> extract(Filter filter) {
		List<Player> result = new LinkedList<Player>();
		for (Player p : all_players){
			if (classifyPlayer(p, filter)){
				result.add(p);
			}
		}
		return result;
	}

	private boolean classifyPlayer(Player p, Filter filter) {
		boolean isInMyTeam = p.getTeam().equalsIgnoreCase(my.getTeam());
		boolean isNotMy = !p.equals(my);
		boolean wantToGetFriends = filter==Filter.FRIENDS;
		boolean classify = (wantToGetFriends == isInMyTeam)&&isNotMy;
		return classify;
	}
	
	private <T extends MovingObject> List<T> filterRange(List<T> objects, double range){
		LinkedList<T> accepted = new LinkedList<T>();
		for (T pickedup : objects) {
			boolean is_near = isNear(pickedup, range);
			if (is_near) {
				accepted.add(pickedup);
			}
		}
		return accepted;
	}

	private boolean isNear(MovingObject pickedup, double range) {
		double dx = pickedup.getX()-this.my.getX();
		double dy = pickedup.getY()-this.my.getY();
		//System.out.println(String.format("%s, %s - %s",dx,dy,range));
		boolean is_near = Math.sqrt((dx*dx)+(dy*dy))<=range;
		return is_near;
	}
}
