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
		controller.getTorpedoRepository().update(); // przesuwanie torped po mapie
		controller.broadcast();
		controller.getTorpedoRepository().removeExplodedTorpedoes();
		/*
			Jeśli kogoś zastanawia czemu update -> broadcast -> removeExplodedTorpedoes
			Update - przesuwamy po mapie torpedy, może się zdarzyć, że któraś wybuchnie, ale nie możemy jej usunąć z pamięci, bo trzeba wysłać tę informację wszystkim
			Broadcast - rozsyłamy informacje o wszystkim, w tym o torpedach, które eksplodowały
			RemoveExplodedTorpedoes - możemy z czystym sumieniem usunąć torpedy
		 */
	}

	private void processNavigation(Navigation navigation) {
 		Player player =	navigation.getPlayer(); // jesteśmy pewni, że tu nie będzie nigdy nulla?
		player.setxPosition(navigation.getNext_x());
		player.setyPosition((navigation.getNext_y()));
		if(navigation.getTorpedo() != null) {
			// player wystrzelił torpedę
			controller.getTorpedoRepository().addTorpedo(navigation.getTorpedo());
		}
	}

}
