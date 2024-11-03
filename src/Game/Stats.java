package Game;

import lombok.Getter;
import lombok.Setter;

/**
 * Clasa Stats tine minte statisticile din joc
 * Inregistreaza nr de winuri per jucator si cate meciuri s au jucat
 */
public final class Stats {
    @Getter @Setter
    private static int playerOneWins;
    @Getter @Setter
    private static int playerTwoWins;
    @Getter @Setter
    private static int totalGamesPlayed;

    private Stats() {

    }

    /**
     * cand castiga p1 se incrementeaza direct campul
      */
    public static void playerOneWon() {
        playerOneWins++;
    }

    /**
     * cand castiga p2 se incrementeaza direct campul
     */
    public static void playerTwoWon() {
        playerTwoWins++;
    }

}
