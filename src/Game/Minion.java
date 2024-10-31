package Game;

import fileio.CardInput;

public class Minion extends Card{
    private int attackDamage;


    public Minion(CardInput input) {
        super(input);
        this.setAttackDamage(input.getAttackDamage());
    }







    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }
}
