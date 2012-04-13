package uno2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jibble.pircbot.Colors;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author roofis0
 */
public class UnoBot extends PircBot {
    private String[] botOps;
    private String gameStarter, gameChannel, updateScript, currChannel = null;
    private boolean gameUp = false;
    private boolean delt = false;
    private boolean drew = false;
    private boolean cheating = false;
    private boolean botAI = false;
    
    private Deck deck = new Deck();
    private PlayerList players = new PlayerList();
    private Messenger msg;
    private unoAIBot bot2 = new unoAIBot();
    private ScoreBoard2 sb;
    private String ScoreBoardFileName;
    
    public UnoBot(){
        this("unoBot");
    }
    
    public UnoBot(String name) {
        this.setName(name);
        try {
            if (new File("Messages.dat").exists()) {
                this.msg = new Messenger("Messages.dat");
            }else{
                this.msg = new Messenger();
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(UnoBot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setBotOps(String[] botOps) {
        this.botOps = botOps;
    }
    
    public void setUpdateScript(String updateScript) {
        this.updateScript = updateScript;
    }
    
    public void setScoreBoardFileName(String fileName) {        
        this.ScoreBoardFileName = fileName;
        File file = new File(fileName);
        if(!file.exists()){
            this.sb = new ScoreBoard2();
        }else {
            try {
                this.sb = new ScoreBoard2(fileName);
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("the file " + fileName + " is not a valid ScoreBoard object\nI will create a new one");
                this.sb = new ScoreBoard2();
            }
        }
    }
    
    private void printPlayers(String channel){
        sendMessage(channel, players.toString());
    }
    
    private boolean isBotOp(String nick) {
        for (String i : botOps) {
            if (i.equalsIgnoreCase(nick)) {
                return true;
            }
        }
        return false;
    }
    
    private void resetScoreBoard() throws FileNotFoundException, IOException{
        this.sb.ScoreBoardToFile("BACKUP_" + this.ScoreBoardFileName);
        this.sb = new ScoreBoard2();
        this.sb.ScoreBoardToFile(this.ScoreBoardFileName);
    }
    
    private boolean checkWin(String channel, Player player) {
        boolean uno = player.hasUno();
        boolean win = player.hasWin();     
        if (uno) {
            sendMessage(channel, Colors.BOLD + Colors.UNDERLINE + Colors.TEAL + player.getName() + " has UNO!!!!");
        } else if (win) {            
            sendMessage(channel, Colors.BOLD + Colors.UNDERLINE + Colors.TEAL +  player.getName() + " has won the match!!!!");
            int points;
            for (Player p : this.players) {
                points = p.points();
                if(points == 0){
                    points = players.pointSum();
                }else{
                    points /= 2;
                }              
               sendMessage(channel, p.getName() + " : " + points);
            }
            
//            String[] list = new String[players.count()];
//            players.remove(player);
//            list[0] = player.getName();
//            String[] losers = players.toStringArray();
//            for(int i = 1 ; i < list.length ; i++){
//                list[i] = losers[i-1];
//            }
            
            sb.updateScoreBoard(players);
            try {
                sb.ScoreBoardToFile(ScoreBoardFileName);
            } catch (FileNotFoundException ex) {                
                sendMessage(channel, "Sorry but I can't find the score board file to save to.");
            } catch (IOException ex) {
                sendMessage(channel, "Sorry but there was an IO exception when i tried to save the score board.");
            }
            
            if(botAI){
                bot2.disconnect();
                botAI = false;
            }
            gameUp = false;
            delt = false;
            players.clear();
            deck.clear();
            
        }
        return win;
    }   
   
    
    private void join(String channel, String name){
        Player player = new Player(name);
        if (!players.contains(player)) {
            players.add(player);
            if(delt)player.draw(deck,7);
            sendMessage(channel, name + " has joined.");
        }else sendMessage(channel, name + ", you are already in the player list.");
    }
    
    private void leave(String channel, String name){
        Player player = new Player(name);
        if (players.contains(player)) {
            players.remove(player);
            if(players.at().getName().equals(player.getName()))players.next();
            sendMessage(channel, name + " has quit the game like a pussy.");
        }
    }
    
    private String showCards(Player player){        
        return player.cardsToString();
    }
    
    private void printScore(String channel) throws FileNotFoundException{
        for (int i = 0; i < sb.players.size() ; i++) {
            this.sendMessage(channel, this.sb.toString(i));
        }                
    }
    
    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        String[] tokens = message.split(" ");        
        //NICK
        if ( tokens[0].equalsIgnoreCase("!nick") && isBotOp(sender) ) {
            changeNick(tokens[1]);   
            this.setName(tokens[1]);
        }
        //INFO
        if ( tokens[0].equalsIgnoreCase("!info")) {
            sendMessage(channel,"LOGIN: " + this.getLogin());
            sendMessage(channel,"NAME: " + this.getName());
            sendMessage(channel,"NICK: " + this.getNick());            
        }
        //HELP
        else if ( tokens[0].equalsIgnoreCase("!help")){
                      
            sendNotice(sender,"!uno ------ Starts an new UNO game.");
            sendNotice(sender,"!join ----- Joins an existing UNO game.");
            sendNotice(sender,"!deal ----- Deals out the cards to start an UNO game.");
            sendNotice(sender,"            but only the person that started the game can deal");
            sendNotice(sender,"!play ----- Plays a card (!play <color> <face>)");
            sendNotice(sender,"            to play a RED FIVE !play r 5");
            sendNotice(sender,"!showcards  Shows you your hand. (!hand)");
            sendNotice(sender,"!draw ----- Draws a card when you don't have a playable card.");
            sendNotice(sender,"!pass ----- If you don't have a playable card after you draw");
            sendNotice(sender,"            then you pass.");
            sendNotice(sender,"!count ---- Show how many cards each player has.");
            sendNotice(sender,"!leave ---- If you're a faggot and want to leave the game early.");
            sendNotice(sender,"!what ----- If you were not paying attention this will tell");
            sendNotice(sender,"            you the top card and whos turn it is.");
            sendNotice(sender,"!players -- Displays the player list.");
            sendNotice(sender,"!score ---- Prints out the score board.");
            sendNotice(sender,"!ai ------- Turns the bot ai on or off.");
            sendNotice(sender,"!endgame -- Ends the game, only the person who started the");
            sendNotice(sender,"            game may end it.");
            sendNotice(sender,"!tell ----- Tell an ofline user a message once they join the channel.");
            sendNotice(sender,"!messages - List all of the people that have messages.");
            sendNotice(sender,"!help ----- This shit.");
            sendNotice(sender,"!rank ----- Shows all users win:lose ratio");
            if(isBotOp(sender)){
            sendNotice(sender,"----------- OP only" + "-----------");
            sendNotice(sender,"!nick ----- Tells the bot to change his nick.");
            sendNotice(sender,"!joinc ---- Tells the bot to join a channel.");
            sendNotice(sender,"!part ----- Tells the bot to part from a channel.");
            sendNotice(sender,"!quit ----- Tells the bot to dissconnect from the entire server.");
            sendNotice(sender,"!resetSB ----- resets the Score Board.");
            }
            
        }
        //JOINC
        else if ( tokens[0].equalsIgnoreCase("!joinc") && isBotOp(sender)  ) {
            joinChannel( tokens[1] );
        }
        //JOIN
        else if ( tokens[0].equalsIgnoreCase("!join") && gameUp  ) {
            join(channel, sender);
            sendMessage(channel, "There are now " + players.size() + " people in the players list");            
        }
        //UPDATE
        else if ( tokens[0].equalsIgnoreCase("!update") && this.isBotOp(sender) && this.updateScript != null  ) {
            
            try {
                Runtime.getRuntime().exec(updateScript);
            } catch (IOException ex) {
                Logger.getLogger(UnoBot.class.getName()).log(Level.SEVERE, null, ex);
            }
                 
        }
        //PART
        else if ( tokens[0].equalsIgnoreCase("!part") && isBotOp(sender)  ) {
            partChannel( tokens[1], "Bye!");
        }
        //QUIT
        else if ( tokens[0].equalsIgnoreCase("!quit") && isBotOp(sender) ) {
            quitServer();
            System.exit(0);
        }
        //TELL
        else if (tokens[0].equalsIgnoreCase("!tell")) {
            String[] msgSplit = message.split(" ", 3);
            this.msg.setMessage(sender, tokens[1], msgSplit[2]);
            sendMessage(channel, "ok i will tell them.");
            try {
                this.msg.MessengerToFile("Messages.dat");
            } catch (FileNotFoundException ex) {
                sendMessage(channel, "Sorry but i could not save the message "
                        + "data to a file since there was a file not found exception");
            } catch (IOException ex) {
                sendMessage(channel, "Sorry but i could not save the message "
                        + "data to a file");
            }
        }
        //MESSAGES
        else if ( tokens[0].equalsIgnoreCase("!messages")){
            sendMessage(channel,msg.forUserToString());
        }
        //ENDGAME
        else if ( (tokens[0].equalsIgnoreCase("!endgame") && gameUp) && (isBotOp(sender) || sender.equals(gameStarter)) ) {
            gameUp = false;
            delt = false;
            players.clear();
            deck.clear();
            sendMessage(channel,"The game was ended by " + sender);
        }
        //LEAVE
        else if (tokens[0].equalsIgnoreCase("!leave")){
            leave(channel,sender);
        }
        //SCORE
        else if (tokens[0].equalsIgnoreCase("!score")){
            if (!this.sb.isEmpty()) {
                try {
                    printScore(channel);
                } catch (FileNotFoundException ex) {
                    sendMessage(channel, "Sorry but i can't find the score board.");
                }
            } else {
                this.sendMessage(channel, "The Score Board is empty");
            }
        }
        //COUNT
        else if ( tokens[0].equalsIgnoreCase("!count") && delt){
            sendMessage(channel, players.countCards());
        }
        //PLAYERS
        else if (tokens[0].equalsIgnoreCase("!players") && gameUp){
            printPlayers(channel);
        }
        //AI
        else if (tokens[0].equalsIgnoreCase("!ai") && !gameUp){
            if (!botAI) {
                bot2.setMessageDelay(4000);
                bot2.setVerbose(false);
                bot2.setAutoNickChange(false);
                bot2.setBotOps(botOps);
                try {
                    bot2.connect(this.getServer(), this.getPort());
                } catch (IOException | IrcException ex) {
                    Logger.getLogger(UnoBot.class.getName()).log(Level.SEVERE, null, ex);
                }
                bot2.joinChannel(channel);
                botAI = true;
            }else{
                bot2.disconnect();
                botAI = false;
            }
        }
        //UNO
        else if ( tokens[0].equalsIgnoreCase("!uno")){
            if(gameUp)sendMessage(channel,"Sorry a game is already started in " + gameChannel);
            else{
                gameUp = true;
                gameChannel = channel;
                gameStarter = sender;
                join(channel, gameStarter);
                sendMessage(channel, "type !join to join the game.");
            }
        }
        //DEAL
        else if ( (tokens[0].equalsIgnoreCase("!deal")) && !delt && gameUp &&((sender.equals(gameStarter)) || (isBotOp(sender)))){
            deck.createDeck();
            players.deal(deck);
            Player playerMaster = new Player(botOps[0]);
            if(cheating && players.contains(playerMaster)){
                players.get(botOps[0]).clearHand();
                players.get(botOps[0]).drawCard(new Card(Card.Color.WILD,Card.Face.WILD));
            }
            this.delt = true;
            sendMessage(channel, "Top Card: " + deck.topCard());
            sendMessage(channel, players.at().getName() + " it is your turn.");
            sendNotice(players.at().getName(), showCards(players.at())); 
            if(botAI && (players.at().getName().equals("unoAI"))){
                        bot2.playAI(channel, players.at(), deck);
                    }
        }
        //WHAT
        else if ( (tokens[0].equalsIgnoreCase("!what")) && (delt)){
            sendMessage(channel, "Top Card: " + deck.topCard());
            sendMessage(channel, players.at().getName() + " it is your turn.");
            //sendNotice(players.at().getName(), showCards(players.at()));
        }
        //DRAW
        else if ( (tokens[0].equalsIgnoreCase("!draw")) && delt && (sender.equals(players.at().getName()))){
            sendNotice(sender,"you drew a " + players.at().draw(deck));
            drew = true;            
        } 
        //PASS
        else if ( (tokens[0].equalsIgnoreCase("!pass")) && delt && (sender.equals(players.at().getName()))){
            if (drew) {
                sendMessage(channel,players.at().getName() + " passed.");
                players.next();
                drew = false;
                sendMessage(channel, "Top Card: " + deck.topCard());
                sendMessage(channel, players.at().getName() + " it is your turn.");
                this.sendNotice(players.at().getName(), showCards(players.at()));
                if(botAI && (players.at().getName().equals("unoAI"))){
                        bot2.playAI(channel, players.at(), deck);
                    }
            }else{
                sendMessage(channel, "You must draw first.");
            }
        }
        //SHOWCARDS
        else if ( (tokens[0].equalsIgnoreCase("!showcards") || tokens[0].equalsIgnoreCase("!hand")) && delt){
            sendNotice(sender, showCards(players.get(sender)));
        }
        //RESET_SB
        else if( tokens[0].equalsIgnoreCase("!resetsb") && isBotOp(sender) ){
            try {
                resetScoreBoard();
                sendMessage(channel,"the Score Board is now empty.");
            } catch (FileNotFoundException ex) {
                sendMessage(channel,"Sorry but i could not find the Score Board file");
            } catch (IOException ex) {
                sendMessage(channel,"Sorry but there was some sort of error.");
            }
        }
        else if(tokens[0].equalsIgnoreCase("!rank")){
            for (int i = 0; i < this.sb.size(); i++) {
                this.sendMessage(channel, sb.playerRankToString(i));
            }
        }
        //PLAY
        else if ( (tokens[0].equalsIgnoreCase("!play")) && delt && gameUp && (sender.equals(players.at().getName()))){
            Card card = Rules.parse(tokens[1] + " " + tokens[2]);
            Player player = players.at();
            if (player.hasCard(card)){
                if(deck.isPlayable(card)){
                    drew = false;
                    //what to do with card.
                    
                    //WILD
                    if ((card.color.equals(Card.Color.WILD))) {
                        String coler = "";
                        if (tokens[2].equalsIgnoreCase("R")) {
                            coler += "RED";
                        } else if (tokens[2].equalsIgnoreCase("B")) {
                            coler += "BLUE";
                        } else if (tokens[2].equalsIgnoreCase("G")) {
                            coler += "GREEN";
                        } else if (tokens[2].equalsIgnoreCase("Y")) {
                            coler += "YELLOW";
                        }else coler += tokens[2].toUpperCase();
                        
                        
                        player.playWild(card, Card.Color.valueOf(coler),deck);
                        players.next();
                        if(card.face.equals(Card.Face.WD4)){
                            players.at().draw(deck, 4);
                            sendMessage(channel, players.at().getName() + " draws 4 cards.");
                            players.next();
                        }
                    }
                    
                    //SKIP
                    else if(card.face.equals(Card.Face.S)){
                        player.play(card, deck);
                        sendMessage(channel, players.next().getName() + " was skipped.");
                        players.next();
                    }
                    
                    //REV
                    else if (card.face.equals(Card.Face.R)) {
                        if (players.size() == 2) {
                            player.play(card, deck);
                            sendMessage(channel, players.next().getName() + " was skipped.");
                            players.next();
                        } else {
                            player.play(card, deck);
                            sendMessage(channel, player.getName() + " reversed the order.");
                            players.rev();
                            players.next();
                        }
                    }
                    
                    //D2
                    else if(card.face.equals(Card.Face.D2)){
                        player.play(card, deck);
                        sendMessage(channel, players.next().getName() + " draws 2 cards.");
                        players.at().draw(deck, 2);
                        players.next();
                    }
                    
                    //THE REST
                    else{
                        player.play(card, deck);
                        players.next();
                    }
                    
                    checkWin(channel, player);
                    
                    //TELL USER TO GO
                    if(gameUp){
                    sendMessage(channel, "Top Card: " + deck.topCard());
                    sendMessage(channel, players.at().getName() + " it is your turn.");
                    this.sendNotice(players.at().getName(), showCards(players.at()));
                    if(botAI && (players.at().getName().equals("unoAI"))){
                        bot2.playAI(channel, players.at(), deck);
                    }
                    }
                }else{
                    sendMessage(channel,"Sorry " + sender + " that card is not playable.");
                }
            }else{
                sendMessage(channel,"Sorry " + sender + " you dont have that card");
            }
        }  
}
    
    @Override
    public void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason){
        if(recipientNick.equals(this.getNick())){
            this.joinChannel(channel);
  
        }
        if(gameUp){
           leave(channel, recipientNick);
        }
        if ( this.getName().equals(recipientNick)){
            this.changeNick(this.getName());
        }
    }

    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
        if (gameUp && channel.equals(gameChannel)) {
            sendMessage(channel, sender + " there is a game up type !join to play.");
        } else if ((this.getNick().equals(sender)) && this.currChannel == null) {
            this.currChannel = channel;
        }

        if (this.msg.containsForUser(sender)) {
            while (msg.containsForUser(sender)) {
                sendMessage(channel, msg.getMessage(sender));
            }
            try {
                this.msg.MessengerToFile("Messages.dat");
            } catch (FileNotFoundException ex) {
                sendMessage(channel, "Sorry but i could not save the message "
                        + "data to a file since there was a file not found exception");
            } catch (IOException ex) {
                sendMessage(channel, "Sorry but i could not save the message "
                        + "data to a file");
            }
        }


    }
    
    @Override
    protected void onUserList(String channel, User[] users) {
        for (User user : users) {
            if (msg.containsForUser(user.getNick())) {
                while (msg.containsForUser(user.getNick())) {
                    sendMessage(channel, msg.getMessage(user.getNick()));
                }
                try {
                    this.msg.MessengerToFile("Messages.dat");
                } catch (FileNotFoundException ex) {
                    sendMessage(channel, "Sorry but i could not save the message "
                            + "data to a file since there was a file not found exception");
                } catch (IOException ex) {
                    sendMessage(channel, "Sorry but i could not save the message "
                            + "data to a file");
                }
            }
        }
    }


    @Override
    protected void onPart(String channel, String sender, String login, String hostname) {
        if(gameUp && channel.equals(gameChannel)){
           leave(channel, sender);
        }
        if ( this.getName().equals(sender)){
            this.changeNick(this.getName());
        }
    }   

    @Override
    protected void onNickChange(String oldNick, String login, String hostname, String newNick) {
        if ( this.getName().equals(oldNick)){
            this.changeNick(this.getName());
        }
    }

    @Override
    protected void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
        if ( this.getName().equals(sourceNick)){
            this.changeNick(this.getName());
        }
        if(gameUp){
           leave(gameChannel, sourceNick);
        }
    }   

    @Override
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        if (sender.equals(botOps[0]) && !delt && message.equalsIgnoreCase("cheat")) {
            sendMessage(sender, "Cheat was: " + this.cheating);
            this.cheating = !this.cheating;
            sendMessage(sender, "Cheat now: " + this.cheating);
        }
        System.out.println(this.currChannel);
    }

    @Override
    protected void onDisconnect() {
        System.out.println("dissconnected!!");
        while(!this.isConnected()){
            try {
                this.reconnect();
                this.joinChannel(this.currChannel);
            } catch ( IOException | IrcException ex) {
                System.out.println("ERROR on disconnect");
            } 
            
        }
    }
}

   
