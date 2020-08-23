/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package com.mjsalerno.unobot;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.SocketFactory;

import org.pircbotx.Configuration;
import org.pircbotx.Configuration.Builder;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.delay.StaticDelay;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ExceptionEvent;

import com.mjsalerno.unobot.opers.OperValidator;
import com.mjsalerno.unobot.opers.SimpleOperValidator;

/**
 *
 * @author roofis0
 */
public class UnoBotMain {
    
    public static String getVarOrDefault(String key, String def) {
        String javaProp = System.getProperty(key, def);
        return System.getenv().getOrDefault(key, javaProp);
    }
    
    public static void main(String[] args) throws Exception {
        
        Properties p = new Properties();

        String path = getVarOrDefault("CONFIG_PATH","./");
        String filename = path + "/config.ini";
        File configFile = new File(filename);
        if (configFile.exists()) {
            try (FileInputStream in = new FileInputStream(configFile)) {
                p.load(in);
            }
        } else { // Allows loading of the config values from either java properties or environment variables
            p.setProperty("Server", getVarOrDefault("Server", "localhost"));
            p.setProperty("Port", getVarOrDefault("Port", "6667"));
            p.setProperty("Channel", getVarOrDefault("#uno", ""));
            p.setProperty("Nick", getVarOrDefault("unoBot", ""));

            p.setProperty("BotOps", getVarOrDefault("BotOps", ""));
            p.setProperty("ScoreBoardFileName", path + getVarOrDefault("ScoreBoardFileName", "/ScoreBoard.dat"));
            p.setProperty("UpdateScript", getVarOrDefault("UpdateScript", null));
            p.setProperty("SSL", getVarOrDefault("SSL", "false"));
            p.setProperty("Token", getVarOrDefault("Token", "!"));
            p.setProperty("nickSrvPasswd", getVarOrDefault("nickSrvPasswd", null));
            p.setProperty("serverPasswd", getVarOrDefault("serverPasswd", null));
            p.setProperty("webIRCPasswd", getVarOrDefault("webIRCPasswd", null));

            p.setProperty("aiWebIRCPasswd", getVarOrDefault("aiWebIRCPasswd", null));
            p.setProperty("aiServerPasswd", getVarOrDefault("aiServerPasswd", null));
            p.setProperty("aiNickSrvPasswd", getVarOrDefault("aiNickSrvPasswd", null));
            p.setProperty("AINick", getVarOrDefault("AINick", "unoAI"));
        }
//        System.setProperty("socksProxyHost", "localhost");
//        System.setProperty("socksProxyPort", "9999");
        
        String server = p.getProperty("Server", "localhost").trim();
        int port = Integer.parseInt(p.getProperty("Port", "6667").trim());
        String channel = p.getProperty("Channel", "#uno").trim();
        String nick = p.getProperty("Nick", "unoBot").trim();
        OperValidator botOps = new SimpleOperValidator(p.getProperty("BotOps", "") );
        String sbFileName = p.getProperty("ScoreBoardFileName", "ScoreBoard.dat").trim();
        String updateScript = p.getProperty("UpdateScript", null);
        boolean sslEnabled = Boolean.parseBoolean(p.getProperty("SSL", "false").trim());
        String token = p.getProperty("Token", "!").trim();
        String nickSrvPasswd = p.getProperty("nickSrvPasswd");
        String serverPasswd = p.getProperty("serverPasswd");
        String webIRCPasswd = p.getProperty("webIRCPasswd");
        
        //AI Settings
        String aiNick = p.getProperty("AINick", "unoAI").trim();
        String aiNickSrvPasswd = p.getProperty("aiNickSrvPasswd");
        String aiServerPasswd = p.getProperty("aiServerPasswd");
        String aiWebIRCPasswd = p.getProperty("aiWebIRCPasswd");
              
        PircBotX bot;
        Configuration configuration2;
        Builder configBuilder = new Configuration.Builder()
                .setName(nick)
                .setLogin(nick)
                .setRealName(nick)
                .setAutoReconnect(true)
                .setAutoNickChange(true)
                .setCapEnabled(true)
                .setMessageDelay( new StaticDelay(500) )
                .addServer(server, port)
                .addAutoJoinChannel(channel)
                .setSocketFactory(sslEnabled ? new UtilSSLSocketFactory().trustAllCertificates() : SocketFactory.getDefault())
                .setSocketTimeout(130 * 1000) // Reduce socket timeouts from 5 minutes to 130 seconds
                .setVersion("mIRC v7.32 Khaled Mardam-Bey"); // Set to something funny

                if(nickSrvPasswd != null) {
                    configBuilder = configBuilder.setNickservPassword(nickSrvPasswd.trim());
                }

                if(serverPasswd != null) {
                    configBuilder = configBuilder.setServerPassword(serverPasswd.trim());
                }

                if(webIRCPasswd != null) {
                    configBuilder = configBuilder.setWebIrcPassword(webIRCPasswd.trim());
                }

                configuration2 = configBuilder.buildConfiguration();
        
        
        
        try {
            bot = new PircBotX(configuration2);
            UnoBot unobot = new UnoBot(bot, sslEnabled, channel);
            unobot.setBotOps(botOps);
            unobot.setUpdateScript(updateScript);
            unobot.setScoreBoardFileName(sbFileName);
            unobot.setToken(token);
            
            //AI Settings
            unobot.setUnoAINick(aiNick);            
            unobot.setAiNickSrvPasswd(aiNickSrvPasswd);
            unobot.setAiServerPasswd(aiServerPasswd);
            unobot.setAiWebIRCPasswd(aiWebIRCPasswd);
            
            bot.getConfiguration().getListenerManager().addListener(unobot);
            bot.getConfiguration().getListenerManager().addListener(new ExceptionListener() );
            
            bot.startBot();
        }
        catch (Exception ex){
            Logger.getLogger(UnoBot.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
    }
    
	static class ExceptionListener extends ListenerAdapter {
		@Override
		public void onException(ExceptionEvent event) throws Exception {
			Exception ex = event.getException();
			Logger.getLogger(UnoBot.class.getName()).log(Level.SEVERE, null, ex);
		}		
	}    
    
}
