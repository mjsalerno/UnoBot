/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uno2;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 *
 * @author roofis0
 */
public class unoBotMain {

    public static void main(String[] args)
            throws Exception {
        
        Properties p = new Properties();
        FileInputStream in = new FileInputStream(new File("./config.ini"));
        p.load(in);
        in.close();

        String server = p.getProperty("Server", "localhost");
        int port = Integer.parseInt(p.getProperty("Port", "6667"));
        String channel = p.getProperty("Channel", "#uno");
        String nick = p.getProperty("Nick", "unoBot");
        String[] botOps = p.getProperty("BotOps", null).split("'");
        String updateScript = p.getProperty("UpdateScript", null);

        unoBot bot = new unoBot(nick);       
        bot.setTimeout(5);
        bot.setBotOps(botOps);
        bot.setUpdateScript(updateScript);
        bot.setMessageDelay(500);
        bot.setVerbose(true);
        bot.setAutoNickChange(true);
        bot.connect(server, port);
        bot.joinChannel(channel);
    }
}
