package implementations;

import forsale.*;

import java.util.ArrayList;
import java.util.Collections;

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

        double value = (v2 - v1)*ratio*0.39;

        if (value >= auction.getCurrentBid()){
            return auction.getCurrentBid() + 1;
        }
        return -1;
    }

    @Override
    public Card chooseCard(PlayerRecord player, SaleState sale) {
        int totalChequesRemaining = 0;
        for (int cheque : sale.getChequesRemaining()) {
            totalChequesRemaining += cheque;
        }

        int totalPropertyValuesRemaining = 0;
        for (PlayerRecord p : sale.getPlayers()) {
            for (Card c: p.getCards()) {
                totalPropertyValuesRemaining += c.getQuality();
            }
        }

        double marketValue = totalChequesRemaining / totalPropertyValuesRemaining;

        ArrayList<Integer> chequesAvailable = new ArrayList<Integer>();
        chequesAvailable.addAll(sale.getChequesAvailable());
        Collections.sort(chequesAvailable);

        ArrayList<Card> heldCards = new ArrayList<Card>();
        heldCards.addAll(player.getCards());
        Collections.sort(heldCards, new CardComparator());

        ArrayList<Card> otherCards = new ArrayList<Card>();
        for (PlayerRecord p : sale.getPlayers()) {
            for (Card c : p.getCards()) {
                if (!heldCards.contains(c)) otherCards.add(c);
            }
        }
        Collections.sort(otherCards, new CardComparator());

        // Find lowest card that exceeds the top value other card, and would be better than market value
        for (int i = 0; i < otherCards.size(); i++) {
            Card otherCard = otherCards.get(otherCards.size() - i - 1);
            int chequeIndex = Math.max(0, chequesAvailable.size() - i - 1);
            for (Card c : heldCards) {
                if (c.getQuality() > otherCard.getQuality() && c.getQuality() * marketValue >= chequesAvailable.get(chequeIndex)) {
                    return c;
                }
            }
        }

        return heldCards.get(0);
    }

    private void log(String output) {
        System.out.println(output);
    }
}
