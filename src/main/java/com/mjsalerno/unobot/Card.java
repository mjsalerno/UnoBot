/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mjsalerno.unobot;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.pircbotx.Colors;

import com.google.common.base.Splitter;

/**
 *
 * @author roofis0
 */
public class Card implements Comparable<Card>, Comparator<Card> {

    private Card.Color wildColor;
    public final Card.Color color;
    public final Card.Face face;
    protected final int points;
    
    public Card(Color color, Face face) {
        this(color, face, null);
    }

    public Card(Color color, Face face, Color wildColor) {
        this.color = color;
        this.face = face;
        this.wildColor = wildColor;

        this.points = face.points;
    }

    public Card() {
        this.color = Color.DEFAULT;
        this.face = Face.DEFAULT;
        this.points = 0;
    }
    
    public void setWildColor(Card.Color color) {
        if(!this.color.equals(Color.WILD)) {
            throw new IllegalArgumentException("Can not set wild color of: " + this.color);
        }
        
        this.wildColor = color;
    }
    
    public Color getWildColor() {
        return this.wildColor;
    }

    @Override
    public String toString() {
        return this.color.toString() + " " + this.face.toString();
    }

    public String toIRCString() {
        return Colors.NORMAL + this.cardColor() + this.toString() + Colors.NORMAL;
    }

