package implementations;

import java.util.ArrayList;
import java.util.Collections;

import forsale.*;

public class ImprovedStrategy implements BidStrategy {
    @Override
    public int bid(PlayerRecord player, AuctionState auction) {

        int playerCount = auction.getPlayers().size();
        int round = auction.getCardsInDeck().size()/playerCount;

        //Calculate how much money we have vs how much money we should have on average
        double averageSpent = 14.0/playerCount;
        double playerSpend = (double)player.getCash()/(round + 1);
        double ratio = (double) playerSpend/averageSpent;

        ArrayList<Card> cardsInAuction = new ArrayList<Card>();
        cardsInAuction.addAll(auction.getCardsInAuction());
        Collections.sort(cardsInAuction, new CardComparator());

        int v1 = cardsInAuction.get(0).getQuality();
        int v2 = cardsInAuction.get(cardsInAuction.size() - 1).getQuality();

        //is this more than we want to spend?
       // if ((double)player.getCash() / (round+1) < 2*auction.getCurrentBid()){
         //   return -1;
        //}
        /*
        System.out.println(v2-v1);
        if (round == 0){
            System.out.println("---------");
        } */

        double value = (v2 - v1)*ratio*0.39;

        if (value >= auction.getCurrentBid()){
            return auction.getCurrentBid() + 1;
        }
        return -1;
    }

    private void log(String output) {
        System.out.println(output);
    }
}
