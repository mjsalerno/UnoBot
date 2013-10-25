package uno2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
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
public class UnoBot extends ListenerAdapter<PircBotX> {

    private String[] botOps;
    private String gameStarter, updateScript, currChannel = null;
    private final String gameChannel;
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
    private PircBotX bot2 = new PircBotX();
    private unoAIBot bot2ai = new unoAIBot(bot2);
    public Timer timer;
    PircBotX bot;

    /*public UnoBot(boolean usingSSL){
     this("unoBot", usingSSL);
     }*/
    public UnoBot(PircBotX bot, String name, boolean usingSSL, String gameChannel) {
        this.gameChannel = gameChannel;
        this.bot = bot;
        bot.setName(name);
        this.usingSSL = usingSSL;
        try {
            if (new File("Messages.dat").exists()) {
                this.msg = new Messenger("Messages.dat");
            } else {
                this.msg = new Messenger();
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(UnoBot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void startTimer(int seconds) {
        timer = new Timer();
        timer.schedule(new turnTask(), seconds * 1000);
    }

    public class turnTask extends TimerTask {

        public void run() {
            stopTimer();
            Card card = players.at().draw(deck);
            bot.sendMessage(gameChannel, players.at().getName() + " ran out of time! They drew a card and lost thier turn.");
            players.next();
            drew = false;
            bot.sendMessage(gameChannel, "Top Card: " + deck.topCard().toIRCString());
            bot.sendMessage(gameChannel, players.at().getName() + " it is your turn.");
            bot.sendNotice(players.at().getName(), showCards(players.at()));
            startTimer(60);
            if (botAI && (players.at().getName().equals("unoAI"))) {
                bot2ai.playAI(gameChannel, players.at(), deck);
            }
        }
    }

    public void stopTimer() {
        timer.cancel();
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
        bot.sendMessage(channel, players.toString());
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
        this.sb.ScoreBoardToFile("BACKUP_" + this.ScoreBoardFileName);
        this.sb = new ScoreBoard2();
        this.sb.ScoreBoardToFile(this.ScoreBoardFileName);
    }

    private boolean checkWin(String channel, Player player) {
        boolean uno = player.hasUno();
        boolean win = player.hasWin();
        if (uno) {
            bot.sendMessage(channel, Colors.BOLD + Colors.UNDERLINE + Colors.TEAL + player.getName() + " has UNO!!!!");
        } else if (win) {
            attack = false;
            extreme = false;
            bot.sendMessage(channel, Colors.BOLD + Colors.UNDERLINE + Colors.TEAL + player.getName() + " has won the match!!!!");
            int points;
            for (Player p : this.players) {
                points = p.points();
                if (points == 0) {
                    points = players.pointSum();
                } else {
                    points /= 2;
                }
                bot.sendMessage(channel, p.getName() + " : " + points);
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
                sb.ScoreBoardToFile(ScoreBoardFileName);
            } catch (FileNotFoundException ex) {
                bot.sendMessage(channel, "Sorry but I can't find the score board file to save to.");
            } catch (IOException ex) {
                bot.sendMessage(channel, "Sorry but there was an IO exception when i tried to save the score board.");
            }

            if (botAI) {
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

    private void join(String channel, String name) {
        Player player = new Player(name);
        if (!players.contains(player)) {
            players.add(player);
            if (delt) {
                player.draw(deck, 7);
            }
            bot.sendMessage(channel, name + " has joined.");
        } else {
            bot.sendMessage(channel, name + ", you are already in the player list.");
        }
    }

    private void leave(String channel, String name) {
        Player player = new Player(name);
        if (players.contains(player)) {
            if(players.at().getName().equals(player.getName())){
                players.remove(player);
                players.next();
                bot.sendMessage(channel, name + " has quit the game.");
            }else{
                players.remove(player);            
                bot.sendMessage(channel, name + " has quit the game.");
            }
        }
    }

    private String showCards(Player player) {
        return player.cardsToIRCString();
    }

    private void printScore(String channel) throws FileNotFoundException {
        for (int i = 0; i < sb.players.size(); i++) {
            this.bot.sendMessage(channel, this.sb.toString(i));
        }
    }

    @Override
    public void onMessage(MessageEvent<PircBotX> event) throws Exception {
        String message = event.getMessage().trim();
        message = message.replaceAll("( )+", " ").trim(); // replace n amount of spaces with only one
        //message = message.replaceAll("  ", " ");//remove double spaces

        String[] tokens = message.split(" ");
        String sender = event.getUser().getNick();
        String channel = event.getChannel().getName();

        //NICK
        if (tokens[0].equalsIgnoreCase("!nick") && isBotOp(sender)) {
            bot.changeNick(tokens[1]);
            bot.setName(tokens[1]);
        }
        //INFO
        if (tokens[0].equalsIgnoreCase("!info")) {
            bot.sendMessage(channel, "LOGIN: " + bot.getLogin());
            bot.sendMessage(channel, "NAME: " + bot.getName());
            bot.sendMessage(channel, "NICK: " + bot.getNick());
        } //HELP
        else if (tokens[0].equalsIgnoreCase("!unohelp")) {

            bot.sendNotice(sender, "!uno ------ Starts an new UNO game.");
            bot.sendNotice(sender, "!uno +a---- Attack mode: When you draw there is a 20% chance");
            bot.sendNotice(sender, "            that you will be UNO attacked and will have to draw");
            bot.sendNotice(sender, "            anywhere from 0 - 7 cards!");
            bot.sendNotice(sender, "!uno +e --- Extreme mode: This inserts twice as many special cards");
            bot.sendNotice(sender, "            into the deck! Special cards include:");
            bot.sendNotice(sender, "            R, S, D2, W, and WD4");
            bot.sendNotice(sender, "!uno +e +a  Enables both Extreme and Attack mode!");
            bot.sendNotice(sender, "!join ----- Joins an existing UNO game.");
            bot.sendNotice(sender, "!deal ----- Deals out the cards to start an UNO game.");
            bot.sendNotice(sender, "            but only the person that started the game can deal");
            bot.sendNotice(sender, "!wait ----- Stops your turn timer.");
            bot.sendNotice(sender, "!play ----- Plays a card (!play <color> <face>) or (!p <color> <face>)");
            bot.sendNotice(sender, "            to play a RED FIVE !play r 5");
            bot.sendNotice(sender, "!showcards  Shows you your hand. (!hand)");
            bot.sendNotice(sender, "!draw ----- Draws a card when you don't have a playable card.");
            bot.sendNotice(sender, "!pass ----- If you don't have a playable card after you draw");
            bot.sendNotice(sender, "            then you pass.");
            bot.sendNotice(sender, "!unocount - Show how many cards each player has.");
            bot.sendNotice(sender, "!leave ---- If you want to leave the game early.");
            bot.sendNotice(sender, "!what ----- If you were not paying attention this will tell");
            bot.sendNotice(sender, "            you the top card and whos turn it is.");
            bot.sendNotice(sender, "!players -- Displays the player list.");
            bot.sendNotice(sender, "!score ---- Prints out the score board.");
            bot.sendNotice(sender, "!ai ------- Turns the bot ai on or off.");
            bot.sendNotice(sender, "!endgame -- Ends the game, only the person who started the");
            bot.sendNotice(sender, "            game may end it.");

            if (messagesEnabled) {
                bot.sendNotice(sender, "!tell ----- Tell an ofline user a message once they join the channel.");
                bot.sendNotice(sender, "!messages - List all of the people that have messages.");
            }

            bot.sendNotice(sender, "!unohelp ----- This shit.");
            bot.sendNotice(sender, "!rank ----- Shows all users win:lose ratio");
            if (isBotOp(sender)) {
                bot.sendNotice(sender, "----------- OP only" + "-----------");
                bot.sendNotice(sender, "!nick ----- Tells the bot to change his nick.");
                bot.sendNotice(sender, "!joinc ---- Tells the bot to join a channel.");
                bot.sendNotice(sender, "!part ----- Tells the bot to part from a channel.");
                bot.sendNotice(sender, "!quit ----- Tells the bot to dissconnect from the entire server.");
                bot.sendNotice(sender, "!resetSB ----- resets the Score Board.");
            }

        } //JOINC
        else if (tokens[0].equalsIgnoreCase("!joinc") && isBotOp(sender)) {
            bot.joinChannel(tokens[1]);
        } //UPDATE
        else if (tokens[0].equalsIgnoreCase("!update") && this.isBotOp(sender) && this.updateScript != null) {

            try {
                Runtime.getRuntime().exec(updateScript);
            } catch (IOException ex) {
                Logger.getLogger(UnoBot.class.getName()).log(Level.SEVERE, null, ex);
            }

        } //PART
        else if (tokens[0].equalsIgnoreCase("!part") && isBotOp(sender)) {
            Channel chan = bot.getChannel(tokens[1]);
            bot.partChannel(chan, "Bye!");
        } //QUIT
        else if (tokens[0].equalsIgnoreCase("!quit") && isBotOp(sender)) {
            bot.quitServer();
            System.exit(0);
        } //RESET_SB
        else if (tokens[0].equalsIgnoreCase("!resetsb") && isBotOp(sender)) {
            try {
                resetScoreBoard();
                bot.sendMessage(channel, "the Score Board is now empty.");
            } catch (FileNotFoundException ex) {
                bot.sendMessage(channel, "Sorry but i could not find the Score Board file");
            } catch (IOException ex) {
                bot.sendMessage(channel, "Sorry but there was some sort of error.");
            }
        }



        if (!channel.equals(gameChannel)) {
            // Do not respond to a game command that's sent outside the gamechannel 
            return;
        }


        //JOIN
        if (tokens[0].equalsIgnoreCase("!join") && gameUp) {
            join(channel, sender);
            bot.sendMessage(channel, "There are now " + players.size() + " people in the players list");
        } //TELL
        else if (tokens[0].equalsIgnoreCase("!tell") && messagesEnabled == true) {
            String[] msgSplit = event.getMessage().split(" ", 3);
            this.msg.setMessage(sender, tokens[1], msgSplit[2]);
            bot.sendMessage(channel, "ok i will tell them.");
            try {
                this.msg.MessengerToFile("Messages.dat");
            } catch (FileNotFoundException ex) {
                bot.sendMessage(channel, "Sorry but i could not save the message "
                        + "data to a file since there was a file not found exception");
            } catch (IOException ex) {
                bot.sendMessage(channel, "Sorry but i could not save the message "
                        + "data to a file");
            }
        } //MESSAGES
        else if (tokens[0].equalsIgnoreCase("!messages") && messagesEnabled == true) {
            bot.sendMessage(channel, msg.forUserToString());
        } //ENDGAME
        else if ((tokens[0].equalsIgnoreCase("!endgame") && gameUp) && (isBotOp(sender) || sender.equals(gameStarter))) {
            stopTimer();
            attack = false;
            extreme = false;
            gameUp = false;
            delt = false;
            players.clear();
            deck.clear();
            bot.sendMessage(channel, "The game was ended by " + sender);
            if(botAI){
                bot2.disconnect();
                botAI = false;
            }
        } //LEAVE
        else if (tokens[0].equalsIgnoreCase("!leave")) {
            leave(channel, sender);
            stopTimer();
            bot.sendMessage(channel, "Top Card: " + deck.topCard().toIRCString());
            bot.sendMessage(channel, players.at().getName() + " it is your turn.");
            bot.sendNotice(players.at().getName(), showCards(players.at()));
            startTimer(60);
            if(botAI && (players.at().getName().equals("unoAI"))){
                bot2ai.playAI(channel, players.at(), deck);
            }
        } //SCORE
        else if (tokens[0].equalsIgnoreCase("!score")) {
            if (!this.sb.isEmpty()) {
                try {
                    printScore(channel);
                } catch (FileNotFoundException ex) {
                    bot.sendMessage(channel, "Sorry but i can't find the score board.");
                }
            } else {
                this.bot.sendMessage(channel, "The Score Board is empty");
            }
        } //COUNT
        else if (tokens[0].equalsIgnoreCase("!unocount") && delt) {
            bot.sendMessage(channel, players.countCards());
        } //PLAYERS
        else if (tokens[0].equalsIgnoreCase("!players") && gameUp) {
            printPlayers(channel);
        } //AI
        else if (tokens[0].equalsIgnoreCase("!ai") && !gameUp) {
            if (!botAI) {
                bot2.setMessageDelay(4000);
                bot2.setVerbose(false);
                bot2.setAutoNickChange(false);
                bot2ai.setBotOps(botOps);
                bot2.getListenerManager().addListener(bot2ai);
                try {
                    if (usingSSL) {
                        bot2.connect(bot.getServer(), bot.getPort(), new UtilSSLSocketFactory().trustAllCertificates());
                    } else {
                        bot2.connect(bot.getServer(), bot.getPort());
                    }
                } catch (IOException | IrcException ex) {
                    Logger.getLogger(UnoBot.class.getName()).log(Level.SEVERE, null, ex);
                }
                bot2.joinChannel(channel);
                botAI = true;
            } else {
                bot2.disconnect();
                botAI = false;
            }
        } //UNO
        else if (tokens[0].equalsIgnoreCase("!uno")) {
            if (gameUp) {
                bot.sendMessage(channel, "Sorry a game is already started in " + gameChannel);
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
                bot.sendMessage(channel, "type !join to join the game.");
            }
        } //DEAL
        else if ((tokens[0].equalsIgnoreCase("!deal")) && !delt && gameUp && ((sender.equals(gameStarter)) || (isBotOp(sender)))) {
            deck.createDeck(this.extreme);
            players.deal(deck);
            Player playerMaster = new Player(botOps[0]);
            if (cheating && players.contains(playerMaster)) {
                players.get(botOps[0]).clearHand();
                players.get(botOps[0]).drawCard(new Card(Card.Color.WILD, Card.Face.WILD));
            }
            this.delt = true;
            bot.sendMessage(channel, "Top Card: " + deck.topCard().toIRCString());
            bot.sendMessage(channel, players.at().getName() + " it is your turn.");
            bot.sendNotice(players.at().getName(), showCards(players.at()));
            startTimer(60);
            if (botAI && (players.at().getName().equals("unoAI"))) {
                bot2ai.playAI(channel, players.at(), deck);
            }
        } //WHAT
        else if ((tokens[0].equalsIgnoreCase("!what")) && (delt)) {
            bot.sendMessage(channel, "Top Card: " + deck.topCard().toIRCString());
            bot.sendMessage(channel, players.at().getName() + " it is your turn.");
            //sendNotice(players.at().getName(), showCards(players.at()));
        } //WAIT
        else if ((tokens[0].equalsIgnoreCase("!wait")) && delt && (sender.equals(players.at().getName()))) {
            stopTimer();
            bot.sendMessage(channel, players.at().getName() + " stopped their turn timer.");
            //sendNotice(players.at().getName(), showCards(players.at()));
        } //DRAW
        else if ((tokens[0].equalsIgnoreCase("!draw")) && delt && (sender.equals(players.at().getName()))) {
            //sendNotice(sender,"you drew a " + players.at().draw(deck).toIRCString());
            if (!drew) {
                if (attack) {
                    boolean prob = rand.nextInt(5) == 1;
                    if (prob) {
                        int attackDraw = rand.nextInt(8);
                        int attackCount = players.at().draw(deck, attackDraw);
                        if (attackCount == attackDraw) {
                            stopTimer();
                            bot.sendMessage(channel, players.at().getName() + " got UNO attacked! They had to draw " + attackDraw + " cards!");
                            bot.sendNotice(sender, "You just got UNO attacked!");
                            bot.sendNotice(sender, showCards(players.get(sender)));
                            bot.sendNotice(sender, "If you still have no card to play then pass by typing !pass");
                            drew = true;
                        } else {
                            bot.sendMessage(channel, "Deck is empty");
                            drew = false;
                        }
                    } else {
                        Card card = players.at().draw(deck);
                        if (card != null) {
                            stopTimer();
                            bot.sendNotice(sender, "you drew a " + card.toString());
                            bot.sendNotice(sender, "If you still have no card to play then pass by typing !pass");
                            drew = true;
                        } else {
                            bot.sendMessage(channel, "Deck is empty");
                            drew = false;
                        }
                    }
                } else {
                    Card card = players.at().draw(deck);
                    if (card != null) {
                        stopTimer();
                        bot.sendNotice(sender, "you drew a " + card.toString());
                        bot.sendNotice(sender, "If you still have no card to play then pass by typing !pass");
                        drew = true;
                    } else {
                        bot.sendMessage(channel, "Deck is empty");
                        drew = false;
                    }
                }
            } else {
                bot.sendMessage(channel, "Sorry " + sender + " but you already "
                        + "drew a card. If you still have no card to play then "
                        + "pass by typing !pass");
            }
        } //PASS
        else if ((tokens[0].equalsIgnoreCase("!pass")) && delt && (sender.equals(players.at().getName()))) {
            if (drew) {
                stopTimer();
                bot.sendMessage(channel, players.at().getName() + " passed.");
                players.next();
                drew = false;
                bot.sendMessage(channel, "Top Card: " + deck.topCard().toIRCString());
                bot.sendMessage(channel, players.at().getName() + " it is your turn.");
                bot.sendNotice(players.at().getName(), showCards(players.at()));
                startTimer(60);
                if (botAI && (players.at().getName().equals("unoAI"))) {
                    bot2ai.playAI(channel, players.at(), deck);
                }
            } else {
                bot.sendMessage(channel, "You must draw first.");
            }
        } //SHOWCARDS
        else if ((tokens[0].equalsIgnoreCase("!showcards") || tokens[0].equalsIgnoreCase("!hand")) && delt) {
            bot.sendNotice(sender, showCards(players.get(sender)));
        } //RANK
        else if (tokens[0].equalsIgnoreCase("!rank")) {
            for (int i = 0; i < this.sb.size(); i++) {
                this.bot.sendMessage(channel, sb.playerRankToString(i));
            }
        } //PLAY
        else if ((tokens[0].equalsIgnoreCase("!play") || tokens[0].equalsIgnoreCase("!p")) && delt && gameUp && (sender.equals(players.at().getName()))) {
            Card card = null;
            try {
                card = Rules.parse(tokens[1] + " " + tokens[2]);
            } catch (Exception e) {
                bot.sendMessage(channel, "Illegal card");
                return;
            }
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
                            bot.sendNotice(sender, "You must set the new color when playing a WILD card");
                            return;
                        }


                        boolean played = player.playWild(card, Card.Color.valueOf(coler), deck);
                        if (!played) {
                            bot.sendMessage(channel, "Sorry " + sender + " that card is not playable. Try something like !play wild red");
                            return;
                        }
                        players.next();
                        if (card.face.equals(Card.Face.WD4)) {
                            int cardCount = players.at().draw(deck, 4);
                            if (cardCount == 4) {
                                bot.sendMessage(channel, players.at().getName() + " draws 4 cards.");
                            } else {
                                bot.sendMessage(channel, "Deck is empty, " + players.at().getName() + " draws " + cardCount + " cards.");
                            }
                            players.next();
                        }
                    } //SKIP
                    else if (card.face.equals(Card.Face.S)) {
                        player.play(card, deck);
                        bot.sendMessage(channel, players.next().getName() + " was skipped.");
                        players.next();
                    } //REV
                    else if (card.face.equals(Card.Face.R)) {
                        if (players.size() == 2) {
                            player.play(card, deck);
                            bot.sendMessage(channel, players.next().getName() + " was skipped.");
                            players.next();
                        } else {
                            player.play(card, deck);
                            bot.sendMessage(channel, player.getName() + " reversed the order.");
                            players.rev();
                            players.next();
                        }
                    } //D2
                    else if (card.face.equals(Card.Face.D2)) {
                        player.play(card, deck);
                        int cardCount = players.next().draw(deck, 2);
                        if (cardCount == 2) {
                            bot.sendMessage(channel, players.at().getName() + " draws 2 cards.");
                        } else {
                            bot.sendMessage(channel, "Deck is empty, " + players.at().getName() + " draws " + cardCount + " cards.");
                        }

                        players.next();
                    } //THE REST
                    else {
                        player.play(card, deck);
                        players.next();
                    }

                    checkWin(channel, player);

                    //TELL USER TO GO
                    if (gameUp) {
                        bot.sendMessage(channel, "Top Card: " + deck.topCard().toIRCString());
                        bot.sendMessage(channel, players.at().getName() + " it is your turn.");
                        bot.sendNotice(players.at().getName(), showCards(players.at()));
                        startTimer(60);
                        if (botAI && (players.at().getName().equals("unoAI"))) {
                            bot2ai.playAI(channel, players.at(), deck);
                        }
                    }
                } else {
                    bot.sendMessage(channel, "Sorry " + sender + " that card is not playable.");
                }
            } else {
                bot.sendMessage(channel, "Sorry " + sender + " you dont have that card");
            }
        }
    }

    @Override
    public void onKick(KickEvent<PircBotX> event) throws Exception {
        String channel = event.getChannel().getName();
        String recipientNick = event.getRecipient().getNick();

        if (recipientNick.equals(bot.getNick())) {
            bot.joinChannel(channel);

        }
        if (gameUp) {
            leave(channel, recipientNick);
        }
        if (bot.getName().equals(recipientNick)) {
            bot.changeNick(bot.getName());
        }
    }

    @Override
    public void onJoin(JoinEvent<PircBotX> event) throws Exception {
        String channel = event.getChannel().getName();
        String sender = event.getUser().getNick();

        if (gameUp && channel.equals(gameChannel)) {
            bot.sendMessage(channel, sender + " there is a game up type !join to play.");
        } else if ((bot.getNick().equals(sender)) && this.currChannel == null) {
            this.currChannel = channel;
        }

        if (messagesEnabled == true) {
            if (this.msg.containsForUser(sender)) {
                while (msg.containsForUser(sender)) {
                    bot.sendMessage(channel, msg.getMessage(sender));
                }
                try {
                    this.msg.MessengerToFile("Messages.dat");
                } catch (FileNotFoundException ex) {
                    bot.sendMessage(channel, "Sorry but i could not save the message "
                            + "data to a file since there was a file not found exception");
                } catch (IOException ex) {
                    bot.sendMessage(channel, "Sorry but i could not save the message "
                            + "data to a file");
                }
            }
        }
    }

    @Override
    public void onUserList(UserListEvent<PircBotX> event) throws Exception {
        String channel = event.getChannel().getName();

        if (messagesEnabled == true) {
            for (User user : event.getUsers()) {
                if (msg.containsForUser(user.getNick())) {
                    while (msg.containsForUser(user.getNick())) {
                        bot.sendMessage(channel, msg.getMessage(user.getNick()));
                    }
                    try {
                        this.msg.MessengerToFile("Messages.dat");
                    } catch (FileNotFoundException ex) {
                        bot.sendMessage(channel, "Sorry but i could not save the message "
                                + "data to a file since there was a file not found exception");
                    } catch (IOException ex) {
                        bot.sendMessage(channel, "Sorry but i could not save the message "
                                + "data to a file");
                    }
                }
            }
        }
    }

    @Override
    public void onPart(PartEvent<PircBotX> event) throws Exception {
        String channel = event.getChannel().getName();
        String sender = event.getUser().getNick();

        if (gameUp && channel.equals(gameChannel)) {
            leave(channel, sender);
        }
        if (bot.getName().equals(sender)) {
            bot.changeNick(bot.getName());
        }
    }

    @Override
    public void onNickChange(NickChangeEvent<PircBotX> event) throws Exception {
        if (bot.getName().equals(event.getOldNick())) {
            bot.changeNick(bot.getName());
        }
    }

    @Override
    public void onQuit(QuitEvent<PircBotX> event) throws Exception {
        if (bot.getName().equals(event.getUser().getNick())) {
            bot.changeNick(bot.getName());
        }
        if (gameUp) {
            leave(gameChannel, event.getUser().getNick());
        }
    }

    @Override
    public void onPrivateMessage(PrivateMessageEvent<PircBotX> event) throws Exception {
        String sender = event.getUser().getNick();
        if (sender.equals(botOps[0]) && !delt && event.getMessage().equalsIgnoreCase("cheat")) {
            bot.sendMessage(sender, "Cheat was: " + this.cheating);
            this.cheating = !this.cheating;
            bot.sendMessage(sender, "Cheat now: " + this.cheating);
        }
        System.out.println(this.currChannel);
    }

    @Override
    public void onDisconnect(DisconnectEvent<PircBotX> event) throws Exception {
        if (manageConnectivity == true) {
            System.out.println("dissconnected!!");
            while (!bot.isConnected()) {
                try {
                    bot.reconnect();
                    bot.joinChannel(this.currChannel);
                } catch (IOException | IrcException ex) {
                    System.out.println("ERROR on disconnect");
                }
            }
        }
    }
}
