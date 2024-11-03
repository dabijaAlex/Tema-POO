package game.heroes;

import game.Hero;
import game.Board;
import game.Minion;
import fileio.CardInput;

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
