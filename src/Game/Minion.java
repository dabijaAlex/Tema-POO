package Game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CardInput;


public class Minion extends Card{
    private int attackDamage;
    @JsonIgnore
    private boolean frozen;


    public Minion(CardInput input) {
        super(input);
        this.setAttackDamage(input.getAttackDamage());
        this.frozen = false;
    }

    public boolean placeCard(Board board, int player){
        return board.addToRow(this, 3 - 3 * (player - 1));
    }

    @JsonIgnore
    public boolean isTank(){
        return false;
    }

    public void useAttack(Minion defender, Board board) {
        defender.setHealth(defender.getHealth() - this.getAttackDamage());
        if(defender.getHealth() <= 0){
            board.removeMinionFromBoard(defender);
        }
    }



    public Minion(Minion other) {
        super(other);
        this.attackDamage = other.attackDamage;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }
}
