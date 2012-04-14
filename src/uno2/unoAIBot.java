package uno2;

import org.jibble.pircbot.PircBot;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author roofis0
 */
public class unoAIBot extends PircBot {
    private String[] botOps;      
    private boolean justDrew;
    private Deck savedDeck = null;
    private Player savedMe;
    private String savedChannel;
    
    public unoAIBot(){
        this.setName("unoAI");
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
    
    public void playAI(String channel,Player me,Deck deck){
        Card card = null;
        if(UnoAI.hasPlayable(me, deck)){
            card = UnoAI.getPlayable(me, deck);
        } else {
            justDrew = true;
            sendMessage(channel, "!draw");
            savedDeck = deck;    
            savedMe = me;
            savedChannel = channel;
        }
        
        if( !justDrew && card.color.equals(Card.Color.WILD) ){
            sendMessage(channel,"!play " + card.face.toString() + " " + UnoAI.colorMostOf(me, deck).toString());
        }else if (!justDrew){
            sendMessage(channel,"!play " + card.color.toString() + " " + card.face.toString());
        }
        
    }
    
    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        String[] Tokens = message.split(" ");        
        //NICK
        if ( Tokens[0].equalsIgnoreCase("!nickai") && this.isBotOp(sender) ) {
            changeNick(Tokens[1]);            
        }
        //HELP
        
        //JOINC
        else if ( Tokens[0].equalsIgnoreCase("!joincai") && this.isBotOp(sender) ) {
            joinChannel( Tokens[1] );
        }
        //QUIT
        else if ( Tokens[0].equalsIgnoreCase("!quit") && this.isBotOp(sender) ) {
            quitServer();
            System.exit(0);
        }
        //UNO
        else if ( Tokens[0].equalsIgnoreCase("!uno")){
                sendMessage(channel, "!join");            
        }
}
    
    @Override
    public void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason){
        if(recipientNick.equals(this.getNick())){
            this.joinChannel(channel);
  
        }
        
        
    }

    @Override
    protected void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {
        if(justDrew && notice.contains("drew")){
            Card drawnCard = null;
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println(notice);
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            String[] split = notice.split(" ");
            drawnCard = UnoEngine.stringToCard(split[4] + " " + split[5]);
            justDrew = false;

            if (savedMe.isCardPlayable(drawnCard, savedDeck)) {
                if (drawnCard.color.equals(Card.Color.WILD)) {
                    sendMessage(savedChannel, "!play " + drawnCard.face.toString() + " " + UnoAI.colorMostOf(savedMe, savedDeck).toString());
                } else {
                    sendMessage(savedChannel, "!play " + drawnCard.color.toString() + " " + drawnCard.face.toString());
                }
            } else {
                sendMessage(savedChannel, "!pass");
            }

        }
    }
}
