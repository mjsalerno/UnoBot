package uno2;

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
public class unoAIBot extends ListenerAdapter<PircBotX>{

    private String[] botOps;
    private boolean justDrew;
    private Deck savedDeck = null;
    private Player savedMe;
    private String savedChannel;
    private PircBotX bot;

    public unoAIBot(PircBotX bot) {
    	this.bot = bot;
        bot.setName("unoAI");
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
        if (UnoAI.hasPlayable(me, deck)) {
            card = UnoAI.getPlayable(me, deck);
        } else {
            justDrew = true;
            bot.sendMessage(channel, "!draw");
            savedDeck = deck;
            savedMe = me;
            savedChannel = channel;
        }

        if (!justDrew && card.color.equals(Card.Color.WILD)) {
        	bot.sendMessage(channel, "!play " + card.face.toString() + " " + UnoAI.colorMostOf(me, deck).toString());
        } else if (!justDrew) {
        	bot.sendMessage(channel, "!play " + card.color.toString() + " " + card.face.toString());
        }

    }

    @Override
	public void onMessage(MessageEvent<PircBotX> event) throws Exception {
		String sender = event.getUser().getNick();
		String channel = event.getChannel().getName();    
        String[] Tokens = event.getMessage().split(" ");

        //NICK
        if (Tokens[0].equalsIgnoreCase("!nickai") && this.isBotOp(sender)) {
        	bot.changeNick(Tokens[1]);
        } //HELP
        //JOINC
        else if (Tokens[0].equalsIgnoreCase("!joincai") && this.isBotOp(sender)) {
        	bot.joinChannel(Tokens[1]);
        } //QUIT
        else if (Tokens[0].equalsIgnoreCase("!quit") && this.isBotOp(sender)) {
        	bot.quitServer();
            System.exit(0);
        } //UNO
        else if (Tokens[0].equalsIgnoreCase("!uno")) {
        	bot.sendMessage(channel, "!join");
        }
    }

	@Override
	public void onKick(KickEvent<PircBotX> event) throws Exception {
		String recipientNick = event.getRecipient().getNick();
        if (recipientNick.equals(bot.getNick())) {
            bot.joinChannel( event.getChannel().getName() );
        }
    }
    
    

    @Override
	public void onNotice(NoticeEvent<PircBotX> event) throws Exception {    	
		String notice = event.getNotice();
		
		System.out.println("OnNotice 3 justDrew=" + justDrew);

        if (justDrew && notice.contains("drew")) {
            Card drawnCard = null;
            String[] split = notice.split(" ");
            drawnCard = UnoEngine.stringToCard(split[3] + " " + split[4]);
            justDrew = false;

            if (savedMe.isCardPlayable(drawnCard, savedDeck)) {
                if (drawnCard.color.equals(Card.Color.WILD)) {
                	bot.sendMessage(savedChannel, "!play " + drawnCard.face.toString() + " " + UnoAI.colorMostOf(savedMe, savedDeck).toString());
                } else {
                    bot.sendMessage(savedChannel, "!play " + drawnCard.color.toString() + " " + drawnCard.face.toString());
                }
            } else {
                bot.sendMessage(savedChannel, "!pass");
            }

        }
    }
}
