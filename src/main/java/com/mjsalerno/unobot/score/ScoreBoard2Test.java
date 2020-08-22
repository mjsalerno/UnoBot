/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mjsalerno.unobot.score;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.mjsalerno.unobot.Deck;
import com.mjsalerno.unobot.Player;
import com.mjsalerno.unobot.PlayerList;

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
        
        
        for (ScoreCard card : sb.getScores()) {
        	System.out.println( card.toRankString() );
        }
        
        System.out.println("\ntop10");
        for (ScoreCard card : sb.getTop10()) {
            System.out.println(card.toString());
        }        
        
        System.out.println("save + reload");

        
        sb.scoreBoardToFile("Test.dat");
        sb = new ScoreBoard2("Test.dat");
        System.out.println("\n");
        System.out.println(sb + "\n\n");
        
        for (ScoreCard card : sb.getScores()) {
            System.out.println(card.toString());
        }
        
        System.out.println("\ntop10");
        for (ScoreCard card : sb.getTop10()) {
            System.out.println(card.toString());
        }        

    }
}
