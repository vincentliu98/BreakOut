package sample;

/**
 * PointsPower Class:
 * Subclass of PowerUp. Add 500 points to the user when the user receives the power-up. Update the hit variable to show
 * whether the power-up has hit the paddle
 *
 * I think it is well designed because it inherits the PowerUp class and uses setters to change values in the Main class
 *
 * @author Vincent Liu, jl729
 */

import javafx.scene.image.Image;

public class PointsPower extends PowerUp {

    public static final int ADDITIONAL_SCORE = 500;

    /**
     * Constructor
     */
    public PointsPower(Image image, double x, double y, Main context) {
        super(image, x, y, context);
    }

    /**
     * Check whether the power-up has hit the paddle.
     * If it has hit the paddle, then add additional score to the user
     *
     * @param pointsPower
     */
    public void checkHit(PointsPower pointsPower) {
        if (pointsPower.hit) {
            context.setCurrent_score(context.getCurrent_score() + ADDITIONAL_SCORE);
            pointsPower.hit = false;
        }
    }

}
