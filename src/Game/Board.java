package Game;

import java.util.ArrayList;
import Game.Minions.Tank;
import fileio.Coordinates;

public class Board {
    private ArrayList<ArrayList<Minion>> board;


    public Board() {
        board = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            board.add(new ArrayList<>());
        }
    }


    public ArrayList<ArrayList<Minion>> getCardsOnTable_copy() {
        ArrayList<ArrayList<Minion>> copy = new ArrayList<>();
        for(int i = 0; i < board.size(); i++){
            copy.add(new ArrayList<>());
            for(int j = 0; j < board.get(i).size(); j++){
                copy.get(i).add(new Minion(board.get(i).get(j)));
            }
        }
        return copy;
    }

    public boolean addToRow(Minion minion, int row) {
        if(board.size() == 5){
            return false;
        }
        board.get(row).add(minion);
        return true;
    }

    //pt p2 - 1 si p1 - 2
    public Tank isTankInRow(int attacking_player) {
        int row;
        if(attacking_player == 1){
            row = 1;
        } else{
            row = 2;
        }
        for(Minion minion : board.get(row)) {
            if(minion.isTank()) {
                return (Tank) minion;
            }

        }
        return null;
    }

    public Minion getMinionFromBoard(Coordinates coordinates) {
        if(board.get(coordinates.getX()).size() <= coordinates.getY()) {
            return null;
        }
        return board.get(coordinates.getX()).get(coordinates.getY());
    }

    public void removeMinionFromBoard(Minion minion) {
        if(minion == null)
            return;
        for(ArrayList<Minion> row : board) {
            row.removeIf(current -> current == (minion));
//            for(Minion current : row) {
//                if(current == minion) {
//                    row.remove(current);
//                }
//            }
        }
    }

    public ArrayList<Minion> getFrozenMinions(){
        ArrayList<Minion> frozen = new ArrayList<>();
        for(ArrayList<Minion> row : board) {
            for(Minion minion : row) {
                if(minion.isFrozen())
                    frozen.add(new Minion(minion));
            }
        }
        return frozen;
    }

    public void resetAttacks(){
        for(ArrayList<Minion> row : board){
            for(Minion minion : row){
                minion.setHasAttacked(false);
            }
        }
    }
    public void resetAbilities(){
        for(ArrayList<Minion> row : board){
            for(Minion minion : row){
                minion.setHasAttacked(false);
            }
        }
    }

    public void unfreeze(int player) {
        ArrayList<Minion> frontRow;
        ArrayList<Minion> backRow;
        if(player == 2){
            frontRow = board.get(0);
            backRow = board.get(1);
        }
        else {
            frontRow = board.get(2);
            backRow = board.get(3);
        }
        for(Minion minion : frontRow){
            minion.setFrozen(false);
        }
        for(Minion minion : backRow){
            minion.setFrozen(false);
        }
    }

    public ArrayList<ArrayList<Minion>> getBoard() {
        return board;
    }

}
