import bagel.Input;

import java.util.Properties;

public abstract class DamageEffect extends GameEntity {
    private int framesActive = 0;

    public DamageEffect(int x, int y, Properties gameProps) {
        super(x, y, gameProps);
    }

    public void moveWithOwner(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    public void setFramesActive(int updatedResult) {
        this.framesActive = updatedResult;
    }

    public int getFramesActive() {
        return framesActive;
    }

    public abstract void update(int x, int y);
}
