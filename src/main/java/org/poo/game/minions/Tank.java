package org.poo.game.minions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.fileio.CardInput;
import org.poo.game.Board;
import org.poo.game.Minion;

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
