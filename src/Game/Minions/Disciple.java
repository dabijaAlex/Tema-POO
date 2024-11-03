package Game.Minions;

import Game.Board;
import Game.Minion;
import fileio.CardInput;

public final class Disciple extends Minion {
    public Disciple(final CardInput card) {
        super(card);
    }

    @Override
    public boolean placeCard(final Board board, final int player) {
        return super.placeCard(board, player);
    }

    @Override
    public void useAbility(final Minion minion) {
        minion.setHealth(minion.getHealth() + 2);
    }
}
