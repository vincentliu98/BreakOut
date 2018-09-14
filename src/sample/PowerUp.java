package sample;

/**
 * PowerUp Class:
 * Superclass for all power-ups
 * Manages movement, collision, and power-state
 *
 * Design:
 * I think it is a good design because it saves plenty of code for me to have one superclass and three subclasses.
 * I have learned to use inheritance in class and applied it to this project. However, for some methods in the subclasses,
 * such as checkHit and updatePowerState, there are some duplicated code, but the methods' contents are pretty different.
 * I will keep thinking about better approaches to improve current design
 *
 * @author Vincent Liu, jl729
 */


import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PowerUp {
    protected final int POWER_SPEED = 100;
    protected final int DISAPPEAR_POSITION = -100;
    protected boolean hit = false;
    protected ImageView myPower;
    protected Point2D myVelocity;
    protected Main context;
    protected double myPowerY;

    /**
     * Constructor for PowerUp objects
     *
     * @param image
     * @param x
     * @param y
     * @param context
     */
    public PowerUp(Image image, double x, double y, Main context) {
        myPower = new ImageView(image);
        this.context = context;
        // make sure it stays within the bounds
        myPower.setX(x);
        myPower.setY(y);
        // turn speed into velocity that can be updated on bounces
        myVelocity = new Point2D(POWER_SPEED, POWER_SPEED);
    }

    /**
     * Controls the movement of the power-up
     *
     * @param elapsedTime
     */
    public void move(double elapsedTime) {
        myPowerY = myPower.getY() + myVelocity.getY() * elapsedTime;
        myPower.setY(myPowerY);
    }

    /**
     * Check if the power-up has hit the paddle. If so, make the power-up disappear and set hit equal true
     *
     * @param myPaddleX
     * @param screenWidth
     */
    public void hitPaddle(double myPaddleX, int screenWidth) {
        if (myPowerY + myPower.getBoundsInParent().getHeight() >= screenWidth - context.PADDLE_HEIGHT && myPaddleX <= myPower.getX() && myPaddleX + context.PADDLE_WIDTH >= myPower.getX()) {
            myPower.setY(DISAPPEAR_POSITION);
            myPower.setX(DISAPPEAR_POSITION);
            hit = true;
        }
    }

    /**
     * Returns internal view of bouncer to interact with other JavaFX methods.
     *
     * @return
     */
    public Node getView() {
        return myPower;
    }
}
