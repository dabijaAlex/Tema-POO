package Game.Heroes;

import Game.Board;
import Game.Card;
import Game.Hero;
import Game.Minion;
import fileio.CardInput;

public class LordRoyce extends Hero {

    public LordRoyce(CardInput hero) {
        super(hero);
    }

    @Override
    public void useAbility(Board board, int row) {
        for(Minion minion : board.getBoard().get(row)){
            minion.setFrozen(true);
        }
        System.out.println("Royce");
    }
}
