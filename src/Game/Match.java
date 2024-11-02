package Game;

import Game.Commands.DebugCommands;
import Game.Commands.GameplayCommands;
import Game.Commands.StatsCommands;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.ActionsInput;
import fileio.Coordinates;
import fileio.GameInput;
import fileio.Input;

import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Match {

    private Board board;
    private Player player1;
    private Player player2;
    private int playerTurn;
    private int whenNextTurn;
    private StatsCommands statsCommands;
    private DebugCommands debugCommands;
    private GameplayCommands gameplayCommands;
    private ActionsInput currentCommand;


    private int roundMana;

    private boolean gameOver;


    public Match(Input input, ArrayNode output, GameInput current_game, ObjectMapper mapper) {
        playerTurn = current_game.getStartGame().getStartingPlayer();
        roundMana = 1;
        whenNextTurn = 0;
        statsCommands = new StatsCommands();
        debugCommands = new DebugCommands();
        gameplayCommands = new GameplayCommands();


        board = new Board();
        gameOver = false;

        player1 = new Player(1, input.getPlayerOneDecks(), current_game.getStartGame().getPlayerOneDeckIdx(), current_game);

        player2 = new Player(2, input.getPlayerTwoDecks(), current_game.getStartGame().getPlayerTwoDeckIdx(), current_game);

        this.PlayRound();




        ArrayList<ActionsInput> commands = current_game.getActions();

        int counter = 0;

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
                    this.gameplayCommands.cardUsesAttack(this, output, command.getCardAttacker(), command.getCardAttacked(), this.getPlayerTurn());
                }
                case "getCardAtPosition" -> {
                    this.debugCommands.getCardAtPosition(command, output, this.board);
                }
                case "cardUsesAbility" -> {
                    this.gameplayCommands.cardUsesAbility(this, output, command.getCardAttacker(), command.getCardAttacked(), this.getPlayerTurn());
                }
                case "useAttackHero" -> {
                    this.gameplayCommands.useAttackHero(this, output, command.getCardAttacker(), this.getEnemyPlayer());
                }
                case "useHeroAbility" -> {
//                    useHeroAbility(counter, getPlayerTurn(), this.board, command, mapper, output);
                    this.gameplayCommands.useHeroAbility(this, output, this.getPlayerTurn());
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

    private void useHeroAbility(int counter, int currentPlayer, Board board,
                            ActionsInput command, ObjectMapper maasdpper, ArrayNode output) {
        int row = command.getAffectedRow();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", command.getCommand());
        objectNode.put("affectedRow", command.getAffectedRow());
        if(getCurrentPlayer().getHero().getMana() > getCurrentPlayer().getAvailableMana()) {
            objectNode.put("error", "Not enough mana to use hero's ability.");
            output.add(objectNode);
            return;
        }
        if(getCurrentPlayer().getHero().isHasUsedAbility()) {
            objectNode.put("error", "Hero has already attacked this turn.");
            output.add(objectNode);
            return;
        }
        boolean isRowAlly = ((currentPlayer == 1 && (row == 2 || row == 3)) ||
                (currentPlayer == 2 && (row == 0 || row == 1))) &&
                (getCurrentPlayer().getHero().getName().equals("Lord Royce") ||
                        getCurrentPlayer().getHero().getName().equals("Empress Thorina"));
        if(isRowAlly) {
            objectNode.put("error", "Selected row does not belong to the enemy.");
            output.add(objectNode);
            return;
        }
        boolean isRowEnemy = ((currentPlayer == 2 && (row == 2 || row == 3)) ||
                (currentPlayer == 1 && (row == 0 || row == 1))) &&
                ((getCurrentPlayer().getHero().getName().equals("King Mudface") ||
                getCurrentPlayer().getHero().getName().equals("General Kocioraw")));
        if(isRowEnemy) {
            objectNode.put("error", "Selected row does not belong to the current player.");
            output.add(objectNode);
            return;
        }
        getCurrentPlayer().getHero().useAbility(board, row);
        getCurrentPlayer().setAvailableMana(getCurrentPlayer().getAvailableMana() - getCurrentPlayer().getHero().getMana());
        getCurrentPlayer().getHero().setHasUsedAbility(true);

    }
//    Reset has attacked on round end

    public void endPlayerTurn() {
        board.unfreeze(getPlayerTurn());
        this.whenNextTurn++;
        if (this.whenNextTurn == 2) {
            this.whenNextTurn = 0;
            this.PlayRound();
        }
        if(this.playerTurn == 1) {
            this.playerTurn = 2;
        } else {
            this.playerTurn = 1;
        }

    }

    public void PlayRound() {
        this.player1.addManaToPlayer(this.roundMana);
        this.player2.addManaToPlayer(this.roundMana);

        this.player1.pullCardFromDeck();
        this.player2.pullCardFromDeck();

        this.board.resetAttacks();
        this.board.resetAbilities();
        this.player1.getHero().setHasUsedAbility(false);
        this.player2.getHero().setHasUsedAbility(false);

        if(this.roundMana < 10) {
            this.roundMana++;
        }
    }

    public Player getCurrentPlayer() {
        if (this.playerTurn == 1) {
            return player1;
        }
        return player2;
    }

    public Player getEnemyPlayer() {
        if (this.playerTurn == 2) {
            return player1;
        }
        return player2;
    }

    public ActionsInput getCurrentCommand() {
        return currentCommand;
    }

    public Player getPlayerByIdx(int idx) {
        if(idx == 1)
            return player1;
        else
            return player2;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setCurrentCommand(ActionsInput currentCommand) {
        this.currentCommand = currentCommand;
    }

    public DebugCommands getDebugCommands() {
        return debugCommands;
    }

    public void setDebugCommands(DebugCommands debugCommands) {
        this.debugCommands = debugCommands;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public int getRoundMana() {
        return roundMana;
    }

    public void setRoundMana(int roundMana) {
        this.roundMana = roundMana;
    }

    public StatsCommands getStatsCommands() {
        return statsCommands;
    }

    public void setStatsCommands(StatsCommands statsCommands) {
        this.statsCommands = statsCommands;
    }

    public int getWhenNextTurn() {
        return whenNextTurn;
    }

    public void setWhenNextTurn(int whenNextTurn) {
        this.whenNextTurn = whenNextTurn;
    }
}