    public String cardColor() {
        String colorString;
        switch (this.color) // is of type SomeValue  
        {
            case RED:
                colorString = bg(Colors.WHITE, Colors.RED);
                break;

            case BLUE:
                colorString = bg(Colors.WHITE, Colors.DARK_BLUE);
                break;

            case GREEN:
                colorString = bg(Colors.WHITE, Colors.DARK_GREEN);
                break;

            case YELLOW:
                colorString = bg(Colors.BLACK, Colors.YELLOW);
                break;

            default:
                //colorString = Colors.BLACK;
                colorString = Colors.REVERSE;
                break;
        }

        return colorString;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Card other = (Card) obj;
        if (this.points != other.points) {
            return false;
        }
        if (this.wildColor != other.wildColor) {
            return false;
        }
        if (this.color != other.color) {
            return false;
        }
        return this.face == other.face;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, face, points, wildColor);
    }

    public static String bg(String foreground, String background) {
        return foreground + "," + background.trim();
    }

    private int value() {
        int num = 0;
        switch (this.color) {
            case RED:
                num += 100;
                break;
            case GREEN:
                num += 200;
                break;
            case BLUE:
                num += 300;
                break;
            case YELLOW:
                num += 400;
                break;
            default:
                num += 500;
                break;
        }

        switch (this.face) {
            case ONE:
                num += 1;
                break;
            case TWO:
                num += 2;
                break;
            case THREE:
                num += 3;
                break;
            case FOUR:
                num += 4;
                break;
            case FIVE:
                num += 5;
                break;
            case SIX:
                num += 6;
                break;
            case SEVEN:
                num += 7;
                break;
            case EIGHT:
                num += 8;
                break;
            case NINE:
                num += 9;
                break;
            case ZERO:
                num += 0;
                break;
            case SKIP:
                num += 10;
                break;
            case REVERSE:
                num += 11;
                break;
            case D2:
                num += 12;
                break;
            case WILD:
                num += 13;
                break;
            default:
                num += 14;
                break;
        }
        return num;
    }

    public int valueForAI() {
        int num = 0;
        switch (this.color) {
            case RED:
                num += 100;
                break;
            case GREEN:
                num += 200;
                break;
            case BLUE:
                num += 300;
                break;
            case YELLOW:
                num += 400;
                break;
            default:
                num += 500;
                break;
        }

        switch (this.face) {
            case ONE:
                num += 11;
                break;
            case TWO:
                num += 10;
                break;
            case THREE:
                num += 9;
                break;
            case FOUR:
                num += 8;
                break;
            case FIVE:
                num += 7;
                break;
            case SIX:
                num += 6;
                break;
            case SEVEN:
                num += 5;
                break;
            case EIGHT:
                num += 4;
                break;
            case NINE:
                num += 3;
                break;
            case ZERO:
                num += 12;
                break;
            case SKIP:
                num += 2;
                break;
            case REVERSE:
                num += 1;
                break;
            case D2:
                num += 0;
                break;
            case WILD:
                num += 13;
                break;
            default:
                num += 14;
                break;
        }
        return num;
    }

    /**
     * translates a String that represents a Card to an actual Card Object
     *
     * @param string the String that represents the Card
     * @return a Card that is what the String provided represents
     */
    public static Card parse(String string) {

        try {
            String parseString = Colors.removeFormattingAndColors(string).toUpperCase();
            List<String> split = Splitter.on(' ').trimResults().omitEmptyStrings().splitToList(parseString);

            String strColor = null;
            String strFace = null;

            if (split.size() >= 2) {
                strColor = split.get(0);
                strFace = split.get(1);
            } else {

                // short hand single word variants Y9/B2/G6/R8 etc 
                if (split.get(0).length() < 2) {
                    return null;
                } else {
                    strColor = split.get(0).substring(0, 1);
                    strFace = split.get(0).substring(1);
                }
            }

            Card.Color color = Card.Color.DEFAULT;

            //check color
            switch (strColor) {
                case "R":
                case "RED":
                    color = Card.Color.RED;
                    break;
                case "B":
                case "BLUE":
                    color = Card.Color.BLUE;
                    break;
                case "G":
                case "GREEN":
                    color = Card.Color.GREEN;
                    break;
                case "Y":
                case "YELLOW":
                    color = Card.Color.YELLOW;
                    break;
                case "W":
                case "WILD":
                    color = Card.Color.WILD;
                    break;
                default:
                    return null; //UNKNOWN
            }

            Card.Face face = Card.Face.DEFAULT;

            //check face
            switch (strFace) {
                case "0":
                case "ZERO":
                    face = Card.Face.ZERO;
                    break;
                case "1":
                case "ONE":
                    face = Card.Face.ONE;
                    break;
                case "2":
                case "TWO":
                    face = Card.Face.TWO;
                    break;
                case "3":
                case "THREE":
                    face = Card.Face.THREE;
                    break;
                case "4":
                case "FOUR":
                    face = Card.Face.FOUR;
                    break;
                case "5":
                case "FIVE":
                    face = Card.Face.FIVE;
                    break;
                case "6":
                case "SIX":
                    face = Card.Face.SIX;
                    break;
                case "7":
                case "SEVEN":
                    face = Card.Face.SEVEN;
                    break;
                case "8":
                case "EIGHT":
                    face = Card.Face.EIGHT;
                    break;
                case "9":
                case "NINE":
                    face = Card.Face.NINE;
                    break;
                case "W":
                case "WILD":
                    face = Card.Face.WILD;
                    break;
                case "WD4":
                    face = Card.Face.WD4;
                    break;
                case "S":
                case "SKIP":
                    face = Card.Face.SKIP;
                    break;
                case "R":
                case "REV":
                case "REVERSE":
                    face = Card.Face.REVERSE;
                    break;
                case "D2":
                    face = Card.Face.D2;
                    break;
                default:
                    return null; //UNKNOWN
            }
            
            Card card;
            
            if((face.equals(Face.WD4) || face.equals(Face.WILD)) && !color.equals(Color.WILD)) {
                card = new Card(Color.WILD, face, color);
            } else {
                card = new Card(color, face);
            }

            return card;

        } catch (Exception ex) {
            return null; // Return null if the parser fails
        }

    }

    @Override
    public int compareTo(Card card) {
        int thisV = value();
        int thatV = 515;
        if (card != null) {
            thatV = card.value();
        }
        return thisV - thatV;
    }

    @Override
    public int compare(Card card1, Card card2) {
        int thisV = 515;
        int thatV = 515;

        if (card1 != null) {
            thisV = card1.value();
        }

        if (card2 != null) {
            thatV = card2.value();
        }

        return thisV - thatV;
    }

    public enum Color {
        RED, BLUE, GREEN, YELLOW, WILD, DEFAULT
    };

    public enum Face {
        ZERO(0),
        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        D2(20),
        REVERSE(20),
        SKIP(0),
        WILD(50),
        WD4(50),
        DEFAULT(0);

        public final int points;

        private Face(int points) {
            this.points = points;
        }
    };
}
