/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forsale;

import implementations.*;
import java.util.ArrayList;

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

        Strategy basicStrategy = new BasicStrategy();
        
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(new Player("Null 1", s));
        players.add(new Player("Null 2", s));
        players.add(new Player("Random 1", r));
        players.add(new Player("Random 2", r));
        players.add(new Player("Basic 1", basicStrategy));
        players.add(new Player("Basic 2", basicStrategy));
        GameManager g = new GameManager(players);
        g.run();
        System.out.println(g.getLog());
    }

}
