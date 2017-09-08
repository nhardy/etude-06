package implementations;

import java.util.ArrayList;
import java.util.Collections;
import forsale.*;

public class RandomChooseCardStrategy implements ChooseCardStrategy {

    public Card chooseCard(PlayerRecord player, SaleState sale) {
        return player.getCards().get((int) (Math.random() * player.getCards().size()));
    }
}
