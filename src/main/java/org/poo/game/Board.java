package org.poo.game;

import java.util.ArrayList;
import org.poo.fileio.Coordinates;
import lombok.Getter;
import lombok.Setter;
import org.poo.game.minions.Tank;

@Getter @Setter
public final class Board {
    private ArrayList<ArrayList<Minion>> board;
    private final int boardSize = 4;
    private final int rowSize = 5;
    private final int p1FrontRow = 2;
    private final int p2FrontRow = 1;
    private final int p1BackRow = 3;
    private final int p2BackRow = 0;

    public Board() {
        board = new ArrayList<>();
        for (int i = 0; i < boardSize; i++) {
            board.add(new ArrayList<>());
        }
    }

    /**
     *
     * @return
     */
    public ArrayList<ArrayList<Minion>> getCardsOnTableCopy() {
        ArrayList<ArrayList<Minion>> copy = new ArrayList<>();
        for (int i = 0; i < board.size(); i++) {
            copy.add(new ArrayList<>());
            for (int j = 0; j < board.get(i).size(); j++) {
                copy.get(i).add(new Minion(board.get(i).get(j)));
            }
        }
        return copy;
    }

    /**
     *
     * @param minion
     * @param row
     * @return
     */
    public boolean addToRow(final Minion minion, final int row) {
        if (board.get(row).size() == rowSize) {
            return false;
        }
        board.get(row).add(minion);
        return true;
    }

    /**
     *
     * @param attackingPlayer
     * @return
     */
    public Tank isTankInRow(final int attackingPlayer) {
        int row;
        if (attackingPlayer == 1) {
            row = 1;
        } else {
            row = 2;
        }
        for (Minion minion: board.get(row)) {
            if (minion.isTank()) {
                return (Tank) minion;
            }

        }
        return null;
    }

    /**
     *
     * @param coordinates
     * @return
     */
    public Minion getMinionFromBoard(final Coordinates coordinates) {
        if (board.get(coordinates.getX()).size() <= coordinates.getY()) {
            return null;
        }
        return board.get(coordinates.getX()).get(coordinates.getY());
    }

    /**
     *
     * @param minion
     */
    public void removeMinionFromBoard(final Minion minion) {
        if (minion == null) {
            return;
        }
        for (ArrayList<Minion> row: board) {
            row.removeIf(current -> current == (minion));
        }
    }

    /**
     *
     * @return
     */
    public ArrayList<Minion> getFrozenMinions() {
        ArrayList<Minion> frozen = new ArrayList<>();
        for (ArrayList<Minion> row: board) {
            for (Minion minion: row) {
                if (minion.isFrozen()) {
                    frozen.add(new Minion(minion));
                }
            }
        }
        return frozen;
    }

    /**
     *
     */
    public void resetAttacks() {
        for (ArrayList<Minion> row: board) {
            for (Minion minion: row) {
                minion.setHasAttacked(false);
            }
        }
    }

    /**
     *
     */
    public void resetAbilities() {
        for (ArrayList<Minion> row: board) {
            for (Minion minion: row) {
                minion.setHasAttacked(false);
            }
        }
    }

    /**
     *
     * @param player
     */
    public void unfreeze(final int player) {
        ArrayList<Minion> frontRow;
        ArrayList<Minion> backRow;
        if (player == 2) {
            frontRow = board.get(p2BackRow);
            backRow = board.get(p2FrontRow);
        } else {
            frontRow = board.get(p1FrontRow);
            backRow = board.get(p1BackRow);
        }
        for (Minion minion: frontRow) {
            minion.setFrozen(false);
        }
        for (Minion minion: backRow) {
            minion.setFrozen(false);
        }
    }

}
