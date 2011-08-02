package uno2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import org.jibble.pircbot.*;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author roofis0
 */
public class unoAIBot extends PircBot {
    private String master = "roofis0";     
    private boolean botAI = false;
    
    public unoAIBot(){
        this.setName("unoAI");
    }  
    
    public void playAI(String channel,Player me,Deck deck){
        Card card = null;
        boolean passed = false;
        if(UnoAI.hasPlayable(me, deck)){
            card = UnoAI.getPlayable(me, deck);
        } else {
            sendMessage(channel, "!draw");
            if (UnoAI.hasPlayable(me, deck)) {
                card = UnoAI.getPlayable(me, deck);
            }else {
                sendMessage(channel,"!pass");
                passed = true;
            }
        }
        
        if(card.color.equals(Card.Color.WILD) && !passed){
            sendMessage(channel,"!play " + card.face.toString() + " " + UnoAI.colorMostOf(me, deck).toString());
        }else if (!passed){
            sendMessage(channel,"!play " + card.color.toString() + " " + card.face.toString());
        }
        
    }
    
    
    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        String[] Tokens = message.split(" ");        
        //NICK
        if ( Tokens[0].equalsIgnoreCase("!nickai") && sender.equalsIgnoreCase(master) ) {
            changeNick(Tokens[1]);            
        }
        //HELP
        
        //JOINC
        else if ( Tokens[0].equalsIgnoreCase("!joincai") && sender.equalsIgnoreCase(master)  ) {
            joinChannel( Tokens[1] );
        }
        //QUIT
        else if ( Tokens[0].equalsIgnoreCase("!quit") && sender.equalsIgnoreCase(master) ) {
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

    

    

    

    
}
