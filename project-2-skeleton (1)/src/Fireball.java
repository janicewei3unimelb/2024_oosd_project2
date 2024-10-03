import bagel.*;

import java.util.Properties;

public class Fireball extends GameEntity {
    private final Image IMAGE;
    private boolean isCollided = false;
    private final int SPEED;

    public Fireball(int x, int y, Properties gameProps) {
        super(x, y, gameProps);
        IMAGE = new Image(gameProps.getProperty("gameObjects.fireball.image"));
        SPEED = Integer.parseInt(gameProps.getProperty("gameObjects.fireball.shootSpeedY"));
    }

    public void setIscollided() {
        this.isCollided = true;
    }

    @Override
    public void draw() {
        IMAGE.draw(this.getX(), this.getY());
    }

    @Override
    public void adjustToInputMovement(Input input) {
        if (input.wasPressed(Keys.UP)) {
            this.setY(this.getY() + this.getScrollSpeedY());
        }
    }

    public void move() {
        this.setY(this.getY() - this.SPEED);
    }

    public void update(Input input) {
        if (!this.isCollided) {
            adjustToInputMovement(input);
            move();
            draw();
        }
    }
}
