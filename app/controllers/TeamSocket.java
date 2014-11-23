package controllers;

import model.Board;
import model.PlayerRepository;
import play.libs.F;
import play.libs.F.Callback0;
import play.libs.Json;
import play.mvc.WebSocket;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class TeamSocket extends WebSocket<String> {

	private PlayerRepository list;
	private play.mvc.WebSocket.Out<String> out=null;
	private Board board;
	private boolean invitationSent = false; 

	public TeamSocket(PlayerRepository players, Board board) {
		super();
		this.list = players;
		this.board = board;
	}

	@Override
	public void onReady(final play.mvc.WebSocket.In<String> in,
			final play.mvc.WebSocket.Out<String> out) {
		// For each event received on the socket,
		
        in.onMessage(new F.Callback<String>() {
            public void invoke(String event) {
                //out.write("I accepted your message: "+event);
                //JsonNode node = Json.parse(event);
                System.out.println(Json.parse(event).path("text"));
                //System.out.println("message:" + node.at("text").textValue());
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
        
        
        this.sendInitMsg(out, board);
        this.out = out;
	}
	
	public void sendUsers() {
		if (out != null){
			sendUsersToSocket(out);
		}
	}

	private void sendUsersToSocket(play.mvc.WebSocket.Out<String> out) {
        ObjectNode result = Json.newObject();
        result.put("messageType", "log");
        result.put("text", "All users already connected");
        out.write(result.toString());
        ObjectNode players = Json.newObject();
        players.put("players", Json.toJson(list.getConnectedPlayers()));
        out.write(players.toString());
	}

	private void sendInitMsg(play.mvc.WebSocket.Out<String> out, Board board) {
		if (out != null){
			ObjectNode result = Json.newObject();
	        result.put("board", Json.toJson(board));
			out.write(result.toString());
		}
	}

}
