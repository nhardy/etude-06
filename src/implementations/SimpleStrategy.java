package implementations;

import forsale.*;

import java.util.ArrayList;
import java.util.Collections;

public class SimpleStrategy implements Strategy {
    @Override
    public int bid(PlayerRecord player, AuctionState auction) {
        //Calculate which round this is
        int round = auction.getCardsInDeck().size()/auction.getPlayers().size();
        //Divide the remaining money up by the number of rounds left.
        int max_bid = player.getCash() / (round+1);

        //Bid if the current bid is smaller than the max_bid.
        if (auction.getCurrentBid() + 1 <= max_bid ){
            return auction.getCurrentBid() + 1;
        }
        return -1;
    }

    @Override
    public Card chooseCard(PlayerRecord player, SaleState sale) {
        return new BasicChooseCardStrategy().chooseCard(player, sale);
    }

    private void log(String output) {
        //System.out.println(output);
    }
}
