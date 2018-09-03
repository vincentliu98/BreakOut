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

public class Main extends Application {
    public static final String TITLE = "Vincent's BreakOut";
    public static final int SIZE = 700;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.WHITE;
    public static final String BOUNCER_IMAGE = "ball.gif";
    public static final String BRICK_IMAGE1 = "brick1.gif";
    public static final String BRICK_IMAGE2 = "brick2.gif";
    public static final String BRICK_IMAGE3 = "brick3.gif";
    public static final String BRICK_IMAGE4 = "brick4.gif";
    public static final String BRICK_IMAGE5 = "brick5.gif";
    public static final String BRICK_IMAGE6 = "brick6.gif";
    public static final String BRICK_IMAGE7 = "brick7.gif";
    public static final String BRICK_IMAGE8 = "brick8.gif";
    public static final String BRICK_IMAGE9 = "brick9.gif";
    public static final String PADDLE_IMAGE = "paddle.gif";
    public static final int MOVER_SPEED = 20;
    public static final int BRICKS_COLUMN = 6;

    public int BRICKS_ROW = 0;
    public int GAME_LIFE = 3;
    public int GAME_LEVEL = 2;
    public ImageView[][] myBrick = null;
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

        // construct bricks
        switch(GAME_LEVEL) {
            case 1: BRICKS_ROW = 5; break;
            case 2: BRICKS_ROW = 6; break;
            case 3: BRICKS_ROW = 7; break;
        }
        myBrick = new ImageView[BRICKS_ROW][BRICKS_COLUMN];
        for (int i = 0; i < BRICKS_ROW; i++) {
            for (int j = 0; j < BRICKS_COLUMN; j++) {
                Image brick_image = null;
                // set different rows to different bricks
                switch(i) {
                    case 0: brick_image=new Image(this.getClass().getClassLoader().getResourceAsStream(BRICK_IMAGE1)); break;
                    case 1: brick_image=new Image(this.getClass().getClassLoader().getResourceAsStream(BRICK_IMAGE2)); break;
                    case 2: brick_image=new Image(this.getClass().getClassLoader().getResourceAsStream(BRICK_IMAGE3)); break;
                    case 3: brick_image=new Image(this.getClass().getClassLoader().getResourceAsStream(BRICK_IMAGE4)); break;
                    case 4: brick_image=new Image(this.getClass().getClassLoader().getResourceAsStream(BRICK_IMAGE5)); break;
                    case 5: brick_image=new Image(this.getClass().getClassLoader().getResourceAsStream(BRICK_IMAGE6)); break;
                    case 6: brick_image=new Image(this.getClass().getClassLoader().getResourceAsStream(BRICK_IMAGE7)); break;
                    case 7: brick_image=new Image(this.getClass().getClassLoader().getResourceAsStream(BRICK_IMAGE8)); break;
                    case 8: brick_image=new Image(this.getClass().getClassLoader().getResourceAsStream(BRICK_IMAGE9)); break;
                }
                myBrick[i][j] = new ImageView(brick_image);
                // brick image has width 70, height 20
                myBrick[i][j].setX(j*brick_image.getWidth()+50*j+20);
                myBrick[i][j].setY(i*brick_image.getHeight()+50*i+20);
                root.getChildren().add(myBrick[i][j]);
            }
        }

