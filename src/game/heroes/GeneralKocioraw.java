package game.heroes;

import game.Board;
import game.Hero;
import game.Minion;
import fileio.CardInput;

public final class GeneralKocioraw extends Hero {
    /**
     *
     * @param hero
     */
    public GeneralKocioraw(final CardInput hero) {
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
            minion.setAttackDamage(minion.getAttackDamage() + 1);
        }
    }
}
