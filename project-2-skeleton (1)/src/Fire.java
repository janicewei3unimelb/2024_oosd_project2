import bagel.*;

import java.util.Properties;

public class Fire extends DamageEffect {
    private final Image IMAGE;
    private final int MAX_ACTIVE_FRAMES;

    public Fire(int x, int y, Properties gameProps) {
        super(x, y, gameProps);
        this.IMAGE = new Image(gameProps.getProperty("gameObjects.fire.image"));
        this.MAX_ACTIVE_FRAMES = Integer.parseInt(gameProps.getProperty("gameObjects.fire.ttl"));
    }

    @Override
    public void draw() {
        System.out.println("the fire is rendered!");
        IMAGE.draw(this.getX(), this.getY());
    }

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
