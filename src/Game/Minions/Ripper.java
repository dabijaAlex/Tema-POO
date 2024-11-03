package Game.Minions;

import Game.Board;
import Game.Minion;
import fileio.CardInput;

public final class Ripper extends Minion {
    public Ripper(final CardInput card) {
        super(card);
    }

    @Override
    public boolean placeCard(final Board board, final int player) {
        return board.addToRow(this, 3 - player);
    }

    @Override
    public void useAbility(final Minion minion) {
        minion.setAttackDamage(minion.getAttackDamage() - 2);
        if (minion.getAttackDamage() < 0) {
            minion.setAttackDamage(0);
        }
    }
}
