package Game;

import fileio.CardInput;

public class Hero extends Card{

    public Hero(CardInput input) {
        super(input);
        this.setHealth(30);
    }


    public void UseAbility() {
        return;
    }

}
