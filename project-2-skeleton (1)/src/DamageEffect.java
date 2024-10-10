import bagel.Input;

import java.util.Properties;

/**
 * Class representing the damage effects to be shown of the game entities
 */
public abstract class DamageEffect extends GameEntity {
    private int framesActive = 0;

    /**
     * create a damage effect with basic information
     *
     * @param x x-coordinate of the damage effect
     * @param y y-coordinate of the damage effect
     * @param gameProps The Game Property where stores essential details information
     */
    public DamageEffect(int x, int y, Properties gameProps) {
        super(x, y, gameProps);
    }

    /**
     * Make the damage effect update their locations to be their owner's
     * @param x x-coordinate of the owner
     * @param y y-coordinate of the owner
     */
    public void moveWithOwner(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    /**
     * Set the frames active by the updated result
     * @param updatedResult the new result that we want to set
     */
    public void setFramesActive(int updatedResult) {
        this.framesActive = updatedResult;
    }

    /**
     * gets its frames active
     * @return its frames active
     */
    public int getFramesActive() {
        return framesActive;
    }

    /**
     * update itself based on owner's location
     * @param x owner's x coordinate
     * @param y owner's y coordinate
     */
    public abstract void update(int x, int y);
}
