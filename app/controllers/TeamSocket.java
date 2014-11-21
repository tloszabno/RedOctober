package controllers;

import java.util.LinkedList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.UsersConnected;
import play.libs.F;
import play.libs.F.Callback0;
import play.libs.Json;
import play.mvc.WebSocket;

public class TeamSocket extends WebSocket<JsonNode> {

	private UsersConnected list;
	private play.mvc.WebSocket.Out<JsonNode> out=null;

	public TeamSocket(UsersConnected list) {
		super();
		this.list = list;
	}

	@Override
	public void onReady(final play.mvc.WebSocket.In<JsonNode> in,
			final play.mvc.WebSocket.Out<JsonNode> out) {
		// For each event received on the socket,
		
        in.onMessage(new F.Callback<JsonNode>() {
            public void invoke(JsonNode event) {
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

	private void sendUsersToSocket(play.mvc.WebSocket.Out<JsonNode> out) {
        ObjectNode result = Json.newObject();
        result.put("messageType", "log");
        result.put("text", "All users already connected");

        out.write(result);
        out.write(Json.toJson(list.usersAlready()));

	}

}
