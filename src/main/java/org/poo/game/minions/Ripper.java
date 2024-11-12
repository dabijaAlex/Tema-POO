package org.poo.game.minions;

import org.poo.fileio.CardInput;
import org.poo.game.Board;
import org.poo.game.Minion;

public final class Ripper extends Minion {
    /**
     *
     * @param card
     */
    public Ripper(final CardInput card) {
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
        return board.addToRow(this, board.getBoardSize() - 1 - player);
    }

    /**
     *
     * @param minion
     */
    @Override
    public void useAbility(final Minion minion) {
        minion.setAttackDamage(minion.getAttackDamage() - 2);
        if (minion.getAttackDamage() < 0) {
            minion.setAttackDamage(0);
        }
    }
}
