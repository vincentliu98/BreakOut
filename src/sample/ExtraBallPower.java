package sample;

/**
 * PointsPower Class:
 * Subclass of PowerUp. Update the hit variable to show whether the power-up has hit the paddle. If hit
 * is true, then in the if statement in the Main class, the bouncer will destroy any bricks it encounters
 *
 * I think it is well designed because it inherits the PowerUp class and uses setters to change values in the Main class
 *
 * @author Vincent Liu, jl729
 */

import javafx.scene.image.Image;

public class ExtraBallPower extends PowerUp {

    /**
     * Constructor
     */
    public ExtraBallPower(Image image, double x, double y, Main context) {
        super(image, x, y, context);
    }

    /**
     * Update the state of power-up after a certain time limit
     *
     * @param thereIsPower
     * @param time_limit
     * @param SECOND_DELAY
     * @param TIME_LIMIT
     */
    public void updatePowerState(boolean thereIsPower, double time_limit, double SECOND_DELAY, double TIME_LIMIT) {
        if (thereIsPower) {
            context.setPower_time_limit(time_limit - SECOND_DELAY);
        }
        if (time_limit <= 0) {
            context.setThereIsExtraPower(false);
            context.setPower_time_limit(TIME_LIMIT);
        }
    }

    /**
     * Check whether the power-up has hit the paddle. If so, expand the paddle.
     * When the time limit for the power-up is up, change the state to there is no power
     *
     * @param thereIsExtraPower
     * @param extraBallPower
     */
    public void checkHit(boolean thereIsExtraPower, ExtraBallPower extraBallPower) {
        if (!thereIsExtraPower && extraBallPower.hit) {
            context.setThereIsExtraPower(true);
            extraBallPower.hit = false;
        }
    }
}