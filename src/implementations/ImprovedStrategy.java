package implementations;

import java.util.ArrayList;
import java.util.Collections;

import forsale.*;

public class ImprovedStrategy implements Strategy {
    @Override
    public int bid(PlayerRecord player, AuctionState auction) {

        int playerCount = auction.getPlayers().size();
        int round = auction.getCardsInDeck().size()/playerCount;

        //Calculate how much money we have vs how much money we should have on average
        int averageSpent = 14/playerCount;
        int playerSpend = player.getCash()/(round + 1);
        double ratio = (double) playerSpend/averageSpent;

        ArrayList<Card> cardsInAuction = new ArrayList<Card>();
        cardsInAuction.addAll(auction.getCardsInAuction());
        Collections.sort(cardsInAuction, new CardComparator());

        int v1 = cardsInAuction.get(0).getQuality();
        int v2 = cardsInAuction.get(cardsInAuction.size() - 1).getQuality();

        double value = (v2 - v1)*ratio;

        if (value >= 15){
            return auction.getCurrentBid() + 1;
        }
        return -1;
    }

    @Override
    public Card chooseCard(PlayerRecord player, SaleState sale) {
        return new BasicChooseCardStrategy().chooseCard(player, sale);
    }

    private void log(String output) {
        System.out.println(output);
    }
}
