package org.poo.game;

import org.poo.fileio.CardInput;
import org.poo.fileio.GameInput;
import org.poo.fileio.DecksInput;
import lombok.Getter;
import lombok.Setter;
import org.poo.game.heroes.EmpressThronia;
import org.poo.game.heroes.GeneralKocioraw;
import org.poo.game.heroes.KingMudface;
import org.poo.game.heroes.LordRoyce;
import org.poo.game.minions.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

@Getter @Setter
public final class Player {
    private ArrayList<Minion> deck;
    private ArrayList<Minion> handCards;
    private Hero hero;
    private int availableMana;
    private int idx;

    /**
     *
     * @param player
     * @param decks
     * @param index
     * @param currentGame
     */
    public Player(final int player, final DecksInput decks,
                  final int index, final GameInput currentGame) {
        if (player == 1) {
            genHero(currentGame.getStartGame().getPlayerOneHero());
            this.idx = 1;
        } else {
            genHero(currentGame.getStartGame().getPlayerTwoHero());
            this.idx = 2;
        }

        deck = new ArrayList<Minion>();
        handCards = new ArrayList<Minion>();
        for (CardInput card : decks.getDecks().get(index)) {
            Minion x = genMinion(card);
            this.deck.add(x);
        }

        Random rand = new Random();
        rand.setSeed(currentGame.getStartGame().getShuffleSeed());
        Collections.shuffle(this.deck, rand);
    }

    /**
     *
     * @param mana
     */
    public void addManaToPlayer(final int mana) {
        availableMana += mana;
    }

    /**
     *
     * @param mana
     */
    public void subManaFromPlayer(final int mana) {
        availableMana -= mana;
    }

    /**
     *
     */
    public void pullCardFromDeck() {
        if (deck.isEmpty()) {
            return;
        }

        handCards.add(deck.get(0));
        deck.remove(0);
    }

    /**
     *
     * @param card
     */
    private void genHero(final CardInput card) {
        hero = new Hero(card);
        switch (card.getName()) {
            case "Lord Royce" -> {
                hero = new LordRoyce(card);
            }
            case "Empress Thorina" -> {
                hero = new EmpressThronia(card);
            }
            case "General Kocioraw" -> {
                hero = new GeneralKocioraw(card);
            }
            case "King Mudface" -> {
                hero = new KingMudface(card);
            }
            default -> {
                hero = null;
            }
        }

    }

    /**
     *
     * @param card
     * @return
     */
    private Minion genMinion(final CardInput card) {
        Minion minion;
        switch (card.getName()) {
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

    /**
     *
     * @return
     */
    public ArrayList<Minion> getHandCardsCopy() {
        ArrayList<Minion> copy = new ArrayList<>();
        for (Minion m : handCards) {
            copy.add(new Minion(m));
        }

        return copy;
    }

    /**
     *
     * @return
     */
    public Hero getHeroCopy() {
        return new Hero(this.hero);
    }

}
