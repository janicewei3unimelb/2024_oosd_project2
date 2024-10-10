/**
 * Score class that stores the player's name and score.
 */
public class Score {
    private final String PLAYER_NAME;
    private final double SCORE;

    /**
     * Creates a score instance of a player
     *
     * @param playerName Player's name
     * @param score Player's score of the game
     */
    public Score(String playerName, double score) {
        this.PLAYER_NAME = playerName;
        this.SCORE = score;
    }

    /**
     * Gets the name of the player
     *
     * @return the player's name
     */
    public String getPlayerName() {
        return PLAYER_NAME;
    }

    /**
     * Gets the score of the player
     *
     * @return player's score
     */
    public double getScore() {
        return SCORE;
    }
}
