/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uno2;

import java.io.*;
import java.util.ArrayList;

/**
 * @author roofis0
 *
 **/
public class ScoreBoard2 implements Serializable{
    
    ArrayList<String> players = new ArrayList<>();
    ArrayList<Integer> score = new ArrayList<>();
    ArrayList<Integer> wins = new ArrayList<>();
    ArrayList<Integer> losses = new ArrayList<>();
    
    public ScoreBoard2(){
    }
    
    public ScoreBoard2(String fileName) throws IOException, ClassNotFoundException{
        File file = new File(fileName);
        try (ObjectInputStream os = new ObjectInputStream(new FileInputStream(file))) {
            ScoreBoard2 oldSB = (ScoreBoard2) os.readObject();
            this.players = oldSB.players;
            this.score = oldSB.score;
            this.losses = oldSB.losses;
            this.wins = oldSB.wins;
        }
    }
    
    public void updateScore(int score, int index, boolean won){
        this.score.set(index, this.score.get(index) + score);
        
        if(won){
            this.wins.set(index, this.wins.get(index) + 1);
        }else{
            this.losses.set(index, this.losses.get(index) + 1);
        }
    }
    
    public void updateScoreBoard(PlayerList pl){
        int at;
        int scoreL;
        boolean won;
        for(Player p : pl){
            scoreL = p.points();
            if(scoreL == 0){
                won = true;
                scoreL = pl.pointSum();
            }else{
                won = false;
                scoreL /= 2;
            }
            if( this.players.contains(p.who())){
                at = this.players.lastIndexOf(p.who());
                updateScore(scoreL, at, won);
            }else{
                this.players.add(p.who());
                this.score.add(0);
                this.losses.add(0);
                this.wins.add(0);
                updateScore(scoreL, this.players.indexOf(p.who()),won);
            }
        }
    }
    
    public void ScoreBoardToFile(String fileName) throws FileNotFoundException, IOException{
        File file = new File(fileName);
        try (FileOutputStream fs = new FileOutputStream(file); ObjectOutputStream os = new ObjectOutputStream(fs)) {
            os.writeObject(this);
            os.close();
            fs.close();
        }
    }
    
    public boolean isEmpty(){
        return this.players.isEmpty();
    }
    
    public int size(){
        return this.players.size();
    }
    
    public String playerRankToString(int index){
        return String.format("%s  %d:%d  %.2f", 
                this.players.get(index),
                this.wins.get(index),
                this.losses.get(index),
                this.getRank(index));
    }
    
    public double getRank(int index){
        int los = this.losses.get(index);
        if (los == 0) los = 1;
       return this.wins.get(index)/los;        
    }
    
    public double getRank(String player){
        return getRank(indexOf(player));
    }
    
    public int indexOf(String player){
        return this.indexOf(player);
    }
    
    @Override
    public String toString(){
        String str = "";
        for (int i = 0; i < this.players.size(); i++) {
            str += "Player: " + this.players.get(i) + " Score: " + this.score.get(i)+ "\n";
        }
        return str;
    }
    
    public String toString(int i){
        return this.players.get(i) + ": " + this.score.get(i);
    }
}
