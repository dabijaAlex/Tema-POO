package Game.Commands;
import Game.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Coordinates;

public final class GameplayCommands {
    public void endPlayerTurn(final Match match) {
        match.getBoard().unfreeze(match.getPlayerTurn());
        match.setWhenNextTurn(match.getWhenNextTurn() + 1);
        if (match.getWhenNextTurn() == 2) {
            match.setWhenNextTurn(0);
            match.PlayRound();
        }
        if (match.getPlayerTurn() == 1) {
            match.setPlayerTurn(2);
        } else {
            match.setPlayerTurn(1);
        }
    }

    public void placeCard(final Match match, final ArrayNode output) {
        Minion minion;
        Player player = match.getPlayerByIdx(match.getPlayerTurn());
        int handIdx = match.getCurrentCommand().getHandIdx();
        minion = player.getHandCards().get(handIdx);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", "placeCard");
        objectNode.put("handIdx", handIdx);
        if (minion.getMana() > player.getAvailableMana()) {
            objectNode.put("error", "Not enough mana to place card on table.");
            output.add(objectNode);
            return;
        }
        boolean placed = minion.placeCard(match.getBoard(), match.getPlayerTurn());
        if (!placed) {
            objectNode.put("error", "Cannot place card on table since row is full.");
            output.add(objectNode);
            return;
        }
        player.getHandCards().remove(handIdx);
        player.subManaFromPlayer(minion.getMana());
    }

    public void useAttackHero(final Match match, final ArrayNode output, final Coordinates attacker_coords, final Player attackedPlayer) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", "useAttackHero");
        objectNode.putPOJO("cardAttacker", attacker_coords);

        Minion attacker = match.getBoard().getMinionFromBoard(attacker_coords);

        if (attacker.isFrozen()) {
            objectNode.put("error", "Attacker card is frozen.");
            output.add(objectNode);
            return;
        }
        if (attacker.isHasAttacked()) {
            objectNode.put("error", "Attacker card has already attacked this turn.");
            output.add(objectNode);
            return;
        }
        Minion tank = match.getBoard().isTankInRow(match.getPlayerTurn());
        if (tank != null) {
            objectNode.put("error", "Attacked card is not of type 'Tank'.");
            output.add(objectNode);
            return;
        }
        attackedPlayer.getHero().setHealth(attackedPlayer.getHero().getHealth() - attacker.getAttackDamage());
        attacker.setHasAttacked(true);
        attacker.setHasUsedAbility(true);
        if (attackedPlayer.getHero().getHealth() <= 0)
            match.setGameOver(true);

