package Game.Minions;

import Game.Board;
import Game.Minion;
import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CardInput;

public class Tank extends Minion {
    public Tank(CardInput card) {
        super(card);
    }


    @Override
    public boolean placeCard(Board board, int player) {
        return board.addToRow(this, 3 - player);
    }

    @Override @JsonIgnore
    public boolean isTank() {
        return true;
    }
}
