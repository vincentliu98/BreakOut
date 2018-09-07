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

import java.util.ArrayList;
import java.util.Scanner;


public class Main extends Application {
    // final says the variable cannot be reassigned
    // static makes the variable global, only 1 instance of that constant will be used. For efficiency
    public static final int SIZE = 700;
    public static final int FRAMES_PER_SECOND = 60;
    public double bouncer_scale = 1;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final int MOVER_SPEED = 80;
    public static final int BRICKS_COLUMN = 8;
    public static final int GAME_LIFE = 3;
    public static final int GAME_LEVEL = 3;
    public static final String TITLE = "Vincent's BreakOut";
    public static final String LEVEL_1 = "level_1.txt";
    public static final String LEVEL_2 = "level_2.txt";
    public static final String LEVEL_3 = "level_3.txt";
    public static final Paint BACKGROUND = Color.WHITE;
    public static final String BOUNCER_IMAGE = "ball.gif";
    public static final String SIZE_IMAGE = "sizepower.gif";
    public static final String EXTRAPOWER_IMAGE = "extraballpower.gif";

    public static final double PADDLE_WIDTH = 80;
    public static final double PADDLE_HEIGHT = 20;
    public double power_timelimit = 8.0;
    public double size_timelimit = 8.0;
    public ArrayList<SizePower> sizePower;
    public ArrayList<ExtraBallPower> extraBallPower;
    public int current_life = 3;
    public int current_level = 1;
    public int current_score = 0;
    public double myPaddleX;
    public ArrayList<Rectangle> bricks;
    public ArrayList<Integer> bricksLife;
    public int BRICKS_NUM = 0;
    public boolean recentlyHit = false;
    public boolean thereIsExtraPower = false;
    public boolean thereIsSizePower = false;
    private Scene myScene;
    public Timeline animation;
    public Rectangle myPaddle;
    private Bouncer myBouncer;
    public int launch = 0;
    public Group root;
    public Text life;
    public Text level;
    public Text score;

