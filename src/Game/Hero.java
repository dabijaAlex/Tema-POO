package Game;

import fileio.CardInput;

public class Hero extends Card{

    public Hero(CardInput input) {
        super(input);
        this.setHealth(30);
    }

    public Hero(Hero other) {
        super(other);
    }

    public void useAbility(Board board, int row) {
        System.out.println("aici ma");
    }
}
