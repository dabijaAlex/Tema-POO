package Game.Minions;

import Game.Board;
import Game.Minion;
import fileio.CardInput;

public class Ripper extends Minion {
    public Ripper(CardInput card) {
        super(card);
    }

    @Override
    public boolean placeCard(Board board, int player) {
        return board.addToRow(this, 3 - player);
    }

    @Override
    public void useAbility(Minion minion) {
        minion.setAttackDamage(minion.getAttackDamage() - 2);
        if(minion.getAttackDamage() < 0)
            minion.setAttackDamage(0);
    }
}
