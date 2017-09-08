/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forsale;

import implementations.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author MichaelAlbert
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // A null strategy - never bid, always play your top card.
        Strategy s = new Strategy() {

            @Override
            public int bid(PlayerRecord p, AuctionState a) {
                return -1;
            }

            @Override
            public Card chooseCard(PlayerRecord p, SaleState s) {
                return p.getCards().get(0);
            }

        };

        // A random strategy - make a random bid up to your amount remaining,
        // choose a rand card to sell.
        Strategy r = new Strategy() {

            @Override
            public int bid(PlayerRecord p, AuctionState a) {
                return (int) (1 + (Math.random()*p.getCash()));
            }

            @Override
            public Card chooseCard(PlayerRecord p, SaleState s) {
                return p.getCards().get((int) (Math.random()*p.getCards().size()));
            }

        };

        Strategy simpleStrategy = new SimpleStrategy();

        Strategy mv1Strategy = new Strategy() {
            @Override
            public int bid(PlayerRecord player, AuctionState auction) {
                return new MarketValue1BidStrategy().bid(player, auction);
            }

            @Override
            public Card chooseCard(PlayerRecord player, SaleState sale) {
                return new BasicChooseCardStrategy().chooseCard(player, sale);
            }
        };
        Strategy mv2BasicStrategy = new Strategy() {
            @Override
            public int bid(PlayerRecord player, AuctionState auction) {
                return new MarketValue2BidStrategy().bid(player, auction);
            }

            @Override
            public Card chooseCard(PlayerRecord player, SaleState sale) {
                return new BasicChooseCardStrategy().chooseCard(player, sale);
            }
        };
        Strategy mv2RandomStrategy = new Strategy() {
            @Override
            public int bid(PlayerRecord player, AuctionState auction) {
                return new MarketValue2BidStrategy().bid(player, auction);
            }

            @Override
            public Card chooseCard(PlayerRecord player, SaleState sale) {
                return new RandomChooseCardStrategy().chooseCard(player, sale);
            }
        };
        Strategy mv2ImprovedStrategy = new Strategy() {
            @Override
            public int bid(PlayerRecord player, AuctionState auction) {
                return new MarketValue2BidStrategy().bid(player, auction);
            }

            @Override
            public Card chooseCard(PlayerRecord player, SaleState sale) {
                return new ImprovedChooseCardStrategy().chooseCard(player, sale);
            }
        };

        Strategy anotherStategy = new AnotherStrategy();

        Strategy simpleTwo = new SimpleTwo();

        Strategy improvedStrategy = new ImprovedStrategy();

        HashMap<String, Integer> wins = new HashMap<String, Integer>();

        for (int n = 0; n < 10000; n++) {
            ArrayList<Player> players = new ArrayList<Player>();

            players.add(new Player("Simple 1", simpleStrategy));
            players.add(new Player("Another", anotherStategy));
            players.add(new Player("Improved 1", improvedStrategy));
            players.add(new Player("Simple Two", simpleTwo));
            // players.add(new Player("MV2 Random 1", mv2RandomStrategy));
            // players.add(new Player("MV2 Random 2", mv2RandomStrategy));
            players.add(new Player("MV2 Basic 1", mv2BasicStrategy));
            players.add(new Player("MV2 Basic 2", mv2BasicStrategy));
            // players.add(new Player("MV2 Improved 1", mv2ImprovedStrategy));
            // players.add(new Player("MV2 Improved 2", mv2ImprovedStrategy));
            Collections.shuffle(players);
            GameManager g = new GameManager(players);
            g.run();

            ArrayList<PlayerRecord> finalStandings = g.getFinalStandings();
            Collections.sort(finalStandings, new Comparator<PlayerRecord>() {
                @Override
                public int compare(PlayerRecord pr1, PlayerRecord pr2) {
                    return pr2.getCash() - pr1.getCash();
                }
            });
            String winner = finalStandings.get(0).getName();
            wins.put(winner, wins.getOrDefault(winner, 0) + 1);
        }

        ArrayList<Map.Entry<String, Integer>> sortedWins = new ArrayList<Map.Entry<String, Integer>>();
        for (Map.Entry<String, Integer> entry : wins.entrySet()) {
            sortedWins.add(entry);
        }
        Collections.sort(sortedWins, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                return e2.getValue() - e1.getValue();
            }
        });

        for (Map.Entry<String, Integer> entry : sortedWins) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

}
