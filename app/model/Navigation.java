package model;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
public class Navigation{
	
	private String user_nick;
	private Double current_x;
	private Double current_y;
	private Double x_prim;
	private Double y_prim;
	private Player player;
	private Torpedo torpedo; // zwykle jest nullem, nie jest nullem gdy gracz wystrzeli torpedę
	
	public Navigation(){
	}
	
	@JsonIgnore
	public void setPlayer(Player player) {
		this.player = player;
		if(torpedo != null)
			torpedo.setUserNick(getUser_nick());
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

	@JsonProperty("x_prim")
	public Double getXPrim() {
		return x_prim;
	}

	@JsonProperty("x_prim")
	public void setXPrim(Double next_x) {
		this.x_prim = next_x;
	}

	@JsonProperty("y_prim")
	public Double getYPrim() {
		return y_prim;
	}

	@JsonProperty("y_prim")
	public void setYPrim(Double next_y) {
		this.y_prim = next_y;
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
		return "nick:"+user_nick+" x_prim:"+x_prim+" y_prim:"+y_prim;
	}
	
	public Double getNext_x() {
		return current_x+x_prim;
	}
	
	public Double getNext_y() {
		return current_y+y_prim;
	}

	@JsonProperty("launched_torpedo")
	public Torpedo getTorpedo() {
		return torpedo;
	}
	@JsonProperty("launched_torpedo")
	public void setTorpedo(TorpedoAdapter torpedo) {
		this.torpedo = torpedo.getTorpedo();
	}
}
