package implementations;

import forsale.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SimpleStrategy implements Strategy {
    @Override
    public int bid(PlayerRecord player, AuctionState auction) {

        int round = auction.getCardsInDeck().size()/auction.getPlayers().size();
        log(String.valueOf(round));
        int max_bid;
        if (round != 0) {
             max_bid = player.getCash() / (round+1);
        } else {
            max_bid = player.getCash();
        }
        if (auction.getCurrentBid()  + 1 >= max_bid ){
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
        //System.out.println(output);
    }

    class CardComparator implements Comparator<Card> {
        @Override
        public int compare(Card c1, Card c2) {
            return c2.getQuality() - c1.getQuality();
        }
    }
}
