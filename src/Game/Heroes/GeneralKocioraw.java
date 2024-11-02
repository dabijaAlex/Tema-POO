package Game.Heroes;

import Game.Board;
import Game.Hero;
import Game.Minion;
import fileio.CardInput;

public class GeneralKocioraw extends Hero {
    public GeneralKocioraw(CardInput hero) {
        super(hero);
    }

    @Override
    public void useAbility(Board board, int row) {
        for(Minion minion : board.getBoard().get(row)){
            minion.setAttackDamage(minion.getAttackDamage() + 1);
        }
        System.out.println("gen");
    }
}
