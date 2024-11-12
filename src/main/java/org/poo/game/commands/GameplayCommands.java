package org.poo.game.commands;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.Coordinates;
import org.poo.game.Match;
import org.poo.game.Minion;
import org.poo.game.Player;
import org.poo.game.Stats;

public final class GameplayCommands {
    private final int p1FrontRow = 2;
    private final int p2FrontRow = 1;
    private final int p1BackRow = 3;
    private final int p2BackRow = 0;

    /**
     *
     * @param match
     */
    public void endPlayerTurn(final Match match) {
        match.getBoard().unfreeze(match.getPlayerTurn());
        match.setWhenNextTurn(match.getWhenNextTurn() + 1);
        if (match.getWhenNextTurn() == 2) {
            match.setWhenNextTurn(0);
            match.playRound();
        }
        if (match.getPlayerTurn() == 1) {
            match.setPlayerTurn(2);
        } else {
            match.setPlayerTurn(1);
        }
    }

    /**
     *
     * @param match
     * @param output
     */
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

    /**
     *
     * @param match
     * @param output
     * @param attackerCoords
     * @param attackedPlayer
     */
    public void useAttackHero(final Match match, final ArrayNode output,
                              final Coordinates attackerCoords, final Player attackedPlayer) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", "useAttackHero");
        objectNode.putPOJO("cardAttacker", attackerCoords);

        Minion attacker = match.getBoard().getMinionFromBoard(attackerCoords);

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
        attackedPlayer.getHero().setHealth(attackedPlayer.getHero().getHealth()
                - attacker.getAttackDamage());
        attacker.setHasAttacked(true);
        attacker.setHasUsedAbility(true);
        if (attackedPlayer.getHero().getHealth() <= 0) {
            match.setGameOver(true);
        }
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

    /**
     *
     * @param match
     * @param output
     * @param attackerCoords
     * @param defenderCoords
     * @param attackerIdx
     */
    public void cardUsesAttack(final Match match, final ArrayNode output,
                               final Coordinates attackerCoords, final Coordinates defenderCoords,
                               final int attackerIdx) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", "cardUsesAttack");
        objectNode.putPOJO("cardAttacker", attackerCoords);
        objectNode.putPOJO("cardAttacked", defenderCoords);

        boolean isAllyCard = (attackerIdx == 2
                && (defenderCoords.getX() == p2BackRow || defenderCoords.getX() == p2FrontRow))
                || (attackerIdx == 1
                && (defenderCoords.getX() == p1FrontRow || defenderCoords.getX() == p1BackRow));
        if (isAllyCard) {
            objectNode.put("error", "Attacked card does not belong to the enemy.");
            output.add(objectNode);
            return;
        }

        Minion tank = match.getBoard().isTankInRow(attackerIdx);

        Minion defender = match.getBoard().getMinionFromBoard(defenderCoords);
        Minion attacker = match.getBoard().getMinionFromBoard(attackerCoords);

        if (attacker.isHasAttacked()) {
            objectNode.put("error", "Attacker card has already attacked this turn.");
            output.add(objectNode);
            return;
        }
        if (defender == null) {
            return;
        }
        if (tank != null && !defender.isTank()) {
            objectNode.put("error", "Attacked card is not of type 'Tank'.");
            output.add(objectNode);
            return;
        }
        attacker.useAttack(defender, match.getBoard());
        attacker.setHasAttacked(true);
        attacker.setHasUsedAbility(true);
    }

    /**
     *
     * @param match
     * @param output
     * @param attackerCoords
     * @param defenderCoords
     * @param attackerIdx
     */
    public void cardUsesAbility(final Match match, final ArrayNode output,
                                final Coordinates attackerCoords, final Coordinates defenderCoords,
                                final int attackerIdx) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", "cardUsesAbility");
        objectNode.putPOJO("cardAttacker", attackerCoords);
        objectNode.putPOJO("cardAttacked", defenderCoords);

        Minion tank = match.getBoard().isTankInRow(attackerIdx);

        Minion defender = match.getBoard().getMinionFromBoard(defenderCoords);
        Minion attacker = match.getBoard().getMinionFromBoard(attackerCoords);

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
            boolean isEnemyCard = (match.getPlayerTurn() == 1
                    && (defenderCoords.getX() < p1FrontRow))
                    || (match.getPlayerTurn() == 2
                    && (defenderCoords.getX() >= p1FrontRow));
            if (isEnemyCard) {
                objectNode.put("error", "Attacked card does not belong to the current player.");
                output.add(objectNode);
                return;
            }
        } else {
            boolean isAllyCard = (attackerIdx == 1 && (defenderCoords.getX() == p1FrontRow
                    || defenderCoords.getX() == p1BackRow))
                    || (attackerIdx == 2 && (defenderCoords.getX() == p2BackRow
                    || defenderCoords.getX() == p2FrontRow));
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
        if (defender.getHealth() <= 0) {
            match.getBoard().removeMinionFromBoard(defender);
        }
    }

    /**
     *
     * @param match
     * @param output
     * @param attackerIdx
     */
    public void useHeroAbility(final Match match, final ArrayNode output,
                               final int attackerIdx) {
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
        boolean isRowAlly = ((attackerIdx == 1 && (row == p1FrontRow || row == p1BackRow))
                || (attackerIdx == 2 && (row == p2BackRow || row == p2FrontRow)))
                && (player.getHero().getName().equals("Lord Royce")
                || player.getHero().getName().equals("Empress Thorina"));
        if (isRowAlly) {
            objectNode.put("error", "Selected row does not belong to the enemy.");
            output.add(objectNode);
            return;
        }
        boolean isRowEnemy = ((attackerIdx == 2 && (row == p1FrontRow || row == p1BackRow))
                || (attackerIdx == 1 && (row == p2BackRow || row == p2FrontRow)))
                && ((player.getHero().getName().equals("King Mudface")
                || player.getHero().getName().equals("General Kocioraw")));
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
