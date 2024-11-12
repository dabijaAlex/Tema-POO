package org.poo.game;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.ActionsInput;
import org.poo.fileio.GameInput;
import org.poo.fileio.Input;
import lombok.Getter;
import lombok.Setter;
import org.poo.game.commands.DebugCommands;
import org.poo.game.commands.GameplayCommands;
import org.poo.game.commands.StatsCommands;

import java.util.ArrayList;

@Setter @Getter
public final class Match {

    private Board board;
    private Player player1;
    private Player player2;
    private int playerTurn;
    private int whenNextTurn;
    private StatsCommands statsCommands;
    private DebugCommands debugCommands;
    private GameplayCommands gameplayCommands;
    private ActionsInput currentCommand;
    private final int maxManaPerRound = 10;


    private int roundMana;

    private boolean gameOver;

    /**
     *
     * @param input
     * @param output
     * @param currentGame
     */
    public Match(final Input input, final ArrayNode output,
                 final GameInput currentGame) {
        playerTurn = currentGame.getStartGame().getStartingPlayer();
        roundMana = 1;
        whenNextTurn = 0;
        statsCommands = new StatsCommands();
        debugCommands = new DebugCommands();
        gameplayCommands = new GameplayCommands();


        board = new Board();
        gameOver = false;

        player1 = new Player(1, input.getPlayerOneDecks(),
                currentGame.getStartGame().getPlayerOneDeckIdx(), currentGame);

        player2 = new Player(2, input.getPlayerTwoDecks(),
                currentGame.getStartGame().getPlayerTwoDeckIdx(), currentGame);

        this.playRound();



        ArrayList<ActionsInput> commands = currentGame.getActions();
        for (ActionsInput command : commands) {
            this.currentCommand = command;
            switch (command.getCommand()) {
                case "getPlayerDeck" -> {
                    this.debugCommands.getPlayerDeck(this, output);
                }
                case "getPlayerHero" -> {
                    this.debugCommands.getPlayerHero(this, output);
                }
                case "getPlayerTurn" -> {
                    this.debugCommands.getPlayerTurn(this.playerTurn, output);
                }
                case "endPlayerTurn" -> {
                    this.gameplayCommands.endPlayerTurn(this);
                }
                case "placeCard" -> {
                    this.gameplayCommands.placeCard(this, output);
                }
                case "getCardsInHand" -> {
                    this.debugCommands.getCardsInHand(this, output);
                }
                case "getPlayerMana" -> {
                    this.debugCommands.getPlayerMana(this, output);
                }
                case "getCardsOnTable" -> {
                    this.debugCommands.getCardsOnTable(this.board, output);
                }
                case "cardUsesAttack" -> {
                    this.gameplayCommands.cardUsesAttack(this, output,
                            command.getCardAttacker(), command.getCardAttacked(),
                            this.getPlayerTurn());
                }
                case "getCardAtPosition" -> {
                    this.debugCommands.getCardAtPosition(command, output, this.board);
                }
                case "cardUsesAbility" -> {
                    this.gameplayCommands.cardUsesAbility(this, output,
                            command.getCardAttacker(), command.getCardAttacked(),
                            this.getPlayerTurn());
                }
                case "useAttackHero" -> {
                    this.gameplayCommands.useAttackHero(this, output,
                            command.getCardAttacker(), this.getEnemyPlayer());
                }
                case "useHeroAbility" -> {
                    this.gameplayCommands.useHeroAbility(this, output,
                            this.getPlayerTurn());
                }
                case "getFrozenCardsOnTable" -> {
                    this.debugCommands.getFrozenCardsOnTable(this.board, output);
                }
                case "getPlayerOneWins" -> {
                    this.statsCommands.getPlayerOneWins(command, output);
                }
                case "getPlayerTwoWins" -> {
                    this.statsCommands.getPlayerTwoWins(command, output);
                }
                case "getTotalGamesPlayed" -> {
                    this.statsCommands.getTotalGamesPlayed(command, output);
                }
                default -> {
                    break;
                }
            }
        }
    }


    /**
     *
     */
    public void endPlayerTurn() {
        board.unfreeze(getPlayerTurn());
        this.whenNextTurn++;
        if (this.whenNextTurn == 2) {
            this.whenNextTurn = 0;
            this.playRound();
        }
        if (this.playerTurn == 1) {
            this.playerTurn = 2;
        } else {
            this.playerTurn = 1;
        }

    }

    /**
     *
     */
    public void playRound() {
        this.player1.addManaToPlayer(this.roundMana);
        this.player2.addManaToPlayer(this.roundMana);

        this.player1.pullCardFromDeck();
        this.player2.pullCardFromDeck();

        this.board.resetAttacks();
        this.board.resetAbilities();
        this.player1.getHero().setHasUsedAbility(false);
        this.player2.getHero().setHasUsedAbility(false);

        if (this.roundMana < maxManaPerRound) {
            this.roundMana++;
        }
    }

    /**
     *
     * @return
     */
    public Player getCurrentPlayer() {
        if (this.playerTurn == 1) {
            return player1;
        }
        return player2;
    }

    /**
     *
     * @return
     */
    public Player getEnemyPlayer() {
        if (this.playerTurn == 2) {
            return player1;
        }
        return player2;
    }


    /**
     *
     * @param idx
     * @return
     */
    public Player getPlayerByIdx(final int idx) {
        if (idx == 1) {
            return player1;
        } else {
            return player2;
        }
    }
}
