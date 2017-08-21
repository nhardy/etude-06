package forsale;

import java.util.ArrayList;

/**
 * The state of a "For sale" game when players are selling to the bank.
 * 
 * @author Michael Albert
 */
public class SaleState {

    private ArrayList<PlayerRecord> players;
    private ArrayList<Integer> chequesAvailable;
    private ArrayList<Integer> chequesRemaining;

    public SaleState(ArrayList<PlayerRecord> players, ArrayList<Integer> chequesAvailable, ArrayList<Integer> chequesRemaining) {
        this.players = players;
        this.chequesAvailable = chequesAvailable;
        this.chequesRemaining = chequesRemaining;
    }

    /**
     * 
     * @return The records for the players involved in the current sale.
     */
    public ArrayList<PlayerRecord> getPlayers() {
        return players;
    }

    /**
     * 
     * @return The cheques available in the current sale.
     */
    public ArrayList<Integer> getChequesAvailable() {
        return chequesAvailable;
    }

    /**
     * 
     * @return The cheques remaining to be sold in future sales.
     */
    public ArrayList<Integer> getChequesRemaining() {
        return chequesRemaining;
    }
    
}
