package sample;

import javafx.scene.image.Image;

public class PointsPower extends PowerUp {

    public static final int ADDITIONAL_SCORE = 500;
    // constructor
    // x and y are the width and height of the canvas
    public PointsPower(Image image, double x, double y, Main context) {
        super(image, x, y, context);
    }

    @Override
    // hit paddle, add score
    public void hitPaddle(double myPaddleX, int screenWidth) {
        super.hitPaddle(myPaddleX, screenWidth);
        context.setCurrent_score(context.getCurrent_score() + ADDITIONAL_SCORE);
    }


}
