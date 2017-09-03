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
        Card card;
        //0
        deck.add(new Card(Card.Color.GREEN, Card.Face.ZERO));
        deck.add(new Card(Card.Color.BLUE, Card.Face.ZERO));
        deck.add(new Card(Card.Color.RED, Card.Face.ZERO));
        deck.add(new Card(Card.Color.YELLOW, Card.Face.ZERO));

        //1
        deck.add(card = new Card(Card.Color.GREEN, Card.Face.ONE));
        deck.add(card);
        deck.add(card = new Card(Card.Color.BLUE, Card.Face.ONE));
        deck.add(card);
        deck.add(card = new Card(Card.Color.RED, Card.Face.ONE));
        deck.add(card);
        deck.add(card = new Card(Card.Color.YELLOW, Card.Face.ONE));
        deck.add(card);

        //2
        deck.add(card = new Card(Card.Color.GREEN, Card.Face.TWO));
        deck.add(card);
        deck.add(card = new Card(Card.Color.BLUE, Card.Face.TWO));
        deck.add(card);
        deck.add(card = new Card(Card.Color.RED, Card.Face.TWO));
        deck.add(card);
        deck.add(card = new Card(Card.Color.YELLOW, Card.Face.TWO));
        deck.add(card);

        //3    
        deck.add(card = new Card(Card.Color.GREEN, Card.Face.THREE));
        deck.add(card);
        deck.add(card = new Card(Card.Color.BLUE, Card.Face.THREE));
        deck.add(card);
        deck.add(card = new Card(Card.Color.RED, Card.Face.THREE));
        deck.add(card);
        deck.add(card = new Card(Card.Color.YELLOW, Card.Face.THREE));
        deck.add(card);

        //4
        deck.add(card = new Card(Card.Color.GREEN, Card.Face.FOUR));
        deck.add(card);
        deck.add(card = new Card(Card.Color.BLUE, Card.Face.FOUR));
        deck.add(card);
        deck.add(card = new Card(Card.Color.RED, Card.Face.FOUR));
        deck.add(card);
        deck.add(card = new Card(Card.Color.YELLOW, Card.Face.FOUR));
        deck.add(card);

        //5
        deck.add(card = new Card(Card.Color.GREEN, Card.Face.FIVE));
        deck.add(card);
        deck.add(card = new Card(Card.Color.BLUE, Card.Face.FIVE));
        deck.add(card);
        deck.add(card = new Card(Card.Color.RED, Card.Face.FIVE));
        deck.add(card);
        deck.add(card = new Card(Card.Color.YELLOW, Card.Face.FIVE));
        deck.add(card);

        //6
        deck.add(card = new Card(Card.Color.GREEN, Card.Face.SIX));
        deck.add(card);
        deck.add(card = new Card(Card.Color.BLUE, Card.Face.SIX));
        deck.add(card);
        deck.add(card = new Card(Card.Color.RED, Card.Face.SIX));
        deck.add(card);
        deck.add(card = new Card(Card.Color.YELLOW, Card.Face.SIX));
        deck.add(card);

        //7
        deck.add(card = new Card(Card.Color.GREEN, Card.Face.SEVEN));
        deck.add(card);
        deck.add(card = new Card(Card.Color.BLUE, Card.Face.SEVEN));
        deck.add(card);
        deck.add(card = new Card(Card.Color.RED, Card.Face.SEVEN));
        deck.add(card);
        deck.add(card = new Card(Card.Color.YELLOW, Card.Face.SEVEN));
        deck.add(card);

        //8
        deck.add(card = new Card(Card.Color.GREEN, Card.Face.EIGHT));
        deck.add(card);
        deck.add(card = new Card(Card.Color.BLUE, Card.Face.EIGHT));
        deck.add(card);
        deck.add(card = new Card(Card.Color.RED, Card.Face.EIGHT));
        deck.add(card);
        deck.add(card = new Card(Card.Color.YELLOW, Card.Face.EIGHT));
        deck.add(card);

        //9
        deck.add(card = new Card(Card.Color.GREEN, Card.Face.NINE));
        deck.add(card);
        deck.add(card = new Card(Card.Color.BLUE, Card.Face.NINE));
        deck.add(card);
        deck.add(card = new Card(Card.Color.RED, Card.Face.NINE));
        deck.add(card);
        deck.add(card = new Card(Card.Color.YELLOW, Card.Face.NINE));
        deck.add(card);

        //WILD
        deck.add(card = new Card(Card.Color.WILD, Card.Face.WILD));
        deck.add(card);
        deck.add(card);
        deck.add(card);
        if (extreme) {
            deck.add(card);
            deck.add(card);
            deck.add(card);
            deck.add(card);
        }

        //WD4
        deck.add(card = new Card(Card.Color.WILD, Card.Face.WD4));
        deck.add(card);
        deck.add(card);
        deck.add(card);
        if (extreme) {
            deck.add(card);
            deck.add(card);
            deck.add(card);
            deck.add(card);
        }

        //R
        deck.add(card = new Card(Card.Color.GREEN, Card.Face.R));
        deck.add(card);
        if (extreme) {
            deck.add(card);
            deck.add(card);
        }
        deck.add(card = new Card(Card.Color.BLUE, Card.Face.R));
        deck.add(card);
        if (extreme) {
            deck.add(card);
            deck.add(card);
        }
        deck.add(card = new Card(Card.Color.RED, Card.Face.R));
        deck.add(card);
        if (extreme) {
            deck.add(card);
            deck.add(card);
        }
        deck.add(card = new Card(Card.Color.YELLOW, Card.Face.R));
        deck.add(card);
        if (extreme) {
            deck.add(card);
            deck.add(card);
        }

        //SKIP
        deck.add(card = new Card(Card.Color.GREEN, Card.Face.S));
        deck.add(card);
        if (extreme) {
            deck.add(card);
            deck.add(card);
        }
        deck.add(card = new Card(Card.Color.BLUE, Card.Face.S));
        deck.add(card);
        if (extreme) {
            deck.add(card);
            deck.add(card);
        }
        deck.add(card = new Card(Card.Color.RED, Card.Face.S));
        deck.add(card);
        if (extreme) {
            deck.add(card);
            deck.add(card);
        }
        deck.add(card = new Card(Card.Color.YELLOW, Card.Face.S));
        deck.add(card);
        if (extreme) {
            deck.add(card);
            deck.add(card);
        }

        //D2
        deck.add(card = new Card(Card.Color.GREEN, Card.Face.D2));
        deck.add(card);
        if (extreme) {
            deck.add(card);
            deck.add(card);
        }
        deck.add(card = new Card(Card.Color.BLUE, Card.Face.D2));
        deck.add(card);
        if (extreme) {
            deck.add(card);
            deck.add(card);
        }
        deck.add(card = new Card(Card.Color.RED, Card.Face.D2));
        deck.add(card);
        if (extreme) {
            deck.add(card);
            deck.add(card);
        }
        deck.add(card = new Card(Card.Color.YELLOW, Card.Face.D2));
        deck.add(card);
        if (extreme) {
            deck.add(card);
            deck.add(card);
        }

        do {
            this.topCard = deck.get(rnd.nextInt(deck.size()));
        } while (this.topCard.color.equals(Card.Color.WILD));

        this.deck.trimToSize();
    }
    
    public final void createDeck() {
        this.createDeck(false);
    }
}
