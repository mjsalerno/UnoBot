/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package main.java.uno2;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocketFactory;
import org.pircbotx.Configuration;

import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;

/**
 *
 * @author roofis0
 */
public class unoBotMain {
    
    public static void main(String[] args) throws Exception {
        
        Properties p = new Properties();
        try (FileInputStream in = new FileInputStream(new File("./config.ini"))) {
            p.load(in);
        }
        
//        System.setProperty("socksProxyHost", "localhost");
//        System.setProperty("socksProxyPort", "9999");
        
        String server = p.getProperty("Server", "localhost").trim();
        int port = Integer.parseInt(p.getProperty("Port", "6667").trim());
        String channel = p.getProperty("Channel", "#uno").trim();
        String nick = p.getProperty("Nick", "unoBot").trim();
        String[] botOps = p.getProperty("BotOps", null).trim().split(",");
        String sbFileName = p.getProperty("ScoreBoardFileName", "ScoreBoard.dat").trim();
        String updateScript = p.getProperty("UpdateScript", null);
        String verbose = p.getProperty("Verbose", "false").trim();
        boolean sslEnabled = Boolean.parseBoolean(p.getProperty("SSL", "false").trim());
        String token = p.getProperty("Token", "!").trim();
        
        PircBotX bot;
        Configuration configuration2;
        configuration2 = new Configuration.Builder()
                .setName(nick)
                .setLogin(nick)
//                    .setNickservPassword("pass")
                .setRealName(nick)
                .setAutoReconnect(true)
                .setAutoNickChange(true)
                .setCapEnabled(true)
                .setMessageDelay(500)
                .setServerHostname(server)
                .setServerPort(port)
                .addAutoJoinChannel(channel)
                .setSocketFactory(sslEnabled ? new UtilSSLSocketFactory().trustAllCertificates() : SSLSocketFactory.getDefault())
                .setSocketTimeout(130 * 1000) // Reduce socket timeouts from 5 minutes to 130 seconds
                .setMessageDelay(600) // Reduce message delays from 1 second to 600 milliseconds (need to experiment to get the lowest value without dropping messages)
                .setVersion("mIRC v7.32 Khaled Mardam-Bey") // Set to something funny
                .buildConfiguration();
        
        
        
        try {
            bot = new PircBotX(configuration2);
                        
            UnoBot unobot = new UnoBot(bot, sslEnabled, channel);
            unobot.setBotOps(botOps);
            unobot.setUpdateScript(updateScript);
            unobot.setScoreBoardFileName(sbFileName);
            unobot.setToken(token);
            
            bot.getConfiguration().getListenerManager().addListener(unobot);
            
            bot.startBot();
        }
        catch (Exception ex){
            Logger.getLogger(UnoBot.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
    }
}
