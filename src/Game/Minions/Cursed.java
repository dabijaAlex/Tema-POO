package Game.Minions;

import Game.Board;
import Game.Minion;
import fileio.CardInput;

public final class Cursed extends Minion {
    public Cursed(final CardInput card) {
        super(card);
    }

    @Override
    public boolean placeCard(final Board board, final int player) {
        return super.placeCard(board, player);
    }

    @Override
    public void useAbility(final Minion minion) {
        int aux = minion.getHealth();
        minion.setHealth(minion.getAttackDamage());
        minion.setAttackDamage(aux);
    }
}
