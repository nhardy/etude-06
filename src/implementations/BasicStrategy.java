package implementations;

import forsale.*;

public class BasicStrategy implements Strategy {
    @Override
    public int bid(PlayerRecord player, AuctionState auction) {
        int totalCashRemaining = 0;
        for (PlayerRecord p: auction.getPlayersInAuction()) {
            totalCashRemaining += p.getCash();
        }
        int totalPropertyValuesRemaining = 0;
        for (Card c : auction.getCardsInAuction()) {
            totalPropertyValuesRemaining += c.getQuality();
        }
        for (Card c : auction.getCardsInDeck()) {
            totalPropertyValuesRemaining += c.getQuality();
        }

        double marketValue = totalCashRemaining / (double) totalPropertyValuesRemaining;

        int lowestValue = Integer.MAX_VALUE;
        for (Card c: auction.getCardsInAuction()) {
            if (c.getQuality() < lowestValue) lowestValue = c.getQuality();
        }

        double propertyValue = lowestValue * marketValue;
        if (auction.getCurrentBid() < propertyValue || player.getCash() < auction.getCurrentBid() + 1) {
            return -1;
        } else {
            return auction.getCurrentBid() + 1;
        }
    }

    @Override
    public Card chooseCard(PlayerRecord player, SaleState sale) {
        // TODO: Expand
        return player.getCards().get((int) Math.random() * player.getCards().size());
    }
}