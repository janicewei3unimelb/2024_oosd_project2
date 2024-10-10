import bagel.*;

import java.util.Properties;

/**
 * Class representing the Blood to be rendered when the driver or the passenger is damaged
 */
public class Blood extends DamageEffect {
    private final Image IMAGE;
    private final int MAX_ACTIVE_FRAMES;

    /**
     * Creates the blood instance with its basic information
     * @param x x-coordinate of the blood
     * @param y y-coordinate of the blood
     * @param gameProps The Game Property where stores essential details information
     */
    public Blood(int x, int y, Properties gameProps) {
        super(x, y, gameProps);
        this.IMAGE = new Image(gameProps.getProperty("gameObjects.blood.image"));
        this.MAX_ACTIVE_FRAMES = Integer.parseInt(gameProps.getProperty("gameObjects.blood.ttl"));
    }

    /**
     * Updates the blood based on its owner's location
     *
     * @param x its owner's x-coordinate
     * @param y its owner's y-coordinate
     */
    @Override
    public void update(int x, int y) {
        moveWithOwner(x, y);
        if (this.getFramesActive() > 0) {
            this.setFramesActive(this.getFramesActive() + 1);
            if (this.getFramesActive() <= MAX_ACTIVE_FRAMES) {
                draw();
            }
        }
    }

    /**
     * Show the image of the blood at its current location
     */
    @Override
    public void draw() {
        IMAGE.draw(this.getX(), this.getY());
    }

    /**
     * Gets fire's maximum number of frames to be active
     * @return fire's maximum number of frames to be active
     */
    public int getMaxActiveFrames() {
        return this.MAX_ACTIVE_FRAMES;
    }



}
