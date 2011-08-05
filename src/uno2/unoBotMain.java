/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uno2;

/**
 *
 * @author roofis0
 */
public class unoBotMain {

    public static void main(String[] args)
            throws Exception {

        //************EDIT THIS************
        
        String master = "roofis0";             // the user the bot will listen to when admin functions are called.
        String server = "spdcx.dyndns.org";    // the server the bot will connect to.
        int port = 7332;                       // the port the bot should connect to on the server.
        String channel = "#spdcx";             // the channel the bot should join once the bot connects.

        unoBot bot = new unoBot();
        bot.setMaster(master);
        bot.setMessageDelay(500);
        bot.setVerbose(true);
        bot.setAutoNickChange(true);
        bot.connect(server, port);
        bot.joinChannel(channel);
    }
}
