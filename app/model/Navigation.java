package model;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
public class Navigation{
	
	private String user_nick;
	private Double current_x;
	private Double current_y;
	private Double next_x;
	private Double next_y;
	private Double current_velocity;
	private Player player;
	
	public Navigation(){
		
	}
	
	@JsonIgnore
	public void setPlayer(Player player){
		this.player = player;
	}
	
	@JsonIgnore
	public Player getPlayer(){
		return player;
	}
	
	@JsonProperty("user_nick")
	public String getUser_nick() {
		return user_nick;
	}
	
	@JsonProperty("user_nick")
	public void setUser_nick(String user_nick) {
		this.user_nick = user_nick;
	}

	@JsonProperty("current_x")
	public Double getCurrent_x() {
		return current_x;
	}

	@JsonProperty("current_x")
	public void setCurrent_x(Double current_x) {
		this.current_x = current_x;
	}

	@JsonProperty("current_y")
	public Double getCurrent_y() {
		return current_y;
	}

	@JsonProperty("current_y")
	public void setCurrent_y(Double current_y) {
		this.current_y = current_y;
	}

	@JsonProperty("next_x")
	public Double getNext_x() {
		return next_x;
	}

	@JsonProperty("next_x")
	public void setNext_x(Double next_x) {
		this.next_x = next_x;
	}

	@JsonProperty("next_y")
	public Double getNext_y() {
		return next_y;
	}

	@JsonProperty("next_y")
	public void setNext_y(Double next_y) {
		this.next_y = next_y;
	}

	@JsonProperty("current_velocity")
	public Double getCurrent_velocity() {
		return current_velocity;
	}

	@JsonProperty("current_velocity")
	public void setCurrent_velocity(Double current_velocity) {
		this.current_velocity = current_velocity;
	}
	
	@JsonProperty("type")
	public void setType(String type) {
		type = type.replaceAll("\"", "");
		if (!type.equalsIgnoreCase("navigation")){
			String message = String.format("Expected JSON object of type \"navigation\" not \"%s\"",type);
			throw new RuntimeException(message);
		}
	}
	
	@JsonProperty("type")
	public String getType() {
		return "navigation";
	}
	
	@Override
	public String toString(){
		return "nick:"+user_nick+" next_x:"+next_x+" next_y:"+next_y;
	}
}
