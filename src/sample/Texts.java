package sample;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Texts {
    public final int SIZE = 700;
    public final int TOP_CAPTION_FONT = 15;
    public final int WIN_LOSE_FONT = 1100;
    public final int RIGHT_MARGIN = 80;
    public final int LEFT_MARGIN = 20;
    public final int GAME_LEVEL = 6;
    private Text life;
    private Text level;
    private Text score;
    protected Main context;

    public Texts(Main context) {
        this.context = context;
        // add captions on the top
        level = new Text() {{
            setTranslateX(LEFT_MARGIN);
            setTranslateY(LEFT_MARGIN);
            setFill(Color.BLACK);
            setFont(Font.font(TOP_CAPTION_FONT));
            setText("Level: " + context.getCurrent_level() + "/" + GAME_LEVEL);
        }};

        life = new Text() {{
            setTranslateX(SIZE / 2 - LEFT_MARGIN);
            setTranslateY(LEFT_MARGIN);
            setFill(Color.BLACK);
            setFont(Font.font(TOP_CAPTION_FONT));
            setText("Life: " + context.getCurrent_life() + "/" + GAME_LEVEL);
        }};

        score = new Text() {{
            setTranslateX(SIZE - RIGHT_MARGIN);
            setTranslateY(LEFT_MARGIN);
            setFill(Color.BLACK);
            setFont(Font.font(TOP_CAPTION_FONT));
            setText("Score: " + context.getCurrent_score());
        }};

    }

    public void initialize() {
        context.getRoot().getChildren().addAll(life, level, score);
    }

    public void updateTexts() {
        level.setText("Level: " + context.getCurrent_level() + "/" + GAME_LEVEL);
        score.setText("Score: " + context.getCurrent_score());
        life.setText("Life: " + context.getCurrent_life() + "/" + GAME_LEVEL);
    }

    public void displayWinLose() {
        if (context.getIsWon()) {
            Text win = new Text() {{
                setTranslateX(SIZE / 8);
                setTranslateY(SIZE / 2);
                setFill(Color.BLACK);
                setFont(Font.font(WIN_LOSE_FONT));
                setText("YOU WON!\n" + "Score: " + context.getCurrent_score());
            }};
            context.getRoot().getChildren().add(win);
            context.getAnimation().stop();
        }

        // lose text
        if (context.getCurrent_life() <= 0) {
            Text fail = new Text() {{
                setTranslateX(SIZE / 8);
                setTranslateY(SIZE / 2);
                setFill(Color.BLACK);
                setFont(Font.font(WIN_LOSE_FONT));
                setText("YOU LOSE!");
            }};
            context.getRoot().getChildren().add(fail);
            context.getAnimation().stop();
        }
    }
}
