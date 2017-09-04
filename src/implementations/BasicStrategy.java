package implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import forsale.*;

public class BasicStrategy implements Strategy {
    @Override
    public int bid(PlayerRecord player, AuctionState auction) {
        int totalCashRemaining = 0;
        for (PlayerRecord p: auction.getPlayersInAuction()) {
            totalCashRemaining += p.getCash();
        }
        int totalPropertyValuesRemaining = 0;
        for (Card c : auction.getCardsInAuction()) {
            totalPropertyValuesRemaining += c.getQuality();
        }
        for (Card c : auction.getCardsInDeck()) {
            totalPropertyValuesRemaining += c.getQuality();
        }

        double marketValue = totalCashRemaining / (double) totalPropertyValuesRemaining;

        int lowestValue = Integer.MAX_VALUE;
        int highestValue = Integer.MIN_VALUE;
        for (Card c: auction.getCardsInAuction()) {
            if (c.getQuality() < lowestValue) lowestValue = c.getQuality();
            if (c.getQuality() > highestValue) highestValue = c.getQuality();
        }

        double lowestPropertyValue = lowestValue * marketValue;
        double highestPropertyValue = highestValue * marketValue;
        log("----- DEBUG -----");
        log("totalCashRemaining: " + totalCashRemaining);
        log("totalPropertyValuesRemaining: " + totalPropertyValuesRemaining);
        log("Market value for " + lowestValue + ": " + lowestPropertyValue);
        log("Market value for " + highestValue + ": " + highestPropertyValue);
        log("Lowest Property Quality: " + lowestValue);
        log("Current Bid is: " + auction.getCurrentBid());
        log("Player's Cash: " + player.getCash());
        log("----- DEBUG -----");
        if (auction.getCurrentBid() > lowestPropertyValue) {
            return -1;
        } else if (auction.getCurrentBid() < highestPropertyValue || player.getCash() < auction.getCurrentBid() + 1) {
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

        int minChequeAvailable = Integer.MAX_VALUE;
        int maxChequeAvailable = Integer.MIN_VALUE;
        for (int cheque : sale.getChequesAvailable()) {
            minChequeAvailable = Math.min(minChequeAvailable, cheque);
            maxChequeAvailable = Math.max(maxChequeAvailable, cheque);
        }

        Card lowestHeld = null;
        Card highestHeld = null;
        ArrayList<Card> sortedCards = new ArrayList<Card>();
        for (Card c : player.getCards()) {
            sortedCards.add(c);
            lowestHeld = lowestHeld == null || lowestHeld.getQuality() > c.getQuality() ? c : lowestHeld;
            highestHeld = highestHeld == null || highestHeld.getQuality() < c.getQuality() ? c : highestHeld;
        }
        Collections.sort(sortedCards, new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                return c2.getQuality() - c1.getQuality();
            }
        });

        for (Card c : sortedCards) {
            // TODO: replace maxChequeAvailable with most likely cheque
            if (c.getQuality() * marketValue > maxChequeAvailable) return c;
        }
        return lowestHeld;
    }

    private void log(String output) {
        // System.out.println(output);
    }
}
