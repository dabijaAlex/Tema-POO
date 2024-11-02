package Game.Minions;

import Game.Board;
import Game.Minion;
import fileio.CardInput;

public class Miraj extends Minion {
    public Miraj(final CardInput card) {
        super(card);
    }

    @Override
    public boolean placeCard(final Board board, final int player) {
        return board.addToRow(this, 3 - player);
    }

    @Override
    public void useAbility(final Minion minion){
        int aux = minion.getHealth();
        minion.setHealth(this.getHealth());
        this.setHealth(aux);
    }
}
