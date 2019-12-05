import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainWindow extends Application {
    private Mediator mediator;

    public void start(Stage primaryStage) {
        try {
            BorderPane root = new BorderPane();
            root.setLayoutX(0);
            root.setLayoutY(0);
            Scene scene = new Scene(root, 400, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("EnjoyPlayer");
            primaryStage.show();

            mediator = new Mediator(root, scene);

            Label borderForTimer = new Label();
            borderForTimer.setLayoutX(261);
            borderForTimer.setLayoutY(26);
            borderForTimer.setPrefSize(67,45);
            borderForTimer.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                new BorderWidths(4, 4, 4, 4))));
            SwingNode trackTime = new SwingNode();
            trackTime.setContent(mediator.getActStatePanel().getTrackTime());
            trackTime.setLayoutX(265);
            trackTime.setLayoutY(30);

            Pane somePane = new Pane();
            somePane.setPrefSize(400,600);
//            somePane.setBorder(new Border(new BorderStroke(Color.BLACK,
//                BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
//                new BorderWidths(4, 4, 4, 4))));
            somePane.getChildren().add(mediator.getActStatePanel().getNextTrack());
            somePane.getChildren().add(mediator.getActStatePanel().getStopPlay());
            somePane.getChildren().add(mediator.getActStatePanel().getPrevTrack());
            somePane.getChildren().add(mediator.getActStatePanel().getVolume());
            somePane.getChildren().add(mediator.getActStatePanel().getCurrentTime());
            somePane.getChildren().add(mediator.getActStatePanel().getShuffle());
            somePane.getChildren().add(mediator.getActStatePanel().getRepeat());
            somePane.getChildren().add(borderForTimer);
            somePane.getChildren().add(trackTime);
            somePane.getChildren().add(mediator.getPlaylistManager().getConfigButtonsBox());
//            somePane.getChildren().add(mediator.getPlaylistManager().getBoxForTracksButtons());
            somePane.getChildren().add(mediator.getPlaylistManager().getScrollPane());
//            somePane.getChildren().add(mediator.getActStatePanel().getTrackTime());

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
