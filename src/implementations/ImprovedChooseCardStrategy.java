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

        // int totalCheques = 0;
        // for (int cheque : allCheques) {
        //     totalCheques += cheque;
        // }
        // int totalPropertyValue = 0;
        // for (Card c : allCards) {
        //     totalPropertyValue += c.getQuality();
        // }
        // double marketValue = totalCheques / (double) totalPropertyValue;

        int currentMaxCheque = chequesAvailable.get(chequesAvailable.size() - 1);
        int maxOtherCard = otherCards.get(otherCards.size() - 1).getQuality();
        if (currentMaxCheque == allCheques.get(allCheques.size() - 1)) {
            for (Card c : player.getCards()) {
                if (c.getQuality() > maxOtherCard
                    // || (chequesRemaining.contains(currentMaxCheque) && c.getQuality() > maxOtherCard - 1 && Math.random() < 0.5)
                ) return c;
            }
        }

        // int currentMinCheque = chequesAvailable.get(0);
        // if (currentMinCheque == 0) {
        //     for (Card c : heldCards) {
        //         // TODO: Tweak multiplier
        //         if (c.getQuality() * marketValue * 1.5 > chequesAvailable.get(1)) return c;
        //     }
        // }

        return heldCards.get(0);
    }
}
