package game.minions;

import game.Board;
import game.Minion;
import fileio.CardInput;

public final class Disciple extends Minion {
    public Disciple(final CardInput card) {
        super(card);
    }

    /**
     *
     * @param board
     * @param player
     * @return
     */
    @Override
    public boolean placeCard(final Board board, final int player) {
        return super.placeCard(board, player);
    }

    /**
     *
     * @param minion
     */
    @Override
    public void useAbility(final Minion minion) {
        minion.setHealth(minion.getHealth() + 2);
    }
}
