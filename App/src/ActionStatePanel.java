import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.control.Label;

import javax.swing.*;
import java.awt.Font;
import java.util.TimerTask;

public class ActionStatePanel {
    private Mediator mediator;

    private Button stopPlay = new Button();
    private Button nextTrack = new Button();
    private Button prevTrack = new Button();
    private Slider volume = new Slider();
    private Slider currentTime = new Slider();
    private ToggleButton shuffle = new ToggleButton();
    private ToggleButton repeat = new ToggleButton();
    private JLabel trackTime = new JLabel();
    private Label trackName = new Label();

    public ActionStatePanel(Mediator mediator) {
        this.mediator = mediator;
        configPlayButtons();
        configFunctionsButtons();
        configSliders();
        configLabels();
    }

    private void configPlayButtons() {
        configCircleButton(stopPlay);
        configCircleButton(nextTrack);
        configCircleButton(prevTrack);

        setListenersForStopPlayButton();
        setListenersForNextTrackButton();
        setListenersForPreviousTrackButton();

        setCircleButtonBack(stopPlay, "play.png");
        setCircleButtonBack(nextTrack, "nextTrack.png");
        setCircleButtonBack(prevTrack, "previousTrack.png");
    }

    private void configFunctionsButtons() {
        configToggleButton(shuffle);
        configToggleButton(repeat);

        shuffle.setLayoutX(310);
        shuffle.setLayoutY(100);
        repeat.setLayoutX(240);
        repeat.setLayoutY(100);

        setToggleButtonBack(shuffle, "shuffle.png");
        setToggleButtonBack(repeat, "repeat.png");
    }

    private void configSliders() {
        volume.setOrientation(Orientation.VERTICAL);
        currentTime.setOrientation(Orientation.HORIZONTAL);
        volume.setPrefSize(20,70);
        volume.setValue(100);
        volume.setMajorTickUnit(10);
        volume.setMinorTickCount(0);
        volume.setLayoutX(370);
        volume.setLayoutY(30);
        volume.valueProperty().addListener(
                (observable, oldValue, newValue) ->
                {
                    int i = newValue.intValue();
                    float newVolume = (float)i / 100;
                    mediator.setNewVolume(newVolume);
                }
        );
        currentTime.setPrefSize(220,30);
        currentTime.setMajorTickUnit(10);
        currentTime.setMinorTickCount(0);
        currentTime.setLayoutX(10);
        currentTime.setLayoutY(70);
        currentTime.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mediator.setNewVolume((float)(currentTime.getValue() / 100));
            }
        });

    }

    private void configLabels() {
        trackTime.setSize(70,30);

        new java.util.Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                trackTime.setText(mediator.getTrackTime());
                double newSliderPosition = mediator.getCurrentSliderPosition();
                currentTime.setValue(newSliderPosition * 100);
            }
        }, 0, 5*100);
        trackTime.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        trackTime.setFont(new java.awt.Font(Font.SERIF, Font.PLAIN, 22));

        trackName.setPrefSize(230, 30);
        trackName.setLayoutX(10);
        trackName.setLayoutY(20);
        trackName.setAlignment(Pos.CENTER);
        trackName.setFont(new javafx.scene.text.Font(18));
    }

    private void setListenersForNextTrackButton() {
        nextTrack.setLayoutX(160);
        nextTrack.setLayoutY(130);
        nextTrack.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mediator.startNextTrack();
            }
        });
    }

    private void setListenersForPreviousTrackButton() {
        prevTrack.setLayoutX(20);
        prevTrack.setLayoutY(130);
        prevTrack.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mediator.startPrevTrack();
            }
        });
    }

    private void setListenersForStopPlayButton() {
        stopPlay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mediator.stopPlayTrack();
            }
        });
        stopPlay.setLayoutX(90);
        stopPlay.setLayoutY(130);
    }

    private void configCircleButton(Button button) {
        button.setShape(new Circle(25));
        button.setMinSize(50,50);
    }

    private void configToggleButton(ToggleButton button) {
        button.setMaxSize(50,50);
    }

    private void setCircleButtonBack(Button button, String picture) {
        Image image = new Image("File:/../pictures/" + picture);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(35);
        imageView.setFitWidth(35);
        button.setGraphic(imageView);
    }

    private void setToggleButtonBack(ToggleButton button, String picture) {
        Image image = new Image("File:/../pictures/" + picture);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        button.setGraphic(imageView);
    }

    public boolean isShuffleTurnedOn() {
        return shuffle.isSelected();
    }

    public boolean isRepeatTurnedOn() {
        return repeat.isSelected();
    }

    public Button getNextTrack() {
        return nextTrack;
    }

    public Button getPrevTrack() {
        return prevTrack;
    }

    public Button getStopPlay() {
        return stopPlay;
    }

    public ToggleButton getRepeat() {
        return repeat;
    }

    public ToggleButton getShuffle() {
        return shuffle;
    }

    public Slider getCurrentTime() {
        return currentTime;
    }

    public Slider getVolume() {
        return volume;
    }

    public JLabel getTrackTime() {
        return trackTime;
    }

    public Label getTrackName() {
        return trackName;
    }

    public void setPlayImage() {
        setCircleButtonBack(stopPlay,"play.png");
    }

    public void setStopImage() {
        setCircleButtonBack(stopPlay, "stop.png");
    }

    public void setTrackName(String trackName) {
        this.trackName.setText(trackName);
    }
}
