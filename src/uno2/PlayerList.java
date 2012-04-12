/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uno2;

/**
 *
 * @author roofis0
 */
public class PlayerList extends CircularArrayList<Player>{
    
   private Boolean forw;
    
    
    public PlayerList(){
        this.forw = true;
    }
    
    public void rev(){
        forw=!forw;
    }
    
    public int count(){
        return super.size();
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
    
    public Player nextPlayer(){
        Player nextPlayer;
        if(forw)nextPlayer = super.next();
        else nextPlayer = super.prev();
        return nextPlayer;
    }   
    
    public Player get(String name){
        Player player = new Player(name);
        return super.get(super.indexOf(player));
    }
    
    public Boolean hasWinner(){
        Boolean win = false;
        for(int i = 0; (i < super.size()) && (!win) ; i++){
            win = !super.get(i).hasCards();
        }
        return win;
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
        Player[] playerArray = new Player[super.size()];
        String[] whoArray = new String[super.size()];
        
        for (int i = 0; i < super.size() ; i++) {
            whoArray[i] = playerArray[i].who();
        }
        return whoArray;
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
}
