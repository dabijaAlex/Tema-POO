package game.minions;

import game.Board;
import game.Minion;
import fileio.CardInput;

public final class Miraj extends Minion {
    /**
     *
     * @param card
     */
    public Miraj(final CardInput card) {
        super(card);
    }

    /**
     *
     * @param board
     * @param player
     * @return
     */
    @Override
    public boolean placeCard(final Board board, final int player) {
        return board.addToRow(this, board.getBoardSize() - 1 - player);
    }

    /**
     *
     * @param minion
     */
    @Override
    public void useAbility(final Minion minion) {
        int aux = minion.getHealth();
        minion.setHealth(this.getHealth());
        this.setHealth(aux);
    }
}
