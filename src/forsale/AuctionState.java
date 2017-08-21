package forsale;

import java.util.ArrayList;

/**
 * Represents the state of the game when an auction is underway 
 * as needed by a strategy.
 * 
 * @author Michael Albert
 */
public class AuctionState {
    
    private final ArrayList<PlayerRecord> players;
    private final ArrayList<PlayerRecord> playersInAuction;
    private final ArrayList<Card> cardsInAuction;
    private final ArrayList<Card> cardsInDeck;
    private final int currentBid;

    public AuctionState(ArrayList<PlayerRecord> players, ArrayList<PlayerRecord> playersInAuction, ArrayList<Card> cardsInAuction, ArrayList<Card> cardsInDeck, int currentBid) {
        this.players = players;
        this.playersInAuction = playersInAuction;
        this.cardsInAuction = cardsInAuction;
        this.cardsInDeck = cardsInDeck;
        this.currentBid = currentBid;
    }

    /**
     * 
     * @return the current bid in the auction. 
     */
    public int getCurrentBid() {
        return currentBid;
    }

    /**
     * 
     * @return The records for all the players in the game.
     */
    public ArrayList<PlayerRecord> getPlayers() {
        return players;
    }

    /**
     * 
     * @return The records for the players remaining in the auction.
     */
    public ArrayList<PlayerRecord> getPlayersInAuction() {
        return playersInAuction;
    }

    /**
     * 
     * @return The cards remaining to be auctioned in this round.
     */
    public ArrayList<Card> getCardsInAuction() {
        return cardsInAuction;
    }

    /**
     * 
     * @return The cards remaining in the deck.
     */
    public ArrayList<Card> getCardsInDeck() {
        return cardsInDeck;
    }
    
    
    
}
