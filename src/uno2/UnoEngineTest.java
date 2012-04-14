/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uno2;

import java.util.Scanner;

/**
 *
 * @author Michael
 */
public class UnoEngineTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        UnoEngine engine = new UnoEngine();
        engine.addPlayer("Mike");
        engine.addPlayer("Adam");
        engine.addPlayer("Paul");
        String card;
        engine.startGame();
        
        while(engine.isGameInProgress()){
            System.out.println("--------------------------------");
            System.out.println("Top Card: " + engine.getTopCard());
            System.out.println("--------------------------------");
            System.out.println(engine.getWhosTurn() + " it is your turn.");
            System.out.println(engine.getCurrentPlayersHand());
            card = kb.nextLine();
            if(card.equalsIgnoreCase("draw")){
                engine.drawCard();
            }
            else if(engine.isCardPlayable(card)){
                engine.play(card);
            }
        }
    }
}
