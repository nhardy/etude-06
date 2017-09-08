package implementations;

import forsale.*;

public interface ChooseCardStrategy {
    /**
     * Chooses a card for a player in the current sale. It is expected that this
     * will be a card which the player holds! If this contract is violated the
     * game management system can choose any one of the player's cards in an
     * arbitrary manner.
     *
     * @param player the player
     * @param sale the state of the current sale
     * @return the card which that player will sell in this sale.
     */
    public Card chooseCard(PlayerRecord player, SaleState sale);
}
