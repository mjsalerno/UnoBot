/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.uno2;

import java.util.Comparator;

/**
 *
 * @author roofis0
 */
public class AIHandComparator implements Comparator<Card>{

    @Override
    public int compare(Card o1, Card o2) {
        return o1.valueForAI() - o2.valueForAI();
    }
    
}
