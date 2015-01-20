package model;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Klasa do zarządzania torpedami na mapie
 */
public class TorpedoRepository {
    private LinkedList<Torpedo> torpedoes = new LinkedList<Torpedo>();

    public TorpedoRepository() {}

    public LinkedList<Torpedo> getTorpedoes() {
        return torpedoes;
    }

    public void addTorpedo(Torpedo torpedo) {
        System.out.println("Added torpedo x=" + torpedo.getX() + " y=" + torpedo.getY() + " dx=" + torpedo.getDeltaX() + " dy=" + torpedo.getDeltaY());
        torpedoes.add(torpedo);
    }

    public void update() {
        for(Torpedo torpedo: torpedoes)
            torpedo.move();
    }

    public void removeExplodedTorpedoes() {
        for(Iterator<Torpedo> iterator = torpedoes.iterator(); iterator.hasNext();) {
            Torpedo torpedo = iterator.next();
            if(!torpedo.isMoving())
                iterator.remove();
        }
    }
}
