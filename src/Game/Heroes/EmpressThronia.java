package Game.Heroes;

import Game.Board;
import Game.Hero;
import Game.Minion;
import fileio.CardInput;

public final class EmpressThronia extends Hero {

    public EmpressThronia(final CardInput hero) {
        super(hero);
    }

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
