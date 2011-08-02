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
class Card implements Comparable,Comparator{
    
    public enum Color{RED,BLUE,GREEN,YELLOW,WILD,DEFAULT};
    public enum Face {ZERO,ONE,TWO,THREE,FOUR,FIVE,SIX,SEVEN,EIGHT,NINE,R,S,D2,WILD,WD4,DEFAULT};
    final Color color;
    final Face face;
    
    public Card(Color color, Face face){
        this.color = color;
        this.face = face;
    }
    
    public Card(){
        this.color=Color.DEFAULT;
        this.face=Face.DEFAULT;
    }
    
    @Override
    public String toString(){
        return this.color.toString() + " " + this.face.toString();
    }
    
    public String toStringIRC(){
        String col = this.color.toString();
        if(col.equals("RED") || col.equals("BLUE") || col.equals("GREEN") || col.equals("YELLOW")){
        return "Colors." + col + " + " + col + " " + this.face.toString();
        }else return toString();
    }
    
    @Override
    public boolean equals(Object obj){
        boolean equ = false;
        if(obj instanceof Card){
            Card card = (Card)obj;
            if(card != null){
                equ = (this.face.equals(card.face)) && (this.color.equals(card.color));
            }
        }
        return equ;
    }
    
    private int value(){
        int num = 0;
        if(this.color.equals(Color.RED)){
            num += 100;
        }else if(this.color.equals(Color.GREEN)){
            num += 200;
        }else if(this.color.equals(Color.BLUE)){
            num += 300;
        }else if(this.color.equals(Color.YELLOW)){
            num += 400;
        }else num+= 500;
        
        if(this.face.equals(Face.ONE)){
            num += 1;
        }else if(this.face.equals(Face.TWO)){
            num += 2;
        }else if(this.face.equals(Face.THREE)){
            num += 3;
        }else if(this.face.equals(Face.FOUR)){
            num += 4;
        }else if(this.face.equals(Face.FIVE)){
            num += 5;
        }else if(this.face.equals(Face.SIX)){
            num += 6;
        }else if(this.face.equals(Face.SEVEN)){
            num += 7;
        }else if(this.face.equals(Face.EIGHT)){
            num += 8;
        }else if(this.face.equals(Face.NINE)){
            num += 9;
        }else if(this.face.equals(Face.ZERO)){
            num += 0;
        }else if(this.face.equals(Face.S)){
            num += 10;
        }else if(this.face.equals(Face.R)){
            num += 11;
        }else if(this.face.equals(Face.D2)){
            num += 12;
        }else if(this.face.equals(Face.WILD)){
            num += 13;
        }else num += 14;        
        return num;
    }
    
    @Override
    public int compareTo(Object o) {
        int thisV = value();
        int thatV = 515;
        if(o instanceof Card){
            Card card = (Card)o;
            if(card != null){
                thatV = card.value();
            }
    }
        return thisV - thatV;   
    
}
    
    @Override
    public int compare(Object o1, Object o2) {
        int thisV = 515;
        int thatV = 515;

        if (o1 instanceof Card) {
            Card card1 = (Card) o1;
            if (card1 != null) {
                thisV = card1.value();
            }
        }

        if (o2 instanceof Card) {
            Card card2 = (Card) o2;
            if (card2 != null) {
                thatV = card2.value();
            }
        }
        return thisV - thatV;
    }
}