        // add bouncer and paddle
        myBouncer = new Bouncer(ball_image, width, height);
        myPaddle = new ImageView(paddle_image);
        // position the elements
        myPaddle.setX(width/2 - myPaddle.getLayoutX());
        myPaddle.setY(height - myPaddle.getLayoutY() - 20);
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
                    // from below
                    //myBrick[i][j].getLayoutBounds().getHeight()/2
                    if (myBouncer.myBouncerY <= myBrick[i][j].getY() + myBrick[i][j].getLayoutBounds().getHeight()) {
                        myBouncer.myVelocity = new Point2D(myBouncer.myVelocity.getX(), -myBouncer.myVelocity.getY());
                        System.out.print("Brick : " + myBrick[i][j].getY());
                        System.out.print("Bouncer : " + myBouncer.myBouncerY);
                        System.out.println("reversed Y");
                    }
                    // from above
                    else if (myBouncer.myBouncerY >= myBrick[i][j].getY() - myBrick[i][j].getLayoutBounds().getHeight()) {
                        myBouncer.myVelocity = new Point2D(myBouncer.myVelocity.getX(), -myBouncer.myVelocity.getY());
                        System.out.print("Brick : " + myBrick[i][j].getY());
                        System.out.print("Bouncer : " + myBouncer.myBouncerY);
                        System.out.println("reversed Y");                    }
                    // from left
                    else if (myBouncer.myBouncerX < myBrick[i][j].getLayoutX()) {
                        myBouncer.myVelocity = new Point2D(-myBouncer.myVelocity.getX(), myBouncer.myVelocity.getY());
                        System.out.println("reversed X");
                    }
                    // from right
                    else if (myBouncer.myBouncerX > myBrick[i][j].getLayoutX()) {
                        myBouncer.myVelocity = new Point2D(-myBouncer.myVelocity.getX(), myBouncer.myVelocity.getY());
                        System.out.println("reversed X");
                    }
                }

//                if (myBouncer.getView().getBoundsInLocal().intersects(myBrick[i][j].getLayoutBounds())) {
//                    // center is between x
//                    if (myBouncer.getView().getBoundsInLocal().getMaxX() + myBouncer.getView().getBoundsInLocal().getMinX()
//                            <= 2 * myBrick[i][j].getBoundsInLocal().getMaxX() && myBouncer.getView().getBoundsInLocal().getMaxX() + myBouncer.getView().getBoundsInLocal().getMinX()
//                            >= 2 * myBrick[i][j].getBoundsInLocal().getMinX()) {
//                        myBouncer.myVelocity = new Point2D(-myBouncer.myVelocity.getX(), myBouncer.myVelocity.getY());
//                        System.out.println("reverse X");
//                    } else if (myBouncer.getView().getBoundsInLocal().getMaxY() + myBouncer.getView().getBoundsInLocal().getMinY()
//                            <= 2 * myBrick[i][j].getBoundsInLocal().getMaxY() && myBouncer.getView().getBoundsInLocal().getMaxY() + myBouncer.getView().getBoundsInLocal().getMinY()
//                            >= 2 * myBrick[i][j].getBoundsInLocal().getMinY()) {
//                        myBouncer.myVelocity = new Point2D(myBouncer.myVelocity.getX(), -myBouncer.myVelocity.getY());
//                        System.out.println("reverse Y");
//                    }
//                }
//                    // Hit from top
//                    if (myBouncer.getView().getBoundsInLocal().getMaxY() >=  myBrick[i][j].getBoundsInLocal().getMinY()) {
//                        myBouncer.myVelocity = new Point2D(myBouncer.myVelocity.getX(), -myBouncer.myVelocity.getY());
//                        System.out.println("reverse Y");
//                    }
//                    // Hit from bottom
//                    else if (myBouncer.getView().getBoundsInLocal().getMinY() <=  myBrick[i][j].getBoundsInLocal().getMaxY()) {
//                        myBouncer.myVelocity = new Point2D(myBouncer.myVelocity.getX(), -myBouncer.myVelocity.getY());
//                        System.out.println("reverse Y");
//                    }
//                    // Hit from left
//                    else if (myBouncer.getView().getBoundsInLocal().getMaxX() >=  myBrick[i][j].getBoundsInLocal().getMinX()) {
//                        myBouncer.myVelocity = new Point2D(-myBouncer.myVelocity.getX(), myBouncer.myVelocity.getY());
//                        System.out.println("reverse X");
//                    }
//                    // Hit from right
//                    else if (myBouncer.getView().getBoundsInLocal().getMinX() <=  myBrick[i][j].getBoundsInLocal().getMaxX()) {
//                        myBouncer.myVelocity = new Point2D(-myBouncer.myVelocity.getX(), myBouncer.myVelocity.getY());
//                        System.out.println("reverse X");
//                    }
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
