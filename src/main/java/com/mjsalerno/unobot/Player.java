/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mjsalerno.unobot;

import java.util.Collections;
import java.util.LinkedList;

import org.pircbotx.Colors;

/**
 *
 * @author roofis0
 */
public class Player {
    
    private String name;    
    
    private LinkedList<Card> pDeck = new LinkedList<>();
   
    /**
     * partial constructor
     * @param name the name of this Player
     */
    public Player(String name){
        this.name = name;
    }
    
    /**
     * Full constructor.
     * @param name the name of this Player.
     * @param Cards a list of cards this PLayer is holding.
     */
    public Player(String name, LinkedList<Card> Cards){
        this.name = name;
        this.pDeck = Cards; 
    }
    
    /**
     * returns the amount of cards this Player is holding
     * @return the amount of cards this Player is holding
     */
    public int howManyCards(){
        return pDeck.size();
    }
    
    /**
     * returns the name of this Player.
     * @return the name of this Player.
     */
    public String getName(){
        return name;
    }
    
    public Card draw(Deck deck){
        Card card = deck.Draw();
        if (card != null) {
        	pDeck.add(card);
        	sortCards();
        }
        return card;
    }
    
    public int draw(Deck deck, int num){
    	int count=0;
        for(int i = 0 ; i < num ; i++){
        	Card card = deck.Draw();
        	if (card == null)
        		break;
        	
            pDeck.add(card);
            sortCards();
            count++;
        }
        return count;        
    }
    
    public Boolean hasCard(Card card){
        Card.Color color = card.color;
        Card.Face face = card.face;
        Boolean has = false;
        for(Card cardz:this.pDeck){
            if((cardz.color.equals(color)) && (cardz.face.equals(face))){
                has = true;
            }
        }
        return has;
    }
    
    public boolean hasUno(){
        return pDeck.size()==1;
    }
    
    public boolean hasWin(){
        return pDeck.size()==0;
    }
    
    public void sortCards(){
        Collections.sort(pDeck);
        
    }
    public void clearHand(){
        pDeck.clear();
    }
    public void drawCard(Card card){
        pDeck.add(card);
    }
    
    public boolean isCardPlayable(Card card, Deck deck){
        boolean playable = deck.isPlayable(card);
        boolean has = hasCard(card);
        return playable && has;
    }
    
    public boolean play(Card card, Deck deck){
        boolean playable = isCardPlayable(card, deck);
        if (playable) {
            deck.playCard(card);
            //this.pDeck.remove(card);
            playable &= this.pDeck.removeFirstOccurrence(card);            
        }
        return playable;
    }
    
    public boolean playWild(Card card, Card.Color color, Deck deck) {
        boolean play = deck.isPlayable(card);
        boolean has = hasCard(card);
        boolean removed = false;
        //Card tmpCard = new Card(color, card.face);
        if ((play && has) && (card.color.equals(Card.Color.WILD))) {
            //cant make new color WILD
            if(color.equals(Card.Color.WILD)) return false;
            deck.playWild(card, color);
            removed = this.pDeck.removeFirstOccurrence(card);
        }
        return play&&has&&removed;
    }
    
    public String cardsToString(){
        StringBuilder sb = new StringBuilder("[");
        int j = 0;
        for (int i = 0; i < pDeck.size(); i++) {
            if(i>0)sb.append(", ");
            sb.append(pDeck.get(j++).toString());
        } 
        sb.append("]");
        return sb.toString();
    }
    
    public LinkedList<Card> getHand(){
        return (LinkedList<Card>) this.pDeck.clone();
    }
    
    public String cardsToIRCString(){
        StringBuilder sb = new StringBuilder("[");
        int j = 0;
        for (int i = 0; i < pDeck.size(); i++) {
            if(i>0)sb.append(", ");
            sb.append(pDeck.get(j++).toIRCString());
        } 
        sb.append(Colors.NORMAL + "]");
        return sb.toString();
    }
    
    public Boolean hasCards(){
        return !this.pDeck.isEmpty();
    }
    
    public int points(){
        int i = 0;
        for (Card card : this.pDeck) {
            i += card.points;
        }
        return i;
    }
    
    @Override
    public boolean equals(Object obj){
        boolean equ = false;
        if(obj instanceof Player){
            Player player = (Player)obj;
            if(player != null){
                equ = (this.name.equals(player.name));
            }
        }
        return equ;
    }
}
