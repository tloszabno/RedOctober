package models;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class UsersConnected {
	private LinkedList<HashMap<String,String>> users;
	
	public UsersConnected() {
		users = new LinkedList<HashMap<String,String>>();
	}
	
	public LinkedList<HashMap<String,String>> connectNew(HashMap<String,String> user){
		users.add(user);
		return users;
	}

	public List<HashMap<String,String>> usersAlready() {
		return users;
	}

	public void disconnect(HashMap<String,String> user) {
		users.remove(user);
	}
}
