package forsale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;

/**
 * The manager for a single game of "For Sale"
 *
 * @author Michael Albert
 */
public class GameManager {

    private final ArrayList<Player> players;
    private ArrayList<Card> cardsRemaining;
    private final ArrayList<Integer> chequesRemaining = new ArrayList<Integer>();
    private static final int[] CHEQUES = {0, 0, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 13, 13, 14, 14, 15, 15};
    private StringBuilder log;

    /**
     * Sets up a game from a list of players (must be 5 or 6).
     * 
     * @param players the players
     */
    public GameManager(ArrayList<Player> players) {
        this.players = players;
    }

    /**
     * Runs the game.
     */
    public void run() {
        
        log = new StringBuilder();
        logPlayers();
        
        // Deal the cards
        cardsRemaining = new ArrayList<Card>(Arrays.asList(Card.values()));
        Collections.shuffle(cardsRemaining);
        
        // Conduct the auction phase
        Player firstBidder = players.get(0);
        while (cardsRemaining.size() > 0) {
            firstBidder = conductAuction(firstBidder);
        }
        
        // Create and shuffle the cheques
        chequesRemaining.clear();
        for (int c : CHEQUES) {
            chequesRemaining.add(c);
        }
        Collections.shuffle(chequesRemaining);
        
        // Conduct the sales phase
        while (chequesRemaining.size() > 0) {
            conductSale();
        }
        
        // Tidy up and finish
        determineFinalStandings();
    }

    private Player conductAuction(Player firstBidder) {

        // Cycle players to the beginner (first bidder)
        while (players.get(0) != firstBidder) {
            players.add(players.remove(0));
        }

        //Initialize
        int currentBid = 0;
        ArrayList<Card> cardsInAuction = new ArrayList<Card>();
        for (int i = 0; i < players.size(); i++) {
            cardsInAuction.add(cardsRemaining.remove(0));
        }
        Collections.sort(cardsInAuction);
        log("Auction: " + cardsInAuction);
        ArrayList<Player> playersInAuction = new ArrayList<Player>(players);

        do {
            AuctionState a = getAuctionState(players, playersInAuction, cardsInAuction, cardsRemaining, currentBid);
            if (a.getPlayersInAuction().size() == 1) {
                Player winner = playersInAuction.get(0);
                Card c = cardsInAuction.remove(0);
                log(winner + " wins, getting " +c + " (" + c.getQuality() +") for " + winner.getBid());
                winner.completeWinningPurchase(c);
                // The auction winner is the first bidder in the next round (if any)
                return winner;
            }
            Player p = playersInAuction.remove(0);
            int bid = p.getStrategy().bid(new PlayerRecord(p), a);
            if (bid <= currentBid || bid > p.getCash()) {
                int price = p.getBid() - p.getBid()/2;
                Card c = cardsInAuction.remove(0);
                log(p + " drops out, getting " + c + " (" + c.getQuality() +") for " + price);
                p.completeLosingPurchase(c);
            } else {
                log(p + " bids " + bid);
                currentBid = bid;
                p.setBid(bid);
                playersInAuction.add(p);
            }
        } while (true);
    }

    private void conductSale() {
        ArrayList<Integer> chequesAvailable = new ArrayList<Integer>();
        for (int i = 0; i < players.size(); i++) {
            chequesAvailable.add(chequesRemaining.remove(0));
        }
        Collections.sort(chequesAvailable);
        SaleState s = getSaleState(chequesAvailable, chequesRemaining);
        TreeMap<Card,Player> cardsPlayed = new TreeMap<Card,Player>();
        for(Player p : players) {
            Card c = p.getStrategy().chooseCard(new PlayerRecord(p), s);
            if (!p.getCards().contains(c)) {
                c = p.getCards().get(0);
            }
            cardsPlayed.put(c, p);
        }
        for(Card c : cardsPlayed.keySet()) {
            Player p = cardsPlayed.get(c);
            p.removeCard(c);
            p.addCash(chequesAvailable.remove(0));
        }
    }

    private ArrayList<PlayerRecord> getPlayerRecords() {
        ArrayList<PlayerRecord> pl = new ArrayList<PlayerRecord>();
        for (Player p : players) {
            pl.add(new PlayerRecord(p));
        }
        return pl;
    }

    private AuctionState getAuctionState(ArrayList<Player> players, ArrayList<Player> playersInAuction, ArrayList<Card> cardsInAuction, ArrayList<Card> cardsRemaining, int currentBid) {
        ArrayList<PlayerRecord> al = new ArrayList<PlayerRecord>();
        for (Player p : playersInAuction) {
            al.add(new PlayerRecord(p));
        }
        return new AuctionState(getPlayerRecords(), al, new ArrayList<Card>(cardsInAuction), new ArrayList<Card>(cardsRemaining), currentBid);
    }

    private SaleState getSaleState(ArrayList<Integer> chequesAvailable, ArrayList<Integer> chequesRemaining) {
        return new SaleState(getPlayerRecords(), new ArrayList<Integer>(chequesAvailable), new ArrayList<Integer>(chequesRemaining));
    }

    private void determineFinalStandings() {
        Collections.sort(players, new Comparator<Player>() {

            @Override
            public int compare(Player o1, Player o2) {
                return o2.getCash() - o1.getCash();
            }
            
        });
        log("Final standings:");
        for(Player p : players) log(p.getCash() + " " + p);
    }
    
    public ArrayList<PlayerRecord> getFinalStandings() {
        return getPlayerRecords();
    }

    private void log(String string) {
        log.append(string);
        log.append("\n");
    }

    private void logPlayers() {
        log.append("Players: ");
        log.append(players);
        log.append("\n");
    }

    /**
     * Gets a log of the game
     * @return a log of the game.
     */
    public String getLog() {
        return log.toString();
    }
}
