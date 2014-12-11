package controllers;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
public class TimeConfiguration {
	private int rate = 600;
	private int start_delay = 1000;
	
	@JsonProperty("rate")
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	
	@JsonProperty("start_delay")
	public int getStart_delay() {
		return start_delay;
	}
	public void setStart_delay(int start_delay) {
		this.start_delay = start_delay;
	}
}
