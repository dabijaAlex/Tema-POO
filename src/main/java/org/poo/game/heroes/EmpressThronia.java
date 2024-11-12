package org.poo.game.heroes;

import org.poo.fileio.CardInput;
import org.poo.game.Board;
import org.poo.game.Hero;
import org.poo.game.Minion;

public final class EmpressThronia extends Hero {

    /**
     *
     * @param hero
     */
    public EmpressThronia(final CardInput hero) {
        super(hero);
    }

    /**
     *
     * @param board
     * @param row
     */
    @Override
    public void useAbility(final Board board, final int row) {
        Minion toDestroy = null;
        int maxHealth = 0;
        for (Minion minion : board.getBoard().get(row)) {
            if (minion.getHealth() > maxHealth) {
                maxHealth = minion.getHealth();
                toDestroy = minion;
            }
        }
        board.removeMinionFromBoard(toDestroy);
    }
}
