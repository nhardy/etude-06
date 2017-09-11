package implementations;

import java.util.ArrayList;
import java.util.Collections;

import forsale.*;

public class ImprovedChooseCardStrategy implements ChooseCardStrategy {
    public Card chooseCard(PlayerRecord player, SaleState sale) {
        ArrayList<Card> heldCards = player.getCards();
        Collections.sort(heldCards, new CardComparator());

        ArrayList<Card> allCards = new ArrayList<Card>();
        ArrayList<Card> otherCards = new ArrayList<Card>();
        for (PlayerRecord p : sale.getPlayers()) {
            allCards.addAll(p.getCards());
            if (!p.equals(player)) {
                otherCards.addAll(p.getCards());
            }
        }
        Collections.sort(allCards, new CardComparator());
        Collections.sort(otherCards, new CardComparator());

        ArrayList<Integer> chequesRemaining = new ArrayList<Integer>();
        chequesRemaining.addAll(sale.getChequesRemaining());
        Collections.sort(chequesRemaining);

        ArrayList<Integer> chequesAvailable = new ArrayList<Integer>();
        chequesAvailable.addAll(sale.getChequesAvailable());
        Collections.sort(chequesAvailable);

        ArrayList<Integer> allCheques = new ArrayList<Integer>();
        allCheques.addAll(chequesRemaining);
        allCheques.addAll(chequesAvailable);

        int currentMaxCheque = chequesAvailable.get(chequesAvailable.size() - 1);
        int maxOtherCard = otherCards.get(otherCards.size() - 1).getQuality();
        if (currentMaxCheque == allCheques.get(allCheques.size() - 1)) {
            for (Card c : heldCards) {
                if (c.getQuality() > maxOtherCard) {
                    System.out.println("Playing " + c + " (" + c.getQuality() + ") for " + currentMaxCheque);
                    return c;
                }
            }
            // for (Card c : heldCards) {
            //     if (chequesRemaining.contains(currentMaxCheque)) {
            //         if (c.getQuality() > maxOtherCard - 1) {
            //             if (Math.random() < 0.5) return c;
            //         }
            //     }
            // }
        }

        return heldCards.get(0);
    }
}
