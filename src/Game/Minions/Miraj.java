package Game.Minions;

import Game.Board;
import Game.Minion;
import fileio.CardInput;

public class Miraj extends Minion {
    public Miraj(CardInput card) {
        super(card);
    }

    @Override
    public boolean placeCard(Board board, int player) {
        return board.addToRow(this, 3 - player);
    }

    @Override
    public void useAbility(Minion minion){
        int aux = minion.getHealth();
        minion.setHealth(this.getHealth());
        this.setHealth(aux);
    }
}
