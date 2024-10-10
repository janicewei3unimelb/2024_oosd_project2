import bagel.*;

import java.util.Properties;

public class Fireball extends GameEntity {
    private final Image IMAGE;
    private boolean isCollided = false;
    private final int SPEED;
    private final double RADIUS;
    private final double DAMAGE;

    public Fireball(int x, int y, Properties gameProps) {
        super(x, y, gameProps);
        IMAGE = new Image(gameProps.getProperty("gameObjects.fireball.image"));
        SPEED = Integer.parseInt(gameProps.getProperty("gameObjects.fireball.shootSpeedY"));
        RADIUS = Double.parseDouble(gameProps.getProperty("gameObjects.fireball.radius"));
        DAMAGE = Double.parseDouble(gameProps.getProperty("gameObjects.enemyCar.damage")) *
                DamageableGameEntity.getHealthUnit();
    }

    public double getDamage() {
        return this.DAMAGE;
    }

    public double getRadius() {
        return this.RADIUS;
    }

    public boolean getIsCollided() {
        return this.isCollided;
    }

    public void setIsCollided() {
        this.isCollided = true;
    }

    @Override
    public void draw() {
        IMAGE.draw(this.getX(), this.getY());
    }

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
