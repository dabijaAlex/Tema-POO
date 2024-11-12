package org.poo.game.minions;

import org.poo.fileio.CardInput;
import org.poo.game.Board;
import org.poo.game.Minion;

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
