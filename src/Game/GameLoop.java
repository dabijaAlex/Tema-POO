package Game;
import com.fasterxml.jackson.databind.ObjectMapper;
import fileio.GameInput;
import fileio.Input;
import fileio.GameInput;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;

public class GameLoop {

    /* private int playerOneDeckIdx;
    private int playerTwoDeckIdx;
    private int shuffleSeed;
    private CardInput playerOneHero;
    private CardInput playerTwoHero;
    private int startingPlayer;

    actions
    */
    private ArrayList<GameInput> games;

    public GameLoop(Input input, ArrayNode output, ObjectMapper mapper) {
        games = input.getGames();
        for (GameInput current_game : games) {
            Match meci= new Match(input, output, current_game, mapper);
        }

    }

}
