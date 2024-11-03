package Game.Commands;
import Game.Stats;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;

public final class StatsCommands {

    public void getPlayerOneWins(final ActionsInput command, final ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", command.getCommand());
        objectNode.put("output", Stats.getPlayerOneWins());
        output.add(objectNode);
    }

    public void getPlayerTwoWins(final ActionsInput command, final ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", command.getCommand());
        objectNode.put("output", Stats.getPlayerTwoWins());
        output.add(objectNode);
    }

    public void getTotalGamesPlayed(final ActionsInput command, final ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", command.getCommand());
        objectNode.put("output", Stats.getTotalGamesPlayed());
        output.add(objectNode);
    }
}


