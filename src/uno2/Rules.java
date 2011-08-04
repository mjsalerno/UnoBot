/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uno2;

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
    
    public static Card parse2(String string){
        Card card;
        string = string.toUpperCase();
        String[] split = string.split(" ");
        if(split[0].equals("WILD") || split[0].equals("WD4")){
            card = new Card(Card.Color.WILD,Card.Face.valueOf(split[0]));
        }else{
           card = new Card(Card.Color.valueOf(split[0]),Card.Face.valueOf(split[1])); 
        } 
        //System.out.println("CARD: " + card.toString());
        return card;
    }
    
    public static Card parse(String string){
        string = string.toUpperCase();
        String newString = "";
        String[] split = string.split(" ");
        
        //check color
        if ( split[0].equals("R")){
            newString += "RED ";
        }else if ( split[0].equals("B")){
            newString += "BLUE ";
        }else if ( split[0].equals("G")){
            newString += "GREEN ";
        }else if ( split[0].equals("Y")){
            newString += "YELLOW ";
        }else if ( split[0].equals("W")){
            newString += "WILD ";
        }else {
            newString += split[0] + " ";
        }
        
        //check face
        if ( split[1].equals("0")){
            newString += "ZERO";
        }else if ( split[1].equals("1")){
            newString += "ONE";
        }else if ( split[1].equals("2")){
            newString += "TWO";
        }else if ( split[1].equals("3")){
            newString += "THREE";
        }else if ( split[1].equals("4")){
            newString += "FOUR";
        }else if ( split[1].equals("5")){
            newString += "FIVE";
        }else if ( split[1].equals("6")){
            newString += "SIX";
        }else if ( split[1].equals("7")){
            newString += "SEVEN";
        }else if ( split[1].equals("8")){
            newString += "EIGHT";
        }else if ( split[1].equals("9")){
            newString += "NINE";
        }else if ( split[0].equals("W")){
            newString += "WILD ";
        }else {
            newString += split[1];
        }
        //System.out.println("NEWSTR: " + newString);
        
        
        
        return parse2(newString);
    }
}
