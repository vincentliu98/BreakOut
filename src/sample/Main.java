package sample;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

/**
 * A basic example JavaFX program for the first lab.
 *
 * @author Robert C. Duvall
 */
public class Main extends Application {
    public static final String TITLE = "Vincent's BreakOut";
    public static final int SIZE = 700;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.WHITE;
    public static final String BOUNCER_IMAGE = "ball.gif";
    public static final String BRICK_IMAGE = "brick1.gif";
    public static final String PADDLE_IMAGE = "paddle.gif";
    public static final int MOVER_SPEED = 20;
    public static final int BRICKS_COLUMN = 6;
    public static int BRICKS_ROW = 5;
    public ImageView[][] myBrick = new ImageView[BRICKS_ROW][BRICKS_COLUMN];


    // some things we need to remember during our game
    private Scene myScene;
    private ImageView myPaddle;
    private Bouncer myBouncer;

    /**
     * Initialize what will be displayed and how it will be updated.
     */
    @Override
    public void start (Stage stage) {
        // attach scene to the stage and display it
        myScene = setupGame(SIZE, SIZE, BACKGROUND);
        stage.setScene(myScene);
        stage.setTitle(TITLE);
        stage.show();
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        var animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    // Create the game's "scene": what shapes will be in the game and their starting properties
    private Scene setupGame (int width, int height, Paint background) {
        // create one top level collection to organize the things in the scene
        var root = new Group();
        // create a place to see the shapes
        var scene = new Scene(root, width, height, background);
        // make some shapes and set their properties
        var ball_image = new Image(this.getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));
        var paddle_image = new Image(this.getClass().getClassLoader().getResourceAsStream(PADDLE_IMAGE));

        // create bricks
        var brick_image = new Image(this.getClass().getClassLoader().getResourceAsStream(BRICK_IMAGE));
//       70 System.out.println(brick_image.getWidth());
//       20 System.out.println(brick_image.getHeight());

        for (int i = 0; i < BRICKS_ROW; i++) {
            for (int j = 0; j < BRICKS_COLUMN; j++) {
                myBrick[i][j] = new ImageView(brick_image);
                myBrick[i][j].setX(j*brick_image.getWidth()+50*j+20);
                myBrick[i][j].setY(i*brick_image.getHeight()+50*i+20);
                root.getChildren().add(myBrick[i][j]);
            }
        }

        // add bouncer and paddle
        myBouncer = new Bouncer(ball_image, width, height);
        System.out.println(myBouncer.getView().getBoundsInLocal());
        myPaddle = new ImageView(paddle_image);
        // position the elements
        myPaddle.setX(width/2 - myPaddle.getLayoutX());
        myPaddle.setY(height - myPaddle.getLayoutY() - 10);
        myPaddle.setScaleX(2);myPaddle.setScaleY(2);

        // add ball, brick, and paddle
        root.getChildren().add(myBouncer.getView());
        root.getChildren().add(myPaddle);
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }

    // Change properties of shapes to animate them
    // Note, there are more sophisticated ways to animate shapes, but these simple ways work fine to start.
    private void step (double elapsedTime) {
        // make the bouncers move
        myBouncer.move(elapsedTime);
        // make the bouncers bounce
        myBouncer.bounceWall(myScene.getWidth());

        // deal with bouncing off the paddle
        if (myBouncer.getView().getBoundsInLocal().intersects(myPaddle.getBoundsInLocal())) {
            myBouncer.myVelocity = new Point2D(myBouncer.myVelocity.getX(), -myBouncer.myVelocity.getY());
        }

        // deal with bouncing off the bricks
        for (int i = 0; i < BRICKS_ROW; i++) {
            for (int j = 0; j < BRICKS_COLUMN; j++) {
                if (myBouncer.getView().getBoundsInLocal().intersects(myBrick[i][j].getBoundsInLocal())) {
                    // if x intersects, change y speed
                    if (Math.abs(myBouncer.getView().getLayoutX() - myBrick[i][j].getX()) >= 0 ||
                            myBouncer.getView().getBoundsInLocal().getMinX() <= myBrick[i][j].getBoundsInLocal().getMaxX()) {
                        myBouncer.myVelocity = new Point2D(myBouncer.myVelocity.getX(), -myBouncer.myVelocity.getY());
                    }
                    if (myBouncer.getView().getBoundsInLocal().getMaxY() >= myBrick[i][j].getBoundsInLocal().getMinY() ||
                            myBouncer.getView().getBoundsInLocal().getMinY() <= myBrick[i][j].getBoundsInLocal().getMaxY()) {
                        // if y intersects, change x speed
                        myBouncer.myVelocity = new Point2D(-myBouncer.myVelocity.getX(), myBouncer.myVelocity.getY());
                    }
                }
            }
        }
    }

    // What to do each time a key is pressed
    private void handleKeyInput (KeyCode code) {
        if (code == KeyCode.LEFT) {
            myPaddle.setX(myPaddle.getX() - MOVER_SPEED);
        }
        else if (code == KeyCode.RIGHT) {
            myPaddle.setX(myPaddle.getX() + MOVER_SPEED);
        }
    }

    /**
     * Start the program.
     */
    public static void main (String[] args) {
        launch(args);
    }
}
