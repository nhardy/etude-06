package implementations;

import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;
import forsale.AuctionState;
import forsale.Card;
import forsale.Player;
import forsale.PlayerRecord;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Collections;

public class SimulationStrategy implements BidStrategy {

    private int round;
    private int playerCount;

    private PlayerRecord me;
    private int myBid;
    private boolean new_round;
    private boolean dropped_out = true;

    private static double default_e = 0.0;
    String names_m[]= new String[5];
    double probs_m[] = new double[5];
    static Hashtable<String, Double> otherStrats = new Hashtable<String, Double>();


    // converts a players bid 'score' to a probability.
    private static double logistic(double x, double e){
        final double k = 0.5;
        return 1.0/(1.0+Math.exp(-(x+e)*k));
    }

    private double getE(String name){
        Double e = otherStrats.get(name);
        if (e == null){
            otherStrats.put(name,default_e);
            return default_e;
        }
        return e;
    }

    //
    private double calculateProb(PlayerRecord player, int nextBid, int win, int lose){

        if ( player.getCash() < nextBid) {
            //they can't bid
            return 0.0;
        }

        double playerTarget = (double)player.getCash()/(round);
        //double value = 2*nextBid - 2*nextBid + (win - lose) - 10;
        double value = (win - lose)+2*playerTarget - 3*nextBid;
        double prob = logistic(value,getE(player.getName()));
        //System.out.println("Round " + String.valueOf(round) + " Prob "+ String.valueOf((prob) + "Value: "+String.valueOf(value)+" player cash: " + String.valueOf(player.getCash())+ " NextBid: " + String.valueOf(nextBid) + " win " + String.valueOf(win) + " lose "+ String.valueOf(lose)));
        return prob;
    }

    private double getCost(int cardGot, int moneyLost){

        //System.out.println(totalPropertyValuesRemaining);
        return cardGot + - 2.6*moneyLost;
    }


    // If only I new how to make these generic
    ArrayList<Integer> removeCard(ArrayList<Integer> cards, int c){
        ArrayList<Integer> newArray = new ArrayList<Integer>();
        for (int i=0;i<cards.size();i++){
            if( i != c) {
                newArray.add(cards.get(i));
            }
        }
        return newArray;
    }
    ArrayList<PlayerRecord> removePlayer(ArrayList<PlayerRecord> players, int p){
        ArrayList<PlayerRecord> newArray = new ArrayList<PlayerRecord>();
        for (int i=0;i<players.size();i++){
            if( i != p) {
                newArray.add(players.get(i));
            }
        }
        return newArray;
    }

    // Approximates the probabilities of bidding for the other strategies moves
    private double simulateRound( ArrayList<PlayerRecord> playersRemaining, int nextBid, ArrayList<Integer> cards, int index)
    {
        // Make a circular array hack
        index = index % (playersRemaining.size());
        // Is this player the current strategy?
        if (index == 0){
            return myMove(playersRemaining,nextBid,cards);
        }

        int finalCard = cards.size() - 1;
        double prob = calculateProb(playersRemaining.get(index), nextBid , cards.get(finalCard), cards.get(0));
        double probDrop = 1.0 - prob;

        if (new_round == true){
            names_m [index-1] = playersRemaining.get(index).getName();
            probs_m [index-1] = prob;
        }

        ArrayList<Integer>  newCards = removeCard(cards, 0);
        ArrayList<PlayerRecord> newPlayers = removePlayer(playersRemaining,index);

        //cut off for calculation
        if(prob > 0.02){
            // First run the simulation if this player doesn't drop out
            double value = prob*simulateRound(playersRemaining,nextBid+1,cards,index+1);

            // Then run it for if they do drop out
            return value + probDrop*simulateRound(newPlayers,nextBid,newCards,index);

        } else {
            //so the player dropped out
            return simulateRound(newPlayers, nextBid, newCards,index);
        }

        //return 0;
    }

    // Logic for how this Strategy would move
    public double myMove(ArrayList<PlayerRecord> playersRemaining, int nextBid, ArrayList<Integer> cards){

        new_round = false;

        if (playersRemaining.size() == 1){
            return getCost(cards.get(0),myBid);
        }

        double costDrop = getCost(cards.get(0),(myBid)/2);

        int oldBid = myBid;
        myBid = nextBid;
        double costBid = simulateRound(playersRemaining,nextBid+1,cards,1);
        myBid = oldBid;


        if (costBid > costDrop){
            return costBid;
        } else {
            return costDrop;
        }

    }


    @Override
    public int bid(PlayerRecord player, AuctionState auction) {

        // Array list of remaining cards
        ArrayList<Integer> cards = new ArrayList<Integer>();
        for (Card c : auction.getCardsInAuction()){
            cards.add(c.getQuality());
        }
        //ordered from smallest to largest;
        Collections.sort(cards);

        //get the remaining players.
        ArrayList<PlayerRecord> playerList = new ArrayList<PlayerRecord>();
        playerList.addAll(auction.getPlayersInAuction());

        me = player;
        playerCount = auction.getPlayersInAuction().size();
        round = auction.getCardsInDeck().size()/auction.getPlayers().size() + 1;
        myBid = auction.getCurrentBid()+1;
        new_round = true;

        if (dropped_out == false){
            int i = 1;
            int iProb = 0;
            for (String n : names_m) {
                if (i < playerList.size()) {

                    PlayerRecord p = playerList.get(i);
                    if (p == null || n == null) {
                        break;
                    }
                    if (n.equals(p.getName())) {
                        double e = otherStrats.get(n);
                        double newProb = e + 0.03 * (1.0 - probs_m[iProb]);
                        otherStrats.put(n, newProb);
                        i++;
                    } else {
                        double e = otherStrats.get(n);
                        double newProb = e - 0.03 * (probs_m[iProb]);
                        otherStrats.put(n, newProb);
                    }
                    iProb++;
                }

            }
        }

        probs_m = new double[5];
        names_m = new String[5];


        double costDrop = getCost(cards.get(0),(me.getCurrentBid())/2);
        double costPlay = simulateRound(playerList,auction.getCurrentBid()+1,cards,1);

        if (costPlay >= costDrop && player.getCash() >= auction.getCurrentBid() + 1){
            dropped_out = false;
            return auction.getCurrentBid() + 1;
        }

        dropped_out = true;
        // no bid
        return -1;
    }

    private void log(String output) {
        System.out.println(output);
    }
}
