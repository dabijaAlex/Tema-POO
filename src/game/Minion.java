package game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CardInput;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Minion extends Card {
    private int attackDamage;
    @JsonIgnore
    private boolean frozen;

    /**
     *
     * @param input
     */
    public Minion(final CardInput input) {
        super(input);
        this.setAttackDamage(input.getAttackDamage());
        this.frozen = false;
    }

    /**
     *
     * @param other
     */
    public Minion(final Minion other) {
        super(other);
        this.attackDamage = other.attackDamage;
        this.frozen = other.frozen;
    }

    /**
     *
     * @param board
     * @param player
     * @return
     */
    public boolean placeCard(final Board board, final int player) {
        return board.addToRow(this, (board.getBoardSize() - 1)
                - (board.getBoardSize() - 1) * (player - 1));
    }

    /**
     *
     * @return
     */
    @JsonIgnore
    public boolean isTank() {
        return false;
    }

    /**
     *
     * @param defender
     * @param board
     */
    public void useAttack(final Minion defender, final Board board) {
        defender.setHealth(defender.getHealth() - this.getAttackDamage());
        if (defender.getHealth() <= 0) {
            board.removeMinionFromBoard(defender);
        }
    }
}
