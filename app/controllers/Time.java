package controllers;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import model.Navigation;
import model.Player;

public class Time extends TimerTask{

private TimeConfiguration config;

private ConcurrentLinkedQueue<Navigation> queue;
private GameController controller;

	public Time(ConcurrentLinkedQueue<Navigation> queue, GameController controller, 
			TimeConfiguration config){
		this.queue = queue;
		this.controller = controller;
		this.config = config;
	}
	
	public void startTimer(){
		Timer t = new Timer();
		t.scheduleAtFixedRate(this, config.getStart_delay(), config.getRate());
	}

	@Override
	public void run() {
		while(!queue.isEmpty()){
			Navigation item = queue.remove();
			//System.out.println("Processing:"+item.toString());
            processNavigation(item);
		}
		controller.broadcast();
	}

	private void processNavigation(Navigation navigation) {
		Player player =	navigation.getPlayer(); // jesteśmy pewni, że tu nie będzie nigdy nulla?
		player.setxPosition(navigation.getNext_x());
		player.setyPosition((navigation.getNext_y()));
		//TODO Collisions, torpedoes, explosions
	}

}
