import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class MainWindow extends Application {
    private Mediator mediator = new Mediator();

    public void start(Stage primaryStage) {
        try {
            BorderPane root = new BorderPane();
            Scene scene = new Scene(root, 400, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("EnjoyPlayer");
            primaryStage.show();

            Button someButton = new Button();
            someButton.setMinSize(50,50);
            someButton.setLayoutX(200);
            someButton.setLayoutY(200);

            Pane somePane = new Pane();
            somePane.setPrefSize(400,400);
            somePane.getChildren().add(mediator.getActStatePanel().getNextTrack());
            somePane.getChildren().add(mediator.getActStatePanel().getStopPlay());
            somePane.getChildren().add(mediator.getActStatePanel().getPrevTrack());
            somePane.getChildren().add(mediator.getActStatePanel().getVolume());
            somePane.getChildren().add(mediator.getActStatePanel().getCurrentTime());
            somePane.getChildren().add(mediator.getActStatePanel().getShuffle());
            somePane.getChildren().add(mediator.getActStatePanel().getRepeat());

            root.getChildren().add(somePane);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
