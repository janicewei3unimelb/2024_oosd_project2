import java.util.Properties;

public abstract class DamageableGameEntity extends GameEntity {
    private Fire fire;
    private Smoke smoke;
    private Blood blood;

    private static final int HEALTH_UNIT = 100;
    private static final int MAX_TIMEOUTFRAMES = 200;
    private static final int MOVE_AWAY_TIMEOUTFRAMES = 10;

    private double healthPoints;
    private int collisionTimeout = 0;
    private final double DAMAGE;
    private final int TIMEOUT_MOVE_SPEED;
    private final double RADIUS;
    private boolean collisionOnTop;
    public DamageableGameEntity(int x, int y, double healthPoints, double damage, int move_speed,
                                double radius, Properties gameProps) {
        super(x, y, gameProps);
        this.healthPoints = healthPoints;
        this.DAMAGE = damage;
        this.TIMEOUT_MOVE_SPEED = move_speed;
        this.RADIUS = radius;
    }

    public double getRadius() {return this.RADIUS; }

    public boolean isDestroyed() {
        return this.healthPoints <= 0;
    }

    public void setCollisionOnTop(boolean result) {
        this.collisionOnTop = result;
    }

    public void updateHealthPoints(double damage) {
        this.healthPoints -= damage;
    }

    public double getHealthPoints() {
        return this.healthPoints;
    }

    public int getCollisionTimeout() {
        return this.collisionTimeout;
    }

    public void setCollisionTimeout(int collisionTimeout) {
        this.collisionTimeout = collisionTimeout;
    }

    public static int getHealthUnit() {
        return HEALTH_UNIT;
    }

    public abstract void takeDamage(double damage);
}
