/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package main.java.uno2;

/**
 *
 * @author roofis0
 */
public class Rules {
    
    public static void skip(Deck deck, PlayerList players){
        if(deck.topCard().face.equals(Card.Face.S)){
            players.next();
        }
    }
    
    public static void reverse(Deck deck, PlayerList players){
        if(deck.topCard().face.equals(Card.Face.R)){
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
        else if(deck.topCard().face.equals(Card.Face.R))reverse(deck,players);
        else if(deck.topCard().face.equals(Card.Face.S))skip(deck,players);
        else if(deck.topCard().face.equals(Card.Face.WD4))drawFour(deck,players);
    }
    
    public static Card parse(String string){
        
        try {
            
            string = string.toUpperCase();
            String newString = "";
            String[] split = string.split(" ");
            
            //check color
            switch (split[0]) {
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
            switch (split[1]){
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
