package org.poo.game;

import org.poo.fileio.CardInput;

public class Hero extends Card {
    private final int heroHealth = 30;

    /**
     *
     * @param input
     */
    public Hero(final CardInput input) {
        super(input);
        this.setHealth(heroHealth);
    }

    /**
     *
     * @param other
     */
    public Hero(final Hero other) {
        super(other);
    }

    /**
     *
     * @param board
     * @param row
     */
    public void useAbility(final Board board, final int row) {
    }
}
