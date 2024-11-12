package org.poo.game.minions;

import org.poo.fileio.CardInput;
import org.poo.game.Board;
import org.poo.game.Minion;

public final class Cursed extends Minion {
    public Cursed(final CardInput card) {
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
        int aux = minion.getHealth();
        minion.setHealth(minion.getAttackDamage());
        minion.setAttackDamage(aux);
    }
}
