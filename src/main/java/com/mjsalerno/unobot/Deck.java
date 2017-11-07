/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mjsalerno.unobot;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author roofis0
 */
public class Deck {

    private ArrayList<Card> deck = new ArrayList<>();
    private Card topCard;
    private Random rnd = new Random();

    public Deck() {
        createDeck();
    }

    /**
     * returns the card that is on the top.
     *
     * @return the card that is on the top.
     */
    public Card topCard() {
        return this.topCard;
    }

    /**
     * Picks a card of the top of the deck, the removes it.
     *
     * @return the card that was picked off.
     */
    public Card draw() {
    	if (deck.isEmpty())
    		return null;
    	
        Card card;
        card = deck.remove(rnd.nextInt(deck.size()));
        return card;
    }

    /**
     * clears the Deck
     */
    public void clear() {
        this.topCard = null;
        this.deck.clear();
    }

    /**
     * Checks if the given card can be player in the current
     * Deck state.
     * @param card the card that will be checked.
     * @return true if the Card can be played else false.
     */
    public Boolean isPlayable(Card card) {
        Boolean ans = false;
        if (card.color.equals(Card.Color.WILD)) {
            ans = true;
        } else if (card.color.equals(this.topCard.color)) {
            ans = true;
        } else if (card.face.equals(this.topCard.face)) {
            ans = true;
        }
        return ans;
    }

    /**
     * Plays the card if the Card can be played
     * @param card that Card that will be played.
     */
    public void playCard(Card card) {
        if (isPlayable(card)) {
            this.topCard = card;
            int place = (this.deck.size() > 0) ? rnd.nextInt(this.deck.size()) : 0;

            this.deck.add(place, card);
        }
    }

    /**
     * plays a wild card, and changed the top card of the
     * Deck to the color specified.
     * @param card the wild card being played.
     * @param color the color that the current top card will change to.
     */
    public void playWild(Card card, Card.Color color) {
        Card tmpCard = new Card(color, card.face);
        if ((isPlayable(card)) && (card.color.equals(Card.Color.WILD))) {
            this.topCard = tmpCard;
            
            int place = (this.deck.size() > 0) ? rnd.nextInt(this.deck.size()) : 0; 
            this.deck.add(place, card);
        }

    }

