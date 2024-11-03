package game.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.Coordinates;
import game.Match;
import game.Player;
import game.Board;
import game.Minion;

public final class DebugCommands {

    /**
     *
     * @param match
     * @param output
     */
    public void getCardsInHand(final Match match, final ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        Player player = match.getPlayerByIdx(match.getCurrentCommand().getPlayerIdx());
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", "getCardsInHand");
        objectNode.put("playerIdx", player.getIdx());
        objectNode.putPOJO("output", player.getHandCardsCopy());
        output.add(objectNode);
    }

    /**
     *
     * @param match
     * @param output
     */
    public void getPlayerDeck(final Match match, final ArrayNode output) {
        Player player = match.getPlayerByIdx(match.getCurrentCommand().getPlayerIdx());
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", "getPlayerDeck");
        objectNode.put("playerIdx", player.getIdx());
        objectNode.putPOJO("output", player.getDeck());
        output.add(objectNode);
    }

    /**
     *
     * @param match
     * @param output
     */
    public void getPlayerHero(final Match match, final ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        Player player = match.getPlayerByIdx(match.getCurrentCommand().getPlayerIdx());
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", "getPlayerHero");
        objectNode.put("playerIdx", player.getIdx());
        objectNode.putPOJO("output", player.getHeroCopy());
        output.add(objectNode);
    }

    /**
     *
     * @param board
     * @param output
     */
    public void getCardsOnTable(final Board board, final ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", "getCardsOnTable");
        objectNode.putPOJO("output", board.getCardsOnTableCopy());
        output.add(objectNode);
    }

    /**
     *
     * @param playerTurn
     * @param output
     */
    public void getPlayerTurn(final int playerTurn, final ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", "getPlayerTurn");
        objectNode.put("output", playerTurn);
        output.add(objectNode);
    }

    /**
     *
     * @param command
     * @param output
     * @param board
     */
    public void getCardAtPosition(final ActionsInput command, final ArrayNode output,
                                  final Board board) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", command.getCommand());
        objectNode.put("x", command.getX());
        objectNode.put("y", command.getY());
        Coordinates coordinates = new Coordinates();
        coordinates.setX(command.getX());
        coordinates.setY(command.getY());
        Minion minion = board.getMinionFromBoard(coordinates);
        if (minion == null) {
            objectNode.put("output", "No card available at that position.");
        } else {
            objectNode.putPOJO("output", new Minion(minion));
        }
        output.add(objectNode);
    }

    /**
     *
     * @param match
     * @param output
     */
    public void getPlayerMana(final Match match, final ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        Player player = match.getPlayerByIdx(match.getCurrentCommand().getPlayerIdx());
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", "getPlayerMana");
        objectNode.put("playerIdx", player.getIdx());
        objectNode.put("output", player.getAvailableMana());
        output.add(objectNode);
    }

    /**
     *
     * @param board
     * @param output
     */
    public void getFrozenCardsOnTable(final Board board, final ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", "getFrozenCardsOnTable");
        objectNode.putPOJO("output", board.getFrozenMinions());
        output.add(objectNode);
    }
}
