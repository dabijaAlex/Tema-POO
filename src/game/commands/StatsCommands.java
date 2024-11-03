package game.commands;
import game.Stats;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;

public final class StatsCommands {

    /**
     *
     * @param command
     * @param output
     */
    public void getPlayerOneWins(final ActionsInput command, final ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", command.getCommand());
        objectNode.put("output", Stats.getPlayerOneWins());
        output.add(objectNode);
    }

    /**
     *
     * @param command
     * @param output
     */
    public void getPlayerTwoWins(final ActionsInput command, final ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", command.getCommand());
        objectNode.put("output", Stats.getPlayerTwoWins());
        output.add(objectNode);
    }

    /**
     * 
     * @param command
     * @param output
     */
    public void getTotalGamesPlayed(final ActionsInput command, final ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", command.getCommand());
        objectNode.put("output", Stats.getTotalGamesPlayed());
        output.add(objectNode);
    }
}


