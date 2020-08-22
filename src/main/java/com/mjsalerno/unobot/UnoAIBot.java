package com.mjsalerno.unobot;


import org.pircbotx.Colors;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.KickEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.NoticeEvent;

import com.mjsalerno.unobot.opers.NullOperValidator;
import com.mjsalerno.unobot.opers.OperValidator;

/*
* To change this template, choose Tools | Templates and open the template in
* the editor.
*/

/**
 *
 * @author roofis0
 */
public class UnoAIBot extends ListenerAdapter {
    
    private static final String PLAY_SPACE = "!play ";
    private static final String SHLD_SND_MSG = "SHOULD HAVE SENT MESSAGE";
    
    private OperValidator botOps = new NullOperValidator();
    private boolean justDrew;
    private Deck savedDeck = null;
    private Player savedMe;
    private String savedChannel;
    private PircBotX bot;
    
    public UnoAIBot(PircBotX bot) {    	
        this.bot = bot;
        justDrew = false;
    }
    
    public void setBotOps(OperValidator botOps) {
        this.botOps = botOps;
    }
    

    public void playAI(String channel, Player me, Deck deck) {
        Card card = null;
        System.out.println("PLAYING AS AI");
        if (UnoAI.hasPlayable(me, deck)) {
            card = UnoAI.getPlayable(me, deck);
        } else {
            justDrew = true;
            bot.sendIRC().message(channel, "!draw");
            savedDeck = deck;
            savedMe = me;
            savedChannel = channel;
        }
        
        if (!justDrew && card.color.equals(Card.Color.WILD)) {
            bot.sendIRC().message(channel, PLAY_SPACE + UnoAI.colorMostOf(me, deck).toString() + " " + card.face.toString());
            System.out.println(SHLD_SND_MSG);
        } else if (!justDrew) {
            bot.sendIRC().message(channel, PLAY_SPACE + card.color.toString() + " " + card.face.toString());
            System.out.println(SHLD_SND_MSG);
        }
        
    }
    
    
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String sender = event.getUser().getNick();
        String channel = event.getChannel().getName();
        String[] Tokens = event.getMessage().split(" ");
        
        //NICK
        if (Tokens[0].equalsIgnoreCase("!nickai") && botOps.isOper(sender)) {
            bot.sendIRC().changeNick(Tokens[1]);
        } //HELP
        //JOINC
        else if (Tokens[0].equalsIgnoreCase("!joincai") && botOps.isOper(sender)) {
            bot.sendIRC().joinChannel(Tokens[1]);
        } //QUIT
        else if (Tokens[0].equalsIgnoreCase("!quit") && botOps.isOper(sender)) {        
            bot.stopBotReconnect();
            bot.sendIRC().quitServer();
        } //UNO
        else if (Tokens[0].equalsIgnoreCase("!uno")) {
            Thread.sleep(500);
            bot.sendIRC().message(channel, "!join");
        } else if (event.getMessage().startsWith( bot.getNick() + " there is a game up")) {
        	bot.sendIRC().message(channel, "!join");
        }        
    }
    
    @Override
    public void onKick(KickEvent event) throws Exception {
        String recipientNick = event.getRecipient().getNick();
        if (recipientNick.equals(bot.getNick())) {
            bot.sendIRC().joinChannel( event.getChannel().getName() );
        }
    }
    

    
    @Override
    public void onNotice(NoticeEvent event) throws Exception {

    	String notice = Colors.removeFormattingAndColors(event.getNotice());
        
        if (justDrew && notice.contains("drew")) {
        	
        	
            Card drawnCard = null;
            String[] split = notice.split(" ");

            drawnCard = Card.parse(split[3] + " " + split[4]);
            
            
            justDrew = false;
            

            
            if (savedMe.isCardPlayable(drawnCard, savedDeck)) {

                if (drawnCard.color.equals(Card.Color.WILD)) {
                    bot.sendIRC().message(savedChannel, PLAY_SPACE + UnoAI.colorMostOf(savedMe, savedDeck).toString() + " " + drawnCard.face.toString());
                    System.out.println(SHLD_SND_MSG);
                } else {
                    bot.sendIRC().message(savedChannel, PLAY_SPACE + drawnCard.color.toString() + " " + drawnCard.face.toString());
                    System.out.println(SHLD_SND_MSG);
                }
            } else {
                bot.sendIRC().message(savedChannel, "!pass");
                System.out.println(SHLD_SND_MSG);
            }
            
        }
        
        if(justDrew && notice.contains("attacked")) {
            Card card = null;
            justDrew = false;
            if (UnoAI.hasPlayable(savedMe, savedDeck)) {
                card = UnoAI.getPlayable(savedMe, savedDeck);
            } else {
                bot.sendIRC().message(savedChannel, "!pass");
            }
            if (!justDrew && card.color.equals(Card.Color.WILD)) {
                bot.sendIRC().message(savedChannel, PLAY_SPACE + UnoAI.colorMostOf(savedMe, savedDeck).toString() + " " + card.face.toString());
            } else if (!justDrew) {
                bot.sendIRC().message(savedChannel, PLAY_SPACE + card.color.toString() + " " + card.face.toString());
            }
        }
    }
}
