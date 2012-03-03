/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uno2;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author roofis0
 */
public class Messenger implements Serializable{
    
    private ArrayList<String> messages = new ArrayList<>();
    private ArrayList<String> forUser = new ArrayList<>();
    private ArrayList<String> fromUser = new ArrayList<>();
    
    
    public Messenger(){        
    }
    
    public Messenger(String fileName)throws IOException, ClassNotFoundException{
        File file = new File(fileName);
        try (ObjectInputStream os = new ObjectInputStream(new FileInputStream(file))) {
            Messenger oldMSG = (Messenger) os.readObject();
            this.forUser = oldMSG.forUser;
            this.fromUser = oldMSG.fromUser;
            this.messages = oldMSG.messages;
        }
    }
    
    public void MessengerToFile(String fileName) throws FileNotFoundException, IOException{
        File file = new File(fileName);
        try (FileOutputStream fs = new FileOutputStream(file); ObjectOutputStream os = new ObjectOutputStream(fs)) {
            os.writeObject(this);
            os.close();
            fs.close();
        }
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
            String fromUserr = this.fromUser.remove(index);
            String msg = this.messages.remove(index);
            str = fromUserr + " told me to tell " + forUser + " " + msg;
        }
        return str;
    }

    public boolean containsForUser(String forUser){
        return this.forUser.contains(forUser);
    }
    
    public boolean isEmpty(){
        return this.messages.isEmpty();
    }

     public String forUserToString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("[");
        for(String users : this.forUser){
            sb.append(users);
        }
        sb.append("]");
        return sb.toString();
    }
}
