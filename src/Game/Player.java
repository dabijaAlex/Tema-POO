package Game;

import Game.Heroes.EmpressThronia;
import Game.Heroes.GeneralKocioraw;
import Game.Heroes.KingMudface;
import Game.Heroes.LordRoyce;
import Game.Minions.*;
import fileio.CardInput;
import fileio.Input;
import fileio.GameInput;
import fileio.DecksInput;

//import Tank;

import java.util.ArrayList;

import java.util.Random;
import java.util.Collections;

public class Player {
    private ArrayList<Minion> deck;
    private ArrayList<Minion> handCards;
    private Hero hero;
    private int availableMana;

    public Player(int player, DecksInput decks, int index, GameInput currentGame) {
        if(player == 1) {
            this.hero = genHero(currentGame.getStartGame().getPlayerOneHero());
        } else {
            this.hero = genHero(currentGame.getStartGame().getPlayerTwoHero());
        }
        if(this.hero == null){
            System.out.println("sa mi bag");
        }
            deck = new ArrayList<Minion>();
        handCards = new ArrayList<Minion>();
        for(CardInput card : decks.getDecks().get(index)) {
//            Minion x = new Minion(card);
            Minion x = genMinion(card);
            this.deck.add(x);
        }
        Random rand = new Random();
        rand.setSeed(currentGame.getStartGame().getShuffleSeed());
        Collections.shuffle(this.deck, rand);
    }

    public void addManaToPlayer(int mana) {
        availableMana += mana;
    }

    public void subManaFromPlayer(int mana) {
        availableMana -= mana;
    }

    public void pullCardFromDeck() {
        if(deck.isEmpty()) {
            return;
        }
        handCards.add(deck.get(0));
        deck.remove(0);
    }

    private Hero genHero(CardInput card) {
        Hero hero = null;
        switch (card.getName()) {
            case "Lord Royce" -> hero = new LordRoyce(card);
            case "Empress Thorina" -> hero = new EmpressThronia(card);
            case "General Kocioraw" -> hero = new GeneralKocioraw(card);
            case "King Mudface" -> hero = new KingMudface(card);
        }
        return hero;
    }

    private Minion genMinion(CardInput card) {
        Minion minion;
        switch(card.getName()) {
            case "Goliath", "Warden" -> {
                minion = new Tank(card);
            }
            case "The Ripper" -> {
                minion = new Ripper(card);
            }
            case "Miraj" -> {
                minion = new Miraj(card);
            }
            case "The Cursed One" -> {
                minion = new Cursed(card);
            }
            case "Disciple" -> {
                minion = new Disciple(card);
            }
            case "Sentinel", "Berserker" -> {
                minion = new Minion(card);
            }
            default -> {
                minion = null;
            }
        }
        return minion;
    }












    public ArrayList<Minion> getHandCardsCopy() {
        ArrayList<Minion> copy = new ArrayList<>();
        for(Minion m : handCards) {
            copy.add(new Minion(m));
        }
        return copy;
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
