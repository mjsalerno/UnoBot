/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uno2;

import java.util.Comparator;
import org.jibble.pircbot.Colors;



/**
 *
 * @author roofis0
 */

public class Card implements Comparable,Comparator{

    
    public enum Color{RED,BLUE,GREEN,YELLOW,WILD,DEFAULT};
    public enum Face {ZERO,ONE,TWO,THREE,FOUR,FIVE,SIX,SEVEN,EIGHT,NINE,R,S,D2,WILD,WD4,DEFAULT};
    final Color color;
    final Face face;
    final int points;
    
    public Card(Color color, Face face){
        this.color = color;
        this.face = face;
        
        switch(face){
         
            case ZERO:
                this.points = 0;
                break;
            case ONE:
                this.points = 1;
                break;
            case TWO:
                this.points = 2;
                break;
            case THREE:
                this.points = 3;
                break;
            case FOUR:
                this.points = 4;
                break;
            case FIVE:
                this.points = 5;
                break;
            case SIX:
                this.points = 6;
                break;
            case SEVEN:
                this.points = 7;
                break;
            case EIGHT:
                this.points = 8;
                break;
            case NINE:
                this.points = 9;
                break;
            case D2:
                this.points = 20;
                break;
            case R:
                this.points = 20;
                break;
            case WILD:
                this.points = 50;
                break;
            case WD4:
                this.points = 50;
                break;
            default:
                this.points = 0;
                break;           
        }
    }
    
    public Card(){
        this.color=Color.DEFAULT;
        this.face=Face.DEFAULT;
        this.points = 0;
    }
    
    @Override
    public String toString(){
        return this.color.toString() + " " + this.face.toString();
    }
    
    public String toIRCString(){
        return Colors.NORMAL + this.cardColor() + this.toString() + Colors.NORMAL;
    }
    
    public String cardColor(){
        String colorString;
        switch (this.color) // is of type SomeValue  
{  
            case RED:
                colorString = Colors.RED;
                break;

            case BLUE:
                colorString = Colors.BLUE;
                break;

            case GREEN:
                colorString = Colors.GREEN;
                break;

            case YELLOW:
                colorString = Colors.YELLOW;
                break;

            default:
                //colorString = Colors.BLACK;
                colorString = Colors.REVERSE;
        }
        
        return colorString;
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
    
    public int valueForAI(){
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
            num += 11;
        }else if(this.face.equals(Face.TWO)){
            num += 10;
        }else if(this.face.equals(Face.THREE)){
            num += 9;
        }else if(this.face.equals(Face.FOUR)){
            num += 8;
        }else if(this.face.equals(Face.FIVE)){
            num += 7;
        }else if(this.face.equals(Face.SIX)){
            num += 6;
        }else if(this.face.equals(Face.SEVEN)){
            num += 5;
        }else if(this.face.equals(Face.EIGHT)){
            num += 4;
        }else if(this.face.equals(Face.NINE)){
            num += 3;
        }else if(this.face.equals(Face.ZERO)){
            num += 12;
        }else if(this.face.equals(Face.S)){
            num += 2;
        }else if(this.face.equals(Face.R)){
            num += 1;
        }else if(this.face.equals(Face.D2)){
            num += 0;
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
