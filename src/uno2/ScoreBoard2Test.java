/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uno2;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Michael Salerno 
 * ID# 108512298 
 * E-Mail roofis20002003@yahoo.com
 * Homework2 
 * CSE214 
 * Recitation 04 Phillip Huff 
 * Grading TA Ming Chen
 *
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
        pl.add(p1);
        pl.add(p2);
        pl.add(p3);

        pl.deal(deck);
        sb.updateScoreBoard(pl);
        System.out.println(sb);
        sb.ScoreBoardToFile("ScoreBoard.dat");
        sb = new ScoreBoard2("Test.dat");
        System.out.println("\n");
        System.out.println(sb + "\n\n");
        
        for (int i = 0; i < sb.players.size(); i++) {
            System.out.println(sb.toString(i));
        }

    }
}
