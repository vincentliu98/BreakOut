package sample;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;

public class Bouncer {
    public static final int BOUNCER_SPEED = -150;
    public static final double BOUNCER_SCALE = 1;
    public double myBouncerX;
    public double myBouncerY;
    public double myBouncerWidth;
    public double myBouncerHeight;

    public ImageView myBouncer;
    public Point2D myVelocity;

    private Main context;

    // constructor
    // x and y are the width and height of the canvas
    public Bouncer (Image image, int x, int y, Main context) {
        myBouncer = new ImageView(image);
        myBouncerWidth = myBouncer.getBoundsInParent().getWidth();
        myBouncerHeight = myBouncer.getBoundsInParent().getHeight();
        // make sure it stays within the bounds
        myBouncer.setX(x/3);
        myBouncer.setY(y/2);
        myBouncer.setScaleX(BOUNCER_SCALE);
        myBouncer.setScaleY(BOUNCER_SCALE);

        this.context = context;

        // turn speed into velocity that can be updated on bounces
        myVelocity = new Point2D(BOUNCER_SPEED, BOUNCER_SPEED);
    }

    // make the ball move
    public void move(double elapsedTime) {
        if (context.launch == 1) {
            myBouncerX = myBouncer.getX() + myVelocity.getX() * elapsedTime;
            myBouncerY = myBouncer.getY() + myVelocity.getY() * elapsedTime;
            myBouncer.setX(myBouncerX);
            myBouncer.setY(myBouncerY);
        }
        else {
            // need to repair the 20
            myBouncer.setX(context.myPaddle.getX() + context.myPaddle.getBoundsInParent().getWidth()/2 - myBouncerWidth/2);
            myBouncer.setY(context.myPaddle.getY() - myBouncerHeight);
        }
    }

    // make the ball bounce off the screen
    public void bounceWall (double screenWidth) {
        // collide with horizontal direction
        if (myBouncer.getX() < 0 || myBouncer.getX() > screenWidth - myBouncer.getBoundsInLocal().getWidth()) {
            myVelocity = new Point2D(-myVelocity.getX(), myVelocity.getY());
            context.setRecentlyHit(false);
        }
        // collide with upper part
        if (myBouncer.getY() < 0) {
            myVelocity = new Point2D(myVelocity.getX(), -myVelocity.getY());
            context.setRecentlyHit(false);
        }
    }

    // Returns internal view of bouncer to interact with other JavaFX methods.
    public Node getView () {
        return myBouncer;
    }

}

