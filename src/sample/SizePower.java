package sample;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;

public class SizePower {
    private static final int POWER_SPEED = 100;

    private double myPowerY;
    private ImageView myPower;
    private Point2D myVelocity;
    public boolean hit = false;

    private Main context;

    // constructor
    // x and y are the width and height of the canvas
    public SizePower(Image image, double x, double y, Main context) {
        myPower = new ImageView(image);
        // make sure it stays within the bounds
        this.context = context;
        myPower.setX(x);
        myPower.setY(y);

        // turn speed into velocity that can be updated on bounces
        myVelocity = new Point2D(POWER_SPEED, POWER_SPEED);
    }

    // make the ball move
    public void move(double elapsedTime) {
        myPowerY = myPower.getY() + myVelocity.getY() * elapsedTime;
        myPower.setY(myPowerY);
    }

    // hit paddle, produce effect
    public void hitPaddle(double myPaddleX, int screenWidth) {
        // need to fix the 20 & 80 part. it is the height of the paddle
        if (myPowerY >= screenWidth - 31 && myPaddleX <= myPower.getX() && myPaddleX + 80 >= myPower.getX()) {
            System.out.print("Hit");
            System.out.println(myPower.getX());
            System.out.println(myPaddleX);
            System.out.println(myPowerY);

            myPower.setY(-100);
            myPower.setX(-100);

            context.myPaddle.setHeight(context.paddle_height * 1.5);
            context.myPaddle.setWidth(context.paddle_width * 1.5);

            hit = true;
        }
    }

    // Returns internal view of bouncer to interact with other JavaFX methods.
    public Node getView() {
        return myPower;
    }

}

