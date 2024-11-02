package Game.Heroes;

import Game.Hero;
import Game.Board;
import Game.Minion;
import fileio.CardInput;

public class KingMudface extends Hero {
    public KingMudface(CardInput hero) {
        super(hero);
    }

    @Override
    public void useAbility(Board board, int row) {
        for(Minion minion : board.getBoard().get(row)){
            minion.setHealth(minion.getHealth() + 1);
        }
        System.out.println("Mudface");
    }
}
