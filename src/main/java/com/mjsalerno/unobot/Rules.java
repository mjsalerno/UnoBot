/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package com.mjsalerno.unobot;

/**
 *
 * @author roofis0
 */
public class Rules {
    
    public static void skip(Deck deck, PlayerList players){
        if(deck.topCard().face.equals(Card.Face.SKIP)){
            players.next();
        }
    }
    
    public static void reverse(Deck deck, PlayerList players){
        if(deck.topCard().face.equals(Card.Face.REVERSE)){
            players.rev();
        }
    }
    
    public static void drawTwo(Deck deck, PlayerList players){
        if(deck.topCard().face.equals(Card.Face.D2)){
            players.next().draw(deck);
            players.at().draw(deck);
        }
    }
    
    public static void drawFour(Deck deck, PlayerList players){
        if(deck.topCard().face.equals(Card.Face.WD4)){
            players.next().draw(deck);
            players.at().draw(deck);
            players.at().draw(deck);
            players.at().draw(deck);
        }
    }
    
    public static void whatDo(Deck deck, PlayerList players){
        if(deck.topCard().face.equals(Card.Face.D2))drawTwo(deck,players);
        else if(deck.topCard().face.equals(Card.Face.REVERSE))reverse(deck,players);
        else if(deck.topCard().face.equals(Card.Face.SKIP))skip(deck,players);
        else if(deck.topCard().face.equals(Card.Face.WD4))drawFour(deck,players);
    }
  
}
