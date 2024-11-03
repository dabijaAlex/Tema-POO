package Game.Heroes;

import Game.Board;
import Game.Hero;
import Game.Minion;
import fileio.CardInput;

public final class LordRoyce extends Hero {

    public LordRoyce(final CardInput hero) {
        super(hero);
    }

    @Override
    public void useAbility(final Board board, final int row) {
        for (Minion minion : board.getBoard().get(row)) {
            minion.setFrozen(true);
        }
    }
}