        if (match.isGameOver()) {
            objectNode = mapper.createObjectNode();
            if (match.getPlayerTurn() == 1) {
                objectNode.put("gameEnded", "Player one killed the enemy hero.");
                Stats.playerOneWon();
            } else {
                objectNode.put("gameEnded", "Player two killed the enemy hero.");
                Stats.playerTwoWon();
            }
            output.add(objectNode);
        }
    }

    public void cardUsesAttack(final Match match, final ArrayNode output, final Coordinates attacker_coords, final Coordinates defender_coords, final int attacker_idx) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", "cardUsesAttack");
        objectNode.putPOJO("cardAttacker", attacker_coords);
        objectNode.putPOJO("cardAttacked", defender_coords);

        boolean isAllyCard = (attacker_idx == 2 && (defender_coords.getX() == 0 || defender_coords.getX() == 1)) || (attacker_idx == 1 && (defender_coords.getX() == 2 || defender_coords.getX() == 3));
        if (isAllyCard) {
            objectNode.put("error", "Attacked card does not belong to the enemy.");
            output.add(objectNode);
            return;
        }

        Minion tank = match.getBoard().isTankInRow(attacker_idx);

        Minion defender = match.getBoard().getMinionFromBoard(defender_coords);
        Minion attacker = match.getBoard().getMinionFromBoard(attacker_coords);

        if (attacker.isHasAttacked()) {
            objectNode.put("error", "Attacker card has already attacked this turn.");
            output.add(objectNode);
            return;
        }
        if (defender == null)
            return;
        if (tank != null && !defender.isTank()) {
            objectNode.put("error", "Attacked card is not of type 'Tank'.");
            output.add(objectNode);
            return;
        }
        attacker.useAttack(defender, match.getBoard());
        attacker.setHasAttacked(true);
        attacker.setHasUsedAbility(true);
    }

    public void cardUsesAbility(final Match match, final ArrayNode output, final Coordinates attacker_coords, final Coordinates defender_coords, final int attacker_idx) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", "cardUsesAbility");
        objectNode.putPOJO("cardAttacker", attacker_coords);
        objectNode.putPOJO("cardAttacked", defender_coords);

        Minion tank = match.getBoard().isTankInRow(attacker_idx);

        Minion defender = match.getBoard().getMinionFromBoard(defender_coords);
        Minion attacker = match.getBoard().getMinionFromBoard(attacker_coords);

        if (attacker.isFrozen()) {
            objectNode.put("error", "Attacker card is frozen.");
            output.add(objectNode);
            return;
        }

        if (attacker.isHasAttacked()) {
            objectNode.put("error", "Attacker card has already attacked this turn.");
            output.add(objectNode);
            return;
        }
        if (attacker.getName().equals("Disciple")) {
            boolean isEnemyCard = (match.getPlayerTurn() == 1 && (defender_coords.getX() < 2)) || (match.getPlayerTurn() == 2 && (defender_coords.getX() >= 2));
            if (isEnemyCard) {
                objectNode.put("error", "Attacked card does not belong to the current player.");
                output.add(objectNode);
                return;
            }
        } else {
            boolean isAllyCard = (attacker_idx == 1 && (defender_coords.getX() == 2 || defender_coords.getX() == 3)) || (attacker_idx == 2 && (defender_coords.getX() == 0 || defender_coords.getX() == 1));
            if (isAllyCard) {
                objectNode.put("error", "Attacked card does not belong to the enemy.");
                output.add(objectNode);
                return;
            }

            if (tank != null && !defender.isTank()) {
                objectNode.put("error", "Attacked card is not of type 'Tank'.");
                output.add(objectNode);
                return;
            }
        }
        attacker.useAbility(defender);
        attacker.setHasAttacked(true);
        attacker.setHasUsedAbility(true);
        if (defender.getHealth() <= 0)
            match.getBoard().removeMinionFromBoard(defender);
    }

    public void useHeroAbility(final Match match, ArrayNode output,
                               final int attacker_idx) {
        int row = match.getCurrentCommand().getAffectedRow();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", "useHeroAbility");
        objectNode.put("affectedRow", row);
        Player player = match.getCurrentPlayer();
        if (player.getHero().getMana() > player.getAvailableMana()) {
            objectNode.put("error", "Not enough mana to use hero's ability.");
            output.add(objectNode);
            return;
        }
        if (player.getHero().isHasUsedAbility()) {
            objectNode.put("error", "Hero has already attacked this turn.");
            output.add(objectNode);
            return;
        }
        boolean isRowAlly = ((attacker_idx == 1 && (row == 2 || row == 3))
                || (attacker_idx == 2 && (row == 0 || row == 1)))
                && (player.getHero().getName().equals("Lord Royce")
                || player.getHero().getName().equals("Empress Thorina"));
        if (isRowAlly) {
            objectNode.put("error", "Selected row does not belong to the enemy.");
            output.add(objectNode);
            return;
        }
        boolean isRowEnemy = ((attacker_idx == 2 && (row == 2 || row == 3)) ||
                (attacker_idx == 1 && (row == 0 || row == 1))) &&
                ((player.getHero().getName().equals("King Mudface") ||
                        player.getHero().getName().equals("General Kocioraw")));
        if (isRowEnemy) {
            objectNode.put("error", "Selected row does not belong to the current player.");
            output.add(objectNode);
            return;
        }
        player.getHero().useAbility(match.getBoard(), row);
        player.setAvailableMana(player.getAvailableMana() - player.getHero().getMana());
        player.getHero().setHasUsedAbility(true);
    }
}