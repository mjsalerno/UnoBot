/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mjsalerno.unobot;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author roofis0
 */
public class PlayerList  {
	private static final long serialVersionUID = 1L;

    /**
     * keeps track if a reverse card was played.
     */
    private Boolean forw;
    
    private CircularArrayList<Player> list = new CircularArrayList<>();

    /**
     * default constructor.
     */
    public PlayerList() {
        this.forw = true;
    }

    /**
     * reverses the list when a reverse card was played.
     */
    public void rev() {
        forw = !forw;
    }   

    /**
     * removes a player from the list.
     * @param player the player that will be removed.
     */
    public synchronized void remove(Player player) {
    	list.remove(list.indexOf(player));
        list.ensurePostion(forw);
    }

    /**
     * deals a hand to all of the players in the list.
     * @param deck the Deck that will be used to draw the cards from.
     */
    public synchronized void deal(Deck deck) {
        for (int i = 0; i < list.size(); i++) {
            Player player = list.get(i);
            player.draw(deck, 7);
        }
    }

    /**
     * returns who's turn it is.
     * @return the Player who's turn it is.
     */
    
    public synchronized Player next() {
        Player nextPlayer;
        if (forw) {
            nextPlayer = list.next();
        } else {
            nextPlayer = list.prev();
        }
        return nextPlayer;
    }

    /**
     * returns a Player.
     * @param name the name of the player to be returned
     * @return the Player with the name specified.
     */
    public synchronized Player get(String name) {
        Player player = new Player(name);
        return list.get(list.indexOf(player));
    }

    /**
     * returns if the player list has a winner or not.
     * @return true if there is a winner, else false
     */
    public synchronized Boolean hasWinner() {

        for (int i = 0; (i < list.size()) ; i++) {
            if ( !list.get(i).hasCards()) {
            	return true;
            }
        }
        return false;
    }

    /**
     * the sum of all Players points earned in the game.
     * @return the points earned.
     */
    public synchronized int pointSum() {
        int sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum += list.get(i).points();
        }
        return sum;
    }

    /**
     * returns a string with stats on how many cards each player has.
     * @return 
     */
    public synchronized String countCards() {
        StringBuilder sb = new StringBuilder("[");
        Player player;
        int j = 0;

        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            player = list.get(j++);
            sb.append(player.getName()).append(": ").append(player.howManyCards());
        }

        sb.append(']');
        return sb.toString();
    }
    
    public synchronized void clearAllHands(){
        for(Player player : list){
            player.clearHand();
        }
    }


    
    public synchronized Map<Player,Integer> getPointMap() {
    	Map<Player,Integer> pointMap = new HashMap<>();
        for (Player p : list) {
            int points = p.points();
            if(points == 0){
                points = this.pointSum();
            }else{
                points /= 2;
            }              
            pointMap.put(p, points);
        }
        return pointMap;
    }
    
    public synchronized Player at() {
    	return list.at();
    }
    
    public synchronized void clear() {
    	list.clear();
    }
    
    public synchronized int size() {
    	return list.size();
    }
    
    public synchronized boolean contains(Player p) {
    	return list.contains(p);
    }
    
    public synchronized boolean add(Player p) {
    	return list.add(p);
    }

    /**
     * returns a String representation of this PlayerList.
     * @return a string that represents this PlayerList.
     */
    @Override
    public synchronized String toString() {
        StringBuilder sb = new StringBuilder("[");
        int j = 0;

        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }

            sb.append(list.get(j++).getName());
        }

        sb.append(']');

        return sb.toString();
    }
    
    
}
