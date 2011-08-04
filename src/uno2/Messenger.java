/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uno2;

import java.util.ArrayList;

/**
 *
 * @author roofis0
 */
public class Messenger {
    
    private ArrayList<String> messages = new ArrayList<String>();
    private ArrayList<String> forUser = new ArrayList<String>();
    private ArrayList<String> fromUser = new ArrayList<String>();
    
    
    public Messenger(){
        
    }
    
    public void setMessage(String fromUser, String forUser, String message){
        this.fromUser.add(fromUser);
        this.forUser.add(forUser);
        this.messages.add(message);
    }
    
    public String getMessage(String forUser) {
        String str = "THERE WAS AN ERROR FINDING THE USER INDEX.";
        if (this.forUser.contains(forUser)) {
            int index = this.forUser.indexOf(forUser);
            forUser = this.forUser.remove(index);
            String fromUser = this.fromUser.remove(index);
            String msg = this.messages.remove(index);
            str = fromUser + " told me to tell " + fromUser + " " + msg;
        }
        return str;
    }

    public boolean containsForUser(String forUser){
        return this.forUser.contains(forUser);
    }

     public String forUserToString() {
        String str = "[";
        for(String users : this.forUser){
            str += users + " ";
        }
        str += "]";
        return str;
    }
}
