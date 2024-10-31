package Game;

import java.util.ArrayList;

public class Board {
    private ArrayList<ArrayList<Minion>> board;


    public Board() {
        ArrayList<ArrayList<Minion>> board = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            board.add(new ArrayList<>());
        }
    }

}
