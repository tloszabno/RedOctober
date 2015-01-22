package model;

import java.util.List;


public class CollisionDetector {
	
	private GameObjectRepository repository;
	
	public CollisionDetector(GameObjectRepository repository) {
		this.repository = repository;
	}
	
	public void detectCollisions() {
		List<MovingObject> torpedos = repository.getTorpedos();
		List<MovingObject> players = repository.getPlayers();
		List<Rock> rocks = repository.getRocks();
		
		for(int i=0 ; i<torpedos.size() ; i++) {
			
			for(int j=0 ; j<rocks.size() ; j++) {
				double distance = Math.sqrt((torpedos.get(i).getX()-rocks.get(j).getX()) * (torpedos.get(i).getX()-rocks.get(j).getX()) + (torpedos.get(i).getY()-rocks.get(j).getY()) * (torpedos.get(i).getY()-rocks.get(j).getY())  ) ;			
				if( distance <= rocks.get(j).getSizeRadius() + + torpedos.get(i).getSizeRadius())
					((Torpedo)torpedos.get(i)).explode();
			}
			
			for(int j=0 ; j<players.size() ; j++) {
				double distance = Math.sqrt((torpedos.get(i).getX()-players.get(j).getX()) * (torpedos.get(i).getX()-players.get(j).getX()) + (torpedos.get(i).getY()-players.get(j).getY()) * (torpedos.get(i).getY()-players.get(j).getY())  ) ;			
				if( distance <= players.get(j).getSizeRadius() + torpedos.get(i).getSizeRadius() ) { //poprawic player radius
					((Player)players.get(j)).shot();
					players.remove(players.get(j));
					j--;
				}
					
			}	
		}
		
		
		for(int i=0 ; i<players.size() ; i++) {
			
			
			for(int j=0 ; j<rocks.size() ; j++) {
				double distance = Math.sqrt((players.get(i).getX()-rocks.get(j).getX()) * (players.get(i).getX()-rocks.get(j).getX()) + (players.get(i).getY()-rocks.get(j).getY()) * (players.get(i).getY()-rocks.get(j).getY())  ) ;			
				if( distance <= players.get(i).getSizeRadius() + rocks.get(j).getSizeRadius() ) { //jaki jest radius gracza??
					((Player)players.get(i)).shot();
					players.remove(players.get(i));
					break;
				}
			}
			
			for(int k=i+1 ; k<players.size() ; k++) {
				double distance = Math.sqrt((players.get(i).getX()-players.get(k).getX()) * (players.get(i).getX()-players.get(k).getX()) + (players.get(i).getY()-players.get(k).getY()) * (players.get(i).getY()-players.get(k).getY())  ) ;			
				if( distance <= 2 * players.get(k).getSizeRadius()) { //poprawic 5.0
					((Player)players.get(i)).shot();
					((Player)players.get(k)).shot();
					players.remove(players.get(i));
					players.remove(players.get(k));
					i--;
					break;
					
				}
			}
			
		}
		
		
		
	}
}
