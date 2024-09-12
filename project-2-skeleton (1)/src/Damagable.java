public interface Damagable {
    int HEALTH_UNIT = 100;
    int COLLISION_TIMEOUT = 200;

    void takeDamage(double damage);
    boolean isDestroyed();
}