    /**
     * puts all of the proper cards in the uno deck.
     * @param extreme
     */
    public final void createDeck(boolean extreme) {
        deck.clear();
        deck = new ArrayList<>(140);

        int wildCount = 4;
        int reverseSkipD2Count = 2;
        
        if (extreme) {
        	wildCount = 8;
        	reverseSkipD2Count = 4;
        }
        
        
        //0
        deckBuildHelper(1, Card.Color.GREEN, Card.Face.ZERO);
        deckBuildHelper(1, Card.Color.BLUE, Card.Face.ZERO);
        deckBuildHelper(1, Card.Color.RED, Card.Face.ZERO);
        deckBuildHelper(1, Card.Color.YELLOW, Card.Face.ZERO);

        //1
        deckBuildHelper(2, Card.Color.GREEN, Card.Face.ONE);
        deckBuildHelper(2, Card.Color.BLUE, Card.Face.ONE);
        deckBuildHelper(2, Card.Color.RED, Card.Face.ONE);
        deckBuildHelper(2, Card.Color.YELLOW, Card.Face.ONE);
        
        //2
        deckBuildHelper(2, Card.Color.GREEN, Card.Face.TWO);
        deckBuildHelper(2, Card.Color.BLUE, Card.Face.TWO);
        deckBuildHelper(2, Card.Color.RED, Card.Face.TWO);
        deckBuildHelper(2, Card.Color.YELLOW, Card.Face.TWO);

        //3    
        deckBuildHelper(2, Card.Color.GREEN, Card.Face.THREE);
        deckBuildHelper(2, Card.Color.BLUE, Card.Face.THREE);
        deckBuildHelper(2, Card.Color.RED, Card.Face.THREE);
        deckBuildHelper(2, Card.Color.YELLOW, Card.Face.THREE);

        //4
        deckBuildHelper(2, Card.Color.GREEN, Card.Face.FOUR);
        deckBuildHelper(2, Card.Color.BLUE, Card.Face.FOUR);
        deckBuildHelper(2, Card.Color.RED, Card.Face.FOUR);
        deckBuildHelper(2, Card.Color.YELLOW, Card.Face.FOUR);
        
        //5
        deckBuildHelper(2, Card.Color.GREEN, Card.Face.FIVE);
        deckBuildHelper(2, Card.Color.BLUE, Card.Face.FIVE);
        deckBuildHelper(2, Card.Color.RED, Card.Face.FIVE);
        deckBuildHelper(2, Card.Color.YELLOW, Card.Face.FIVE);

        //6
        deckBuildHelper(2, Card.Color.GREEN, Card.Face.SIX);
        deckBuildHelper(2, Card.Color.BLUE, Card.Face.SIX);
        deckBuildHelper(2, Card.Color.RED, Card.Face.SIX);
        deckBuildHelper(2, Card.Color.YELLOW, Card.Face.SIX);

        //7
        deckBuildHelper(2, Card.Color.GREEN, Card.Face.SEVEN);
        deckBuildHelper(2, Card.Color.BLUE, Card.Face.SEVEN);
        deckBuildHelper(2, Card.Color.RED, Card.Face.SEVEN);
        deckBuildHelper(2, Card.Color.YELLOW, Card.Face.SEVEN);

        //8
        deckBuildHelper(2, Card.Color.GREEN, Card.Face.EIGHT);
        deckBuildHelper(2, Card.Color.BLUE, Card.Face.EIGHT);
        deckBuildHelper(2, Card.Color.RED, Card.Face.EIGHT);
        deckBuildHelper(2, Card.Color.YELLOW, Card.Face.EIGHT);

        //9
        deckBuildHelper(2, Card.Color.GREEN, Card.Face.NINE);
        deckBuildHelper(2, Card.Color.BLUE, Card.Face.NINE);
        deckBuildHelper(2, Card.Color.RED, Card.Face.NINE);
        deckBuildHelper(2, Card.Color.YELLOW, Card.Face.NINE);


        
        //WILD        
        deckBuildHelper(wildCount, Card.Color.WILD, Card.Face.WILD);        

        //WD4
        deckBuildHelper(wildCount, Card.Color.WILD, Card.Face.WD4);


        //R
        deckBuildHelper(reverseSkipD2Count, Card.Color.GREEN, Card.Face.REVERSE);
        deckBuildHelper(reverseSkipD2Count, Card.Color.BLUE, Card.Face.REVERSE);
        deckBuildHelper(reverseSkipD2Count, Card.Color.RED, Card.Face.REVERSE);
        deckBuildHelper(reverseSkipD2Count, Card.Color.YELLOW, Card.Face.REVERSE);
        
        //SKIP        
        deckBuildHelper(reverseSkipD2Count, Card.Color.GREEN, Card.Face.SKIP);
        deckBuildHelper(reverseSkipD2Count, Card.Color.BLUE, Card.Face.SKIP);
        deckBuildHelper(reverseSkipD2Count, Card.Color.RED, Card.Face.SKIP);
        deckBuildHelper(reverseSkipD2Count, Card.Color.YELLOW, Card.Face.SKIP);


        //D2
        deckBuildHelper(reverseSkipD2Count, Card.Color.GREEN, Card.Face.D2);
        deckBuildHelper(reverseSkipD2Count, Card.Color.BLUE, Card.Face.D2);
        deckBuildHelper(reverseSkipD2Count, Card.Color.RED, Card.Face.D2);
        deckBuildHelper(reverseSkipD2Count, Card.Color.YELLOW, Card.Face.D2);        


        do {
            this.topCard = deck.get(rnd.nextInt(deck.size()));
        } while (this.topCard.color.equals(Card.Color.WILD));

        this.deck.trimToSize();
    }
    
    private void deckBuildHelper(int count, Card.Color color, Card.Face face) {
    	Card card = new Card(color,face);
    	for (int i=0; i<count; i++) {
    		deck.add(card);
    	}
    }
    
    public final void createDeck() {
        this.createDeck(false);
    }
}
