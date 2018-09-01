package sample;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import java.util.Random;

public class Bouncer {
    public static final int BOUNCER_MIN_SPEED = -100;
    public static final int BOUNCER_MAX_SPEED = 100;
    public static final int BOUNCER_MIN_SIZE = 1;
    public static final int BOUNCER_MAX_SIZE = 2;

    private Random dice = new Random();
    public ImageView myBouncer;
    private Point2D myVelocity;

    // constructor
    // x and y are the width and height of the canvas
    public Bouncer (Image image, int x, int y) {
        myBouncer = new ImageView(image);

        // make sure it stays a circle. set x and y the same
        int size = getRandomInRange(BOUNCER_MIN_SIZE, BOUNCER_MAX_SIZE);
        myBouncer.setScaleX(size);
        myBouncer.setScaleY(size);

        // make sure it stays within the bounds
        myBouncer.setX(getRandomInRange(size, x-size));
        myBouncer.setY(getRandomInRange(size, y-size));

        // turn speed into velocity that can be updated on bounces
        myVelocity = new Point2D(getRandomInRange(BOUNCER_MIN_SPEED, BOUNCER_MAX_SPEED),
                getRandomInRange(BOUNCER_MIN_SPEED, BOUNCER_MAX_SPEED));
    }

    // make the ball move
    public void move(double elapsedTime) {
        myBouncer.setX(myBouncer.getX() + myVelocity.getX() * elapsedTime);
        myBouncer.setY(myBouncer.getY() + myVelocity.getY() * elapsedTime);
    }

    // make the ball bounce off the screen
    public void bounce (double screenWidth, double screenHeight) {
        // collide with horizontal direction
        if (myBouncer.getX() < 0 || myBouncer.getX() > screenWidth - myBouncer.getBoundsInLocal().getWidth()) {
            myVelocity = new Point2D(-myVelocity.getX(), myVelocity.getY());
        }
        // collide with vertical direction
        if (myBouncer.getY() < 0 || myBouncer.getY() > screenHeight - myBouncer.getBoundsInLocal().getHeight()) {
            myVelocity = new Point2D(myVelocity.getX(), -myVelocity.getY());
        }
    }

    // Returns a non-zero random value in the range (min, max)
    private int getRandomInRange (int min, int max) {
        return min + dice.nextInt(max-min) + 1;
    }

    // Returns internal view of bouncer to interact with other JavaFX methods.
    public Node getView () {
        return myBouncer;
    }

}

