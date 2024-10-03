import bagel.*;

import java.util.Properties;

public class Taxi extends DamageableGameEntity {

    private final Image IMAGE;
    private final Image DAMAGEDIMAGE;

    private final int SPEEDX;

    private boolean isMovingY;
    private boolean isMovingX;

    private Driver driver;

    public Taxi(int x, int y, double healthPoints, double damage, int timeout_move_speed,
                double radius, Properties gameProps) {
        super(x, y, healthPoints, damage, timeout_move_speed, radius, gameProps);
        this.IMAGE = new Image(gameProps.getProperty("gameObjects.taxi.image"));
        this.DAMAGEDIMAGE = new Image(gameProps.getProperty("gameObjects.taxi.damagedImage"));

        this.SPEEDX = Integer.parseInt(gameProps.getProperty("gameObjects.taxi.speedX"));

    }

    public Driver getDriver() {
        return this.driver;
    }

    public void setDriver(Driver newDriver) {
        this.driver = newDriver;
    }

    public boolean isMovingY() {
        return isMovingY;
    }

    public boolean isMovingX() {
        return isMovingX;
    }

    @Override
    public void draw() {
        if (!this.isDestroyed()) {
            IMAGE.draw(this.getX(), this.getY());
        } else {
            DAMAGEDIMAGE.draw(this.getX(), this.getY());
        }
    }

    public void update(Input input) {
        if (!this.isDestroyed()) {
            adjustToInputMovement(input);
        } else {

        }
        this.draw();

    }

    @Override
    public void takeDamage(double damage) {
        updateHealthPoints(damage);
    }

    @Override
    public void adjustToInputMovement(Input input) {

        if (input.wasPressed(Keys.UP)) {
            isMovingY = true;
        }  else if(input.wasReleased(Keys.UP)) {
            isMovingY = false;
        } else if(input.isDown(Keys.LEFT)) {
            this.setX(this.getX() - SPEEDX);
            isMovingX = true;
        }  else if(input.isDown(Keys.RIGHT)) {
            this.setX(this.getX() + SPEEDX);
            isMovingX =  true;
        } else if(input.wasReleased(Keys.LEFT) || input.wasReleased(Keys.RIGHT)) {
            isMovingX = false;
        }
    }

}
