import bagel.*;

import java.util.Properties;

/**
 * Class representing the fire to be rendered when the vehicles are damaged
 */
public class Fire extends DamageEffect {
    private final Image IMAGE;
    private final int MAX_ACTIVE_FRAMES;

    /**
     * Creates the fire instance with its basic information
     * @param x x-coordinate of the fire
     * @param y y-coordinate of the fire
     * @param gameProps The Game Property stores essential details information
     */
    public Fire(int x, int y, Properties gameProps) {
        super(x, y, gameProps);
        this.IMAGE = new Image(gameProps.getProperty("gameObjects.fire.image"));
        this.MAX_ACTIVE_FRAMES = Integer.parseInt(gameProps.getProperty("gameObjects.fire.ttl"));
    }

    /**
     * Show the image of the fire at its current location
     */
    @Override
    public void draw() {
        IMAGE.draw(this.getX(), this.getY());
    }

    /**
     * Updates the fire based on its owner's location
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


}
