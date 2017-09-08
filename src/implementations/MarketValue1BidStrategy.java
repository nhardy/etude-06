package implementations;

import java.util.ArrayList;
import java.util.Collections;

import forsale.*;

public class MarketValue1BidStrategy implements BidStrategy {
    @Override
    public int bid(PlayerRecord player, AuctionState auction) {
        int roundsLeft = auction.getCardsInDeck().size() / auction.getPlayers().size();

        int totalCashRemaining = 0;
        for (PlayerRecord p : roundsLeft == 0 ? auction.getPlayersInAuction() : auction.getPlayers()) {
            totalCashRemaining += p.getCash();
        }
        // int totalCashRemainingInAuction = 0;
        // for (PlayerRecord p : auction.getPlayersInAuction()) {
        //     totalCashRemainingInAuction += p.getCash();
        // }
        int totalPropertyValuesRemaining = 0;
        for (Card c : auction.getCardsInAuction()) {
            totalPropertyValuesRemaining += c.getQuality();
        }
        for (Card c : auction.getCardsInDeck()) {
            totalPropertyValuesRemaining += c.getQuality();
        }

        // "Market Value" for 1 point of Quality is determined by the total cash remaining
        // divided among all of the remaining property values
        double marketValue = totalCashRemaining / (double) totalPropertyValuesRemaining;

        ArrayList<Card> cardsInAuction = new ArrayList<Card>();
        cardsInAuction.addAll(auction.getCardsInAuction());
        Collections.sort(cardsInAuction, new CardComparator());

        if (auction.getCurrentBid() + 1 >= cardsInAuction.get(0).getQuality() * marketValue * 2 || player.getCash() < auction.getCurrentBid() + 1) {
            return -1;
        } else {
            return auction.getCurrentBid() + 1;
        }
    }
}
