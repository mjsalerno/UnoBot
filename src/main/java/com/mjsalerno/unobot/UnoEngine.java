
package com.mjsalerno.unobot;

/**
 *
 * @author Michael
 */
public class UnoEngine {
    
    private PlayerList playerList;
    private Deck deck;
    private boolean inProgress;
    private boolean delt;
    
    public UnoEngine(){
        playerList  = new PlayerList();
        deck = new Deck();
        inProgress = false;
        delt = false;
    }
    
    /**
     * returns the name of the player whose turn it is
     * @return the name of the player whose turn it is
     */
    public String getWhosTurn(){
        if(!inProgress){
            throw new RuntimeException("Sorry but there is no started game");
        }else return this.playerList.at().getName();
    }
    
    /**
     * gets the top card of the uno deck
     * @return the top card of the uno deck
     */
    public String getTopCard(){
        if(!inProgress){
            throw new RuntimeException("Sorry but there is no started game");
        }else return this.deck.topCard().toString();
    }
    
    /**
     * gets the hand of the player whose turn it is
     * @return a String representing the hand of the player whose turn it is.
     */
    public String getCurrentPlayersHand(){
        return this.playerList.at().cardsToString();
    }
    
    /**
     * adds a player tho the list of people playing
     * @param name the name of the person playing
     * @return true if the player was successfully added, else false
     */
    public boolean addPlayer(String name){
        Player player  = new Player(name);
        if(delt)player.draw(deck,7);
        return this.playerList.add(player);
    }
    
    /**
     * removes a player from the list of people playing
     * @param name the name of the player that is to be removed
     */
    public void removePlayer(String name){
        this.playerList.remove(new Player(name));
    }
    
    /**
     * clears the list of people playing
     */
    public void removeAllPlayers(){
        this.playerList = new PlayerList();
    }
    
    /**
     * returns if there is a game in progress
     * @return true if a game is in progress, else false
     */
    public boolean isGameInProgress(){
        return this.inProgress;
    }
    
    /**
     * returns if the current player has a card and if it can be played
     * @param card the card to be checked
     * @return true if the card can be played, else false
     */
    public boolean isCardPlayable(String card){
        return this.playerList.at().isCardPlayable(Card.parse(card), deck);
    }
    
    /**
     * stars a game if on is not already in progress
     */
    public void startGame() {
        if (inProgress) {
            throw new RuntimeException("Sorry but a game was already started.");
        } else if (this.playerList.size() < 2) {
            throw new RuntimeException("Sorry but there are not enough people playing"
                    + "\ncount must be > 1 but its " + this.playerList.size());
        } else {
            this.playerList.clearAllHands();
            this.deck.createDeck();
            this.playerList.deal(deck);
            this.delt = true;
            this.inProgress = true;
        }
    }
    
    /**
     * adds a random card from the deck in the current players hand
     */
    public void drawCard(){
        this.playerList.at().draw(deck);
    }

    /**
     * stops a game that is already in progress,
     * resets everything but the playerList
     */
    public void stopGame() {
        this.deck = new Deck();
        this.delt = false;
        this.inProgress = false;
        this.playerList.clearAllHands();
    }
    
    /**
     * gets all of the people playing in the current game
     * @return a String representing all of the people playing
     */
    public String getPlayerListString(){
        return this.playerList.toString();
    }
    
    /**
     * returns a string that represents how many cards each player has
     * @return the String that represents how many cards every player has.
     */
    public String getCardCount(){
        if(!inProgress){
            throw new RuntimeException("Sorry but there is no started game");
        }else return this.playerList.countCards();
    }
    
    /**
     * checks to see if the current player has uno
     * @return true if the current player has uno, else false
     */
    public boolean currentPlayerHasUno(){
        return this.playerList.at().hasUno();
    }
    
    /**
     * check to see if the current player won the game
     * @return true if the current player has won the game, else false
     */
    public boolean currentPlayerWins(){
        return this.playerList.at().hasWin();
    }
    
    /**
     * plays a card if it can be played given a string
     * can parse strings RED FOUR, r 4, red 4....
     * Wild Draw Four is wd4
     * wild cards should be played W/WILD/wild/wd4 blue
     * WIlD BLUE
     * if the user wants to change the color blue
     * @param cardString a string that represents the card
     */
    public void play(String cardString){    
        Card card = Card.parse(cardString);

            Player player = playerList.at();
            if (player.hasCard(card)){
                if(deck.isPlayable(card)){
                    //what to do with card.
                    
                    //WILD
                    if (card.face.equals(Card.Face.WILD) || card.face.equals(Card.Face.WD4)) {

                        
                        
                        player.playWild(card, deck);
                        playerList.next();
                        if(card.face.equals(Card.Face.WD4)){
                            playerList.at().draw(deck, 4);
                            System.out.println(playerList.at().getName() + " draws 4 cards.");
                            playerList.next();
                        }
                    }
                    
                    //SKIP
                    else if(card.face.equals(Card.Face.SKIP)){
                        player.play(card, deck);
                        System.out.println(playerList.next().getName() + " was skipped.");
                        playerList.next();
                    }
                    
                    //REV
                    else if (card.face.equals(Card.Face.REVERSE)) {
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
    
    /**
     * checks to see if a user has uno or won a game
     * @param player the player that will be checked
     * @return true if the player has won the match, else false
     */
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
    

    
    
}
