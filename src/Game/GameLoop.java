package Game;
import com.fasterxml.jackson.databind.ObjectMapper;
import fileio.GameInput;
import fileio.Input;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.util.ArrayList;

public class GameLoop {

    public GameLoop(Input input, ArrayNode output, ObjectMapper mapper) {
        Stats.initStats();
        ArrayList<GameInput> games = input.getGames();
        for (GameInput current_game : games) {
            Stats.totalGamesPlayed ++;
            Match match= new Match(input, output, current_game, mapper);
            System.out.println("x = " + Stats.totalGamesPlayed);
        }
    }
}
