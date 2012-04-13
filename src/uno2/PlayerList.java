/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uno2;

/**
 *
 * @author roofis0
 */
public class PlayerList extends CircularArrayList<Player> {

    /**
     * keeps track if a reverse card was played.
     */
    private Boolean forw;

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
    public void remove(Player player) {
        super.remove(super.indexOf(player));
    }

    /**
     * deals a hand to all of the players in the list.
     * @param deck the Deck that will be used to draw the cards from.
     */
    public void deal(Deck deck) {
        for (int i = 0; i < super.size(); i++) {
            Player player = super.get(i);
            player.draw(deck, 7);
        }
    }

    /**
     * returns who's turn it is.
     * @return the Player who's turn it is.
     */
    public Player nextPlayer() {
        Player nextPlayer;
        if (forw) {
            nextPlayer = super.next();
        } else {
            nextPlayer = super.prev();
        }
        return nextPlayer;
    }

    /**
     * returns a Player.
     * @param name the name of the player to be returned
     * @return the Player with the name specified.
     */
    public Player get(String name) {
        Player player = new Player(name);
        return super.get(super.indexOf(player));
    }

    /**
     * returns if the player list has a winner or not.
     * @return true if there is a winner, else false
     */
    public Boolean hasWinner() {
        Boolean win = false;
        for (int i = 0; (i < super.size()) && (!win); i++) {
            win = !super.get(i).hasCards();
        }
        return win;
    }

    /**
     * the sum of all Players points earned in the game.
     * @return the points earned.
     */
    public int pointSum() {
        int sum = 0;
        for (int i = 0; i < super.size(); i++) {
            sum += super.get(i).points();
        }
        return sum;
    }

    /**
     * returns a string with stats on how many cards each player has.
     * @return 
     */
    public String countCards() {
        StringBuilder sb = new StringBuilder("[");
        Player player;
        int j = 0;

        for (int i = 0; i < super.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            player = super.get(j++);
            sb.append(player.getName()).append(": ").append(player.howManyCards());
        }

        sb.append(']');
        return sb.toString();
    }

    /**
     * returns a String array that represents this PlayerList.
     * @return the String array 
     */
    public String[] toStringArray() {
        Player[] playerArray = new Player[super.size()];
        String[] whoArray = new String[super.size()];

        for (int i = 0; i < super.size(); i++) {
            whoArray[i] = playerArray[i].getName();
        }
        return whoArray;
    }

    /**
     * returns a String representation of this PlayerList.
     * @return a string that represents this PlayerList.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        int j = 0;

        for (int i = 0; i < super.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }

            sb.append(super.get(j++).getName());
        }

        sb.append(']');

        return sb.toString();
    }
}
