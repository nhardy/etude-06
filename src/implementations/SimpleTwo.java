package implementations;

import forsale.*;

import java.util.ArrayList;
import java.util.Collections;

public class SimpleTwo implements BidStrategy {
    @Override
    public int bid(PlayerRecord player, AuctionState auction) {
        ArrayList<Card> cardsInAuction = new ArrayList<Card>();
        cardsInAuction.addAll(auction.getCardsInAuction());
        Collections.sort(cardsInAuction, new CardComparator());

        int v1 = cardsInAuction.get(cardsInAuction.size() - 1).getQuality();
        int v2 = cardsInAuction.get(0).getQuality();

        double diff = v1 - v2;

        // Bid if there is a big difference between the drop out card and the max card.
        if (diff >= 15){
            return auction.getCurrentBid() + 1;
        }
        return -1;
    }

    private void log(String output) {
        System.out.println(output);
    }
}
