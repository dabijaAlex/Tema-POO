package Game.Heroes;

import Game.Board;
import Game.Card;
import Game.Hero;
import Game.Minion;
import fileio.CardInput;

public class EmpressThronia extends Hero {

    public EmpressThronia(CardInput hero) {
        super(hero);
    }

    @Override
    public void useAbility(Board board, int row) {
        Minion to_destroy = null;
        int max_health = 0;
        for(Minion minion : board.getBoard().get(row)){
            if(minion.getHealth() > max_health){
                max_health = minion.getHealth();
                to_destroy = minion;
            }
        }
        board.removeMinionFromBoard(to_destroy);
        System.out.println("girl");
    }
}
