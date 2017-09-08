package implementations;

import java.util.ArrayList;
import java.util.Collections;

import forsale.*;

public class ImprovedChooseCardStrategy implements ChooseCardStrategy {
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

        double marketValue = totalChequesRemaining / (double) totalPropertyValuesRemaining;

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

        // System.out.println("Cheques Available: " + chequesAvailable);
        // System.out.print("Hand: [" + heldCards.get(0) + " (" + heldCards.get(0).getQuality() + ")");
        // for (int i = 1; i < heldCards.size(); i++) {
        //     System.out.print(", " + heldCards.get(i) + " (" + heldCards.get(i).getQuality() + ")");
        // }
        // System.out.println("]");

        // Find lowest card that exceeds the top value other card, and would be better than market value
        for (int i = 0; i < otherCards.size(); i++) {
            Card otherCard = otherCards.get(otherCards.size() - i - 1);
            int chequeIndex = Math.max(0, chequesAvailable.size() - i - 1);
            for (Card c : heldCards) {
                if (c.getQuality() > otherCard.getQuality() && c.getQuality() * marketValue <= chequesAvailable.get(chequeIndex)) {
                    // System.out.println("Playing card: " + c + " (" + c.getQuality() + ")");
                    // System.out.printf("%.3f", c.getQuality() * marketValue);
                    // System.out.println("Cheques Remaining: " + sale.getChequesRemaining());
                    return c;
                }
            }
        }


        // System.out.println("Playing lowest card: " + heldCards.get(0) + " (" + heldCards.get(0).getQuality() + ")");

        return heldCards.get(0);
    }
}
