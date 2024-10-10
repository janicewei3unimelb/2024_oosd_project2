import bagel.Input;

import java.util.Properties;

/**
 * a class representing damageable game entities with x and y coordinates and other information
 */
public abstract class DamageableGameEntity extends GameEntity {

    private static final int HEALTH_UNIT = 100;
    private static final int MAX_TIMEOUTFRAMES = 200;
    private static final int MOVE_AWAY_TIMEOUTFRAMES = 10;
    private double healthPoints;
    private int collisionTimeoutFrame = 0;
    private final double DAMAGE;
    private final int TIMEOUT_MOVE_SPEED;
    private final double RADIUS;
    private boolean collisionOnTop;

    /**
     * Creates a entity instance with relative information
     *
     * @param x x-coordinate of the entity
     * @param y y-coordinate of the entity
     * @param healthPoints The health points of entity
     * @param damage The damage points it can impact on other entities
     * @param move_speed The speed that it should move in the first few frames during the collision timeout
     * @param radius The radius of the entity
     * @param gameProps The Game Property where stores essential details information
     */
    public DamageableGameEntity(int x, int y, double healthPoints, double damage, int move_speed,
                                double radius, Properties gameProps) {
        super(x, y, gameProps);
        this.healthPoints = healthPoints;
        this.DAMAGE = damage;
        this.TIMEOUT_MOVE_SPEED = move_speed;
        this.RADIUS = radius;
    }

    private void showCollisionEffect(boolean isOnTop) {
        if (isOnTop) {
            this.setY(this.getY() - TIMEOUT_MOVE_SPEED);
        } else {
            this.setY(this.getY() + TIMEOUT_MOVE_SPEED);
        }
    }

    /**
     * updates on the entity based on user's current input
     * @param input user's current input to the game play
     */
    public abstract void adjustToInputMovement(Input input);

    /**
     * updates on the collision timeout and other information that should be triggered during the timeout
     */
    public void updateCollisionTimeout() {
        if (this.getCollisionTimeoutFrames() > this.getMaxTimeoutframes()) {
            this.setCollisionTimeoutFrame(0);
        }
        if (this.getCollisionTimeoutFrames() > 0) {
            this.setCollisionTimeoutFrame(this.getCollisionTimeoutFrames() + 1);
            if (this.getCollisionTimeoutFrames() < this.getMoveAwayTimeoutframes()) {
                showCollisionEffect(this.getCollisionOnTop());
            }
        }
    }

    /**
     * gets the result of whether the entity was on the top when a collision occur
     *
     * @return true if it is on the top; otherwise, false
     */
    public boolean getCollisionOnTop() {
        return this.collisionOnTop;
    }

    /**
     * sets the result of whether the entity was on the top when a collision occur
     *
     * @param result the result that we want to assign to collisionOnTop
     */
    public void setCollisionOnTop(boolean result) {
        this.collisionOnTop = result;
    }

    /**
     * gets the duration of the move away time frames
     *
     * @return duration of the move away time frames
     */
    public int getMoveAwayTimeoutframes() {
        return this.MOVE_AWAY_TIMEOUTFRAMES;
    }

    /**
     * gets the duration of the timeout frames
     *
     * @return duration of the timeout frames
     */
    public int getMaxTimeoutframes (){
        return this.MAX_TIMEOUTFRAMES;
    }

    /**
     * Gets the radius of the entity to detect collisions
     * @return radius of the entity
     */
    public double getRadius() {return this.RADIUS; }


    /**
     * Gets the damage points that it can give to other entities
     * @return its damage points
     */
    public double getDamage() {
        return this.DAMAGE;
    }

    /**
     * Gets the result of whether the entity's health point is no greater than 0
     * @return true if the entity's health point is no greater than 0; otherwise, false
     */
    public boolean isDestroyed() {
        return this.healthPoints <= 0;
    }

    /**
     * reduce the health point of the entity by damage points
     * @param damage the amount of points that should be reduced from health point
     */
    public void updateHealthPoints(double damage) {
        this.healthPoints -= damage;
    }

    /**
     * gets the health points of the entity
     * @return the health point of the entity
     */
    public double getHealthPoints() {
        return this.healthPoints;
    }

    /**
     * Gets entity's current collision timeout frame
     * @return entity's current collision timeout frame
     */
    public int getCollisionTimeoutFrames() {
        return this.collisionTimeoutFrame;
    }

    /**
     * Sets  entity's current collision timeout frame
     * @param collisionTimeoutFrame entity's updated current collision timeout frame
     */
    public void setCollisionTimeoutFrame(int collisionTimeoutFrame) {
        this.collisionTimeoutFrame = collisionTimeoutFrame;
    }

    /**
     * Gets the health unit to calculate the correct health and damage points
     * @return the health unit
     */
    public static int getHealthUnit() {
        return HEALTH_UNIT;
    }

    /**
     * take damage from other entities involved in the collisions
     * @param damage damage points to be reduced from the health points
     * @param onTopOfCollision true if the entity is on the top when when the collision occur; otherwise, false
     */
    public abstract void takeDamage(double damage, boolean onTopOfCollision);
}
