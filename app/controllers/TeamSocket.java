package controllers;

import java.util.LinkedList;

import models.UsersConnected;
import play.libs.F;
import play.libs.F.Callback0;
import play.mvc.WebSocket;

public class TeamSocket extends WebSocket<String> {

	private UsersConnected list;
	private play.mvc.WebSocket.Out<String> out=null;

	public TeamSocket(UsersConnected list) {
		super();
		this.list = list;
	}

	@Override
	public void onReady(final play.mvc.WebSocket.In<String> in,
			final play.mvc.WebSocket.Out<String> out) {
		// For each event received on the socket,
		
        in.onMessage(new F.Callback<String>() {
            public void invoke(String event) {
                //out.write("I accepted your message: "+event);
            	sendUsersToSocket(out);
            }
        });

        // When the socket is closed.
        in.onClose(new Callback0() {
			@Override
			public void invoke() throws Throwable {
				TeamActor.disconnect(TeamSocket.this);
				TeamSocket.this.out=null;
			}
		});
        
        this.out = out;
	}
	
	public void sendUsers() {
		if (out != null){
			sendUsersToSocket(out);
		}
	}

	private void sendUsersToSocket(play.mvc.WebSocket.Out<String> out) {
		out.write("All users already connected:");
        for (String name : list.usersAlready()){
        	out.write(name);
        }
	}

}
