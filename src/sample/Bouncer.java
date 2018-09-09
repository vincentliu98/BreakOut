package sample;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Bouncer {
    protected final int LOSE_ONE_LIFE = 1;
    public static double myBouncerX;
    public static double myBouncerY;
    public static double myBouncerWidth;
    public static double myBouncerHeight;
    public static int BOUNCER_SPEED;

    private ImageView myBouncer;
    private Point2D myVelocity;

    private Main context;

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
        if (context.getLaunch() == true) {
            myBouncerX = myBouncer.getX() + myVelocity.getX() * elapsedTime;
            myBouncerY = myBouncer.getY() + myVelocity.getY() * elapsedTime;
            myBouncer.setX(myBouncerX);
            myBouncer.setY(myBouncerY);
        } else {
            myBouncer.setX(context.myPaddle.getX() + context.myPaddle.getBoundsInParent().getWidth() / 2 - myBouncerWidth / 2);
            myBouncer.setY(context.myPaddle.getY() - context.myPaddle.getBoundsInParent().getHeight() / 2 - myBouncerHeight);
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
            context.setCurrent_life(context.getCurrent_life() - LOSE_ONE_LIFE);
            // reset the ball
            context.setLaunch(context.getLaunch());
            myBouncer.setX(context.myPaddle.getX() + context.myPaddle.getBoundsInParent().getWidth() / 2 - myBouncerWidth / 2);
            myBouncer.setY(context.myPaddle.getY() - myBouncerHeight/2);
        }
    }

    public Point2D getMyVelocity() {
        return myVelocity;
    }

    public void setMyVelocity(Point2D myVelocity) {
        this.myVelocity = myVelocity;
    }

    // Returns internal view of bouncer to interact with other JavaFX methods.
    public Node getView() {
        return myBouncer;
    }

}

