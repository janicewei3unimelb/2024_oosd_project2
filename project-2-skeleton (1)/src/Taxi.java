import bagel.*;
import java.util.Properties;

public class Taxi extends DamageableGameEntity {

    private final Image IMAGE;
    private final Image DAMAGEDIMAGE;

    private final int SPEEDX;

    private boolean isMovingY;
    private boolean isMovingX;

    private Driver driver;
    private final Smoke SMOKE;
    private final Fire FIRE;

    public Taxi(int x, int y, double healthPoints, double damage, int timeout_move_speed,
                double radius, Properties gameProps) {
        super(x, y, healthPoints, damage, timeout_move_speed, radius, gameProps);
        this.IMAGE = new Image(gameProps.getProperty("gameObjects.taxi.image"));
        this.DAMAGEDIMAGE = new Image(gameProps.getProperty("gameObjects.taxi.damagedImage"));

        this.SPEEDX = Integer.parseInt(gameProps.getProperty("gameObjects.taxi.speedX"));
        SMOKE = new Smoke(x, y, gameProps);
        FIRE = new Fire(x, y, gameProps);
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
        updateCollisionTimeout();

        if (this.isDestroyed()) {
            moveAway(input);
        } else if (!this.isDestroyed() && this.getDriver().getDriverIsInTaxi() && this.getCollisionTimeout() <= 0) {
            adjustToInputMovement(input);
        } else if(!this.isDestroyed() && this.getCollisionTimeout() > 0 && this.getCollisionTimeout() <
                this.getMoveAwayTimeoutframes()) {
            showCollisionEffect(this.getCollisionOnTop());
        } else if (!this.isDestroyed() && !this.getDriver().getDriverIsInTaxi()) {
            moveAway(input);
        }
        if (this.isDestroyed()) {
            FIRE.setFramesActive(FIRE.getFramesActive() + 1);
        }
        SMOKE.update(this.getX(), this.getY());
        FIRE.update(this.getX(), this.getY());
        this.draw();
    }

    public void moveAway(Input input) {
        if (input.isDown(Keys.UP)) {
            this.setY(this.getY() + this.getScrollSpeedY());
        }
    }

    @Override
    public void takeDamage(double damage, boolean onTop) {
        updateHealthPoints(damage);
        SMOKE.setFramesActive(SMOKE.getFramesActive() + 1);
        this.setCollisionTimeout(this.getCollisionTimeout() + 1);
        this.setCollisionOnTop(onTop);
    }

    @Override
    public void adjustToInputMovement(Input input) {

        if (input.isDown(Keys.UP)) {
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
