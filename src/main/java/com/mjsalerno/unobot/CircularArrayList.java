/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mjsalerno.unobot;

import java.util.ArrayList;

/**
 * @author Michael Salerno 
 **/
public class CircularArrayList<T> extends ArrayList<T>{
    
	private static final long serialVersionUID = 1L;
	
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
    	at++;
        if ( at >= super.size()  ) 
        	at = 0;
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
