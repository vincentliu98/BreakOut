package sample;

import javafx.application.Application;
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
    public static final Paint BACKGROUND = Color.AZURE;
    public static final String BOUNCER_IMAGE = "ball.gif";
    public static final String BRICK_IMAGE = "brick1.gif";
    public static final String PADDLE_IMAGE = "paddle.gif";
    public static final int MOVER_SPEED = 5;

    // some things we need to remember during our game
    private Scene myScene;
    private ImageView myPaddle;
    private Bouncer myBouncer;
    private ImageView myBrick;

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
        var brick_image = new Image(this.getClass().getClassLoader().getResourceAsStream(BRICK_IMAGE));
        myBouncer = new Bouncer(ball_image, width, height);
        myPaddle = new ImageView(paddle_image);
        myBrick = new ImageView(brick_image);

        // position the elements
        myPaddle.setX(width/2 - myPaddle.getFitWidth()/2);
        myPaddle.setY(height - myPaddle.getFitHeight() - 20);
        myBrick.setX(width/2 - myBrick.getFitWidth()/2);
        myBrick.setY(height/2 - myBrick.getFitHeight()/2);

        // add ball, brick, and paddle
        root.getChildren().add(myBouncer.getView());
        root.getChildren().add(myPaddle);
        root.getChildren().add(myBrick);
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }

    // Change properties of shapes to animate them
    // Note, there are more sophisticated ways to animate shapes, but these simple ways work fine to start.
    private void step (double elapsedTime) {
        // make the bouncers move
        myBouncer.move(elapsedTime);
        // make the bouncers bounce
        myBouncer.bounce(myScene.getWidth(), myScene.getHeight());
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
