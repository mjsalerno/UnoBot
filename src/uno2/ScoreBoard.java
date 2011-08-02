/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uno2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author roofis0
 */
public class ScoreBoard {
    
    public static void updateScore(String[] arr) throws FileNotFoundException, IOException {
        ArrayList<String> list = new ArrayList<String>();
        list.addAll(Arrays.asList(arr));
        
        boolean winAdded = false;
        
        File score = new File("score.txt");
        File lastGame = new File("lastGame.txt");
        
            score.createNewFile();
            PrintWriter out = new PrintWriter(new FileOutputStream(lastGame));
            Scanner in = new Scanner(new FileInputStream(score));
            String[] split;
            int temp = 0;
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
                    winAdded = true;
                    list.remove(0);
                }
                for(String str : list){
                    out.println(str + " 0 " + "1");
                }
                
            }
            out.close();
            in.close();
            score.delete();
            lastGame.renameTo(score);
            sortScore();
    }
    
    
    
    public static void sortScore() throws FileNotFoundException{
        File score = new File("score.txt");        
        LinkedList<String> file = new LinkedList(); 
                 
            Scanner in = new Scanner(new FileInputStream(score));
            while (in.hasNext()) {
                file.add(in.nextLine());
            }
            in.close();
            
            ScoreComparator sc = new ScoreComparator();
            Collections.sort(file, sc);
            
            PrintWriter out = new PrintWriter(new FileOutputStream(score));
            for(String str : file){
                out.println(str);
            }
            out.close();
            
        
    }
    
    public static void sortScoreBoard(){
        try {
            File score = new File("score.txt");
            ArrayList file = new ArrayList();
            
            Scanner in = new Scanner(new FileInputStream(score));
                while (in.hasNext()) {
                    file.add(in.nextLine());
                }
                in.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ScoreBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
