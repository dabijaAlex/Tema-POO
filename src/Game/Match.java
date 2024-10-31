package Game;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.ActionsInput;
import fileio.GameInput;
import fileio.Input;

import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Match {

    private Player player1;
    private Player player2;
    private int playerTurn;

    private int roundMana;


    public Match(Input input, ArrayNode output, GameInput current_game, ObjectMapper mapper) {
        playerTurn = current_game.getStartGame().getStartingPlayer();
        roundMana = 1;


        Hero hero = new Hero(current_game.getStartGame().getPlayerOneHero());
        player1 = new Player(hero, input.getPlayerOneDecks(), current_game.getStartGame().getPlayerOneDeckIdx(), current_game);

        hero = new Hero(current_game.getStartGame().getPlayerTwoHero());
        player2 = new Player(hero, input.getPlayerTwoDecks(), current_game.getStartGame().getPlayerTwoDeckIdx(), current_game);

        this.PlayRound(player1, player2, roundMana);

        ArrayList<ActionsInput> commands = current_game.getActions();

        for (ActionsInput command : commands) {
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("command", command.getCommand());
            switch (command.getCommand()) {



                case "getPlayerDeck" -> {
                        objectNode.put("playerIdx", command.getPlayerIdx());
                        if (command.getPlayerIdx() == 1) {
                            objectNode.putPOJO("output", player1.getDeck());
                        } else {
                            objectNode.putPOJO("output", player2.getDeck());
                        }
                }
                case "getPlayerHero" -> {
                    objectNode.put("playerIdx", command.getPlayerIdx());
                    Hero x;
                    if (command.getPlayerIdx() == 1) {
                        x = player1.getHero();
                    } else {
                        x = player2.getHero();
                    }

                    objectNode.putPOJO("output", x);

                }
                case "getPlayerTurn" -> {
                    objectNode.put("output", playerTurn);
                }
                default -> {
                    break;
                }
            }
            output.add(objectNode);
        }



    }

    private void PlayRound(Player player1, Player player2, int roundMana) {
        player1.addManaToPlayer(roundMana);
        player2.addManaToPlayer(roundMana);

        player1.pullCardFromDeck();
        player2.pullCardFromDeck();
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }


}
