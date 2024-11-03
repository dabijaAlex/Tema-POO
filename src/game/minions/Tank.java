package game.minions;

import game.Board;
import game.Minion;
import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CardInput;

public final class Tank extends Minion {
    /**
     *
     * @param card
     */
    public Tank(final CardInput card) {
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
     * @return
     */
    @Override @JsonIgnore
    public boolean isTank() {
        return true;
    }
}
