package com.mjsalerno.unobot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import com.google.common.collect.ImmutableSortedSet;
import javax.net.SocketFactory;


import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.WaitForQueue;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.KickEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.NickChangeEvent;
import org.pircbotx.hooks.events.PartEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.events.QuitEvent;
import org.pircbotx.hooks.events.UserListEvent;

/**
 *
 * @author roofis0
 */
public class UnoBot extends ListenerAdapter {
    
    private static final String UR_TURN = " it is your turn.";
    private static final String ERR_SAVE_MSG = "Sorry but I could not save the message ";
    private static final String MSG_FILE_NAME = "Messages.dat";
    private static final String TOP_CARD = "Top Card: ";
    private String unoAINick = "unoAI";
    
    private String[] botOps;
    private String gameStarter, updateScript, currChannel = null;
    private final String gameChannel;
    private String token = "!";
    private boolean gameUp = false;
    private boolean delt = false;
    private boolean drew = false;
    private boolean cheating = false;
    private boolean botAI = false;
    private boolean usingSSL = false;
    private boolean attack = false;
    private boolean extreme = false;
    private Random rand = new Random();
    private boolean messagesEnabled = true;
    private boolean manageConnectivity = true;
    private Deck deck = new Deck();
    private PlayerList players = new PlayerList();
    private Messenger msg;
    private ScoreBoard2 sb;
    private String ScoreBoardFileName;
    private PircBotX bot2;
    private UnoAIBot bot2ai = new UnoAIBot(bot2);
    public Timer timer;
    public Timer unotimer;
    protected PircBotX bot;
    
    private String aiNickSrvPasswd = null;
    private String aiServerPasswd = null;
    private String aiWebIRCPasswd = null;
    
    /*public UnoBot(boolean usingSSL){
    this("unoBot", usingSSL);
    }*/
    
