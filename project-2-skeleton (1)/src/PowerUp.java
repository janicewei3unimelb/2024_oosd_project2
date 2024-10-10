import bagel.*;

import java.util.Properties;

public abstract class PowerUp extends GameEntity {
    private final double RADIUS;
    private final int DURATION;
    private boolean isCollided;
    private int framesActive;
    private int moveY;

    public PowerUp(int x, int y, double radius, int duration, Properties gameProps) {
        super(x, y, gameProps);
        RADIUS = radius;
        DURATION = duration;
        isCollided = false;
        framesActive = 0;
        this.moveY = 0;
    }

    public boolean hasCollidedWith(Driver driver) {
        double collisionDistance = this.getRadius() + driver.getRadius();
        double currDistance = Math.sqrt(Math.pow(this.getX() - driver.getX(), 2) +
                Math.pow(this.getY() - driver.getY(), 2));
        return currDistance <= collisionDistance;
    }


    public boolean hasCollidedWith(Taxi taxi) {
        // if the distance between the two objects is less than the sum of their radius, they are collided
        double collisionDistance = this.getRadius() + taxi.getRadius();
        double currDistance = Math.sqrt(Math.pow(this.getX() - taxi.getX(), 2) +
                Math.pow(this.getY() - taxi.getY(), 2));
        return currDistance <= collisionDistance;
    }

    public abstract void update(Input input);

    public int getFramesActive() {
        return this.framesActive;
    }

    public void setFramesActive(int result) {
        this.framesActive = result;
    }

    public int getSpeedY() { return this.getScrollSpeedY(); }

    public int getDuration() { return this.DURATION; }

    public double getRadius() { return this.RADIUS; }

    public boolean getIsCollided() {
        return this.isCollided;
    }

    public void setIsCollided() {
        this.isCollided = true;
    }

    public boolean getIsActive() {
        return isCollided && framesActive <= DURATION && framesActive > 0;
    }

    public void move() {
        this.setY(this.getY() + this.getSpeedY() * moveY);
    }

    public void adjustToInputMovement(Input input) {
        if (input.wasPressed(Keys.UP)) {
            moveY = 1;
        }  else if(input.wasReleased(Keys.UP)) {
            moveY = 0;
        }
    }

}
