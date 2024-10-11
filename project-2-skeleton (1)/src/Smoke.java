import bagel.*;

import java.util.Properties;

/**
 * The class representing the smoke that should be rendered 20 frames when the vehicles are damaged
 */
public class Smoke extends DamageEffect {
    private final Image IMAGE;
    private final int MAX_ACTIVE_FRAMES;

    /**
     *
     * @param x The x-coordinate of the smoke
     * @param y The y-coordinate of the smoke
     * @param gameProps The Game Property where stores essential information
     */
    public Smoke(int x, int y, Properties gameProps) {
        super(x, y, gameProps);
        this.IMAGE = new Image(gameProps.getProperty("gameObjects.smoke.image"));
        this.MAX_ACTIVE_FRAMES = Integer.parseInt(gameProps.getProperty("gameObjects.smoke.ttl"));
    }

    /**
     * Update the smoke based on its owner's location
     *
     * @param x The x-coordinate of its owner's location
     * @param y The y-coordinate of its owner's location
     */
    @Override
    public void update(int x, int y) {
        if (this.getFramesActive() > this.MAX_ACTIVE_FRAMES) {
            this.setFramesActive(0);
        }
        moveWithOwner(x, y);
        if (this.getFramesActive() > 0) {
            this.setFramesActive(this.getFramesActive() + 1);
            draw();
        }
    }

    /**
     * show the image of the smoke on its current location
     */
    @Override
    public void draw() {
        IMAGE.draw(this.getX(), this.getY());
    }

}
