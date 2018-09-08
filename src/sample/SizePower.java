package sample;

import javafx.scene.image.Image;

public class SizePower extends PowerUp {
    private static double PADDLE_EXPAND = 1.5;
    // constructor
    // x and y are the width and height of the canvas
    public SizePower(Image image, double x, double y, Main context) {
        super(image, x, y, context);
    }

    @Override
    // hit paddle, add score
    public void hitPaddle(double myPaddleX, int screenWidth) {
        super.hitPaddle(myPaddleX, screenWidth);
        context.myPaddle.setHeight(context.PADDLE_HEIGHT * PADDLE_EXPAND);
        context.myPaddle.setWidth(context.PADDLE_WIDTH * PADDLE_EXPAND);
    }


}

