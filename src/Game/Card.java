package Game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CardInput;

import java.util.ArrayList;



public class Card {
    private int mana;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;

    @JsonIgnore
    private boolean hasAttacked;
    @JsonIgnore
    private boolean hasUsedAbility;



    public Card(CardInput cardInput) {
        this.mana = cardInput.getMana();
        this.description = cardInput.getDescription();
        this.colors = cardInput.getColors();
        this.name = cardInput.getName();
        this.health = cardInput.getHealth();
    }

    public Card(Card other){
        this.mana = other.mana;
        this.description = other.description;
        this.colors = other.colors;
        this.name = other.name;
        this.health = other.health;
        this.hasAttacked = false;
        this.hasUsedAbility = false;
    }

    @Override
    public String toString() {
        return "Card{" +
                "name='" + name + '\'' +
                ", colors=" + colors +
                ", mana=" + mana +
                ", health=" + health +
                ", description='" + description + '\'' +
                '}';
    }

    public void useAbility(Minion minion) {

    }

    public boolean isHasUsedAbility() {
        return hasUsedAbility;
    }

    public void setHasUsedAbility(boolean hasUsedAbility) {
        this.hasUsedAbility = hasUsedAbility;
    }

    public boolean isHasAttacked() {
        return hasAttacked;
    }

    public void setHasAttacked(boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
