package forsale;

/**
 * The interface for describing strategies in "For Sale". A strategy must be
 * able to make a bid in an auction (including passing), and must choose a
 * card to play in a sale.
 *
 * @author Michael Albert
 */
public interface Strategy {

    /**
     * Makes a bid for a player given the state of the current auction. A return
     * value less than the current bid (or more than the player's cash)
     * indicates that the player is dropping out of the auction.
     *
     * @param p the player
     * @param a the state of the auction
     * @return the bid
     */
    public int bid(PlayerRecord p, AuctionState a);

    /**
     * Chooses a card for a player in the current sale. It is expected that this
     * will be a card which the player holds! If this contract is violated the
     * game management system can choose any one of the player's cards in an
     * arbitrary manner.
     *
     * @param p the player
     * @param s the state of the current sale
     * @return the card which that player will sell in this sale.
     */
    public Card chooseCard(PlayerRecord p, SaleState s);

}
