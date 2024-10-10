import bagel.*;

import java.util.Properties;

public class Smoke extends DamageEffect {
    private final Image IMAGE;
    private final int MAX_ACTIVE_FRAMES;
    public Smoke(int x, int y, Properties gameProps) {
        super(x, y, gameProps);
        this.IMAGE = new Image(gameProps.getProperty("gameObjects.smoke.image"));
        this.MAX_ACTIVE_FRAMES = Integer.parseInt(gameProps.getProperty("gameObjects.smoke.ttl"));
    }

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

    @Override
    public void draw() {
        IMAGE.draw(this.getX(), this.getY());
    }

}
