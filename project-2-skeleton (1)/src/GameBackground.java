import bagel.Image;
import bagel.Window;

import java.util.Properties;

/**
 * A class representing the background of the game play.
 */
public class GameBackground {
    private final Image SUNNY_BACKGROUND;
    private final Image RAINY_BACKGROUND;

    private int y1, y2;
    private final int X;
    private final int SPEED_Y;
    private static final int MAX_Y = 1152;

    /**
     * Creates a background to be shown in the game play screen
     *
     * @param gameProps Game Property where stores essential information about the components of the game
     */
    public GameBackground(Properties gameProps) {
        SUNNY_BACKGROUND = new Image(gameProps.getProperty("backgroundImage.sunny"));
        RAINY_BACKGROUND = new Image(gameProps.getProperty("backgroundImage.raining"));
        y1 = Integer.parseInt(gameProps.getProperty("window.height")) / 2;
        y2 = -1 * Integer.parseInt(gameProps.getProperty("window.height")) / 2;
        X = Integer.parseInt(gameProps.getProperty("window.width")) / 2;
        SPEED_Y = Integer.parseInt(gameProps.getProperty("gameObjects.taxi.speedY"));
    }

    /**
     * update the shown background based on the current weather type and if the taxi/driver is moving up
     * @param weatherType The current weather in the game
     * @param moveUp true if the up arrow is pressed meaning that the backgrounds should move down; otherwise, false
     */
    public void updateBackground(String weatherType, boolean moveUp) {
        Image currentBackground = weatherType.equals("RAINING") ? RAINY_BACKGROUND : SUNNY_BACKGROUND;
        if (moveUp) {
            y1 += SPEED_Y;
            y2 += SPEED_Y;

            // Reset positions when moving off-screen
            if (y1 >= MAX_Y) {
                y1 = y2 - Window.getHeight();
            }
            if (y2 >= MAX_Y) {
                y2 = y1 - Window.getHeight();
            }
        }

        // Render both backgrounds
        render(currentBackground, y1);
        render(currentBackground, y2);
    }

    // Render the backgrounds
    private void render(Image img, int y) {
        img.draw(X, y); // Render at the center of the screen
    }

}

