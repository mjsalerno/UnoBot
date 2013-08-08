/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uno2;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;

/**
 *
 * @author roofis0
 */
public class unoBotMain {

    public static void main(String[] args)
            throws Exception {
        
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
        
        PircBotX bot = new PircBotX();
        

        bot.setMessageDelay(500);
        bot.setVerbose(Boolean.parseBoolean(verbose));
        bot.setAutoNickChange(true);
        bot.setName(nick);
        
        if (sslEnabled) {
            bot.connect(server, port, new UtilSSLSocketFactory().trustAllCertificates() );
        } else {
            bot.connect(server, port);
        }
        
        UnoBot unobot = new UnoBot(bot, nick, sslEnabled);
        unobot.setBotOps(botOps);
        unobot.setUpdateScript(updateScript);
        unobot.setScoreBoardFileName(sbFileName);
        
        bot.getListenerManager().addListener(unobot);
        
        bot.joinChannel(channel);
        
    }
}
