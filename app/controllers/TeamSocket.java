package controllers;

import java.util.List;

import model.Board;
import model.FriendFilter;
import model.Navigation;
import model.Player;
import model.Torpedo;
import play.libs.F;
import play.libs.F.Callback0;
import play.libs.Json;
import play.mvc.WebSocket;
import akka.actor.Inbox;

import com.fasterxml.jackson.databind.JsonNode;

public class TeamSocket extends WebSocket<String> {

	private play.mvc.WebSocket.Out<String> out = null;
	private Player my;
	private GameController game;

	public TeamSocket(GameController game, Player associated) {
		this.game = game;
		this.my = associated;
	}

	@Override
	public void onReady(final play.mvc.WebSocket.In<String> in,
			final play.mvc.WebSocket.Out<String> out) {
		// For each event received on the socket,
		in.onMessage(new F.Callback<String>() {
			public void invoke(String event) {
				try {
					processIncommingMessage(out, event);
				} catch (Throwable th) {
					th.printStackTrace();
				}
			}
		});

		// When the socket is closed.
		in.onClose(new Callback0() {
			@Override
			public void invoke() throws Throwable {
				System.out.println("User disconnected");
				game.disconnect(TeamSocket.this);
				game.getTorpedoRepository().removeUserTorpedoes(TeamSocket.this.my.getNick());
				TeamSocket.this.out.close();
				TeamSocket.this.out = null;
			}
		});

		this.sendInitMsg(out, game.getBoard());
		this.out = out;
	}

	public void sendUsers() {
		if (out != null) {
			sendUsersToSocket(out);
		}
	}

	private void sendUsersToSocket(play.mvc.WebSocket.Out<String> out) {
		double radar_range = game.getBoard().getRadarRadius();
		List<Torpedo> torpedos = game.getTorpedoRepository().getTorpedoes();
		FriendFilter filter = new FriendFilter(my, game.getPlayers(), torpedos, game.getIntervals(), radar_range);
		out.write(Json.toJson(filter).toString());
	}

	private void sendInitMsg(play.mvc.WebSocket.Out<String> out, Board board) {
		if (out != null) {
			out.write(Json.toJson(board).toString());
		}
	}

	private Navigation assemblyNavigationObject(JsonNode node) {
		Navigation nav = Json.fromJson(node, Navigation.class);
		Player p = game.searchPlayer(nav.getUser_nick());
		nav.setPlayer(p);
		return nav;
	}

	private void processIncommingMessage(final play.mvc.WebSocket.Out<String> out, String event) {
		JsonNode node = Json.parse(event);
		JsonNode type = node.get("type");
		if ((!type.isNull())&&type.toString()
				.equalsIgnoreCase("\"navigation\"")) {
			Navigation nav = assemblyNavigationObject(node);
			if (nav.getPlayer() != null){
				Inbox in = Inbox.create(game.getSystem());
				in.send(game.getQueue(), nav);
			} else {
				throw new RuntimeException("Backend received invalid user name in navigation object");
			}
		} else {
			sendUsersToSocket(out);
		}
	}

}
