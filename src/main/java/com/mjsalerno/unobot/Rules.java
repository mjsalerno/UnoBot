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
    
    public static Card parse(String string){
        
        try {
            
            String newString = "";
            String[] split = string.toUpperCase().split(" ");
	    
            
            String strColor = null;
            String strFace = null;
            
            if ( split.length >= 2) {
            	strColor = split[0];
            	strFace = split[1];
            } else {
            	
            	// short hand single word variants Y9/B2/G6/R8 etc 
            	if (split[0].length() != 2) {
            		return null;
            	} else {
            		strColor = String.valueOf( split[0].charAt(0) );
            		strFace = String.valueOf( split[0].charAt(1) );
            	}
            }
                        
            //check color
            switch (strColor) {
                case "R":
                    newString += "RED ";
                    break;
                case "B":
                    newString += "BLUE ";
                    break;
                case "G":
                    newString += "GREEN ";
                    break;
                case "Y":
                    newString += "YELLOW ";
                    break;
                case "W":
                    newString += "WILD ";
                    break;
                default:
                    newString += split[0] + " ";
                    break;
            }
            
            //check face
            switch (strFace){
                case "0":
                    newString += "ZERO";
                    break;
                case "1":
                    newString += "ONE";
                    break;
                case "2":
                    newString += "TWO";
                    break;
                case "3":
                    newString += "THREE";
                    break;
                case "4":
                    newString += "FOUR";
                    break;
                case "5":
                    newString += "FIVE";
                    break;
                case "6":
                    newString += "SIX";
                    break;
                case "7":
                    newString += "SEVEN";
                    break;
                case "8":
                    newString += "EIGHT";
                    break;
                case "9":
                    newString += "NINE";
                    break;
                case "W":
                    newString += "WILD";
                    break;
                case "S":
                    newString += "SKIP";
                    break;
                case "R":
                case "REV":
                    newString += "REVERSE";
                    break;                                
                default:
                    newString += split[1];
                    break;
            }
            
            Card card;
            split = newString.split(" ");
            if(split[0].equals("WILD") || split[0].equals("WD4")){
                card = new Card(Card.Color.WILD,Card.Face.valueOf(split[0]));
            }else{
                card = new Card(Card.Color.valueOf(split[0]),Card.Face.valueOf(split[1]));
            }
            return card;
            
        }catch (Exception ex) {
            return null; // Return null if the parser fails
        }
        
    }
}
