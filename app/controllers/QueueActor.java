package controllers;

import model.Navigation;
import akka.actor.UntypedActorWithStash;
import akka.japi.Procedure;

public class QueueActor extends UntypedActorWithStash {

	@Override
	public void onReceive(Object message) throws Exception {
		try {
			if (message instanceof Navigation) {
				// We receive incomming message
				Navigation nav = (Navigation) message;
				System.out.println("Received navigation object! "
						+ nav.toString());
				// We are putting message back to the queue
				stash();
			} else if (message.equals("getAll")) {
				// When we get appropriate signal, we are going to process them
				// all
				System.out.println("Unstashing messages");
				unstashAll();
				getContext().become(new Procedure<Object>() {
					@Override
					public void apply(Object message2) throws Exception {
						if (message2 instanceof Navigation) {
							Navigation nav = (Navigation) message2;
							System.out
									.println("Ready to process navigation object! "
											+ nav.toString());
						} else if (message2.equals("stopProcessing")) {
							getContext().unbecome();
						}
					}
				});
			} else {
				System.out.println("Unable to handle received message");
				unhandled(message);
			}

		} catch (Throwable th) {
			th.printStackTrace();
		}
	}

}
