/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uno2;

import java.util.Comparator;

/**
 *
 * @author roofis0
 */
public class ScoreComparator implements Comparator<String>{

    @Override
    public int compare(String o1, String o2) {
        String[] split1 = o1.split(" ");
        String[] split2 = o2.split(" ");
        
        Double a1 = Double.parseDouble(split1[1]);
        Double b1 = Double.parseDouble(split1[2]);
        Double score1 = ((a1)/(a1+b1));
        
        Double a2 = Double.parseDouble(split2[1]);
        Double b2 = Double.parseDouble(split2[2]);
        Double score2 = ((a2)/(a2+b2));
        
        int result = 0;
        if(score1 > score2){
            result = -1;
        }else if(score1 < score2){
            result = 1;
        }
       
        return result;
    }
    
}
