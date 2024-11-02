package Game.Minions;

import Game.Board;
import Game.Minion;
import fileio.CardInput;

public class Disciple extends Minion {
    public Disciple(CardInput card) {
        super(card);
    }

    @Override
    public boolean placeCard(Board board, int player) {
        return super.placeCard(board, player);
    }

    @Override
    public void useAbility(Minion minion) {
        minion.setHealth(minion.getHealth() + 2);
    }
}
