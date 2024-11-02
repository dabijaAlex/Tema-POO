package Game;

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

    private int roundMana;
    private int roundNumber;

    private boolean gameOver;


    public Match(Input input, ArrayNode output, GameInput current_game, ObjectMapper mapper) {
        playerTurn = current_game.getStartGame().getStartingPlayer();
        roundMana = 1;
        whenNextTurn = 0;
        roundNumber = 0;
        board = new Board();
        gameOver = false;

//        Hero hero = new Hero(current_game.getStartGame().getPlayerOneHero());
        player1 = new Player(1, input.getPlayerOneDecks(), current_game.getStartGame().getPlayerOneDeckIdx(), current_game);

//        hero = new Hero(current_game.getStartGame().getPlayerTwoHero());
        player2 = new Player(2, input.getPlayerTwoDecks(), current_game.getStartGame().getPlayerTwoDeckIdx(), current_game);

        this.PlayRound();


        ArrayList<ActionsInput> commands = current_game.getActions();

        int counter = 0;

        for (ActionsInput command : commands) {
            counter++;
//            if(gameOver)
//                break;
            switch (command.getCommand()) {



                case "getPlayerDeck" -> {
                    ObjectNode objectNode = mapper.createObjectNode();
                    objectNode.put("command", command.getCommand());
                    objectNode.put("playerIdx", command.getPlayerIdx());
                    if (command.getPlayerIdx() == 1) {

                        objectNode.putPOJO("output", player1.getDeck());
                    } else {
                        objectNode.putPOJO("output", player2.getDeck());
                    }
                    output.add(objectNode);
                }
                case "getPlayerHero" -> {
                    ObjectNode objectNode = mapper.createObjectNode();
                    objectNode.put("command", command.getCommand());
                    objectNode.put("playerIdx", command.getPlayerIdx());
                    Hero x;
                    if (command.getPlayerIdx() == 1) {
                        x = player1.getHero();
                    } else {
                        x = player2.getHero();
                    }

                    objectNode.putPOJO("output", new Hero(x));
                    output.add(objectNode);

                }
                case "getPlayerTurn" -> {
                    ObjectNode objectNode = mapper.createObjectNode();
                    objectNode.put("command", command.getCommand());
                    objectNode.put("output", playerTurn);
                    output.add(objectNode);
                }
                case "endPlayerTurn" -> {
                    this.endPlayerTurn();
                }
                case "placeCard" -> {
                    Minion minion;
                    Player current_player = getCurrentPlayer();
                    minion = current_player.getHandCards().get(command.getHandIdx());
                    if(minion == null) {
                        return;
                    }
                    if(minion.getMana() > current_player.getAvailableMana()) {
                        ObjectNode objectNode = mapper.createObjectNode();
                        objectNode.put("command", command.getCommand());
                        objectNode.put("handIdx", command.getHandIdx());
                        objectNode.put("error", "Not enough mana to place card on table.");
                        output.add(objectNode);
                        break;
                    }

                    boolean placed = minion.placeCard(board, getPlayerTurn());

                    if(!placed) {
                        ObjectNode objectNode = mapper.createObjectNode();
                        objectNode.put("command", command.getCommand());
                        objectNode.put("handIdx", command.getHandIdx());
                        objectNode.put("error", "Cannot place card on table since row is full.");
                        output.add(objectNode);
                    } else {
                        current_player.getHandCards().remove(command.getHandIdx());
                        current_player.subManaFromPlayer(minion.getMana());
                    }

                }
                case "getCardsInHand" -> {
                    Player player;
                    if (command.getPlayerIdx() == 1) {
                        player = player1;
                    } else {
                        player = player2;
                    }
//                    System.out.println("player idx = " + command.getPlayerIdx());

                    ObjectNode objectNode = mapper.createObjectNode();
                    objectNode.put("command", command.getCommand());
                    objectNode.put("playerIdx", command.getPlayerIdx());
                    objectNode.putPOJO("output", player.getHandCardsCopy());
                    output.add(objectNode);

                }
                case "getPlayerMana" -> {
                    ObjectNode objectNode = mapper.createObjectNode();
                    objectNode.put("command", command.getCommand());
                    objectNode.put("playerIdx", command.getPlayerIdx());
                    if (command.getPlayerIdx() == 1) {
                        objectNode.put("output", player1.getAvailableMana());
                    } else {
                        objectNode.put("output", player2.getAvailableMana());
                    }
                    output.add(objectNode);
                }
                case "getCardsOnTable" -> {
                    ObjectNode objectNode = mapper.createObjectNode();
                    objectNode.put("command", command.getCommand());
                    objectNode.putPOJO("output", board.getCardsOnTable_copy());
                    output.add(objectNode);
                }
                case "cardUsesAttack" -> {
                    attackCard(counter, command.getCardAttacker(), command.getCardAttacked(), getPlayerTurn(), this.board, command, mapper, output);
                }
                case "getCardAtPosition" -> {
                    ObjectNode objectNode = mapper.createObjectNode();
                    objectNode.put("command", command.getCommand());
                    objectNode.put("x", command.getX());
                    objectNode.put("y", command.getY());
                    Coordinates coordinates = new Coordinates();
                    coordinates.setX(command.getX());
                    coordinates.setY(command.getY());
                    Minion minion = board.getMinionFromBoard(coordinates);
                    if (minion == null)
                        objectNode.put("output", "No card available at that position.");
                    else {
                        objectNode.putPOJO("output", new Minion(minion));
                    }
                    output.add(objectNode);
                }
                case "cardUsesAbility" -> {
                    useAbility(counter, command.getCardAttacker(), command.getCardAttacked(), getPlayerTurn(), this.board, command, mapper, output);
                }
                case "useAttackHero" -> {

                    gameOver = useAttackHero(getEnemyPlayer().getHero(), command.getCardAttacker(), board, command, mapper, output, getPlayerTurn());
                    if(gameOver) {
                        ObjectNode objectNode = mapper.createObjectNode();
                        if(getPlayerTurn() == 1) {
                            objectNode.put("gameEnded", "Player one killed the enemy hero.");
                            Stats.playerOneWins++;
                        } else {
                            objectNode.put("gameEnded", "Player two killed the enemy hero.");
                            Stats.playerTwoWins++;
                        }
                        output.add(objectNode);
                    }
                }
                case "useHeroAbility" -> {
                    useHeroAbility(counter, getPlayerTurn(), this.board, command, mapper, output);
                }
                case "getFrozenCardsOnTable" -> {
                    ObjectNode objectNode = mapper.createObjectNode();
                    objectNode.put("command", command.getCommand());
                    objectNode.putPOJO("output", board.getFrozenMinions());
                    output.add(objectNode);
                }
                case "getPlayerOneWins" -> {
                    ObjectNode objectNode = mapper.createObjectNode();
                    objectNode.put("command", command.getCommand());
                    objectNode.put("output", Stats.playerOneWins);
                    output.add(objectNode);
                }
                case "getPlayerTwoWins" -> {
                    ObjectNode objectNode = mapper.createObjectNode();
                    objectNode.put("command", command.getCommand());
                    objectNode.put("output", Stats.playerTwoWins);
                    output.add(objectNode);
                }
                case "getTotalGamesPlayed" -> {
                    ObjectNode objectNode = mapper.createObjectNode();
                    objectNode.put("command", command.getCommand());
                    objectNode.put("output", Stats.totalGamesPlayed);
                    output.add(objectNode);
                }
                default -> {
                    break;
                }
            }
        }


    }

    private void useHeroAbility(int counter, int currentPlayer, Board board,
                            ActionsInput command, ObjectMapper mapper, ArrayNode output) {
        int row = command.getAffectedRow();
        if(getCurrentPlayer().getHero().getMana() > getCurrentPlayer().getAvailableMana()) {
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("command", command.getCommand());
            objectNode.put("affectedRow", command.getAffectedRow());
            objectNode.put("error", "Not enough mana to use hero's ability.");
            output.add(objectNode);
            return;
        }

        if(getCurrentPlayer().getHero().isHasUsedAbility()) {
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("command", command.getCommand());
            objectNode.put("affectedRow", command.getAffectedRow());
            objectNode.put("error", "Hero has already attacked this turn.");
            output.add(objectNode);
            return;
        }

        if(currentPlayer == 1 && (row == 2 || row == 3) &&
                (getCurrentPlayer().getHero().getName().equals("Lord Royce") ||
                        getCurrentPlayer().getHero().getName().equals("Empress Thorina"))) {
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("command", command.getCommand());
            objectNode.put("affectedRow", command.getAffectedRow());
            objectNode.put("error", "Selected row does not belong to the enemy.");
            output.add(objectNode);
            return;
        }
        if(currentPlayer == 2 && (row == 0 || row == 1) &&
                (getCurrentPlayer().getHero().getName().equals("Lord Royce") ||
                        getCurrentPlayer().getHero().getName().equals("Empress Thorina"))) {
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("command", command.getCommand());
            objectNode.put("affectedRow", command.getAffectedRow());
            objectNode.put("error", "Selected row does not belong to the enemy.");
            output.add(objectNode);
            return;
        }

        if(currentPlayer == 2 && (row == 2 || row == 3) &&
                (getCurrentPlayer().getHero().getName().equals("King Mudface") ||
                        getCurrentPlayer().getHero().getName().equals("General Kocioraw"))) {
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("command", command.getCommand());
            objectNode.put("affectedRow", command.getAffectedRow());
            objectNode.put("error", "Selected row does not belong to the current player.");
            output.add(objectNode);
            return;
        }
        if(currentPlayer == 1 && (row == 0 || row == 1) &&
                (getCurrentPlayer().getHero().getName().equals("King Mudface") ||
                        getCurrentPlayer().getHero().getName().equals("General Kocioraw"))) {
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("command", command.getCommand());
            objectNode.put("affectedRow", command.getAffectedRow());
            objectNode.put("error", "Selected row does not belong to the current player.");
            output.add(objectNode);
            return;
        }
        getCurrentPlayer().getHero().useAbility(board, row);
        getCurrentPlayer().setAvailableMana(getCurrentPlayer().getAvailableMana() - getCurrentPlayer().getHero().getMana());
        getCurrentPlayer().getHero().setHasUsedAbility(true);

    }

    private boolean useAttackHero(Hero enemy_hero, Coordinates attacker_coords, Board board,
                               ActionsInput command, ObjectMapper mapper, ArrayNode output, int attackedPlayer){
        Minion tank = board.isTankInRow(attackedPlayer);

        Minion attacker = board.getMinionFromBoard(attacker_coords);

        if(attacker.isFrozen()) {
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("command", command.getCommand());
            objectNode.putPOJO("cardAttacker", attacker_coords);
            objectNode.put("error", "Attacker card is frozen.");
            output.add(objectNode);
            return false;
        }

        if(attacker.isHasAttacked()) {
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("command", command.getCommand());
            objectNode.putPOJO("cardAttacker", attacker_coords);
            objectNode.put("error", "Attacker card has already attacked this turn.");
            output.add(objectNode);
            return false;
        }
        if(tank != null){
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("command", command.getCommand());
            objectNode.putPOJO("cardAttacker", attacker_coords);
            objectNode.put("error", "Attacked card is not of type 'Tank'.");
            output.add(objectNode);
            return false;
        }
        enemy_hero.setHealth(enemy_hero.getHealth() - attacker.getAttackDamage());
        attacker.setHasAttacked(true);
        attacker.setHasUsedAbility(true);
        if(enemy_hero.getHealth() <= 0)
            return true;
        return false;

    }

    private void useAbility(int counter, Coordinates attacker_coords, Coordinates defender_coords, int attackedPlayer, Board board,
                            ActionsInput command, ObjectMapper mapper, ArrayNode output) {
        Minion tank = board.isTankInRow(attackedPlayer);

        Minion defender = board.getMinionFromBoard(defender_coords);
        Minion attacker = board.getMinionFromBoard(attacker_coords);

        if(attacker == null)
            return;

        if(attacker.isFrozen()) {
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("command", command.getCommand());
            objectNode.putPOJO("cardAttacker", attacker_coords);
            objectNode.putPOJO("cardAttacked", defender_coords);
            objectNode.put("error", "Attacker card is frozen.");
            output.add(objectNode);
            return;
        }

        if(attacker.isHasAttacked()) {
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("command", command.getCommand());
            objectNode.putPOJO("cardAttacker", attacker_coords);
            objectNode.putPOJO("cardAttacked", defender_coords);
            objectNode.put("error", "Attacker card has already attacked this turn.");
            output.add(objectNode);
            return;
        }
        if(attacker.getName().equals("Disciple")) {
            if(this.getPlayerTurn() == 1 && (defender_coords.getX() < 2)){
                ObjectNode objectNode = mapper.createObjectNode();
                objectNode.put("command", command.getCommand());
                objectNode.putPOJO("cardAttacker", attacker_coords);
                objectNode.putPOJO("cardAttacked", defender_coords);
                objectNode.put("error", "Attacked card does not belong to the current player.");
                output.add(objectNode);
                return;
            }
            if(this.getPlayerTurn() == 2 && (defender_coords.getX() >= 2)){
                ObjectNode objectNode = mapper.createObjectNode();
                objectNode.put("command", command.getCommand());
                objectNode.putPOJO("cardAttacker", attacker_coords);
                objectNode.putPOJO("cardAttacked", defender_coords);
                objectNode.put("error", "Attacked card does not belong to the current player.");
                output.add(objectNode);
                return;
            }
            attacker.useAbility(defender);
            attacker.setHasAttacked(true);
            attacker.setHasUsedAbility(true);
            return;
        }
        if(attackedPlayer == 1 && (defender_coords.getX() == 2 || defender_coords.getX() == 3)) {
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("command", command.getCommand());
            objectNode.putPOJO("cardAttacker", attacker_coords);
            objectNode.putPOJO("cardAttacked", defender_coords);
            objectNode.put("error", "Attacked card does not belong to the enemy.");
            output.add(objectNode);

            return;
        }
        if(attackedPlayer == 2 && (defender_coords.getX() == 0 || defender_coords.getX() == 1)) {
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("command", command.getCommand());
            objectNode.putPOJO("cardAttacker", attacker_coords);
            objectNode.putPOJO("cardAttacked", defender_coords);
            objectNode.put("error", "Attacked card does not belong to the enemy.");
            output.add(objectNode);
            return;
        }


        if(attacker.isFrozen()) {
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("command", command.getCommand());
            objectNode.putPOJO("cardAttacker", attacker_coords);
            objectNode.putPOJO("cardAttacked", defender_coords);
            objectNode.put("error", "Attacker card is frozen.");
            output.add(objectNode);
            return;
        }

        if(defender == null)
            return;
        if(tank != null && tank != defender && defender.isTank() == false) {
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("command", command.getCommand());
            objectNode.putPOJO("cardAttacker", attacker_coords);
            objectNode.putPOJO("cardAttacked", defender_coords);
            objectNode.put("error", "Attacked card is not of type 'Tank'.");
            output.add(objectNode);
            System.out.println(counter + " not a tank");
            return;
        }

        attacker.useAbility(defender);
        if(defender.getHealth() == 0) {
            board.removeMinionFromBoard(defender);
        }
        attacker.setHasAttacked(true);
        attacker.setHasUsedAbility(true);

    }

    private void attackCard(int counter, Coordinates attacker_coords, Coordinates defender_coords, int attackedPlayer, Board board,
                            ActionsInput command, ObjectMapper mapper, ArrayNode output) {
        if(attackedPlayer == 1 && (defender_coords.getX() == 2 || defender_coords.getX() == 3)) {
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("command", command.getCommand());
            objectNode.putPOJO("cardAttacker", attacker_coords);
            objectNode.putPOJO("cardAttacked", defender_coords);
            objectNode.put("error", "Attacked card does not belong to the enemy.");
            output.add(objectNode);
            System.out.println(counter+ " not enemy");

            return;
        }
        if(attackedPlayer == 2 && (defender_coords.getX() == 0 || defender_coords.getX() == 1)) {
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("command", command.getCommand());
            objectNode.putPOJO("cardAttacker", attacker_coords);
            objectNode.putPOJO("cardAttacked", defender_coords);
            objectNode.put("error", "Attacked card does not belong to the enemy.");
            output.add(objectNode);
            System.out.println(counter + " not enemy");
            return;
        }

        Minion tank = board.isTankInRow(attackedPlayer);

        Minion defender = board.getMinionFromBoard(defender_coords);
        Minion attacker = board.getMinionFromBoard(attacker_coords);

        if(attacker == null)
            return;

        if(attacker.isHasAttacked() == true) {
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("command", command.getCommand());
            objectNode.putPOJO("cardAttacker", attacker_coords);
            objectNode.putPOJO("cardAttacked", defender_coords);
            objectNode.put("error", "Attacker card has already attacked this turn.");
            output.add(objectNode);
            System.out.println(counter + " already attacked this turn");
            return;
        }
        if(defender == null)
            return;
        if(tank != null && tank != defender && defender.isTank() == false) {
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("command", command.getCommand());
            objectNode.putPOJO("cardAttacker", attacker_coords);
            objectNode.putPOJO("cardAttacked", defender_coords);
            objectNode.put("error", "Attacked card is not of type 'Tank'.");
            output.add(objectNode);
            System.out.println(counter + " not a tank");
            return;
        }

        attacker.useAttack(defender, board);
        attacker.setHasAttacked(true);
        attacker.setHasUsedAbility(true);


    }
//    Reset has attacked on round end

    private void endPlayerTurn() {
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

    private void PlayRound() {
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
        roundNumber++;
    }

    private Player getCurrentPlayer() {
        if (this.playerTurn == 1) {
            return player1;
        }
        return player2;
    }

    private Player getEnemyPlayer() {
        if (this.playerTurn == 2) {
            return player1;
        }
        return player2;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }


}
