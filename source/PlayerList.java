
import java.util.LinkedList;

public class PlayerList {
    
    private DLList<Player> players = new DLList<Player>();    
    private Boolean forw = true;
    
    
    public PlayerList(){        
    }
    
    public void rev(){
        forw=!forw;
    }
    
    public int count(){
        return this.players.size();
    }
    
    public void clear(){
        this.players.clear();
    }
    
    public void add(Player player){
        players.add(player);
    }
    
    public void remove(Player player){
        players.remove(players.indexOf(player));                
    }
    
    public void deal(Deck deck){
        for(int i = 0; i < players.size();i++){
            Player player = players.get(i);
            player.draw(deck,7);
        }
    }
    
    public Player next(){
        Player nextPlayer;
        if(forw)nextPlayer = players.next();
        else nextPlayer = players.prev();
        return nextPlayer;
    }
    
    public Player at(){
        return players.at();
    }
    
    public Player get(String name){
        Player player = new Player(name);
        return players.get(players.indexOf(player));
    }
    
    public Boolean hasWinner(){
        Boolean win = false;
        for(int i = 0; (i<players.size()) && (!win) ; i++){
            win = !players.get(i).hasCards();
        }
        return win;
    }
    
    public boolean contains(Player player){
        return players.contains(player);
    }
    
    public String countCards(){
        StringBuilder sb = new StringBuilder("[");
        Player player;
        int j = 0;

        for (int i = 0; i < this.count(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            player = players.get(j++);
            sb.append(player.who()).append(": ").append(player.howManyCards());
        }

        sb.append(']');
        return sb.toString();
    }
    
    public String[] toStringArray(){
        LinkedList<Player> list = players.toLinkedList();
        String[] arr = new String[this.players.size()];
        int i = 0;
        for(Player player : list){
            arr[i++] = player.who();
        }
        return arr;
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder("[");
        int j = 0;

        for (int i = 0; i < this.count(); i++) {
            if (i > 0) {
                sb.append(", ");
            }

            sb.append(players.get(j++).who());
        }

        sb.append(']');
        
        return sb.toString();
    }
}
