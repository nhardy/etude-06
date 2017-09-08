package implementations;

import forsale.*;

public interface BidStrategy {
    /**
     * Makes a bid for a player given the state of the current auction. A return
     * value less than the current bid (or more than the player's cash)
     * indicates that the player is dropping out of the auction.
     *
     * @param player the player
     * @param auction the state of the auction
     * @return the bid
     */
    public int bid(PlayerRecord player, AuctionState auction);
}
