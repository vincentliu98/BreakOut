package sample;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Bouncer {
    public double myBouncerX;
    public double myBouncerY;
    public double myBouncerWidth;
    public double myBouncerHeight;
    private int BOUNCER_SPEED;

    public ImageView myBouncer;
    public Point2D myVelocity;

    public Main context;

    // constructor
    // x and y are the width and height of the canvas
    public Bouncer(Image image, int speed, double scale, Main context) {
        this.context = context;

        myBouncer = new ImageView(image);
        myBouncerWidth = myBouncer.getBoundsInParent().getWidth();
        myBouncerHeight = myBouncer.getBoundsInParent().getHeight();
        // make sure it stays within the bounds
        myBouncer.setScaleX(scale);
        myBouncer.setScaleY(scale);
        BOUNCER_SPEED = speed;
        // turn speed into velocity that can be updated on bounces
        myVelocity = new Point2D(BOUNCER_SPEED, BOUNCER_SPEED);
    }

    // make the ball move
    public void move(double elapsedTime) {
        if (context.launch == true) {
            myBouncerX = myBouncer.getX() + myVelocity.getX() * elapsedTime;
            myBouncerY = myBouncer.getY() + myVelocity.getY() * elapsedTime;
            myBouncer.setX(myBouncerX);
            myBouncer.setY(myBouncerY);
        } else {
            myBouncer.setX(context.myPaddle.getX() + context.myPaddle.getBoundsInParent().getWidth() / 2 - myBouncerWidth / 2);
            myBouncer.setY(context.myPaddle.getY() - myBouncerHeight);
        }
    }

    // make the ball bounce off the screen
    public void bounceWall(double screenWidth, Stage stage) {
        // collide with horizontal direction
        if (myBouncer.getX() < 0 || myBouncer.getX() > screenWidth - myBouncer.getBoundsInLocal().getWidth()) {
            myVelocity = new Point2D(-myVelocity.getX(), myVelocity.getY());
            context.setRecentlyHit(false);
        }
        // collide with upper part
        else if (myBouncer.getY() < 0) {
            myVelocity = new Point2D(myVelocity.getX(), -myVelocity.getY());
            context.setRecentlyHit(false);
        } else if (myBouncer.getY() >= screenWidth) {
            context.current_life--;
            // reset the ball
            context.launch = false;
            myBouncer.setX(context.myPaddle.getX() + context.myPaddle.getBoundsInParent().getWidth() / 2 - myBouncerWidth / 2);
            myBouncer.setY(context.myPaddle.getY() - myBouncerHeight);
        }
    }

    // Returns internal view of bouncer to interact with other JavaFX methods.
    public Node getView() {
        return myBouncer;
    }

}

