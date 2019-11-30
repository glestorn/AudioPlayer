import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class ActionStatePanel {
    private Mediator mediator;

    private Button stopPlay = new Button();
    private Button nextTrack = new Button();
    private Button prevTrack = new Button();
    private Slider volume = new Slider();
    private Slider currentTime = new Slider();
    private ToggleButton shuffle = new ToggleButton();
    private ToggleButton repeat = new ToggleButton();

    private boolean playStatus = false;

    public ActionStatePanel(Mediator mediator) {
        this.mediator = mediator;
        configPlayButtons();
        configFunctionsButtons();
        configSliders();
    }

    private void configPlayButtons() {
        configCircleButton(stopPlay);
        configCircleButton(nextTrack);
        configCircleButton(prevTrack);

        setListenersForStopPlayButton();
        setListenersForNextTrackButton();
        setListenersForPreviousTrackButton();

        setCircleButtonBack(stopPlay, "stop.png");
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
//        volume.setPrefHeight(70);
//        volume.setShowTickMarks(true);
        volume.setPrefSize(20,70);
        volume.setMajorTickUnit(10);
        volume.setMinorTickCount(0);
//        volume.setShowTickLabels(true);
        volume.setLayoutX(370);
        volume.setLayoutY(30);
        volume.valueProperty().addListener(
                (observable, oldValue, newValue) ->
                {
                    int i = newValue.intValue();
                    System.out.println("Volume is : " + i);
                }
        );
        currentTime.setPrefSize(220,30);
//        currentTime.setShowTickMarks(true);
        currentTime.setMajorTickUnit(10);
        currentTime.setMinorTickCount(0);
//        currentTime.setShowTickLabels(true);
        currentTime.setLayoutX(10);
        currentTime.setLayoutY(60);
        currentTime.valueProperty().addListener(
                (observable, oldValue, newValue) ->
                {
                    int i = newValue.intValue();
                    System.out.println("CurrentTime is :" + i);
                }
        );

    }

    private void setListenersForNextTrackButton() {
        nextTrack.setLayoutX(160);
        nextTrack.setLayoutY(130);
        nextTrack.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Next track button was clicked!!!");
            }
        });
    }

    private void setListenersForPreviousTrackButton() {
        prevTrack.setLayoutX(20);
        prevTrack.setLayoutY(130);
        prevTrack.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Previous track button was clicked!!!");
            }
        });
    }

    private void setListenersForStopPlayButton() {
        stopPlay.setLayoutX(90);
        stopPlay.setLayoutY(130);
        stopPlay.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mediator.stopPlayTrack();
                if (playStatus) {
                    playStatus = false;
                    setCircleButtonBack(stopPlay, "play.png");
                    System.out.println("Now it's playing");
                }
                else {
                    playStatus = true;
                    setCircleButtonBack(stopPlay, "stop.png");
                    System.out.println("Now it's stopping");
                }
            }
        });
    }

    private void configCircleButton(Button button) {
        button.setShape(new Circle(25));
        button.setMinSize(50,50);
    }

    private void configToggleButton(ToggleButton button) {
        button.setMinSize(50,50);
    }

    private void setCircleButtonBack(Button button, String picture) {
        Image image = new Image("File:/..pictures/" + picture);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(35);
        imageView.setFitWidth(35);
        button.setGraphic(imageView);
    }

    private void setToggleButtonBack(ToggleButton button, String picture) {
        Image image = new Image("File:/..pictures/" + picture);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(35);
        imageView.setFitWidth(35);
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
}
