package sample;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Application {
    // final says the variable cannot be reassigned
    // static makes the variable global, only 1 instance of that constant will be used. For efficiency
    public final int SIZE = 700;
    public final int FRAMES_PER_SECOND = 60;
    public double bouncer_scale = 1;
    public final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public final int MOVER_SPEED = 80;
    public final int BRICKS_COLUMN = 8;
    public final int GAME_LEVEL = 6;
    public final int GAME_LIFE = 3;
    public final int LEVEL_1_SPEED = -150;
    public final int LEVEL_2_SPEED = -200;
    public final int LEVEL_3_SPEED = -300;
    public final double PADDLE_WIDTH = 80;
    public final double PADDLE_HEIGHT = 20;
    public final double TIME_LIMIT = 8.0;
    public final int LEFT_MARGIN = 20;
    public final int TOP_MARGIN = 60;
    public final int BRICK_WIDTH = 80;
    public final int BRICK_HEIGHT = 40;
    public final int LIFE_3_SCORE = 40;
    public final int LIFE_2_SCORE = 50;
    public final int LIFE_1_SCORE = 60;
    // the probability of dropping power-ups, less than 1/Number of power-ups
    public final double POWER_UP_PROB = 0.1;
    public final double BALL_EXPAND = 1.5;
    public final String TITLE = "Vincent's BreakOut";
    public final String LEVEL_1 = "level_1.txt";
    public final String LEVEL_2 = "level_2.txt";
    public final String LEVEL_3 = "level_3.txt";
    public final String BOUNCER_IMAGE = "ball.gif";
    public final String SIZE_IMAGE = "sizepower.gif";
    public final String EXTRA_POWER_IMAGE = "extraballpower.gif";
    public final String POINTS_IMAGE = "pointspower.gif";
    public final String GAME_BACKGROUND = "game_background.jpg";

    private int bricks_num = 0;
    private int current_life = 3;
    private int current_level = 1;
    private int current_score = 0;
    private double power_time_limit = 8.0;
    private double size_time_limit = 8.0;
    private double myPaddleX;
    private boolean isNext = false;
    private boolean bounceWeird = true;
    private boolean thereIsExtraPower = false;
    private boolean thereIsSizePower = false;
    private boolean launch = false;
    private boolean recentlyHit = false;
    private boolean isWon = false;
    private ArrayList<SizePower> sizePower;
    private ArrayList<ExtraBallPower> extraBallPower;
    private ArrayList<PointsPower> pointsPower;
    private ArrayList<Rectangle> bricks;
    private ArrayList<Integer> bricksLife;
    private Scene myScene;
    private Scene scene;
    private Timeline animation;
    public Rectangle myPaddle;
    private Bouncer myBouncer;
    private Texts myText;
    private Group root;

    /**
     * Initialize what will be displayed and how it will be updated.
     */
    @Override
    public void start(Stage stage) {
        myScene = setupGame(SIZE, SIZE, stage);
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
    private Scene setupGame(int width, int height, Stage stage) {
        // create one top level collection to organize the things in the scene
        root = new Group();
        var background_image = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream(GAME_BACKGROUND)));
        background_image.setFitHeight(SIZE);
        background_image.setFitWidth(SIZE);
        root.getChildren().add(background_image);
        // create a place to see the shapes
        scene = new Scene(root, width, height);
        // add bouncer and paddle
        var ball_image = new Image(this.getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));
        Scanner input = null;
        // construct bouncers and bricks and add bricks
        switch (current_level) {
            case 1:
            case 4:
                myBouncer = new Bouncer(ball_image, LEVEL_1_SPEED, bouncer_scale, this);
                input = new Scanner(this.getClass().getClassLoader().getResourceAsStream(LEVEL_1));
                break;
            case 2:
            case 5:
                myBouncer = new Bouncer(ball_image, LEVEL_2_SPEED, bouncer_scale, this);
                input = new Scanner(this.getClass().getClassLoader().getResourceAsStream(LEVEL_2));
                break;
            case 3:
            case 6:
                myBouncer = new Bouncer(ball_image, LEVEL_3_SPEED, bouncer_scale, this);
                input = new Scanner(this.getClass().getClassLoader().getResourceAsStream(LEVEL_3));
                break;
        }
        int count = 0;
        // initialize arrayList
        extraBallPower = new ArrayList<>();
        sizePower = new ArrayList<>();
        pointsPower = new ArrayList<>();
        bricks = new ArrayList<>();
        bricksLife = new ArrayList<>();
        while (input.hasNext()) {
            int temp = input.nextInt();
            if (temp != 1) {
                Rectangle b = new Rectangle(
                        LEFT_MARGIN + count % BRICKS_COLUMN * 80,
                        TOP_MARGIN + Math.round(count / BRICKS_COLUMN) * 70,
                        BRICK_WIDTH,
                        BRICK_HEIGHT
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
            if (temp1 != 1) {
                setBricks_num(bricks_num + 1);
            }
        }
        // add paddle
        myPaddle = new Rectangle(width / 2 - PADDLE_WIDTH, height - PADDLE_HEIGHT, PADDLE_WIDTH, PADDLE_HEIGHT);
        myPaddle.setFill(Color.GRAY);
        root.getChildren().add(myPaddle);
        // add bouncer
        root.getChildren().add(myBouncer.getView());
        // initialize texts
        myText = new Texts(this);
        myText.initialize();
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode(), stage));
        return scene;
    }

    private void step(double elapsedTime, Stage stage) {
        // update texts and display final text when the user wins or loses
        myText.updateTexts();
        myText.displayWinLose();

        if (isNext) {
            current_level++;
            isNext = false;
            launch = false;
            start(stage);
        }
        myBouncer.move(elapsedTime);
        myBouncer.bounceWall(myScene.getWidth(), stage);
        myBouncer.bouncePaddle();
        // move paddle across screen
        if (myPaddleX < 0) {
            myPaddle.setX(SIZE - myPaddle.getBoundsInLocal().getWidth());
            myPaddleX = myPaddle.getX();
        } else if (myPaddleX >= SIZE) {
            myPaddle.setX(0);
            myPaddleX = myPaddle.getX();
        }

        // move extraballpower power-ups and check for collisions
        for (int i = 0; i < extraBallPower.size(); i++) {
            if (extraBallPower != null) {
                extraBallPower.get(i).move(elapsedTime);
                extraBallPower.get(i).hitPaddle(myPaddleX, SIZE);
                extraBallPower.get(i).updatePowerState(thereIsExtraPower, power_time_limit, SECOND_DELAY, TIME_LIMIT);
                extraBallPower.get(i).checkHit(thereIsExtraPower, extraBallPower.get(i));
            }
        }

        // move sizepower power-ups and check for collisions
        for (int i = 0; i < sizePower.size(); i++) {
            if (sizePower.get(i) != null) {
                sizePower.get(i).move(elapsedTime);
                sizePower.get(i).hitPaddle(myPaddleX, SIZE);
                sizePower.get(i).updatePowerState(thereIsSizePower, size_time_limit, SECOND_DELAY, TIME_LIMIT);
                sizePower.get(i).checkHit(thereIsSizePower, sizePower.get(i), myPaddle);
            }
        }

        // move pointspower power-ups and check for collisions
        for (int i = 0; i < pointsPower.size(); i++) {
            pointsPower.get(i).move(elapsedTime);
            pointsPower.get(i).hitPaddle(myPaddleX, SIZE);
            pointsPower.get(i).checkHit(pointsPower.get(i));
        }

        int count = 0;
        // deal with bouncing off the bricks
        for (int i = 0; i < bricks.size(); i++) {
            // win text
            if (bricks.get(i).getX() == 0) {
                count++;
            }
            if (count == bricks.size() && current_level != GAME_LEVEL) {
                isNext = true;
            } else if (count == bricks.size() && current_level == GAME_LEVEL) isWon = true;


            if (myBouncer.getView().getBoundsInLocal().intersects(bricks.get(i).getBoundsInLocal())) {
                double brickX = bricks.get(i).getBoundsInLocal().getMaxX() / 2 + bricks.get(i).getBoundsInLocal().getMinX() / 2;
                double prob = Math.random();
                switch (bricksLife.get(i) + 1) {
                    case 2:
                        current_score += LIFE_1_SCORE;
                        break;
                    case 3:
                        current_score += LIFE_2_SCORE;
                        break;
                    case 4:
                        current_score += LIFE_3_SCORE;
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
                // drop power-ups when the brick is about to disappear
                if (bricksLife.get(i) == 0) {
                    if (prob < POWER_UP_PROB) {
                        // sizepower power-ups
                        var size_image = new Image(this.getClass().getClassLoader().getResourceAsStream(SIZE_IMAGE));
                        sizePower.add(new SizePower(size_image, brickX, bricks.get(i).getBoundsInParent().getMaxY(), this));
                        root.getChildren().add(sizePower.get(sizePower.size() - 1).getView());
                    }
                    // extra ball-power power-ups
                    else if (prob < 2 * POWER_UP_PROB) {
                        var extraPower_image = new Image(this.getClass().getClassLoader().getResourceAsStream(EXTRA_POWER_IMAGE));
                        extraBallPower.add(new ExtraBallPower(extraPower_image, brickX, bricks.get(i).getBoundsInParent().getMaxY(), this));
                        root.getChildren().add(extraBallPower.get(extraBallPower.size() - 1).getView());
                    } else if (prob < 3 * POWER_UP_PROB) {
                        var pointsPower_image = new Image(this.getClass().getClassLoader().getResourceAsStream(POINTS_IMAGE));
                        pointsPower.add(new PointsPower(pointsPower_image, brickX, bricks.get(i).getBoundsInParent().getMaxY(), this));
                        root.getChildren().add(pointsPower.get(pointsPower.size() - 1).getView());
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
                        myBouncer.setMyVelocity(new Point2D(myBouncer.getMyVelocity().getX(), -myBouncer.getMyVelocity().getY()));
                    }
                    // else, reverse X
                    else {
                        myBouncer.setMyVelocity(new Point2D(-myBouncer.getMyVelocity().getX(), myBouncer.getMyVelocity().getY()));
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
            myPaddleX = myPaddle.getX();
        } else if (code == KeyCode.SPACE) {
            setLaunch(launch);
        } else if (code == KeyCode.L) {
            current_life++;
        } else if (code == KeyCode.N) {
            bounceWeird = !bounceWeird;
        } else if (code == KeyCode.E) {
            myBouncer.getView().setScaleX(bouncer_scale * BALL_EXPAND);
            myBouncer.getView().setScaleY(bouncer_scale * BALL_EXPAND);
        } else if (code == KeyCode.R) {
            animation.stop();
            start(stage);
        } else if (code == KeyCode.DIGIT1) {
            current_level = 1;
            animation.stop();
            start(stage);
        } else if (code == KeyCode.DIGIT2) {
            current_level = 2;
            animation.stop();
            start(stage);
        } else if (code == KeyCode.DIGIT3) {
            current_level = 3;
            animation.stop();
            start(stage);
        } else if (code == KeyCode.DIGIT4) {
            current_level = 4;
            animation.stop();
            start(stage);
        } else if (code == KeyCode.DIGIT5) {
            current_level = 5;
            animation.stop();
            start(stage);
        } else if (code == KeyCode.DIGIT6) {
            current_level = 6;
            animation.stop();
            start(stage);
        }
    }


    public boolean getBounceWeird() {
        return bounceWeird;
    }

    public boolean getRecentlyHit() {
        return recentlyHit;
    }

    public void setRecentlyHit(boolean recentlyHit) {
        this.recentlyHit = !recentlyHit;
    }

    public boolean getLaunch() {
        return launch;
    }

    public void setLaunch(boolean launch) {
        this.launch = !launch;
    }

    public void setBricks_num(int bricks_num) {
        this.bricks_num = bricks_num;
    }

    public int getCurrent_life() {
        return current_life;
    }

    public void setCurrent_life(int current_life) {
        this.current_life = current_life;
    }

    public int getCurrent_score() {
        return current_score;
    }

    public void setCurrent_score(int current_score) {
        this.current_score = current_score;
    }

    public int getCurrent_level() {
        return current_level;
    }

    public Group getRoot() {
        return root;
    }

    public Timeline getAnimation() {
        return animation;
    }

    public boolean getIsWon() {
        return isWon;
    }

    public void setThereIsExtraPower(boolean thereIsExtraPower) {
        this.thereIsExtraPower = thereIsExtraPower;
    }

    public void setPower_time_limit(double power_time_limit) {
        this.power_time_limit = power_time_limit;
    }

    public void setThereIsSizePower(boolean thereIsSizePower) {
        this.thereIsSizePower = thereIsSizePower;
    }

    public void setSize_time_limit(double size_time_limit) {
        this.size_time_limit = size_time_limit;
    }
}