    /**
     * Initialize what will be displayed and how it will be updated.
     */
    @Override
    public void start(Stage stage) {
        myScene = setupGame(SIZE, SIZE, BACKGROUND, stage);
        stage.setScene(myScene);
        stage.setTitle(TITLE);
        stage.show();
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY, stage));
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    // Create the game's "scene": what shapes will be in the game and their starting properties
    private Scene setupGame(int width, int height, Paint background, Stage stage) {
        // create one top level collection to organize the things in the scene
        root = new Group();
        // create a place to see the shapes
        var scene = new Scene(root, width, height, background);
        // add bouncer and paddle
        var ball_image = new Image(this.getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));
        // change speed
        switch (current_level) {
            case 1:
                myBouncer = new Bouncer(ball_image, -150, bouncer_scale,this);
            case 2:
                myBouncer = new Bouncer(ball_image, -200, bouncer_scale,this);
            case 3:
                myBouncer = new Bouncer(ball_image, -300, bouncer_scale,this);
        }

        myPaddle = new Rectangle(width / 2 - PADDLE_WIDTH, height - PADDLE_HEIGHT, PADDLE_WIDTH, PADDLE_HEIGHT);
        myPaddle.setFill(Color.GRAY);
        // position the elements
        root.getChildren().add(myBouncer.getView());
        root.getChildren().add(myPaddle);

        // construct and add bricks
        Scanner input = null;
        switch (current_level) {
            case 1:
                input = new Scanner(this.getClass().getClassLoader().getResourceAsStream(LEVEL_1));
                break;
            case 2:
                input = new Scanner(this.getClass().getClassLoader().getResourceAsStream(LEVEL_2));
                break;
            case 3:
                input = new Scanner(this.getClass().getClassLoader().getResourceAsStream(LEVEL_3));
                break;
        }
        int count = 0;
        bricks = new ArrayList<>();
        bricksLife = new ArrayList<>();
        while (input.hasNext()) {
            int temp = input.nextInt();
            if (temp != 1) {
                Rectangle b = new Rectangle(
                        20 + count % BRICKS_COLUMN * 80,
                        40 + Math.round(count / BRICKS_COLUMN) * 90,
                        80,
                        40
                );
                switch (temp) {
                    case 2:
                        b.setFill(Color.YELLOW);
                        break;
                    case 3:
                        b.setFill(Color.BLUE);
                        break;
                    case 4:
                        b.setFill(Color.RED);
                        break;
                }
                bricks.add(b);
                bricksLife.add(temp - 1);
                root.getChildren().add(b);
            }
            count++;
        }

        input.reset();
        while (input.hasNext()) {
            int temp1 = input.nextInt();
            System.out.print(temp1);
            if (temp1 != 1) {
                BRICKS_NUM++;
            }
        }


        // set up power-ups
        extraBallPower = new ArrayList<ExtraBallPower>();
        sizePower = new ArrayList<SizePower>();

        // add captions on the top
        level = new Text() {{
            setTranslateX(10);
            setTranslateY(20);
            setFill(Color.BLACK);
            setFont(Font.font(15));
            setText("Level: " + current_level + "/" + GAME_LEVEL);
        }};

        life = new Text() {{
            setTranslateX(10 + width / 2);
            setTranslateY(20);
            setFill(Color.BLACK);
            setFont(Font.font(15));
            setText("Life: " + current_life + "/" + GAME_LIFE);
        }};

        score = new Text() {{
            setTranslateX(width - 100);
            setTranslateY(20);
            setFill(Color.BLACK);
            setFont(Font.font(15));
            setText("Score: " + current_score);
        }};

        root.getChildren().addAll(life, level, score);

        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode(), stage));
        return scene;
    }

    // Change properties of shapes to animate them
    // Note, there are more sophisticated ways to animate shapes, but these simple ways work fine to start.
    private void step(double elapsedTime, Stage stage) {
        level.setText("Level: " + current_level + "/" + GAME_LEVEL);
        score.setText("Score: " + current_score);
        life.setText("Life: " + current_life + "/" + GAME_LIFE);

        // win text
        if (bricks.size() == 0 && current_level == 3 && current_life > 0) {
            Text win = new Text() {{
                setTranslateX(SIZE / 8);
                setTranslateY(SIZE / 2);
                setFill(Color.BLACK);
                setFont(Font.font(110));
                setText("YOU WON!");
            }};
            root.getChildren().add(win);
        }

        // lose text
        if (current_life <= 0) {
            Text fail = new Text() {{
                setTranslateX(SIZE / 8);
                setTranslateY(SIZE / 2);
                setFill(Color.BLACK);
                setFont(Font.font(110));
                setText("YOU LOSE!");
            }};
            root.getChildren().add(fail);
        }

        // make the bouncers move
        myBouncer.move(elapsedTime);
        // make the bouncers bounce
        myBouncer.bounceWall(myScene.getWidth(), stage);

        // deal with bouncing off the paddle
        if (myBouncer.getView().getBoundsInLocal().intersects(myPaddle.getBoundsInLocal())) {
            setRecentlyHit(true);
            myBouncer.myVelocity = new Point2D(myBouncer.myVelocity.getX(), -myBouncer.myVelocity.getY());
        }

        // if it's within the time limit, then decrease timelimit
        if (thereIsExtraPower) {
            power_timelimit -= SECOND_DELAY;
        }
        if (thereIsSizePower) {
            size_timelimit -= SECOND_DELAY;
        }

        // when the time limit runs out, invalidate power-up
        if (power_timelimit <= 0) {
            thereIsExtraPower = false;
            power_timelimit = 8.0;
        }
        if (size_timelimit <= 0) {
            thereIsSizePower = false;
            size_timelimit = 8.0;
        }

        // move extraballpower power-ups and check for collisions
        for (int i = 0; i < extraBallPower.size(); i++) {
            if (extraBallPower.get(i) != null) {
                extraBallPower.get(i).move(elapsedTime);
                extraBallPower.get(i).hitPaddle(myPaddleX, SIZE);
                // after hit the paddle, set to true
                if (!thereIsExtraPower && extraBallPower.get(i).hit == true) {
                    System.out.println(i);
                    thereIsExtraPower = true;
                    extraBallPower.get(i).hit = false;
                    System.out.println("restting boolean");
                }
            }
        }

        // move sizepower power-ups and check for collisions
        for (int i = 0; i < sizePower.size(); i++) {
            if (sizePower.get(i) != null) {
                sizePower.get(i).move(elapsedTime);
                sizePower.get(i).hitPaddle(myPaddleX, SIZE);
                if (!thereIsSizePower && sizePower.get(i).hit == true) {
                        thereIsSizePower = true;
                }
                if (! thereIsSizePower) {
                    myPaddle.setWidth(PADDLE_WIDTH);
                    myPaddle.setHeight(PADDLE_HEIGHT);
                }
            }
        }

        // deal with bouncing off the bricks
        for (int i = 0; i < bricks.size(); i++) {
            if (myBouncer.getView().getBoundsInLocal().intersects(bricks.get(i).getBoundsInLocal())) {
                double brickX = bricks.get(i).getBoundsInLocal().getMaxX() / 2 + bricks.get(i).getBoundsInLocal().getMinX() / 2;
                double prob = Math.random();
                switch (bricksLife.get(i) + 1) {
                    case 2:
                        current_score += 60;
                        break;
                    case 3:
                        current_score += 50;
                        break;
                    case 4:
                        current_score += 40;
                        break;
                }
                bricksLife.set(i, bricksLife.get(i) - 1);
                switch (bricksLife.get(i) + 1) {
                    case 2:
                        bricks.get(i).setFill(Color.YELLOW);
                        break;
                    case 3:
                        bricks.get(i).setFill(Color.BLUE);
                        break;
                }
                if (bricksLife.get(i) == 0) {
                    if (prob < 0.1) {
                        // sizepower power-ups
                        var size_image = new Image(this.getClass().getClassLoader().getResourceAsStream(SIZE_IMAGE));
                        sizePower.add(new SizePower(size_image, brickX, bricks.get(i).getBoundsInParent().getMaxY(), this));
                        root.getChildren().add(sizePower.get(sizePower.size() - 1).getView());
                    }
                    // extra ball-power power-ups
                    else if (prob < 0.2) {
                        var extraPower_image = new Image(this.getClass().getClassLoader().getResourceAsStream(EXTRAPOWER_IMAGE));
                        extraBallPower.add(new ExtraBallPower(extraPower_image, brickX, bricks.get(i).getBoundsInParent().getMaxY(), this));
                        root.getChildren().add(extraBallPower.get(extraBallPower.size() - 1).getView());
                    }
                    // eliminate the rectangle when no life
                    bricks.get(i).setWidth(0);
                    bricks.get(i).setHeight(0);
                    bricks.get(i).setX(0.0);
                    bricks.get(i).setY(0.0);
                    root.getChildren().remove(bricks.get(i));
                }


                if (!thereIsExtraPower) {
                    if (myBouncer.myBouncerY + myBouncer.myBouncerHeight <= bricks.get(i).getBoundsInLocal().getMinY()
                            || myBouncer.myBouncerY >= bricks.get(i).getBoundsInLocal().getMaxY()) {
                        myBouncer.myVelocity = new Point2D(myBouncer.myVelocity.getX(), -myBouncer.myVelocity.getY());
                        System.out.println("reversed Y");
                    }
                    // else, reverse X
                    else {
                        myBouncer.myVelocity = new Point2D(-myBouncer.myVelocity.getX(), myBouncer.myVelocity.getY());
                        System.out.println("reversed X");
                    }
                }
            }
        }

    }

    // What to do each time a key is pressed
    private void handleKeyInput(KeyCode code, Stage stage) {
        if (code == KeyCode.LEFT) {
            myPaddle.setX(myPaddle.getX() - MOVER_SPEED);
            myPaddleX = myPaddle.getX();
        } else if (code == KeyCode.RIGHT) {
            myPaddle.setX(myPaddle.getX() + MOVER_SPEED);
        } else if (code == KeyCode.SPACE) {
            setLaunch(1);
        } else if (code == KeyCode.L) {
            current_life++;
        } else if (code == KeyCode.E){
            myBouncer.getView().setScaleX(bouncer_scale * 1.5);
            myBouncer.getView().setScaleY(bouncer_scale * 1.5);
        }else if (code == KeyCode.R) {
            animation.stop();
            start(stage);
        }else if (code == KeyCode.DIGIT1) {
            current_level = 1;
            launch = 0;
            animation.stop();
            start(stage);
        } else if (code == KeyCode.DIGIT2) {
            current_level = 2;
            launch = 0;
            animation.stop();
            start(stage);
        } else if (code == KeyCode.DIGIT3) {
            current_level = 3;
            launch = 0;
            animation.stop();
            start(stage);
        }
    }

    public void setRecentlyHit(boolean recentlyHit) {
        this.recentlyHit = recentlyHit;
    }

    public void setLaunch(int launch) {
        this.launch = launch;
    }
}
