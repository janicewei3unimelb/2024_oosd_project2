import bagel.*;

/**
 * This is the base screen class which is the parent of all screens in the game
 */
public abstract class Screen {

    /**
     * Updates the screen based on the inputs user entered
     *
     * @param input User's input
     * @return the result of whether the screen should finish and switch to the next stage
     */
    public abstract boolean update(Input input);

}
