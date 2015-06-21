/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main.java.uno2;

import java.util.ArrayList;

/**
 * @author Michael Salerno 
 **/
public class CircularArrayList<T> extends ArrayList<T>{
    
    private int at;
    
    public CircularArrayList(){
        super();
        at = 0;
    }
    
    @Override
    public void clear(){
        super.clear();
        at = 0;
    }

    public T next(){
        if ( ++at > (super.size() - 1) ) at = 0;
        return super.get(at);
    }
    
     public T prev(){
        if ( --at < 0 ) at = (super.size() - 1);
        return super.get(at);
    }

    public T at() {
        return super.get(at);
    }
}
