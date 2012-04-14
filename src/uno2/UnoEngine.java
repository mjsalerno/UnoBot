
package uno2;

/**
 *
 * @author Michael
 */
public class UnoEngine {
    
    private PlayerList playerList;
    private Deck deck;
    private boolean inProgress;
    private boolean drew;
    private boolean delt;
    
    public UnoEngine(){
        playerList  = new PlayerList();
        deck = new Deck();
        drew = false;
        inProgress = false;
        delt = false;
    }
    
    public void play(String cardString){    
        Card card = stringToCard(cardString);
        String[] Tokens = cardString.split(" ");
            Player player = playerList.at();
            if (player.hasCard(card)){
                if(deck.isPlayable(card)){
                    drew = false;
                    //what to do with card.
                    
                    //WILD
                    if ((card.color.equals(Card.Color.WILD))) {
                        String coler = "";
                        if (Tokens[2].equalsIgnoreCase("R")) {
                            coler += "RED";
                        } else if (Tokens[2].equalsIgnoreCase("B")) {
                            coler += "BLUE";
                        } else if (Tokens[2].equalsIgnoreCase("G")) {
                            coler += "GREEN";
                        } else if (Tokens[2].equalsIgnoreCase("Y")) {
                            coler += "YELLOW";
                        }else coler += Tokens[2].toUpperCase();
                        
                        
                        player.playWild(card, Card.Color.valueOf(coler),deck);
                        playerList.next();
                        if(card.face.equals(Card.Face.WD4)){
                            playerList.at().draw(deck, 4);
                            System.out.println(playerList.at().getName() + " draws 4 cards.");
                            playerList.next();
                        }
                    }
                    
                    //SKIP
                    else if(card.face.equals(Card.Face.S)){
                        player.play(card, deck);
                        System.out.println(playerList.next().getName() + " was skipped.");
                        playerList.next();
                    }
                    
                    //REV
                    else if (card.face.equals(Card.Face.R)) {
                        if (playerList.size() == 2) {
                            player.play(card, deck);
                            System.out.println(playerList.next().getName() + " was skipped.");
                            playerList.next();
                        } else {
                            player.play(card, deck);
                            System.out.println(player.getName() + " reversed the order.");
                            playerList.rev();
                            playerList.next();
                        }
                    }
                    
                    //D2
                    else if(card.face.equals(Card.Face.D2)){
                        player.play(card, deck);
                        System.out.println(playerList.next().getName() + " draws 2 cards.");
                        playerList.at().draw(deck, 2);
                        playerList.next();
                    }
                    
                    //THE REST
                    else{
                        player.play(card, deck);
                        playerList.next();
                    }
                    
                    checkWin(player);
                    
                }else{
                    System.out.println("Sorry " + playerList.at() + " that card is not playable.");
                }
            }else{
                System.out.println("Sorry " + playerList.at() + " you dont have that card");
            }
    }
    
    private boolean checkWin(Player player) {
        boolean uno = player.hasUno();
        boolean win = player.hasWin();     
        if (uno) {
            System.out.println(player.getName() + " has UNO!!!!");
        } else if (win) {            
            System.out.println(player.getName() + " has won the match!!!!");
            int points;
            for (Player p : this.playerList) {
                points = p.points();
                if(points == 0){
                    points = playerList.pointSum();
                }else{
                    points /= 2;
                }              
                System.out.println(p.getName() + " : " + points);
            }
            
//            String[] list = new String[players.count()];
//            players.remove(player);
//            list[0] = player.getName();
//            String[] losers = players.toStringArray();
//            for(int i = 1 ; i < list.length ; i++){
//                list[i] = losers[i-1];
//            }
            
//            sb.updateScoreBoard(players);
//            try {
//                sb.ScoreBoardToFile(ScoreBoardFileName);
//            } catch (FileNotFoundException ex) {                
//                System.out.println("Sorry but I can't find the score board file to save to.");
//            } catch (IOException ex) {
//                System.out.println("Sorry but there was an IO exception when i tried to save the score board.");
//            }            
            
            inProgress = false;
            delt = false;
            playerList.clear();
            deck.clear();
            
        }
        return win;
    }   
    
    public static Card stringToCard(String string){
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
    
    }
    
    
}
