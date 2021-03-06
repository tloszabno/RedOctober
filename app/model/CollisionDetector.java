package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import controllers.GameController;


public class CollisionDetector {

    private static List<ShotInfo> shotInfos;
    private GameController gameController;

    public CollisionDetector() {
    }

    public CollisionDetector(GameController controller) {
        this.gameController = controller;
        shotInfos = new LinkedList<ShotInfo>();
    }

    public void detectCollisions() {
        List<Torpedo> torpedoes = gameController.getTorpedoRepository().getTorpedoes();//repository.getTorpedos();
        List<Player> players = gameController.getPlayerRepository().getConnectedPlayers();//repository.getPlayers();

        shotInfos.clear();

        for (Torpedo torpedo : torpedoes) {
            if (torpedo.isMoving()) {
                Iterator<Player> playerIterator = players.iterator();
                while (playerIterator.hasNext()) {
                    Player player = playerIterator.next();
                    if (shouldTopedoeShotPlayer(torpedo, player)) {
                        player.shot();
                        torpedo.explode();
                        notifyShot(player, torpedo.getUserNick());
                        //playerIterator.remove();
                    }
                }
            }
        }

        Iterator<Player> playerIterator1 = players.iterator();
        while (playerIterator1.hasNext()) {
            Player player1 = playerIterator1.next();

            if( !player1.getIsShot() ) {
                List<Player> tmpPlayers = new ArrayList<Player>(players);
                Iterator<Player> playerIterator2 = tmpPlayers.iterator();

                while (playerIterator2.hasNext()) {
                    Player player2 = playerIterator2.next();

                    if (playersCollides(player1, player2) && !player2.getIsShot()) {
                        player1.shot();
                        player2.shot();
                        notifyShot(player1, player2.getNick());
                        notifyShot(player2, player1.getNick());
                        //playerIterator1.remove();
                        //playerIterator2.remove();
                    }
                }
            }
        }
    }


    private boolean playersCollides(Player player1, Player player2) {
        if (player1.getNick().equals(player2.getNick())) {
            return false;
        }

        double distance = getDistanceBetween(player1, player2);
        return distance <= 20;

    }

    private void notifyShot(Player player, String nickBy) {

        Player shotBy = findPlayerByName(nickBy);
        if( shotBy != null ){
            gameController.updateScoreByKillerPlayer(player,shotBy);
        }

        ShotInfo shotInfo = new ShotInfo();
        shotInfo.setShot(player.getNick());
        shotInfo.setShotBy(nickBy);
        shotInfos.add(shotInfo);
    }

    private boolean shouldTopedoeShotPlayer(Torpedo torpedo, Player player) {
        if (player.getNick().equals(torpedo.getUserNick())) {
            return false;
        }
        if( player.getIsShot() || torpedo.getIsExploded()) {
            return false;
        }

        double distance = getDistanceBetween(torpedo, player);
        return distance <= 20;
    }

    private double getDistanceBetween(MovingObject obj1, MovingObject obj2) {
        double tmp = Math.pow(obj1.getX() - obj2.getX(), 2) + Math.pow(obj1.getY() - obj2.getY(), 2);
        return Math.sqrt(tmp);
    }

    public static List<ShotInfo> getShotInfos() {
        return shotInfos;
    }

    private Player findPlayerByName(String userNick) {
        for (Player p : gameController.getPlayerRepository().getConnectedPlayers()) {
            if (p.getNick().equalsIgnoreCase(userNick)) {
                return p;
            }
        }
        System.out.println("Cannot find user=" + userNick);
        return null;
    }
}
