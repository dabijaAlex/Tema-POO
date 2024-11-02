package Game.Minions;

import Game.Board;
import Game.Minion;
import fileio.CardInput;

public class Cursed extends Minion {
    public Cursed(CardInput card) {
        super(card);
    }

    @Override
    public boolean placeCard(Board board, int player) {
        return super.placeCard(board, player);
    }

    @Override
    public void useAbility(Minion minion) {
        int aux = minion.getHealth();
        minion.setHealth(minion.getAttackDamage());
        minion.setAttackDamage(aux);
    }
}
