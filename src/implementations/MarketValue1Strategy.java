package implementations;

import java.util.ArrayList;
import java.util.Collections;

import forsale.*;

public class MarketValue1Strategy implements Strategy {
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
        // System.out.println(output);
    }
}
