/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uno2;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Michael Salerno
 **/
public class ScoreBoard2Test {

    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
        ScoreBoard2 sb = new ScoreBoard2();
        Deck deck = new Deck();
        deck.createDeck();

        Player p1 = new Player("mike");
        Player p2 = new Player("paul");
        Player p3 = new Player("adam");

        PlayerList pl = new PlayerList();
       
        pl.add(p2);
        pl.add(p3);

        pl.deal(deck);
         pl.add(p1);
        sb.updateScoreBoard(pl);
        System.out.println(sb);
        
        for (int i = 0; i < sb.size(); i++) {
                System.out.println(sb.playerRankToString(i));
            }
        
        sb.ScoreBoardToFile("Test.dat");
        sb = new ScoreBoard2("Test.dat");
        System.out.println("\n");
        System.out.println(sb + "\n\n");
        
        for (int i = 0; i < sb.players.size(); i++) {
            System.out.println(sb.toString(i));
        }

    }
}
