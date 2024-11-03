package Game.Heroes;

import Game.Hero;
import Game.Board;
import Game.Minion;
import fileio.CardInput;

public final class KingMudface extends Hero {
    public KingMudface(final CardInput hero) {
        super(hero);
    }

    @Override
    public void useAbility(final Board board, final int row) {
        for (Minion minion : board.getBoard().get(row)) {
            minion.setHealth(minion.getHealth() + 1);
        }
    }
}
