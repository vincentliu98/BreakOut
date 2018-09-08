package sample;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class PowerUp {
    protected static final int POWER_SPEED = 100;
    public boolean hit = false;
    protected ImageView myPower;
    protected Point2D myVelocity;
    protected Main context;
    public double myPowerY;

    public PowerUp(Image image,  double x, double y, Main context) {
        myPower = new ImageView(image);
        this.context = context;
        // make sure it stays within the bounds
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

    public void hitPaddle(double myPaddleX, int screenWidth) {
        if (myPowerY + myPower.getBoundsInParent().getHeight() >= screenWidth - context.PADDLE_HEIGHT && myPaddleX <= myPower.getX() && myPaddleX + context.PADDLE_WIDTH >= myPower.getX()) {
            myPower.setY(-100);
            myPower.setX(-100);
            hit = true;
        }
    }

    // Returns internal view of bouncer to interact with other JavaFX methods.
    public Node getView() {
        return myPower;
    }
}
