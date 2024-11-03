package game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CardInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;



public class Card {
    @Getter @Setter
    private int mana;
    @Getter @Setter
    private int health;
    @Getter @Setter
    private String description;
    @Getter @Setter
    private ArrayList<String> colors;
    @Getter @Setter
    private String name;

    @Getter @Setter @JsonIgnore
    private boolean hasAttacked;
    @Getter @Setter @JsonIgnore
    private boolean hasUsedAbility;

    /**
     *
     * @param cardInput
     */
    public Card(final CardInput cardInput) {
        this.mana = cardInput.getMana();
        this.description = cardInput.getDescription();
        this.colors = cardInput.getColors();
        this.name = cardInput.getName();
        this.health = cardInput.getHealth();
    }

    /**
     *
     * @param other
     */
    public Card(final Card other) {
        this.mana = other.mana;
        this.description = other.description;
        this.colors = other.colors;
        this.name = other.name;
        this.health = other.health;
        this.hasAttacked = false;
        this.hasUsedAbility = false;
    }

    /**
     *
     * @param minion
     */
    public void useAbility(final Minion minion) {

    }
}
