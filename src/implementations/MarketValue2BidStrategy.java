package implementations;

import java.util.ArrayList;
import java.util.Collections;

import forsale.*;

public class MarketValue2BidStrategy implements BidStrategy {
    @Override
    public int bid(PlayerRecord player, AuctionState auction) {
        int roundsLeft = auction.getCardsInDeck().size() / auction.getPlayers().size();

        int totalCashRemaining = 0;
        for (PlayerRecord p : roundsLeft == 0 ? auction.getPlayersInAuction() : auction.getPlayers()) {
            totalCashRemaining += p.getCash();
        }
        int totalCashRemainingInAuction = 0;
        for (PlayerRecord p : auction.getPlayersInAuction()) {
            totalCashRemainingInAuction += p.getCash();
        }
        int totalPropertyValuesRemaining = 0;
        for (Card c : auction.getCardsInAuction()) {
            totalPropertyValuesRemaining += c.getQuality();
        }
        for (Card c : auction.getCardsInDeck()) {
            totalPropertyValuesRemaining += c.getQuality();
        }

        // "Market Value" for 1 point of Quality is determined by the total cash remaining
        // divided among all of the remaining property values
        double marketValue = totalCashRemaining * (roundsLeft + 1) / (double) totalPropertyValuesRemaining;

        ArrayList<Card> cardsInAuction = new ArrayList<Card>();
        cardsInAuction.addAll(auction.getCardsInAuction());
        Collections.sort(cardsInAuction, new CardComparator());

        double ratioAimingFor = 0;
        Card cardAimingFor = null;
        for (int i = 1; i < cardsInAuction.size(); i++) {
            Card c = cardsInAuction.get(i);
            double ratioToMax = (c.getQuality() / (double) cardsInAuction.get(cardsInAuction.size() - 1).getQuality());
            int anticipatedMaxBid = (int) (auction.getCurrentBid() + ((((totalCashRemainingInAuction - player.getCash()) / (auction.getPlayersInAuction().size() - 1)) / (roundsLeft + 1))) * ratioToMax);
            double ratio = (c.getQuality() * marketValue) / anticipatedMaxBid;
            if (ratio > ratioAimingFor && player.getCash() >= anticipatedMaxBid) {
                ratioAimingFor = ratio;
                cardAimingFor = c;
            }
        }

        if (cardAimingFor == null || player.getCash() < auction.getCurrentBid() + 1) {
            return -1;
        } else {
            return auction.getCurrentBid() + 1;
        }
    }
}
