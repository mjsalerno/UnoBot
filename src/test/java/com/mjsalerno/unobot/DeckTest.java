package com.mjsalerno.unobot;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class DeckTest {
	
	Deck deck;
	
	@Before
	public void setupDeck() {
		deck = new Deck();
	}
	
	@Test
	public void testDeckCreation() {
		
		assertTrue( deck.size() == 108);
		
		deck.createDeck(true);
		assertTrue( deck.size() == 140);		
	}
	@Test
	public void testTopCardColor() {
		deck.playWild( new Card(Card.Color.GREEN, Card.Face.WILD), Card.Color.GREEN);
		assertTrue( deck.topCard().color.equals(Card.Color.GREEN ) );		
		assertFalse( deck.topCard().color.equals(Card.Color.BLUE ) );
		
		assertTrue( deck.isPlayable(new Card(Card.Color.YELLOW, Card.Face.WILD)) );
		assertTrue( deck.isPlayable(new Card(Card.Color.GREEN, Card.Face.ONE)) );
		
	}
	
	@Test
	public void testPlayer() {
		
		Player p = new Player("test");
		
		assertFalse( p.playWild(new Card(Card.Color.GREEN, Card.Face.WILD), Card.Color.GREEN, deck) );
		assertFalse( p.playWild(new Card(Card.Color.GREEN, Card.Face.WD4), Card.Color.GREEN, deck) );		
		assertFalse( p.play(new Card(Card.Color.GREEN, Card.Face.ONE), deck) );
		
		p.drawCard( new Card(Card.Color.WILD, Card.Face.WILD) );
		p.drawCard( new Card(Card.Color.WILD, Card.Face.WD4) );
		p.drawCard( new Card(Card.Color.GREEN, Card.Face.ONE)  );
		
		
		
		assertTrue( p.playWild(new Card(Card.Color.GREEN, Card.Face.WILD), Card.Color.GREEN, deck) );
		assertTrue( p.playWild(new Card(Card.Color.GREEN, Card.Face.WD4), Card.Color.GREEN, deck) );		
		assertTrue( p.play(new Card(Card.Color.GREEN, Card.Face.ONE), deck) );
		
		
		assertFalse( p.play(new Card(Card.Color.GREEN, Card.Face.ONE), deck) );
		p.drawCard( new Card(Card.Color.GREEN, Card.Face.ONE)  );
		assertTrue( p.play(new Card(Card.Color.GREEN, Card.Face.ONE), deck) );//
		
		assertTrue( p.howManyCards() == 0);		
		assertTrue( p.hasWin() );
	}

}
