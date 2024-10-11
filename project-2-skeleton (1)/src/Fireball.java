import bagel.*;

import java.util.Properties;

/**
 * Class representing fireballs shot by the enemy cars
 */
public class Fireball extends GameEntity {
    private final Image IMAGE;
    private boolean isCollided = false;
    private final int SPEED;
    private final double RADIUS;
    private final double DAMAGE;

    /**
     *
     * @param x x-coordinate of the fireball
     * @param y y-coordinate of the fireball
     * @param gameProps The Game Property where stores essential details information
     */
    public Fireball(int x, int y, Properties gameProps) {
        super(x, y, gameProps);
        IMAGE = new Image(gameProps.getProperty("gameObjects.fireball.image"));
        SPEED = Integer.parseInt(gameProps.getProperty("gameObjects.fireball.shootSpeedY"));
        RADIUS = Double.parseDouble(gameProps.getProperty("gameObjects.fireball.radius"));
        DAMAGE = Double.parseDouble(gameProps.getProperty("gameObjects.enemyCar.damage")) *
                DamageableGameEntity.getHealthUnit();
    }

    /**
     * Gets the damage points that it can give to other game entities
     *
     * @return the damage points that it can give to other game entities
     */
    public double getDamage() {
        return this.DAMAGE;
    }

    /**
     * Gets the fireballs' radius to detect collisions
     *
     * @return the fireball's radius
     */
    public double getRadius() {
        return this.RADIUS;
    }

    /**
     * Gets the status of whether the fireball is collided or not
     *
     * @return true if the fireball is collided; otherwise, false
     */
    public boolean getIsCollided() {
        return this.isCollided;
    }

    /**
     * Sets the status of isCollided to be true
     */
    public void setIsCollided() {
        this.isCollided = true;
    }

    /**
     * shows the image of fireball at its current location
     */
    @Override
    public void draw() {
        IMAGE.draw(this.getX(), this.getY());
    }

    /**
     * Updates the fireball's location based on user's input
     * @param input User's current input
     */
    private void adjustToInputMovement(Input input) {
        if (input.wasPressed(Keys.UP)) {
            this.setY(this.getY() + this.getScrollSpeedY());
        }
    }

    private void move() {
        this.setY(this.getY() - this.SPEED);
    }

    /**
     * Update on the fireball's status based on the user's current input
     * @param input User's current input
     */
    public void update(Input input) {
        if (!this.isCollided) {
            adjustToInputMovement(input);
            move();
            draw();
        }
    }
}
