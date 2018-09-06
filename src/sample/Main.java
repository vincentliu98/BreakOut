package sample;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
    public static final String SIZE_IMAGE = "sizepower.gif";
    public static final String EXTRAPOWER_IMAGE = "extraballpower.gif";
    public static final int MOVER_SPEED = 40;
    public static final int BRICKS_COLUMN = 6;
    public static final int GAME_LIFE = 3;
    public static final int GAME_LEVEL = 3;

    public double paddle_width = 80;
    public double paddle_height = 20;
    public double timeLimit = 8.0;
    public SizePower[] sizePower;
    public ExtraBallPower[] extraBallPower;
    public int current_life = 3;
    public int current_level = 1;
    public int current_score = 0;
    public double myPaddleX;
    public Rectangle[] bricks;
    public int BRICKS_NUM = 0;
    public boolean recentlyHit = false;
    public boolean thereIsExtraPower = false;
    public boolean thereIsSizePower = false;
    private Scene myScene;
    public Rectangle myPaddle;
    private Bouncer myBouncer;
    public int launch = 0;
    public Group root;

    /**
     * Initialize what will be displayed and how it will be updated.
     */
    @Override
    public void start(Stage stage) {
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
    private Scene setupGame(int width, int height, Paint background) {
        // create one top level collection to organize the things in the scene
        root = new Group();
        // create a place to see the shapes
        var scene = new Scene(root, width, height, background);
        // add bouncer and paddle
        var ball_image = new Image(this.getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));

        myBouncer = new Bouncer(ball_image, width, height, this);
        myPaddle = new Rectangle(width / 2 - paddle_width, height - paddle_height, paddle_width, paddle_height);
        myPaddle.setFill(Color.GRAY);
        // position the elements
        root.getChildren().add(myBouncer.getView());
        root.getChildren().add(myPaddle);

        // construct and add bricks
        switch (GAME_LEVEL) {
            case 1:
                BRICKS_NUM = 24;
                break;
            case 2:
                BRICKS_NUM = 30;
                break;
            case 3:
                BRICKS_NUM = 36;
                break;
        }
        bricks = new Rectangle[BRICKS_NUM];
        for (int i = 0; i < BRICKS_NUM / BRICKS_COLUMN; i++) {
            for (int j = 0; j < BRICKS_COLUMN; j++) {
//                if (j == BRICKS_COLUMN - 1) {
//                    Text question = new Text(){{
//                        setText("?");
//                    }};
//                    stack.getChildren().add(question);
//                }
                bricks[i * BRICKS_COLUMN + j] = new Rectangle(
                        60 + j * 100,
                        20 + i * 60,
                        60,
                        40
                );
                // set different rows to different bricks
                switch (i) {
                    case 0:
                        bricks[i * BRICKS_COLUMN + j].setFill(Color.RED);
                        break;
                    case 1:
                        bricks[i * BRICKS_COLUMN + j].setFill(Color.BLACK);
                        break;
                    case 2:
                        bricks[i * BRICKS_COLUMN + j].setFill(Color.YELLOW);
                        break;
                    case 3:
                        bricks[i * BRICKS_COLUMN + j].setFill(Color.GREEN);
                        break;
                    case 4:
                        bricks[i * BRICKS_COLUMN + j].setFill(Color.BLUE);
                        break;
                    case 5:
                        bricks[i * BRICKS_COLUMN + j].setFill(Color.PINK);
                        break;
                    case 6:
                        bricks[i * BRICKS_COLUMN + j].setFill(Color.GRAY);
                        break;
                }
                root.getChildren().add(bricks[i * BRICKS_COLUMN + j]);
            }
        }

        // set up power-ups
        extraBallPower = new ExtraBallPower[BRICKS_NUM];
        sizePower = new SizePower[BRICKS_NUM];

        // add captions on the top
        Text life = new Text() {{
            setTranslateX(10);
            setTranslateY(20);
            setFill(Color.BLACK);
            setFont(Font.font(15));
            setText("Level: " + current_level + "/" + GAME_LEVEL);
        }};

        Text level = new Text() {{
            setTranslateX(10 + width / 2);
            setTranslateY(20);
            setFill(Color.BLACK);
            setFont(Font.font(15));
            setText("Life: " + current_life + "/" + GAME_LIFE);
        }};

        Text score = new Text() {{
            setTranslateX(width - 100);
            setTranslateY(20);
            setFill(Color.BLACK);
            setFont(Font.font(15));
            setText("Score: " + current_score);
        }};

        root.getChildren().addAll(life, level, score);

        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }

    // Change properties of shapes to animate them
    // Note, there are more sophisticated ways to animate shapes, but these simple ways work fine to start.
    private void step(double elapsedTime) {
        // make the bouncers move
        myBouncer.move(elapsedTime);
        // make the bouncers bounce
        myBouncer.bounceWall(myScene.getWidth());

        // deal with bouncing off the paddle
        if (myBouncer.getView().getBoundsInLocal().intersects(myPaddle.getBoundsInLocal())) {
            setRecentlyHit(true);
            myBouncer.myVelocity = new Point2D(myBouncer.myVelocity.getX(), -myBouncer.myVelocity.getY());
        }

        // if it's within the time limit, then decrease timelimit
        if (thereIsExtraPower) {
            System.out.println(timeLimit);
            timeLimit -= SECOND_DELAY;
        }

        // when the time limit runs out, invalidate power-up
        if (timeLimit <= 0) {
            System.out.println("Invalidating power-up");
            thereIsExtraPower = false;
            timeLimit = 8.0;
        }

        // deal with bouncing off the bricks
        for (int i = 0; i < BRICKS_NUM / BRICKS_COLUMN; i++) {
            for (int j = 0; j < BRICKS_COLUMN; j++) {
                // move extraballpower power-ups and check for collisions
                if (extraBallPower[i * BRICKS_COLUMN + j] != null) {
                    extraBallPower[i * BRICKS_COLUMN + j].move(elapsedTime);
                    extraBallPower[i * BRICKS_COLUMN + j].hitPaddle(myPaddleX, SIZE);
                    // after hit the paddle, set to true
                    if (!thereIsExtraPower && extraBallPower[i * BRICKS_COLUMN + j].hit == true) {
                        System.out.println(i * BRICKS_COLUMN + j);
                        thereIsExtraPower = true;
                        extraBallPower[i * BRICKS_COLUMN + j].hit = false;
                        System.out.println("restting boolean");
                    }
                }

                // move sizepower power-ups and check for collisions
                if (sizePower[i * BRICKS_COLUMN + j] != null) {
                    sizePower[i * BRICKS_COLUMN + j].move(elapsedTime);
                    sizePower[i * BRICKS_COLUMN + j].hitPaddle(myPaddleX, SIZE);
                    if (!thereIsSizePower && sizePower[i * BRICKS_COLUMN + j].hit == true) {
                        thereIsSizePower = true;
                    }
                }

                if (myBouncer.getView().getBoundsInLocal().intersects(bricks[i * BRICKS_COLUMN + j].getBoundsInLocal())) {
                    double brickX = bricks[i * BRICKS_COLUMN + j].getBoundsInLocal().getMaxX() / 2 + bricks[i * BRICKS_COLUMN + j].getBoundsInLocal().getMinX() / 2;
                    double drop = Math.random();
                    double prob = Math.random();
                    if (prob < 0.33) {
                        // sizepower power-ups
                        var size_image = new Image(this.getClass().getClassLoader().getResourceAsStream(SIZE_IMAGE));
                        sizePower[i * BRICKS_COLUMN + j] = new SizePower(size_image, brickX, bricks[i * BRICKS_COLUMN + j].getBoundsInParent().getMaxY(), this);
                        root.getChildren().add(sizePower[i * BRICKS_COLUMN + j].getView());
                    }
                    // extra ball-power power-ups
                    else if (prob < 0.66) {
                        var extraPower_image = new Image(this.getClass().getClassLoader().getResourceAsStream(EXTRAPOWER_IMAGE));
                        extraBallPower[i * BRICKS_COLUMN + j] = new ExtraBallPower(extraPower_image, brickX, bricks[i * BRICKS_COLUMN + j].getBoundsInParent().getMaxY(), this);
                        root.getChildren().add(extraBallPower[i * BRICKS_COLUMN + j].getView());
                    }


                    if (!thereIsExtraPower) {
                        if (myBouncer.myBouncerY + myBouncer.myBouncerHeight <= bricks[i * BRICKS_COLUMN + j].getBoundsInLocal().getMinY()
                                || myBouncer.myBouncerY >= bricks[i * BRICKS_COLUMN + j].getBoundsInLocal().getMaxY()) {
                            myBouncer.myVelocity = new Point2D(myBouncer.myVelocity.getX(), -myBouncer.myVelocity.getY());
                            System.out.println("reversed Y");
                        }
                        // else, reverse X
                        else {
                            myBouncer.myVelocity = new Point2D(-myBouncer.myVelocity.getX(), myBouncer.myVelocity.getY());
                            System.out.println("reversed X");
                        }
                    }
                    // eliminate the rectangle
                    bricks[i * BRICKS_COLUMN + j].setWidth(0);
                    bricks[i * BRICKS_COLUMN + j].setHeight(0);
                    bricks[i * BRICKS_COLUMN + j].setX(0.0);
                    bricks[i * BRICKS_COLUMN + j].setY(0.0);
                    root.getChildren().remove(bricks[i * BRICKS_COLUMN + j]);
                }
            }
        }

    }

    // What to do each time a key is pressed
    private void handleKeyInput(KeyCode code) {
        if (code == KeyCode.LEFT) {
            myPaddle.setX(myPaddle.getX() - MOVER_SPEED);
            myPaddleX = myPaddle.getX();
        } else if (code == KeyCode.RIGHT) {
            myPaddle.setX(myPaddle.getX() + MOVER_SPEED);
        } else if (code == KeyCode.SPACE) {
            setLaunch(1);
        }
    }

    public void setRecentlyHit(boolean recentlyHit) {
        this.recentlyHit = recentlyHit;
    }

    public void setLaunch(int launch) {
        this.launch = launch;
    }
}
