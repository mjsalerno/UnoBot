
public class unoBotMain {

    public static void main(String[] args)
            throws Exception {
        unoBot bot = new unoBot();
        bot.setMessageDelay(500);        
        bot.setVerbose(true);
        bot.setAutoNickChange(true);
        bot.connect("spdcx.dyndns.org", 7332);
        bot.joinChannel("#spdcx");        
        
        
//        unoAIBot bot2 = new unoAIBot();
//        bot2.setMessageDelay(500);        
//        bot2.setVerbose(false);
//        bot2.setAutoNickChange(true);
//        bot2.connect("spdcx.dyndns.org", 7332);
//        bot2.joinChannel("#spdcx");
        


    }
}
