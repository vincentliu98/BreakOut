package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Welcome extends Application {
    public static final int SIZE = 700;
    public static final Paint BACKGROUND = Color.BLACK;
    public static final String BOUNCER_IMAGE = "ball.gif";
    public static final String TITLE = "Welcome to BreakOut";

    private Scene myScene;


    public void start(Stage stage) {
        myScene = setupGame(SIZE, SIZE, BACKGROUND);

        stage.setScene(myScene);
        stage.setTitle(TITLE);
        stage.show();

        stage.getScene().setOnKeyPressed(
                event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        new Main().start(stage);
                    }
                }
        );

    }

    private Scene setupGame(int width, int height, Paint background) {
        // create one top level collection to organize the things in the scene
        var root = new Group();
        // create a place to see the shapes
        var scene = new Scene(root, width, height, background);
        // add bouncer and paddle
        Text pressEnter = new Text() {{
            setTranslateX(30);
            setTranslateY(height / 2);
            setFill(Color.WHITE);
            setFont(Font.font(80));
            setText("BreakOut \nVincent Liu");
        }};

        Text Toplay = new Text() {{
            setTranslateX(width / 3 * 2);
            setTranslateY(height * 9 / 10);
            setFill(Color.WHITE);
            setFont(Font.font(20));
            setText("Press Enter to play");
        }};
        //https://www.breakoutedu.com/how-it-worksold/
        ImageView image = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream("Welcome_background.jpg")));
        image.setX(0);
        image.setY(0);
        image.setFitWidth(SIZE);
        image.setFitHeight(SIZE);
        root.getChildren().add(image);
        root.getChildren().add(pressEnter);
        root.getChildren().add(Toplay);
        return scene;

    }

    public static void main(String[] args) {
        launch(args);
    }
}
