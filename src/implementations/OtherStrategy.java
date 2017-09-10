package implementations;

import forsale.*;

import java.util.ArrayList;
import java.util.Collections;

public class OtherStrategy implements BidStrategy
{
    double averageRemaining;
    ArrayList<Integer> cards = new ArrayList<Integer>();
    double averageCard;
    ArrayList<Card> cardvalue;
    int roundsleft = 5;
    int range;
    int pLeft = 5;
    private static final int HIGH_RANGE = 23;
    private static final int LOW_RANGE =16;

    @Override
    public int bid(PlayerRecord p, AuctionState a) {

        cardvalue = a.getCardsInAuction();
        for (int i = 0; i < cardvalue.size(); i++) {
            cards.add(cardvalue.get(i).getQuality());
        }
        Collections.sort(cards);
        Collections.reverse(cards);

        range = cards.get(0) - cards.get(cards.size() - 1);
        roundsleft = 5;
        for (int i = 0; i < cards.size(); i++) {
            averageCard += cards.get(i);
        }
        averageCard = averageCard / 6;


        if (p.getCash() > 0 && roundsleft > 0) {
            averageRemaining = p.getCash() / roundsleft;
            roundsleft--;
        }

        if (p.getCash() > 0 && roundsleft > 0) {
            averageRemaining = p.getCash() / roundsleft;
            roundsleft--;
        }
        ArrayList<PlayerRecord> playersize = a.getPlayersInAuction();
        pLeft = playersize.size();


        if (p.getCash() == 0) {
            return -1;
        }

        //if 3 people have pulled out on low cards and high bid drop out
        else if (pLeft < 3 && cards.get(0) < 25 && a.getCurrentBid() > 5) {
            return -1;
        }
        //if card is 30 and have 6 or more dollars left bid 6

        else if (cards.indexOf(30) != -1 && 6 <= p.getCash()) {
            return 6;
        }
        //if card is 30 and have less than 8 dollars bid remaining cash.
        else if (cards.indexOf(30) != -1) {
            return p.getCash();
        }
        //if card is 29 and have 7 or more dollars left bid 7
        else if (cards.indexOf(29) != -1 && 5 <= p.getCash()) {
            return 5;
        }
        //if card is 29 and have less than 7 dollars bid remaining cash
        else if (cards.indexOf(29) != -1) {
            return p.getCash();
        }
        //if card is 28 and have 6 or more dollars left bid 6
        else if (cards.indexOf(28) != -1 && 4 <= p.getCash()) {
            return 4;
        }
        //if card is 28 and have less than 6 dollars bit remaining cash
        else if (cards.indexOf(28) != -1) {
            return p.getCash();
        }
        //if bid is too high pull out
        else if (a.getCurrentBid() >= 6) {
            return -1;
        }
        //if high cards with low range pull out
        else if (range < LOW_RANGE && 28 > cards.get(0) && cards.get(0) > 25) {
            return -1;
        }
        //if middle range cards and low remaining cash per turn pull out

        else if (cards.get(cards.size() - 1) > 6 && cards.get(0) < 25 && averageRemaining < 3.5) {
            return -1;
        }
        //if the cards are greater than 6 and range is low pull out
        else if (cards.get(cards.size() - 1) > 6 && range < LOW_RANGE) {
            return -1;
        }

        //if 3 low cards and atleast 1 high card and current bid less than 5 plus 1 to current bid
        else if (cards.get(cards.size() - 3) < 10 && cards.get(0) >= 25 && a.getCurrentBid() < 5) {
            return 1 + a.getCurrentBid();
        }
        //base cases


                /*low average betting round*/
        if (range > HIGH_RANGE && averageCard < 12) {
            if (a.getCurrentBid() < 4) {
                return 4;
            }
        }
        if (LOW_RANGE < range && range < HIGH_RANGE && averageCard < 12) {
            if (a.getCurrentBid() <= 4) {
                return 4;
            }
        }
        if (range < LOW_RANGE && averageCard < 12) {
            if (a.getCurrentBid() <= 2) {
                return 2;
            }
        }
                /*high average betting round*/
        if (range > HIGH_RANGE && averageCard > 18) {
            if (a.getCurrentBid() <= 3) {
                return 4;
            }
        }
        if (LOW_RANGE < range && range < HIGH_RANGE && averageCard > 18) {
            if (a.getCurrentBid() <= 3) {
                return 4;
            }
        }
        if (range < LOW_RANGE && averageCard > 18) {
            if (a.getCurrentBid() <= 1) {
                return 2;
            }
        }


        /** average betting round */
        if (range > HIGH_RANGE && 12 < averageCard && averageCard < 18) {
            if (a.getCurrentBid() <= 6) {
                return 3;
            }
        }
        if (LOW_RANGE < range && range < HIGH_RANGE && 12 < averageCard && averageCard < 18) {
            if (a.getCurrentBid() <= 4) {
                return 2;
            }
        }
        if (LOW_RANGE > range && 12 < averageCard && averageCard < 18) {
            if (a.getCurrentBid() <= 2) {
                return 2;
            }
        }

        /** default to catch any mistakes */
        if (range < LOW_RANGE) {
            if (p.getCash() > 4) {
                return 2;
            }
        } else if (LOW_RANGE <= range && range <= HIGH_RANGE) {
            if (p.getCash() > 5) {
                return 3;
            }
        } else {
            if (p.getCash() > 6) {
                return 1 + a.getCurrentBid();
            }
        }
        return -1;
    }
}
