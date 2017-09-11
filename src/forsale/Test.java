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

        Strategy mv1Strategy = new CreateStrategy(new MarketValue1BidStrategy(), new BasicChooseCardStrategy());

        Strategy mv2BasicStrategy = new CreateStrategy(new MarketValue2BidStrategy(), new BasicChooseCardStrategy());

        Strategy mv2RandomStrategy = new CreateStrategy(new MarketValue2BidStrategy(), new RandomChooseCardStrategy());

        Strategy mv2ImprovedStrategy = new CreateStrategy(new MarketValue2BidStrategy(), new ImprovedChooseCardStrategy());


        Strategy anotherStrategy = new AnotherStrategy();


        Strategy simpleStrategy = new CreateStrategy(new SimpleStrategy(), new BasicChooseCardStrategy());

        Strategy simpleTwo = new CreateStrategy(new SimpleTwo(), new BasicChooseCardStrategy());

        HashMap<String, Integer> wins = new HashMap<String, Integer>();

        for (int n = 0; n < 10000; n++) {
            ArrayList<Player> players = new ArrayList<Player>();

            // players.add(new Player("Another Strat", anotherStrategy));

            players.add(new Player("Improved 1", new CreateStrategy(new ImprovedStrategy(), new BasicChooseCardStrategy())));
            players.add(new Player("Improved 2", new CreateStrategy(new ImprovedStrategy(), new ImprovedChooseCardStrategy())));

            players.add(new Player("Simple Two", simpleTwo));
            // players.add(new Player("MV2 Random 1", mv2RandomStrategy));
            // players.add(new Player("MV2 Random 2", mv2RandomStrategy));
            players.add(new Player("Other Group", new CreateStrategy(new OtherStrategy(), new BasicChooseCardStrategy() )));

            players.add(new Player("Simulation Strategy", new CreateStrategy(new SimulationStrategy(), new BasicChooseCardStrategy())));
            players.add(new Player("Simulation Strategy Improved", new CreateStrategy(new SimulationStrategy(), new ImprovedChooseCardStrategy())));
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
