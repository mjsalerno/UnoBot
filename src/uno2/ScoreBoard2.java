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
    
    public ScoreBoard2(){
    }
    
    public ScoreBoard2(String fileName) throws IOException, ClassNotFoundException{
        File file = new File(fileName);
        try (ObjectInputStream os = new ObjectInputStream(new FileInputStream(file))) {
            ScoreBoard2 oldSB = (ScoreBoard2) os.readObject();
            this.players = oldSB.players;
            this.score = oldSB.score;
        }
    }
    
    public void updateScore(int score, int index){
        this.score.set(index, this.score.get(index) + score);
    }
    
    public void updateScoreBoard(PlayerList pl){
        int at;
        int scoreL;
        for(Player p : pl){
            scoreL = p.points();
            if(scoreL == 0){
                scoreL = pl.pointSum();
            }else{
                scoreL /= 2;
            }
            if( this.players.contains(p.who())){
                at = this.players.lastIndexOf(p.who());
                updateScore(scoreL, at);
            }else{
                this.players.add(p.who());
                this.score.add(0);
                updateScore(scoreL, this.players.indexOf(p.who()));
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
