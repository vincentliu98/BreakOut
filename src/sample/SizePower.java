package sample;

/**
 * SizePower Class:
 * Subclass of PowerUp. Expand the paddle size by the variable PADDLE_EXPAND and update the hit variable to show
 * whether the power-up has hit the paddle
 *
 * I think it is well designed because it inherits the PowerUp class and uses setters to change values in the Main class
 *
 * @author Vincent Liu, jl729
 */

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class SizePower extends PowerUp {
    private static double PADDLE_EXPAND = 1.5;

    /**
     * Constructor
     */
    public SizePower(Image image, double x, double y, Main context) {
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
            context.setSize_time_limit(time_limit - SECOND_DELAY);
        }
        if (time_limit <= 0) {
            context.setThereIsSizePower(false);
            context.setPower_time_limit(TIME_LIMIT);
        }
    }

    /**
     * Check whether the power-up has hit the paddle. If so, expand the paddle.
     * When the time limit for the power-up is up, change the state to there is no power
     *
     * @param thereIsSizePower
     * @param sizePower
     * @param myPaddle
     */
    public void checkHit(boolean thereIsSizePower, SizePower sizePower, Rectangle myPaddle) {
        if (!thereIsSizePower && sizePower.hit) {
            thereIsSizePower = true;
            context.myPaddle.setHeight(context.PADDLE_HEIGHT * PADDLE_EXPAND);
            context.myPaddle.setWidth(context.PADDLE_WIDTH * PADDLE_EXPAND);
        }
        if (!thereIsSizePower) {
            myPaddle.setWidth(context.PADDLE_WIDTH);
            myPaddle.setHeight(context.PADDLE_HEIGHT);
        }
    }

}

