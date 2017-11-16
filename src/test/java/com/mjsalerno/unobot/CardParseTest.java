package com.mjsalerno.unobot;

import static org.junit.Assert.*;

import org.junit.Test;

public class CardParseTest {
	
	private Card card;

	@Test 
	public void testBasicInvalid() {
		assertNull( Card.parse("Red"));
		assertNull( Card.parse("Green"));
		assertNull( Card.parse("Blue"));
		
		assertNull( Card.parse("1"));
		assertNull( Card.parse("WD6"));
		assertNull( Card.parse("D3"));
		assertNull( Card.parse("SKIP"));
		assertNull( Card.parse("S"));
		

		assertNull(Card.parse("W"));
		assertNull(Card.parse("WILD"));
		assertNull(Card.parse("WD4"));
	}
	
	// Test various editions of ordinary number cards
	@Test
	public void testNumericCards() {		
		//Red 1 variant
		assertNotNull( Card.parse("RED 1"));
		card = Card.parse("RED 1");
		assertNotNull(card);
		assertEquals( Card.Color.RED, card.color);
		assertEquals( Card.Face.ONE, card.face);
		
		//Yellow 9
		assertNotNull( Card.parse("Yellow 9"));
		assertNotNull( Card.parse("YELLOW 9"));
		card = Card.parse("y 9");
		assertNotNull(card);
		assertEquals( Card.Color.YELLOW, card.color);
		assertEquals( Card.Face.NINE, card.face);
		
		//Blue 3
		assertNotNull( Card.parse("B 3"));
		assertNotNull( Card.parse("Blue 3"));
		assertNotNull( Card.parse("BLUE 3"));
		card = Card.parse("b 3");
		assertNotNull(card);
		assertEquals( Card.Color.BLUE, card.color);
		assertEquals( Card.Face.THREE, card.face);
		
	}
	
	@Test
	public void testShortVariants() {	
		//Blue 3
		card = Card.parse("b3");
		assertNotNull(card);
		assertEquals( Card.Color.BLUE, card.color);
		assertEquals( Card.Face.THREE, card.face);		
		
		//Yellow 0
		card = Card.parse("Y0");
		assertNotNull(card);
		assertEquals( Card.Color.YELLOW, card.color);
		assertEquals( Card.Face.ZERO, card.face);
		
		//Red 9
		card = Card.parse("R9");
		assertNotNull(card);
		assertEquals( Card.Color.RED, card.color);
		assertEquals( Card.Face.NINE, card.face);		
	}
	
	
	@Test
	public void testDraw2() {
		card = Card.parse("B D2");
		assertNotNull(card);
		assertEquals( Card.Color.BLUE, card.color);
		assertEquals( Card.Face.D2, card.face);		
		
		card = Card.parse("Green D2");
		assertNotNull(card);
		assertEquals( Card.Color.GREEN, card.color);
		assertEquals( Card.Face.D2, card.face);
		
		card = Card.parse("yD2");
		assertNotNull(card);
		assertEquals( Card.Color.YELLOW, card.color);
		assertEquals( Card.Face.D2, card.face);
	}
	
	@Test
	public void testReverse() {
		card = Card.parse("B R");
		assertNotNull(card);
		assertEquals( Card.Color.BLUE, card.color);
		assertEquals( Card.Face.REVERSE, card.face);		
		
		card = Card.parse("Green R");
		assertNotNull(card);
		assertEquals( Card.Color.GREEN, card.color);
		assertEquals( Card.Face.REVERSE, card.face);
		
		card = Card.parse("Green REV");
		assertNotNull(card);
		assertEquals( Card.Color.GREEN, card.color);
		assertEquals( Card.Face.REVERSE, card.face);
		
		card = Card.parse("Green REVERSE");
		assertNotNull(card);
		assertEquals( Card.Color.GREEN, card.color);
		assertEquals( Card.Face.REVERSE, card.face);		
		
	}
	@Test
	public void testSkip() {
		card = Card.parse("B s");
		assertNotNull(card);
		assertEquals( Card.Color.BLUE, card.color);
		assertEquals( Card.Face.SKIP, card.face);		
		
		card = Card.parse("Green S");
		assertNotNull(card);
		assertEquals( Card.Color.GREEN, card.color);
		assertEquals( Card.Face.SKIP, card.face);
		
		card = Card.parse("Y Skip");
		assertNotNull(card);
		assertEquals( Card.Color.YELLOW, card.color);
		assertEquals( Card.Face.SKIP, card.face);
		
		card = Card.parse("rs");
		assertNotNull(card);
		assertEquals( Card.Color.RED, card.color);
		assertEquals( Card.Face.SKIP, card.face);
	}
	
	//wild = shift color
	@Test
	public void testWild() {
		card = Card.parse("Green w");
		assertNotNull(card);
		assertEquals( Card.Color.GREEN, card.color);
		assertEquals( Card.Face.WILD, card.face);

		card = Card.parse("B WILD");
		assertNotNull(card);
		assertEquals( Card.Color.BLUE, card.color);
		assertEquals( Card.Face.WILD, card.face);

		card = Card.parse("yw");
		assertNotNull(card);
		assertEquals( Card.Color.YELLOW, card.color);
		assertEquals( Card.Face.WILD, card.face);		
		
	}

	//WD4 = wild + draw 4
	@Test
	public void testWD4() {
		card = Card.parse("Green wd4");
		assertNotNull(card);
		assertEquals( Card.Color.GREEN, card.color);
		assertEquals( Card.Face.WD4, card.face);

		card = Card.parse("B WD4");
		assertNotNull(card);
		assertEquals( Card.Color.BLUE, card.color);
		assertEquals( Card.Face.WD4, card.face);
		
		card = Card.parse("ywd4");
		assertNotNull(card);
		assertEquals( Card.Color.YELLOW, card.color);
		assertEquals( Card.Face.WD4, card.face);	
	}
	
	@Test
	public void testWILDfromGame() {
		card = Card.parse("WILD WILD");
		assertNotNull(card);
		assertEquals( Card.Color.WILD, card.color);
		assertEquals( Card.Face.WILD, card.face);

		card = Card.parse("WILD WD4");
		assertEquals( Card.Color.WILD, card.color);
		assertEquals( Card.Face.WD4, card.face);		
		
	}
	
	@Test
	public void testCardEquals() {
		card = new Card(Card.Color.BLUE, Card.Face.FIVE);
		assertTrue(card.equals(card));
		
		//test if parse tostring
		assertTrue(card.equals( Card.parse(card.toString())  ));
		
		assertTrue(card.equals( Card.parse(card.toIRCString())  ));
		
	}
	
	
}
