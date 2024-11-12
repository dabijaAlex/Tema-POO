package org.poo.game.heroes;

import org.poo.fileio.CardInput;
import org.poo.game.Board;
import org.poo.game.Hero;
import org.poo.game.Minion;

public final class KingMudface extends Hero {
    /**
     *
     * @param hero
     */
    public KingMudface(final CardInput hero) {
        super(hero);
    }

    /**
     *
     * @param board
     * @param row
     */
    @Override
    public void useAbility(final Board board, final int row) {
        for (Minion minion : board.getBoard().get(row)) {
            minion.setHealth(minion.getHealth() + 1);
        }
    }
}
