package controllers;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import model.Navigation;

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
			System.out.println("Processing:"+item.toString());
			//TODO Change boat position
		}
		controller.broadcast();
	}

}
