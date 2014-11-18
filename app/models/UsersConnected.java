package models;

import java.util.LinkedList;
import java.util.List;

public class UsersConnected {
	private LinkedList<String> users;
	
	public UsersConnected() {
		users = new LinkedList<String>();
	}
	
	public LinkedList<String> connectNew(String name){
		users.add(name);
		return users;
	}

	public List<String> usersAlready() {
		return users;
	}

	public void disconnect(String user) {
		users.remove(user);
	}
}
