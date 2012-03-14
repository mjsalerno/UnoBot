/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uno2;

import java.util.LinkedList;

/**
 *
 * @author roofis0
 */
public class PlayerList extends LinkedList<Player>{
    
    //private DLList<Player> players = new DLList<>();
    private Boolean forw;
    private int at;
    
    
    public PlayerList(){
        this.forw = true;
        this.at = 0;
    }
    
    public void rev(){
        forw=!forw;
    }
    
    public int count(){
        return super.size();
    }
    
    public void clear(){
        super.clear();
    }
    
    public boolean add(Player player){
        return super.add(player);
    }
    
    public void remove(Player player){
        super.remove(super.indexOf(player));                
    }
    
    public void deal(Deck deck){
        for(int i = 0; i < super.size();i++){
            Player player = super.get(i);
            player.draw(deck,7);
        }
    }
    
    public Player next(){
        Player nextPlayer;
        if(forw)nextPlayer = super.get(++at);
        else nextPlayer = super.get(--at);
        return nextPlayer;
    }
    
    public Player at(){
        return this.at();
    }
    
    public Player get(String name){
        Player player = new Player(name);
        return super.get(super.indexOf(player));
    }
    
    public Player get(int i){
        return super.get(i);
    }
    
    public Boolean hasWinner(){
        Boolean win = false;
        for(int i = 0; (i < super.size()) && (!win) ; i++){
            win = !super.get(i).hasCards();
        }
        return win;
    }
    
    public boolean contains(Player player){
        return super.contains(player);
    }
    
    public int pointSum(){
        int sum = 0;
        for(int i = 0; i < super.size() ; i++){
            sum += super.get(i).points();
        }
        return sum;
    }
    
    public String countCards(){
        StringBuilder sb = new StringBuilder("[");
        Player player;
        int j = 0;

        for (int i = 0; i < this.count(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            player = super.get(j++);
            sb.append(player.who()).append(": ").append(player.howManyCards());
        }

        sb.append(']');
        return sb.toString();
    }
    
    public String[] toStringArray(){
        LinkedList<Player> list = this;
        String[] arr = new String[super.size()];
        int i = 0;
        for(Player player : list){
            arr[i++] = player.who();
        }
        return arr;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("[");
        int j = 0;

        for (int i = 0; i < this.count(); i++) {
            if (i > 0) {
                sb.append(", ");
            }

            sb.append(super.get(j++).who());
        }

        sb.append(']');
        
        return sb.toString();
    }

    @Override
    public PlayerListIterator iterator() {
        return new PlayerListIterator(this);
    }

    public Player remove(int at) {
        return super.remove(at);
    }
}
