package com.mjsalerno.unobot;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.KickEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.NoticeEvent;

/*
* To change this template, choose Tools | Templates and open the template in
* the editor.
*/

/**
 *
 * @author roofis0
 */
public class UnoAIBot extends ListenerAdapter {
    
    private String[] botOps;
    private boolean justDrew;
    private Deck savedDeck = null;
    private Player savedMe;
    private String savedChannel;
    private PircBotX bot;
    
    public UnoAIBot(PircBotX bot) {
        this.bot = bot;
        justDrew = false;
    }
    
    public void setBotOps(String[] botOps) {
        this.botOps = botOps;
    }
    
    private boolean isBotOp(String nick) {
        for (String i : botOps) {
            if (i.equalsIgnoreCase(nick)) {
                return true;
            }
        }
        return false;
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
            bot.sendIRC().message(channel, "!play " + card.face.toString() + " " + UnoAI.colorMostOf(me, deck).toString());
            System.out.println("SHOULD HAVE SENT MESSAGE");
        } else if (!justDrew) {
            bot.sendIRC().message(channel, "!play " + card.color.toString() + " " + card.face.toString());
            System.out.println("SHOULD HAVE SENT MESSAGE");
        }
        
    }
    
    
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String sender = event.getUser().getNick();
        String channel = event.getChannel().getName();
        String[] Tokens = event.getMessage().split(" ");
        
        //NICK
        if (Tokens[0].equalsIgnoreCase("!nickai") && this.isBotOp(sender)) {
            bot.sendIRC().changeNick(Tokens[1]);
        } //HELP
        //JOINC
        else if (Tokens[0].equalsIgnoreCase("!joincai") && this.isBotOp(sender)) {
            bot.sendIRC().joinChannel(Tokens[1]);
        } //QUIT
        else if (Tokens[0].equalsIgnoreCase("!quit") && this.isBotOp(sender)) {
            bot.sendIRC().quitServer();
            System.exit(0);
        } //UNO
        else if (Tokens[0].equalsIgnoreCase("!uno")) {
            Thread.sleep(3000);
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
        String notice = event.getNotice();
        if (justDrew && notice.contains("drew")) {
            Card drawnCard = null;
            String[] split = notice.split(" ");
            drawnCard = UnoEngine.stringToCard(split[3] + " " + split[4]);
            justDrew = false;
            
            if (savedMe.isCardPlayable(drawnCard, savedDeck)) {
                if (drawnCard.color.equals(Card.Color.WILD)) {
                    bot.sendIRC().message(savedChannel, "!play " + drawnCard.face.toString() + " " + UnoAI.colorMostOf(savedMe, savedDeck).toString());
                    System.out.println("SHOULD HAVE SENT MESSAGE");
                } else {
                    bot.sendIRC().message(savedChannel, "!play " + drawnCard.color.toString() + " " + drawnCard.face.toString());
                    System.out.println("SHOULD HAVE SENT MESSAGE");
                }
            } else {
                bot.sendIRC().message(savedChannel, "!pass");
                System.out.println("SHOULD HAVE SENT MESSAGE");
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
                bot.sendIRC().message(savedChannel, "!play " + card.face.toString() + " " + UnoAI.colorMostOf(savedMe, savedDeck).toString());
            } else if (!justDrew) {
                bot.sendIRC().message(savedChannel, "!play " + card.color.toString() + " " + card.face.toString());
            }
        }
    }
}
