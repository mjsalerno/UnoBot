/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mjsalerno.unobot.score;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import com.mjsalerno.unobot.Player;
import com.mjsalerno.unobot.PlayerList;

/**
 * @author roofis0
 *
 **/
public class ScoreBoard2 implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private HashMap<String, ScoreCard> scores = new HashMap<>();
    

    
    public ScoreBoard2(){
    }
    
    public ScoreBoard2(String fileName) throws IOException, ClassNotFoundException{
        File file = new File(fileName);
        try (ObjectInputStream os = new ObjectInputStream(new FileInputStream(file))) {
            ScoreBoard2 oldSB = (ScoreBoard2) os.readObject();
            this.scores = oldSB.scores;
        }
    }

    
    public void updateScoreBoard(PlayerList pl){
        boolean won;
        
        
        for(Map.Entry<Player,Integer> e : pl.getPointMap().entrySet()){
            int scoreL = e.getValue();
            Player p = e.getKey();
            
            if(scoreL == 0){
                won = true;
            }else{
                won = false;
            }
            
            scores.putIfAbsent(p.getName(), new ScoreCard(p.getName()));
            ScoreCard card = scores.get(p.getName());
            card.addScore(scoreL);
            if (won) 
            	card.incrementWins();
            else
            	card.incrementLosses();
            

        }
    }
    
    public void scoreBoardToFile(String fileName) throws FileNotFoundException, IOException{
        File file = new File(fileName);
        try (FileOutputStream fs = new FileOutputStream(file); ObjectOutputStream os = new ObjectOutputStream(fs)) {
            os.writeObject(this);
            os.close();
            fs.close();
        }
    }
    
    public boolean isEmpty(){
        return this.scores.isEmpty();
    }
    
    public Collection<ScoreCard> getTop10() {
       	
    	ArrayList<ScoreCard> top10 = new ArrayList<>();
    	
    	TreeSet<ScoreCard> sorted = new TreeSet<>(scores.values());
    	for (ScoreCard card : sorted) {
    		top10.add(card);
    		
    		if (top10.size() >= 10) 
    			break;
    	}
    	return top10;
    }
    /*
    public int size(){
        return this.scores.size();
    }
    
    /*
    public String playerRankToString(int index){
        Double d = this.getRank(index);
        DecimalFormat df = new DecimalFormat("0.0");    
        
        return String.format("%10s  %d:%d  %s", 
                this.players.get(index),
                this.wins.get(index),
                this.losses.get(index),
                df.format(d));
    }
    
    
    public Double getRank(int index){
        Double los = this.losses.get(index).doubleValue();
        if (los == 0.0) los = 1.0;
       return this.wins.get(index)/los;        
    }*/

 
    public Collection<ScoreCard> getScores() {
    	return new TreeSet<ScoreCard>(scores.values());
    }
    
    @Override
    public String toString(){
        String str = "";
        for (ScoreCard card : scores.values()) {
        	
            str += "Player: " + card.getName() + " Score: " + card.getScore() + "\n";
        }
        return str;
    }
    
}
