package Game;

import fileio.CardInput;
import fileio.Input;
import fileio.GameInput;
import fileio.DecksInput;

import java.util.ArrayList;

import java.util.Random;
import java.util.Collections;

public class Player {
    private ArrayList<Minion> deck;
    private ArrayList<Minion> handCards;
    private Hero hero;
    private int availableMana;

    public Player(Hero hero, DecksInput decks, int index, GameInput currentGame) {
        this.hero = hero;
        deck = new ArrayList<Minion>();
        handCards = new ArrayList<Minion>();
        for(CardInput card : decks.getDecks().get(index)) {
            Minion x = new Minion(card);
            this.deck.add(x);
        }
        Random rand = new Random();
        rand.setSeed(currentGame.getStartGame().getShuffleSeed());
        Collections.shuffle(this.deck, rand);
    }

    public void addManaToPlayer(int mana) {
        availableMana += mana;
    }

    public void pullCardFromDeck() {
        handCards.add(deck.get(0));
        deck.remove(0);
    }

    public ArrayList<Minion> getHandCards() {
        return handCards;
    }

    public int getAvailableMana() {
        return availableMana;
    }

    public void setAvailableMana(int avilableMana) {
        this.availableMana = avilableMana;
    }

    public ArrayList<Minion> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Minion> deck) {
        this.deck = deck;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }
}
