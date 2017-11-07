package com.mjsalerno.unobot;

import static org.junit.Assert.*;

import org.junit.Test;

public class RulesTest {
	
	Card card;

	@Test 
	public void testBasicInvalid() {
		assertNull( Rules.parse("Red"));
		assertNull( Rules.parse("Green"));
		assertNull( Rules.parse("Blue"));
		
		assertNull( Rules.parse("1"));
		assertNull( Rules.parse("WD6"));
		assertNull( Rules.parse("D3"));
		assertNull( Rules.parse("SKIP"));
		assertNull( Rules.parse("S"));
	}
	
	// Test various editions of ordinary number cards
	@Test
	public void testNumericCards() {		
		//Red 1 variant
		assertNotNull( Rules.parse("RED 1"));
		card = Rules.parse("RED 1");
		assertNotNull(card);
		assertEquals( Card.Color.RED, card.color);
		assertEquals( Card.Face.ONE, card.face);
		
		//Yellow 9
		assertNotNull( Rules.parse("Yellow 9"));
		assertNotNull( Rules.parse("YELLOW 9"));
		card = Rules.parse("y 9");
		assertNotNull(card);
		assertEquals( Card.Color.YELLOW, card.color);
		assertEquals( Card.Face.NINE, card.face);
		
		//Blue 3
		assertNotNull( Rules.parse("B 3"));
		assertNotNull( Rules.parse("Blue 3"));
		assertNotNull( Rules.parse("BLUE 3"));
		card = Rules.parse("b 3");
		assertNotNull(card);
		assertEquals( Card.Color.BLUE, card.color);
		assertEquals( Card.Face.THREE, card.face);
		
	}
	
	@Test
	public void testDraw2() {
		card = Rules.parse("B D2");
		assertNotNull(card);
		assertEquals( Card.Color.BLUE, card.color);
		assertEquals( Card.Face.D2, card.face);		
		
		card = Rules.parse("Green D2");
		assertNotNull(card);
		assertEquals( Card.Color.GREEN, card.color);
		assertEquals( Card.Face.D2, card.face);				
	}
	
	@Test
	public void testReverse() {
		card = Rules.parse("B R");
		assertNotNull(card);
		assertEquals( Card.Color.BLUE, card.color);
		assertEquals( Card.Face.R, card.face);		
		
		card = Rules.parse("Green R");
		assertNotNull(card);
		assertEquals( Card.Color.GREEN, card.color);
		assertEquals( Card.Face.R, card.face);
	}
	@Test
	public void testSkip() {
		card = Rules.parse("B s");
		assertNotNull(card);
		assertEquals( Card.Color.BLUE, card.color);
		assertEquals( Card.Face.S, card.face);		
		
		card = Rules.parse("Green S");
		assertNotNull(card);
		assertEquals( Card.Color.GREEN, card.color);
		assertEquals( Card.Face.S, card.face);		
	}
	
	//wild = shift color
	@Test
	public void testWild() {
		card = Rules.parse("Green w");
		assertNotNull(card);
		assertEquals( Card.Color.GREEN, card.color);
		assertEquals( Card.Face.WILD, card.face);

		card = Rules.parse("B WILD");
		assertNotNull(card);
		assertEquals( Card.Color.BLUE, card.color);
		assertEquals( Card.Face.WILD, card.face);
		
	}

	//WD4 = wild + draw 4
	@Test
	public void testWD4() {
		card = Rules.parse("Green wd4");
		assertNotNull(card);
		assertEquals( Card.Color.GREEN, card.color);
		assertEquals( Card.Face.WD4, card.face);

		card = Rules.parse("B WD4");
		assertNotNull(card);
		assertEquals( Card.Color.BLUE, card.color);
		assertEquals( Card.Face.WD4, card.face);
		
	}
	
	
}
