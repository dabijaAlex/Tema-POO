package game.heroes;

import game.Board;
import game.Hero;
import game.Minion;
import fileio.CardInput;

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
