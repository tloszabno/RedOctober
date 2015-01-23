package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class GameObjectRepository {


	// wydaje mi sie potrzebne bedzie np. pobranie tylko playerow lub tylko torped, wiec
	//zrobilem 3 listy + czwarta zawierajaca wszystko
	private List<GameObject> allObjects;
	private List<Rock> rocks;
	private List<MovingObject> torpedos;
	private List<MovingObject> players;
	
	public GameObjectRepository() {
		allObjects = new ArrayList<GameObject>();
		rocks = new ArrayList<Rock>();
		torpedos = new ArrayList<MovingObject>();
		players = new ArrayList<MovingObject>();
	}

	public List<GameObject> getAllObjects() {
		return allObjects;
	}

	public List<Rock> getRocks() {
		return rocks;
	}

	public List<MovingObject> getTorpedos() {
		return torpedos;
	}

	public List<MovingObject> getPlayers() {
		return players;
	}
	
	public void addObject(GameObject object) {
		allObjects.add(object);
		
		if( object instanceof Rock )
			rocks.add((Rock) object);
		
		if( object instanceof Torpedo )
			torpedos.add((Torpedo) object);
		
		if( object instanceof Player )
			players.add((Player) object);
	}
	
	public void removeObject(GameObject object) {
		allObjects.remove(object);
		
		if( object instanceof Rock )
			rocks.remove((Rock) object);
		
		if( object instanceof Torpedo )
			torpedos.remove((Torpedo) object);
		
		if( object instanceof Player )
			players.remove((MovingObject) object);
	}
	
	public void update() {
        for(MovingObject torpedo: torpedos)
            torpedo.move();

        for(MovingObject player: players)
        	player.move();
    }

    public void removeExplodedTorpedoes() {
        for(Iterator<MovingObject> iterator = torpedos.iterator(); iterator.hasNext();) {
        	MovingObject torpedo = iterator.next();
            if(!((Torpedo)torpedo).isMoving())
                iterator.remove();
        }
    }
}









