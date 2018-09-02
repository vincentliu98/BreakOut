package sample;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;

public class Bouncer {
    public static final int BOUNCER_SPEED = 100;
    public static final double BOUNCER_SCALE = 1.5;

    public ImageView myBouncer;
    public Point2D myVelocity;

    // constructor
    // x and y are the width and height of the canvas
    public Bouncer (Image image, int x, int y) {
        myBouncer = new ImageView(image);

        // make sure it stays within the bounds
        myBouncer.setX(x/3);
        myBouncer.setY(y/2);
        myBouncer.setScaleX(BOUNCER_SCALE);
        myBouncer.setScaleY(BOUNCER_SCALE);

        // turn speed into velocity that can be updated on bounces
        myVelocity = new Point2D(BOUNCER_SPEED, BOUNCER_SPEED);
    }

    // make the ball move
    public void move(double elapsedTime) {
        myBouncer.setX(myBouncer.getX() + myVelocity.getX() * elapsedTime);
        myBouncer.setY(myBouncer.getY() + myVelocity.getY() * elapsedTime);
    }

    // make the ball bounce off the screen
    public void bounceWall (double screenWidth) {
        // collide with horizontal direction
        if (myBouncer.getX() < 0 || myBouncer.getX() > screenWidth - myBouncer.getBoundsInLocal().getWidth()) {
            myVelocity = new Point2D(-myVelocity.getX(), myVelocity.getY());
        }
        // collide with upper part
        if (myBouncer.getY() < 0) {
            myVelocity = new Point2D(myVelocity.getX(), -myVelocity.getY());
        }
    }

    // Returns internal view of bouncer to interact with other JavaFX methods.
    public Node getView () {
        return myBouncer;
    }

}

