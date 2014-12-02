package controllers;

import java.util.concurrent.ConcurrentLinkedQueue;

import model.Navigation;
import akka.actor.UntypedActorWithStash;

public class QueueActor extends UntypedActorWithStash {

	private ConcurrentLinkedQueue<Navigation> queue;

	public QueueActor(ConcurrentLinkedQueue<Navigation> queue){
		this.queue = queue;
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		try {
			if (message instanceof Navigation) {
				Navigation navigation = (Navigation) message;
				queue.add(navigation);
			} else {
				System.out.println("Unable to handle received message");
				unhandled(message);
			}

		} catch (Throwable th) {
			th.printStackTrace();
		}
	}

}
