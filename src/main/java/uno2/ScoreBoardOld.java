/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.uno2;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author roofis0
 */
public class ScoreBoardOld {
    
    public static void updateScore(String[] arr) throws FileNotFoundException, IOException {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(arr));
        
        boolean winAdded = false;
        
        File score = new File("score.txt");
        File lastGame = new File("lastGame.txt");
        
            score.createNewFile();
        Scanner in;
        try (PrintWriter out = new PrintWriter(new FileOutputStream(lastGame))) {
            in = new Scanner(new FileInputStream(score));
            String[] split;
            int temp ;
            while (in.hasNext()) {
                split = in.nextLine().split(" ");
                if(!winAdded && split[0].equals(list.get(0))){
                    temp = Integer.parseInt(split[1]);
                    temp++;
                    winAdded = true;
                    out.println(split[0] + " " + temp + " " + split[2]);
                    list.remove(0);                    
                }else if(list.contains(split[0])){
                    temp = Integer.parseInt(split[2]);
                    temp++;
                    out.println(split[0] + " " + split[1] + " " + temp);
                    list.remove(split[0]);
                }else{
                    out.println(split[0] + " " + split[1] + " " + split[2]);
                    list.remove(split[0]);
                }
            }
            if(!list.isEmpty()){
                if(!winAdded){
                    out.println(list.get(0) + " 1 " + "0");
                    list.remove(0);
                }
                for(String str : list){
                    out.println(str + " 0 " + "1");
                }
                
            }
        }
            in.close();
            score.delete();
            lastGame.renameTo(score);
            sortScore();
    }
    
    
    
    public static void sortScore() throws FileNotFoundException{
        File score = new File("score.txt");        
        LinkedList<String> file = new LinkedList(); 
        try (Scanner in = new Scanner(new FileInputStream(score))) {
            while (in.hasNext()) {
                file.add(in.nextLine());
            }
        }
            
            ScoreComparator sc = new ScoreComparator();
            Collections.sort(file, sc);
        try (PrintWriter out = new PrintWriter(new FileOutputStream(score))) {
            for(String str : file){
                out.println(str);
            }
        }
            
        
    }
    
    public static void sortScoreBoard(){
        try {
            File score = new File("score.txt");
            ArrayList file = new ArrayList();
            try (Scanner in = new Scanner(new FileInputStream(score))) {
                while (in.hasNext()) {
                    file.add(in.nextLine());
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ScoreBoardOld.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
