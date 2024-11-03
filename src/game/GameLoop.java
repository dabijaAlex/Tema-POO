package game;
import fileio.GameInput;
import fileio.Input;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.util.ArrayList;

public final class GameLoop {

    /**
     *
     * @param input
     * @param output
     */
    public GameLoop(final Input input, final ArrayNode output) {
        Stats.setTotalGamesPlayed(0);
        Stats.setPlayerOneWins(0);
        Stats.setPlayerTwoWins(0);
        ArrayList<GameInput> games = input.getGames();
        for (GameInput current_game : games) {
            Stats.setTotalGamesPlayed(Stats.getTotalGamesPlayed() + 1);
            Match match = new Match(input, output, current_game);
        }
    }
}