    public UnoBot(PircBotX bot, boolean usingSSL, String gameChannel) {
        this.gameChannel = gameChannel;
        this.bot = bot;
        
        
        this.usingSSL = usingSSL;
        try {
            if (new File(MSG_FILE_NAME).exists()) {
                this.msg = new Messenger(MSG_FILE_NAME);
            } else {
                this.msg = new Messenger();
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(UnoBot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public UnoBot(PircBotX bot, boolean usingSSL, String gameChannel, String token) {
        this(bot, usingSSL, gameChannel);
        this.setToken(token);
    }
    
    public void startTimer(int seconds) {
        timer = new Timer();
        timer.schedule(new turnTask(), seconds * 1000);
    }
    
    public void startUnoTimer(int seconds){
        unotimer = new Timer();
        unotimer.schedule(new unoTask(), seconds*1000);
    }
    
    public void setUnoAINick(String nick) {
        this.unoAINick = nick;
    }
    
    public String getUnoAINick() {
        return this.unoAINick;
    }
    
    public void setAiNickSrvPasswd(String passwd) {
	this.aiNickSrvPasswd = passwd;
    }
    public void setAiServerPasswd(String passwd) {
        this.aiServerPasswd = passwd;
    }
    public void setAiWebIRCPasswd(String passwd) {
        this.aiWebIRCPasswd = passwd;
    }
    
    public class turnTask extends TimerTask {
        
        public void run() {
            stopTimer();
            bot.sendIRC().message(gameChannel, players.at().getName() + " ran out of time! They drew a card and lost their turn.");
            players.next();
            drew = false;
            bot.sendIRC().message(gameChannel, TOP_CARD + deck.topCard().toIRCString());
            bot.sendIRC().message(gameChannel, players.at().getName() + UR_TURN);
            bot.sendIRC().notice(players.at().getName(), showCards(players.at()));
            startTimer(60);
            if (botAI && (players.at().getName().equals(unoAINick))) {
                bot2ai.playAI(gameChannel, players.at(), deck);
            }
        }
    }
    
    public class unoTask extends TimerTask {
        public void run() {
            attack = false;
            extreme = false;
            gameUp = false;
            delt = false;
            players.clear();
            bot.sendIRC().message(gameChannel,"This game is taking too long to start so I'm stopping it.");
            if(botAI){
                botAI = false;
                bot2.stopBotReconnect();
                bot2.sendIRC().quitServer();
            }
        }
    }
    
    public void stopTimer() {
        timer.cancel();
    }
    
    public void stopUnoTimer(){
        unotimer.cancel();
    }
    
    public void setBotOps(String[] botOps) {
        this.botOps = botOps;
    }
    
    public void setUpdateScript(String updateScript) {
        this.updateScript = updateScript;
    }
    
    public boolean isExtreme() {
        return this.extreme;
    }
    
    public boolean isAttack() {
        return this.attack;
    }
    
    public String getToken() {
        return this.token;
    }
    
    public void setToken(String token) {
        if(token == null || token.length() < 1) {
            this.token = "!";
        } else {
            this.token = token;
        }
    }
    
    public void setMessagesEnabled(boolean messagesEnabled) {
        this.messagesEnabled = messagesEnabled;
    }
    
    public void setManageConnectivity(boolean manageConnectivity) {
        this.manageConnectivity = manageConnectivity;
    }
    
    public void setScoreBoardFileName(String fileName) {
        this.ScoreBoardFileName = fileName;
        File file = new File(fileName);
        if (!file.exists()) {
            this.sb = new ScoreBoard2();
        } else {
            try {
                this.sb = new ScoreBoard2(fileName);
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("the file " + fileName + " is not a valid ScoreBoard object\nI will create a new one");
                this.sb = new ScoreBoard2();
            }
        }
    }
    
    private void printPlayers(String channel) {
        bot.sendIRC().message(channel, players.toString());
    }
    
    private boolean isBotOp(String nick) {
        for (String i : botOps) {
            if (i.equalsIgnoreCase(nick)) {
                return true;
            }
        }
        return false;
    }
    
    private void resetScoreBoard() throws FileNotFoundException, IOException {
        this.sb.scoreBoardToFile("BACKUP_" + this.ScoreBoardFileName);
        this.sb = new ScoreBoard2();
        this.sb.scoreBoardToFile(this.ScoreBoardFileName);
    }
    
    private boolean checkWin(String channel, Player player) {
        boolean uno = player.hasUno();
        boolean win = player.hasWin();
        if (uno) {
            bot.sendIRC().message(channel, Colors.BOLD + Colors.UNDERLINE + Colors.TEAL + player.getName() + " has UNO!!!!");
        } else if (win) {
            attack = false;
            extreme = false;
            bot.sendIRC().message(channel, Colors.BOLD + Colors.UNDERLINE + Colors.TEAL + player.getName() + " has won the match!!!!");
            int points;
            for (Player p : this.players) {
                points = p.points();
                if (points == 0) {
                    points = players.pointSum();
                } else {
                    points /= 2;
                }
                bot.sendIRC().message(channel, p.getName() + " : " + points);
            }
            
            /*
            String[] list = new String[players.count()];
            players.remove(player);
            list[0] = player.getName();
            String[] losers = players.toStringArray();
            for(int i = 1 ; i < list.length ; i++){
            list[i] = losers[i-1];
            }
            */
            
            sb.updateScoreBoard(players);
            try {
                sb.scoreBoardToFile(ScoreBoardFileName);
            } catch (FileNotFoundException ex) {
                bot.sendIRC().message(channel, "Sorry but I can't find the score board file to save to.");
            } catch (IOException ex) {
                bot.sendIRC().message(channel, "Sorry but there was an IO exception when i tried to save the score board.");
            }
            
            if (botAI) {
                botAI = false;
                bot2.stopBotReconnect();
                bot2.sendIRC().quitServer();
                bot2 = null;
            }
            gameUp = false;
            delt = false;
            players.clear();
            deck.clear();
            
        }
        return win;
    }
    
    private void join(String channel, String name) {
        Player player = new Player(name);
        if (!players.contains(player)) {
            players.add(player);
            if (delt) {
                player.draw(deck, 7);
            }
            bot.sendIRC().message(channel, name + " has joined.");
        } else {
            bot.sendIRC().message(channel, name + ", you are already in the player list.");
        }
    }
    
    private void leave(String channel, String name) {
        Player player = new Player(name);
        if (players.contains(player)) {
            if(players.at().getName().equals(player.getName())){
                players.remove(player);
                if (players.size()>0){
                    players.next();
                    bot.sendIRC().message(channel, name + " has quit the game.");
                }
                else{
                    if(delt){
                        stopTimer();
                        deck.clear();
                        delt = false;
                    }else{
                        stopUnoTimer();
                    }
                    attack = false;
                    extreme = false;
                    gameUp = false;
                    delt = false;
                    players.clear();
                    bot.sendIRC().message(channel, name + " was the last player in the game, the game has ended");
                    if(botAI){
                        bot2.stopBotReconnect();
                        bot2.sendIRC().quitServer();
                        bot2 = null;
                        botAI = false;
                    }
                }
                
            }else{
                if (players.size()>0){
                    bot.sendIRC().message(channel, name + " has quit the game.");
                }
                else{
                    if(delt){
                        stopTimer();
                        deck.clear();
                        delt = false;
                    }else{
                        stopUnoTimer();
                    }
                    attack = false;
                    extreme = false;
                    gameUp = false;
                    delt = false;
                    players.clear();
                    bot.sendIRC().message(channel, name + " was the last player in the game, the game has ended");
                    if(botAI){
                        bot2.stopBotReconnect();
                        bot2.sendIRC().quitServer();
                        bot2 = null;
                        botAI = false;
                    }
                }
            }
        }
    }
    
    private String showCards(Player player) {
        return player.cardsToIRCString();
    }
    
    private void printScore(String channel) throws FileNotFoundException {
        for (int i = 0; i < sb.players.size(); i++) {
            this.bot.sendIRC().message(channel, this.sb.toString(i));
        }
    }
    
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage().trim());
        message = message.replaceAll("( )+", " ").trim(); // replace n amount of spaces with only one
        //message = message.replaceAll("  ", " ");//remove double spaces
        String[] tokens = message.split(" ");
        String sender = event.getUser().getNick();
        String channel = event.getChannel().getName();
        
        //NICK
        if (tokens[0].equalsIgnoreCase(this.token + "nick") && isBotOp(sender)) {
            bot.sendIRC().changeNick(tokens[1]);
//            bot.setName(tokens[1]);
        }
        //INFO
        if (tokens[0].equalsIgnoreCase(this.token + "info")) {
            bot.sendIRC().message(channel, "LOGIN: " + bot.getUserBot().getLogin());
            bot.sendIRC().message(channel, "NAME: " + bot.getUserBot().getRealName());
            bot.sendIRC().message(channel, "NICK: " + bot.getUserBot().getNick());
        } //HELP
        else if (tokens[0].equalsIgnoreCase(this.token + "unohelp")) {
            
            bot.sendIRC().notice(sender, this.token + "uno ------ Starts an new UNO game.");
            bot.sendIRC().notice(sender, this.token + "uno +a---- Attack mode: When you draw there is a 20% chance");
            bot.sendIRC().notice(sender, "            that you will be UNO attacked and will have to draw");
            bot.sendIRC().notice(sender, "            anywhere from 0 - 7 cards!");
            bot.sendIRC().notice(sender, this.token + "uno +e --- Extreme mode: This inserts twice as many special cards");
            bot.sendIRC().notice(sender, "            into the deck! Special cards include:");
            bot.sendIRC().notice(sender, "            R, S, D2, W, and WD4");
            bot.sendIRC().notice(sender, this.token + "uno +e +a  Enables both Extreme and Attack mode!");
            bot.sendIRC().notice(sender, this.token + "join ----- Joins an existing UNO game.");
            bot.sendIRC().notice(sender, this.token + "deal ----- Deals out the cards to start a UNO game.");
            bot.sendIRC().notice(sender, "            but only the person that started the game can deal");
            bot.sendIRC().notice(sender, this.token + "wait ----- Stops your turn timer.");
            bot.sendIRC().notice(sender, this.token + "play ----- Plays a card (!play <color> <face>) or (!p <color> <face>)");
            bot.sendIRC().notice(sender, "            to play a RED FIVE !play r 5");
            bot.sendIRC().notice(sender, this.token + "showcards  Shows you your hand. (!hand)");
            bot.sendIRC().notice(sender, this.token + "draw ----- Draws a card when you don't have a playable card.");
            bot.sendIRC().notice(sender, this.token + "pass ----- If you don't have a playable card after you draw");
            bot.sendIRC().notice(sender, "            then you pass.");
            bot.sendIRC().notice(sender, this.token + "unocount - Show how many cards each player has.");
            bot.sendIRC().notice(sender, this.token + "leave ---- If you want to leave the game early.");
            bot.sendIRC().notice(sender, this.token + "what ----- If you were not paying attention this will tell");
            bot.sendIRC().notice(sender, "            you the top card and whos turn it is.");
            bot.sendIRC().notice(sender, this.token + "players -- Displays the player list.");
            bot.sendIRC().notice(sender, this.token + "score ---- Prints out the score board.");
            bot.sendIRC().notice(sender, this.token + "ai ------- Turns the bot ai on or off.");
            bot.sendIRC().notice(sender, this.token + "endgame -- Ends the game, only the person who started the");
            bot.sendIRC().notice(sender, "            game may end it.");
            
            if (messagesEnabled) {
                bot.sendIRC().notice(sender, this.token + "tell ----- Tell an offline user a message once they join the channel.");
                bot.sendIRC().notice(sender, this.token + "messages - List all of the people that have messages.");
            }
            
            bot.sendIRC().notice(sender, this.token + "unohelp -- This help menu.");
            bot.sendIRC().notice(sender, this.token + "rank ----- Shows all users win:lose ratio");
            if (isBotOp(sender)) {
                bot.sendIRC().notice(sender, "----------- OP only" + "-----------");
                bot.sendIRC().notice(sender, this.token + "nick ----- Tells the bot to change his nick.");
                bot.sendIRC().notice(sender, this.token + "joinc ---- Tells the bot to join a channel.");
                bot.sendIRC().notice(sender, this.token + "part ----- Tells the bot to part from a channel.");
                bot.sendIRC().notice(sender, this.token + "quit ----- Tells the bot to dissconnect from the entire server.");
                bot.sendIRC().notice(sender, this.token + "resetSB -- Resets the Score Board.");
            }
            
        } //JOINC
        else if (tokens[0].equalsIgnoreCase(this.token + "joinc") && isBotOp(sender)) {
            bot.sendIRC().joinChannel(tokens[1]);
        } //UPDATE
        else if (tokens[0].equalsIgnoreCase(this.token + "update") && this.isBotOp(sender) && this.updateScript != null) {
            
            try {
                Runtime.getRuntime().exec(updateScript);
            } catch (IOException ex) {
                Logger.getLogger(UnoBot.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } //PART
        else if (tokens[0].equalsIgnoreCase(this.token + "part") && isBotOp(sender)) {
            Channel chan = bot.getUserChannelDao().getChannel(tokens[1]);
            chan.send().part();
        } //QUIT
        else if (tokens[0].equalsIgnoreCase(this.token + "quit") && isBotOp(sender)) {
            bot.sendIRC().quitServer();
            System.exit(0);
        } //RESET_SB
        else if (tokens[0].equalsIgnoreCase(this.token + "resetsb") && isBotOp(sender)) {
            try {
                resetScoreBoard();
                bot.sendIRC().message(channel, "the Score Board is now empty.");
            } catch (FileNotFoundException ex) {
                bot.sendIRC().message(channel, "Sorry but i could not find the Score Board file");
            } catch (IOException ex) {
                bot.sendIRC().message(channel, "Sorry but there was some sort of error.");
            }
        }
        
        
        
        if (!channel.equals(gameChannel)) {
            // Do not respond to a game command that's sent outside the gamechannel
            return;
        }
        
        
        
        //MESSAGES
        if (tokens[0].equalsIgnoreCase(this.token + "messages") && messagesEnabled) {
            bot.sendIRC().message(channel, msg.forUserToString());
        } //TELL
        else if (tokens[0].equalsIgnoreCase(this.token + "tell") && messagesEnabled) {
            String[] msgSplit = event.getMessage().split(" ", 3);
            this.msg.setMessage(sender, tokens[1], msgSplit[2]);
            bot.sendIRC().message(channel, "ok i will tell them.");
            try {
                this.msg.messengerToFile(MSG_FILE_NAME);
            } catch (FileNotFoundException ex) {
                bot.sendIRC().message(channel, ERR_SAVE_MSG
                        + "data to a file since there was a file not found exception");
            } catch (IOException ex) {
                bot.sendIRC().message(channel, ERR_SAVE_MSG
                        + "data to a file");
            }
        } //SCORE
        else if (tokens[0].equalsIgnoreCase(this.token + "score")) {
            if (!this.sb.isEmpty()) {
                try {
                    printScore(channel);
                } catch (FileNotFoundException ex) {
                    bot.sendIRC().message(channel, "Sorry but i can't find the score board.");
                }
            } else {
                this.bot.sendIRC().message(channel, "The Score Board is empty");
            }
        } //AI
        else if (tokens[0].equalsIgnoreCase(this.token + "ai") && !gameUp) {
            
            if (!botAI) {
                Configuration configuration2;
                Configuration.Builder configBuilder = new Configuration.Builder()
                        .setName(unoAINick)
                        .setLogin(unoAINick)
// Nickserv password will be the same as provided when the following line is uncommented
//                    .setNickservPassword(bot.getConfiguration().getNickservPassword()) // In case you want a nickserv password for your unobot
                        .setRealName(bot.getNick())
                        .setAutoReconnect(true)
                        .setAutoNickChange(true)
                        .setCapEnabled(true)
                        .setMessageDelay(4000)
			.addServers(bot.getConfiguration().getServers())
                        .addAutoJoinChannel(channel)
                        .setSocketFactory(usingSSL ? new UtilSSLSocketFactory().trustAllCertificates() : SocketFactory.getDefault())
                        .setSocketTimeout(130 * 1000) // Reduce socket timeouts from 5 minutes to 130 seconds
                        .setVersion("mIRC v7.32 Khaled Mardam-Bey"); // Set to something funny
                        
                        if(aiNickSrvPasswd != null) {
                            configBuilder = configBuilder.setNickservPassword(aiNickSrvPasswd.trim());
                        }

                        if(aiServerPasswd != null) {
                            configBuilder = configBuilder.setServerPassword(aiServerPasswd.trim());
                        }

                        if(aiWebIRCPasswd != null) {
                            configBuilder = configBuilder.setWebIrcPassword(aiWebIRCPasswd.trim());
                        }
                        
                        configuration2 = configBuilder.buildConfiguration();
                
                try {
                    this.bot2 = new PircBotX(configuration2);
                    
                    bot2ai = new UnoAIBot(bot2);
                    bot2.getConfiguration().getListenerManager().addListener(bot2ai);
                    bot2ai.setBotOps(botOps);
                    botAI = true;
                    this.bot2.startBot();
                }
                catch (Exception ex){
                    Logger.getLogger(UnoBot.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                botAI = false;
                bot2.stopBotReconnect();
                bot2.sendIRC().quitServer();
                bot2 = null;
            }
            
        } //UNO ***************************** UNO GAME *********************************
        else if (tokens[0].equalsIgnoreCase(this.token + "uno")) {
            if (gameUp) {
                bot.sendIRC().message(channel, "Sorry a game is already started in " + gameChannel);
            } else {
                if (tokens.length > 1 && tokens[1].equalsIgnoreCase("+e")) {
                    this.extreme = true;
                }
                if (tokens.length > 2 && tokens[2].equalsIgnoreCase("+e")) {
                    this.extreme = true;
                }
                if (tokens.length > 1 && tokens[1].equalsIgnoreCase("+a")) {
                    this.attack = true;
                }
                if (tokens.length > 2 && tokens[2].equalsIgnoreCase("+a")) {
                    this.attack = true;
                }
                gameUp = true;
                gameStarter = sender;
                join(channel, gameStarter);
                bot.sendIRC().message(channel, "type !join to join the game.");
                startUnoTimer(300);
                
                WaitForQueue queue = new WaitForQueue(bot);
                while (gameUp){
                    
                    boolean hitReturn = false;
                    event = queue.waitFor(MessageEvent.class);
                    message = Colors.removeFormattingAndColors(event.getMessage().trim());
                    message = message.replaceAll("( )+", " ").trim(); // replace n amount of spaces with only one
                    //message = message.replaceAll("  ", " ");//remove double spaces
                    tokens = message.split(" ");
                    sender = event.getUser().getNick();
                    channel = event.getChannel().getName();
                    
                    if (!channel.equals(gameChannel)) {
                        // Do not respond to a game command that's sent outside the gamechannel
                        return;
                    }
                    
                    //JOIN
                    if (tokens[0].equalsIgnoreCase(this.token + "join") && gameUp) {
                        join(channel, sender);
                        bot.sendIRC().message(channel, "There are now " + players.size() + " people in the players list");
                    } //ENDGAME
                    else if ((tokens[0].equalsIgnoreCase(this.token + "endgame") && gameUp) && (isBotOp(sender) || sender.equals(gameStarter))) {
                        if(delt){
                            stopTimer();
                            deck.clear();
                            delt = false;
                        }else{
                            stopUnoTimer();
                        }
                        attack = false;
                        extreme = false;
                        gameUp = false;
                        delt = false;
                        players.clear();
                        bot.sendIRC().message(channel, "The game was ended by " + sender);
                        if(botAI){
                            bot2.stopBotReconnect();
                            bot2.sendIRC().quitServer();
                            bot2 = null;
                            botAI = false;
                        }
                    } //LEAVE
                    else if (tokens[0].equalsIgnoreCase(this.token + "leave")) {
                        leave(channel, sender);
                        if (gameUp){
                            stopTimer();
                            bot.sendIRC().message(channel, TOP_CARD + deck.topCard().toIRCString());
                            bot.sendIRC().message(channel, players.at().getName() + UR_TURN);
                            bot.sendIRC().notice(players.at().getName(), showCards(players.at()));
                            startTimer(60);
                            if(botAI && (players.at().getName().equals(unoAINick))){
                                bot2ai.playAI(channel, players.at(), deck);
                            }
                        }
                    } //COUNT
                    else if (tokens[0].equalsIgnoreCase(this.token + "unocount") && delt) {
                        bot.sendIRC().message(channel, players.countCards());
                    } //PLAYERS
                    else if (tokens[0].equalsIgnoreCase(this.token + "players") && gameUp) {
                        printPlayers(channel);
                    } //DEAL
                    else if ((tokens[0].equalsIgnoreCase(this.token + "deal")) && !delt && gameUp && ((sender.equals(gameStarter)) || (isBotOp(sender)))) {
                        deck.createDeck(this.extreme);
                        stopUnoTimer();
                        players.deal(deck);
                        Player playerMaster = new Player(botOps[0]);
                        if (cheating && players.contains(playerMaster)) {
                            players.get(botOps[0]).clearHand();
                            players.get(botOps[0]).drawCard(new Card(Card.Color.WILD, Card.Face.WILD));
                        }
                        this.delt = true;
                        bot.sendIRC().message(channel, TOP_CARD + deck.topCard().toIRCString());
                        bot.sendIRC().message(channel, players.at().getName() + UR_TURN);
                        bot.sendIRC().notice(players.at().getName(), showCards(players.at()));
                        startTimer(60);
                        if (botAI && (players.at().getName().equals(unoAINick))) {
                            bot2ai.playAI(channel, players.at(), deck);
                        }
                    } //WHAT
                    else if ((tokens[0].equalsIgnoreCase(this.token + "what")) && (delt)) {
                        bot.sendIRC().message(channel, TOP_CARD + deck.topCard().toIRCString());
                        bot.sendIRC().message(channel, players.at().getName() + UR_TURN);
                        //sendIRC().notice(players.at().getName(), showCards(players.at()));
                    } //WAIT
                    else if ((tokens[0].equalsIgnoreCase(this.token + "wait")) && delt && (sender.equals(players.at().getName()))) {
                        stopTimer();
                        bot.sendIRC().message(channel, players.at().getName() + " stopped their turn timer.");
                        //sendIRC().notice(players.at().getName(), showCards(players.at()));
                    } //DRAW
                    else if ((tokens[0].equalsIgnoreCase(this.token + "draw")) && delt && (sender.equals(players.at().getName()))) {
                        //sendIRC().notice(sender,"you drew a " + players.at().draw(deck).toIRCString());
                        if (!drew) {
                            if (attack) {
                                boolean prob = rand.nextInt(5) == 1;
                                if (prob) {
                                    int attackDraw = rand.nextInt(8);
                                    int attackCount = players.at().draw(deck, attackDraw);
                                    if (attackCount == attackDraw) {
                                        stopTimer();
                                        bot.sendIRC().message(channel, players.at().getName() + " got UNO attacked! They had to draw " + attackDraw + " cards!");
                                        bot.sendIRC().notice(sender, "You just got UNO attacked!");
                                        bot.sendIRC().notice(sender, showCards(players.get(sender)));
                                        bot.sendIRC().notice(sender, "If you still have no card to play then pass by typing !pass");
                                        drew = true;
                                    } else {
                                        bot.sendIRC().message(channel, "Deck is empty");
                                        drew = false;
                                    }
                                } else {
                                    Card card = players.at().draw(deck);
                                    if (card != null) {
                                        stopTimer();
                                        bot.sendIRC().notice(sender, "you drew a " + card.toString());
                                        bot.sendIRC().notice(sender, "If you still have no card to play then pass by typing !pass");
                                        drew = true;
                                    } else {
                                        bot.sendIRC().message(channel, "Deck is empty");
                                        drew = false;
                                    }
                                }
                            } else {
                                Card card = players.at().draw(deck);
                                if (card != null) {
                                    stopTimer();
                                    bot.sendIRC().notice(sender, "you drew a " + card.toString());
                                    bot.sendIRC().notice(sender, "If you still have no card to play then pass by typing !pass");
                                    drew = true;
                                } else {
                                    bot.sendIRC().message(channel, "Deck is empty");
                                    drew = false;
                                }
                            }
                        } else {
                            bot.sendIRC().message(channel, "Sorry " + sender + " but you already "
                                    + "drew a card. If you still have no card to play then "
                                    + "pass by typing !pass");
                        }
                    } //PASS
                    else if ((tokens[0].equalsIgnoreCase(this.token + "pass")) && delt && (sender.equals(players.at().getName()))) {
                        if (drew) {
                            stopTimer();
                            bot.sendIRC().message(channel, players.at().getName() + " passed.");
                            players.next();
                            drew = false;
                            bot.sendIRC().message(channel, TOP_CARD + deck.topCard().toIRCString());
                            bot.sendIRC().message(channel, players.at().getName() + UR_TURN);
                            bot.sendIRC().notice(players.at().getName(), showCards(players.at()));
                            startTimer(60);
                            if (botAI && (players.at().getName().equals(unoAINick))) {
                                bot2ai.playAI(channel, players.at(), deck);
                            }
                        } else {
                            bot.sendIRC().message(channel, "You must draw first.");
                        }
                    } //SHOWCARDS
                    else if ((tokens[0].equalsIgnoreCase(this.token + "showcards") || tokens[0].equalsIgnoreCase(this.token + "hand")) && delt) {
                        bot.sendIRC().notice(sender, showCards(players.get(sender)));
                    } //RANK
                    else if (tokens[0].equalsIgnoreCase(this.token + "rank")) {
                        for (int i = 0; i < this.sb.size(); i++) {
                            this.bot.sendIRC().message(channel, sb.playerRankToString(i));
                        }
                    } //PLAY
                    else if ((tokens[0].equalsIgnoreCase(this.token + "play") || tokens[0].equalsIgnoreCase(this.token + "p")) && delt && gameUp && (sender.equals(players.at().getName()))) {
                        Card card = null;
//                        try {
			if (tokens.length >= 3) {
			    card = Rules.parse(tokens[1] + " " + tokens[2]);
			}
                        if (card == null) {
                            bot.sendIRC().message(channel, "Illegal card");
                            hitReturn = true;
                        }
                        else {
                            Player player = players.at();
                            if (player.hasCard(card)) {
                                if (deck.isPlayable(card)) {
                                    stopTimer();
                                    drew = false;
                                    //what to do with card.
                                    
                                    //WILD
                                    if ((card.color.equals(Card.Color.WILD))) {
                                        String coler = "";
                                        if (tokens[2].equalsIgnoreCase("R") || tokens[2].equalsIgnoreCase("RED")) {
                                            coler += "RED";
                                        } else if (tokens[2].equalsIgnoreCase("B") || tokens[2].equalsIgnoreCase("BLUE")) {
                                            coler += "BLUE";
                                        } else if (tokens[2].equalsIgnoreCase("G") || tokens[2].equalsIgnoreCase("GREEN")) {
                                            coler += "GREEN";
                                        } else if (tokens[2].equalsIgnoreCase("Y") || tokens[2].equalsIgnoreCase("YELLOW")) {
                                            coler += "YELLOW";
                                        } else {
                                            //coler += tokens[2].toUpperCase();
                                            bot.sendIRC().notice(sender, "You must set the new color when playing a WILD card");
                                            hitReturn = true;
                                        }
                                        
                                        if (!hitReturn) {
                                            
                                            boolean played = player.playWild(card, Card.Color.valueOf(coler), deck);
                                            if (!played) {
                                                bot.sendIRC().message(channel, "Sorry " + sender + " that card is not playable. Try something like !play wild red");
                                                hitReturn = true;
                                            }
                                            else {
                                                players.next();
                                                if (card.face.equals(Card.Face.WD4)) {
                                                    int cardCount = players.at().draw(deck, 4);
                                                    if (cardCount == 4) {
                                                        bot.sendIRC().message(channel, players.at().getName() + " draws 4 cards.");
                                                    } else {
                                                        bot.sendIRC().message(channel, "Deck is empty, " + players.at().getName() + " draws " + cardCount + " cards.");
                                                    }
                                                    players.next();
                                                }
                                            }
                                        }
                                    } //SKIP
                                    else if (card.face.equals(Card.Face.S)) {
                                        player.play(card, deck);
                                        bot.sendIRC().message(channel, players.next().getName() + " was skipped.");
                                        players.next();
                                    } //REV
                                    else if (card.face.equals(Card.Face.R)) {
                                        if (players.size() == 2) {
                                            player.play(card, deck);
                                            bot.sendIRC().message(channel, players.next().getName() + " was skipped.");
                                            players.next();
                                        } else {
                                            player.play(card, deck);
                                            bot.sendIRC().message(channel, player.getName() + " reversed the order.");
                                            players.rev();
                                            players.next();
                                        }
                                    } //D2
                                    else if (card.face.equals(Card.Face.D2)) {
                                        player.play(card, deck);
                                        int cardCount = players.next().draw(deck, 2);
                                        if (cardCount == 2) {
                                            bot.sendIRC().message(channel, players.at().getName() + " draws 2 cards.");
                                        } else {
                                            bot.sendIRC().message(channel, "Deck is empty, " + players.at().getName() + " draws " + cardCount + " cards.");
                                        }
                                        
                                        players.next();
                                    } //THE REST
                                    else {
                                        player.play(card, deck);
                                        players.next();
                                    }
                                    
                                    if (!hitReturn) {
                                        
                                        checkWin(channel, player);
                                        
                                        //TELL USER TO GO
                                        if (gameUp) {
                                            bot.sendIRC().message(channel, TOP_CARD + deck.topCard().toIRCString());
                                            bot.sendIRC().message(channel, players.at().getName() + UR_TURN);
                                            bot.sendIRC().notice(players.at().getName(), showCards(players.at()));
                                            startTimer(60);
                                            if (botAI && (players.at().getName().equals(unoAINick))) {
                                                bot2ai.playAI(channel, players.at(), deck);
                                            }
                                        }
                                    }
                                } else {
                                    bot.sendIRC().message(channel, "Sorry " + sender + " that card is not playable.");
                                }
                            } else {
                                bot.sendIRC().message(channel, "Sorry " + sender + " you dont have that card");
                            }
                        }
                    }
                }
                queue.close();
            }
        }
    }
    
    @Override
    public void onKick(KickEvent event) throws Exception {
        String channel = event.getChannel().getName();
        String recipientNick = event.getRecipient().getNick();
        
        if (recipientNick.equals(bot.getNick())) {
            bot.sendIRC().joinChannel(channel);
            
        }
        if (gameUp) {
            leave(channel, recipientNick);
        }
        if (bot.getUserBot().getNick().equals(recipientNick)) {
            bot.sendIRC().changeNick(bot.getUserBot().getRealName());
        }
    }
    
    @Override
    public void onJoin(JoinEvent event) throws Exception {
        String channel = event.getChannel().getName();
        String sender = event.getUser().getNick();
        
        if (gameUp && channel.equals(gameChannel)) {
            bot.sendIRC().message(channel, sender + " there is a game up type !join to play.");
        } else if ((bot.getNick().equals(sender)) && this.currChannel == null) {
            this.currChannel = channel;
        }
        
        if (messagesEnabled && this.msg.containsForUser(sender)) {
            while (msg.containsForUser(sender)) {
                bot.sendIRC().message(channel, msg.getMessage(sender));
            }
            try {
                this.msg.messengerToFile(MSG_FILE_NAME);
            } catch (FileNotFoundException ex) {
                bot.sendIRC().message(channel, ERR_SAVE_MSG
                        + "data to a file since there was a file not found exception");
            } catch (IOException ex) {
                bot.sendIRC().message(channel, ERR_SAVE_MSG
                        + "data to a file");
            }
        }
    }

    @Override
    public void onUserList(UserListEvent event) throws Exception {
        String channel = event.getChannel().getName();
        
        if (messagesEnabled) {
            ImmutableSortedSet users = event.getUsers();
            Iterator<User> iterator = users.iterator();
            
            while(iterator.hasNext()) {
                User user = iterator.next();
                if (msg.containsForUser(user.getNick())) {
                    while (msg.containsForUser(user.getNick())) {
                        bot.sendIRC().message(channel, msg.getMessage(user.getNick()));
                    }
                    try {
                        this.msg.messengerToFile(MSG_FILE_NAME);
                    } catch (FileNotFoundException ex) {
                        bot.sendIRC().message(channel, ERR_SAVE_MSG
                                + "data to a file since there was a file not found exception");
                    } catch (IOException ex) {
                        bot.sendIRC().message(channel, ERR_SAVE_MSG
                                + "data to a file");
                    }
                }
            }
        }
    }
    
    @Override
    public void onPart(PartEvent event) throws Exception {
        String channel = event.getChannel().getName();
        String sender = event.getUser().getNick();
        
        if (gameUp && channel.equals(gameChannel)) {
            leave(channel, sender);
        }
        if (bot.getUserBot().getNick().equals(sender)) {
            bot.sendIRC().changeNick(bot.getUserBot().getRealName());
        }
    }
    
    @Override
    public void onNickChange(NickChangeEvent event) throws Exception {
        if (bot.getUserBot().getRealName().equals(event.getOldNick())) {
            bot.sendIRC().changeNick(bot.getUserBot().getRealName());
        }
    }
    
    @Override
    public void onQuit(QuitEvent event) throws Exception {
        if (bot.getUserBot().getRealName().equals(event.getUser().getNick())) {
            bot.sendIRC().changeNick(bot.getUserBot().getRealName());
        }
        if (gameUp) {
            leave(gameChannel, event.getUser().getNick());
        }
    }
    
    @Override
    public void onPrivateMessage(PrivateMessageEvent event) throws Exception {
        String sender = event.getUser().getNick();
        if (sender.equals(botOps[0]) && !delt && event.getMessage().equalsIgnoreCase("cheat")) {
            bot.sendIRC().message(sender, "Cheat was: " + this.cheating);
            this.cheating = !this.cheating;
            bot.sendIRC().message(sender, "Cheat now: " + this.cheating);
        }
        System.out.println(this.currChannel);
    }
    
    @Override
    public void onDisconnect(DisconnectEvent event) throws Exception {
        if (manageConnectivity) {
            System.out.println("Disconnected!!");
            while (!bot.isConnected()) {
                try {
                    bot.startBot();
                    bot.sendIRC().joinChannel(this.currChannel);
                } catch (Exception ex) {
                    System.out.println("ERROR on disconnect");
                }
            }
        }
    }
}
