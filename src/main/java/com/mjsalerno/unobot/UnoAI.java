/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mjsalerno.unobot;

import java.util.Collections;
import java.util.LinkedList;

/**
 *
 * @author roofis0
 */
public class UnoAI {
    
    public static boolean hasPlayable(Player me, Deck deck){
        boolean hasPlayable = false;
        LinkedList<Card> hand = me.getHand();
        for (int i = 0; (i < hand.size()) && !hasPlayable; i++) {
            hasPlayable = deck.isPlayable(hand.get(i));
        }
        return hasPlayable;
    }
    
    
    public static Card getPlayable(Player me, Deck deck){
        boolean isPlayable = false;
        Card card = null;
        LinkedList<Card> hand = me.getHand();  
        Collections.sort(hand, new AIHandComparator());
        
        for (int i = 0; (i < hand.size()) && !isPlayable; i++) {
            isPlayable = deck.isPlayable(hand.get(i));
            if ( isPlayable ){
                card = hand.get(i);
            }
        }        
        return card;
    }
    
    public static Card.Color colorMostOf(Player me, Deck deck){
        int red=0;
        int blue=0;
        int green=0;
        int yellow=0;
        
        LinkedList<Card> hand = me.getHand();
        Card.Color color = Card.Color.DEFAULT;
        
        Card.Color Red = Card.Color.RED;
        Card.Color Blue = Card.Color.BLUE;
        Card.Color Green = Card.Color.GREEN;
        Card.Color Yellow = Card.Color.YELLOW;
        
        for(Card card : hand){
            color = card.color;
            
            if(color.equals(Red))red++;
            else if(color.equals(Blue))blue++;
            else if(color.equals(Green))green++;
            else if(color.equals(Yellow))yellow++;
        }
        
        if(red >= green && red >= blue && red >= yellow) color = Red;
        else if(blue >= red && blue >= green && blue >= yellow) color = Blue;
        else if(green >= red && green >= blue && green >= yellow) color = Green;
        else if(yellow >= red && yellow >= blue && yellow >= green) color = Yellow;
        return color;
    }
}
