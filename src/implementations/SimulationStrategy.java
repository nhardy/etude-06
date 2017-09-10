package implementations;

import forsale.AuctionState;
import forsale.Card;
import forsale.Player;
import forsale.PlayerRecord;

import java.util.ArrayList;
import java.util.Collections;

public class SimulationStrategy implements BidStrategy {

    private int round;
    private int playerCount;

    private PlayerRecord me;
    private int myBid;
    private AuctionState auction_m;

    // converts a players bid 'score' to a probability.
    private static double logistic(double x){
        final double k = 0.13;
        return 1.0/(1.0+Math.exp(-x*k));
    }

    //
    private double calculateProb(PlayerRecord player, int nextBid, int win, int lose){

        if ( player.getCash() < nextBid) {
            //they can't bid
            return 0.0;
        }

       /* int max_bid = player.getCash() / (round);

        //Bid if the current bid is smaller than the max_bid.
        if (nextBid <= max_bid ){
            return 1.0;
        }
        return 0;*/


        double playerTarget = (double)player.getCash()/(round);
        //double value = 2*nextBid - 2*nextBid + (win - lose) - 10;
        double value = (win - lose)+2*playerTarget - 3*nextBid;
        double prob = logistic(value);
        //System.out.println("Round " + String.valueOf(round) + " Prob "+ String.valueOf((prob) + "Value: "+String.valueOf(value)+" player cash: " + String.valueOf(player.getCash())+ " NextBid: " + String.valueOf(nextBid) + " win " + String.valueOf(win) + " lose "+ String.valueOf(lose)));
        return prob;
    }

    private double getCost(int cardGot, int moneyLost){
        double averageSpent = 14.0/playerCount;
        double playerSpend = (double)me.getCash()/(round);

        return cardGot - 2.6*moneyLost;
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
        if (!playersRemaining.get(0).getName().equals(me.getName())){
            System.out.println("Error: ");
        }

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

        /*
        System.out.println(String.valueOf(costDrop)+ " " + String.valueOf(costBid) );
        //compare costs
        if (costDrop > costBid ){
            System.out.println("Money " + String.valueOf(player.getCash()) + " Round " + String.valueOf(round));
            System.out.println("---------");
            return -1;
        }
        if (player.getCash() < auction.getCurrentBid()+1){
            System.out.println("Money " + String.valueOf(player.getCash()) + " Round " + String.valueOf(round));
            System.out.println("---------");
            System.out.println("too poor");
        }*/

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

        double costDrop = getCost(cards.get(0),(me.getCurrentBid())/2);
        double costPlay = simulateRound(playerList,auction.getCurrentBid()+1,cards,1);

        if (costPlay >= costDrop){
            return auction.getCurrentBid() + 1;
        }

        // no bid
        return -1;
    }

    private void log(String output) {
        System.out.println(output);
    }
}
