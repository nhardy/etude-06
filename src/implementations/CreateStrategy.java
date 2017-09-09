package implementations;

import forsale.*;

public class CreateStrategy implements Strategy {

    private BidStrategy bidStrat_m;
    private ChooseCardStrategy cardStrat_m;

    public CreateStrategy(BidStrategy bidStrat, ChooseCardStrategy cardStrat){
        bidStrat_m = bidStrat;
        cardStrat_m = cardStrat;
    }

    @Override
    public int bid(PlayerRecord player, AuctionState auction) {
        return bidStrat_m.bid(player,auction);
    }

    @Override
    public Card chooseCard(PlayerRecord player, SaleState sale) {
        return cardStrat_m.chooseCard(player,sale);
    }

}
