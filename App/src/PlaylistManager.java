import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlaylistManager {
    private Mediator mediator;
    private HBox configButtonsBox = new HBox();
    private VBox boxForTracksButtons = new VBox();
    private Slider vboxSlider = new Slider();
    private PlaylistButton[] playlistsButtons;
    private Button addPlaylistButton = new Button();
    private Button nextPlaylistButton = new Button();
    private Button previousPlaylistButton = new Button();
    private ArrayList<TrackButton> tracks = new ArrayList<>();
    private ArrayList<Playlist> playlists = new ArrayList<>();
    private BorderPane root;
    private Playlist currentPlaylist = null;
    //todo check currentPlaylist - it doesn't work
//    private Scene scene;
    private PlaylistButton currentButton;

    public PlaylistManager(Mediator mediator, BorderPane root, Scene scene) {
        this.mediator = mediator;
//        this.root = root;
//        this.scene = scene;

//        Button track1 = new Button("1st Track");

//        track1.setBorder(new Border(new BorderStroke(Color.BLACK,
//                BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
//                new BorderWidths(0, 0, 0, 0))));
//        track1.setStyle("-fx-border-width: 0;" +
//                "-fx-background-color: white");
//        track1.setMinWidth(400);
//        tracks.add(track1);
//        tracks.add(new Button("Hello world"));
//        tracks.add(new Button("How are you"));

        playlistsButtons = new PlaylistButton[4];
        System.out.println(playlistsButtons.length);
        for (int i = 0; i < playlistsButtons.length; i++) {
            System.out.println(i);
            playlistsButtons[i] = new PlaylistButton();
            playlistsButtons[i].setDisable(true);
            playlistsButtons[i].playlist = null;
            playlistsButtons[i].setMinSize(80, 30);
            playlistsButtons[i].setMaxSize(80,30);

            playlistsButtons[i].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getButton().equals(MouseButton.PRIMARY)) {
                        currentPlaylist = ((PlaylistButton)(event.getSource())).playlist;
                        boxForTracksButtons.getChildren().clear();
                        ArrayList<TrackButton> tracks = ((PlaylistButton)(event.getSource())).
                                playlist.getTracks();
                        for (TrackButton track : tracks) {
                            track.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {
                                    mediator.startNewTrack(((TrackButton)(event.getSource())).path);
                                }
                            });
                            boxForTracksButtons.getChildren().add(track);
                        }
                    }
                    else if (event.getButton().equals(MouseButton.SECONDARY)) {
                        currentButton = ((PlaylistButton)(event.getSource()));
                        Stage popupWindow = new Stage();
                        popupWindow.setResizable(false);

                        Button addFiles = new Button();
                        addFiles.setPrefSize(100,15);
                        addFiles.setText("Add Files");
                        addFiles.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                addFiles();
                                if (currentPlaylist != null
                                        && currentPlaylist.getFilePath().equals(currentButton.playlist.getFilePath())) {
                                    boxForTracksButtons.getChildren().clear();
                                    ArrayList<TrackButton> tracks = currentButton.
                                            playlist.getTracks();
                                    for (TrackButton track : tracks) {
                                        track.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                                            @Override
                                            public void handle(MouseEvent event) {
                                                mediator.startNewTrack(((TrackButton)(event.getSource())).path);
                                            }
                                        });
                                        boxForTracksButtons.getChildren().add(track);
                                    }
                                }
                                popupWindow.close();
                            }
                        });
                        addFiles.setLayoutY(0);
                        Button deleteFiles = new Button();
                        deleteFiles.setPrefSize(100,15);
                        deleteFiles.setText("Delete Playlist");
                        deleteFiles.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                if (currentPlaylist != null
                                        && currentPlaylist.getFilePath().equals(currentButton.playlist.getFilePath())) {
                                    mediator.resetTrack();
                                    currentPlaylist = null;
                                    boxForTracksButtons.getChildren().clear();
                                }
                                deletePlaylist();
                                popupWindow.close();
                                refreshArrowsKeys();
                            }
                        });
                        deleteFiles.setLayoutY(25);
                        BorderPane rootForPopup = new BorderPane();
                        Scene popupScene = new Scene(rootForPopup, 100, 50);
                        popupWindow.setScene(popupScene);
                        popupWindow.initStyle(StageStyle.UNDECORATED);
                        Pane somePane = new Pane();
                        somePane.setPrefSize(100, 50);
                        somePane.setLayoutX(0);
                        somePane.setLayoutY(0);
                        rootForPopup.getChildren().add(somePane);
                        somePane.getChildren().add(addFiles);
                        somePane.getChildren().add(deleteFiles);

                        popupWindow.show();
                        popupWindow.setAlwaysOnTop(true);
                        popupWindow.focusedProperty().addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                popupWindow.close();
                            }
                        });
                    }
                }
            });

            configButtonsBox.getChildren().add(playlistsButtons[i]);
        }
        addPlaylistButton.setPrefSize(25,30);
        addPlaylistButton.setText("+");
        addPlaylistButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("New playlist name");
                dialog.setContentText("Please enter name of new playlist:");
                // Traditional way to get the response value
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    try {
                        File file = new File("File:/../playlists/" + result.get() + ".json");
                        boolean creatingResult;
                        creatingResult = file.createNewFile();
                        if (creatingResult) {
                            playlists.add(new Playlist(result.get(),
                                    file.getAbsolutePath().replace("File:\\..\\","")));
                            addPlaylistRefresh();
                        } else {
                            System.out.println("File wasn't created");
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        configButtonsBox.getChildren().add(addPlaylistButton);

        previousPlaylistButton.setMinSize(28,30);
        previousPlaylistButton.setMaxSize(28,30);
        previousPlaylistButton.setText("<");
        previousPlaylistButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int number = -1;
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    System.out.println("Playlist button for 0: " + playlistsButtons[0].playlist.getPlaylistName());
                    for (int i = 0; i < playlists.size() - 1; i++) {
                        System.out.println("Playlist " + i + ": " + playlists.get(i).getPlaylistName());
                        if (playlists.get(i).equals(playlistsButtons[0].playlist)) {
                            number = i;
                        }
                    }
                    if (number == -1) {
                        System.out.println("Couldn't find last button playlist in array");
                        return;
                    }

                    for (int i = playlistsButtons.length - 1; i > 0; i--) {
                        playlistsButtons[i].playlist = playlistsButtons[i - 1].playlist;
                        playlistsButtons[i].setText(playlistsButtons[i].playlist.getPlaylistName());
                    }
//                    if (number + 1 < playlists.size()) {
                        playlistsButtons[0].playlist = playlists.get(number - 1);
                        playlistsButtons[0].setText(
                                playlistsButtons[0].playlist.getPlaylistName());
//                    }
//                    else {
//                        playlistsButtons[playlistsButtons.length - 1].playlist = null;
//                        playlistsButtons[playlistsButtons.length - 1].setText("");
//                    }
                    refreshArrowsKeys();
                }
            }
        });
        configButtonsBox.getChildren().add(previousPlaylistButton);

        nextPlaylistButton.setMinSize(28,30);
        nextPlaylistButton.setMaxSize(28,30);
        nextPlaylistButton.setText(">");
        nextPlaylistButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int number = -1;
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    for (int i = 0; i < playlists.size() - 1; i++) {
                        if (playlists.get(i).equals(playlistsButtons[playlistsButtons.length - 1].playlist)) {
                           number = i;
                        }
                    }
                    if (number == -1) {
                        System.out.println("Couldn't find last button playlist in array");
                        return;
                    }

                    System.out.println("buttons length: " + playlistsButtons.length);
                    for (int i = 0; i < playlistsButtons.length - 1; i++) {
                        System.out.println(i + " was processed");
//                        playlistsButtons[i] = playlistsButtons[i + 1];
                        playlistsButtons[i].playlist = playlistsButtons[i + 1].playlist;
                        playlistsButtons[i].setText(playlistsButtons[i].playlist.getPlaylistName());
                    }
                    playlistsButtons[playlistsButtons.length - 1].playlist = playlists.get(number + 1);
                    playlistsButtons[playlistsButtons.length - 1].setText(
                            playlistsButtons[playlistsButtons.length - 1].playlist.getPlaylistName());
                    refreshArrowsKeys();
                }
            }
        });
        configButtonsBox.getChildren().add(nextPlaylistButton);

        configButtonsBox.setLayoutX(0);
        configButtonsBox.setLayoutY(200);

        ScrollPane someScrollPane = new ScrollPane();
        someScrollPane.setPrefSize(400, 370);
        someScrollPane.setLayoutX(0);
        someScrollPane.setLayoutY(230);
        someScrollPane.setContent(boxForTracksButtons);
        boxForTracksButtons.setPrefSize(400, 370);
        boxForTracksButtons.setLayoutX(0);
        boxForTracksButtons.setLayoutY(230);
//        for (Button trackButton : tracks) {
//            boxForTracksButtons.getChildren().add(trackButton);
//        }

    }

    private void addPlaylistRefresh() {
        for (PlaylistButton playlistButton : playlistsButtons) {
            if (playlistButton.playlist == null) {
                playlistButton.playlist = playlists.get(playlists.size() - 1);
                playlistButton.setText(playlistButton.playlist.getPlaylistName());
                playlistButton.setDisable(false);
                if (playlists.get(0) == null) {
                    System.out.println("Something went wrong");
                }
                else {
                    System.out.println(playlists.get(0).getPlaylistName() + playlists.size());
                }
                break;
            }
        }
        refreshArrowsKeys();
    }

    private void refreshArrowsKeys() {
        if (playlistsButtons[playlistsButtons.length - 1].playlist == null) {
            nextPlaylistButton.setDisable(true);
        }
        else if (playlistsButtons[playlistsButtons.length - 1].playlist.equals(
                playlists.get(playlists.size() - 1))) {
            nextPlaylistButton.setDisable(true);
        }
        else {
            nextPlaylistButton.setDisable(false);
        }

        if (playlistsButtons[0].playlist == null) {
            previousPlaylistButton.setDisable(true);
        }
        else if (playlistsButtons[0].playlist.equals(playlists.get(0))) {
            previousPlaylistButton.setDisable(true);
        }
        else {
            previousPlaylistButton.setDisable(false);
        }
    }

    public Button getAddPlaylistButton() {
        return addPlaylistButton;
    }

    public HBox getConfigButtonsBox() {
        return configButtonsBox;
    }

    public VBox getBoxForTracksButtons() {
        return boxForTracksButtons;
    }

    private void addFiles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP3 files", "*.mp3"),
                new FileChooser.ExtensionFilter("WAV files", "*.wav")
        );
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(new Stage());
        if (selectedFiles != null) {
            currentButton.playlist.addFiles(selectedFiles);
        }
    }

    private void deletePlaylist() {
        //todo clean tracks field (if delete - clean, if add files - refresh)
        File file = new File(currentButton.playlist.getFilePath());
        if (file.delete()) {
//            int index = playlists.indexOf(currentButton.playlist);
            int buttonIndex = -1;
            for (int i = 0; i < playlistsButtons.length; i++) {
                if (playlistsButtons[i].equals(currentButton)) {
                    buttonIndex = i;
                }
            }
            for (int i = buttonIndex; i < playlistsButtons.length - 1; i++) {
                if (playlistsButtons[i + 1].playlist == null) {
                    playlistsButtons[i].playlist = null;
                    playlistsButtons[i].setText("");
                    playlistsButtons[i].setDisable(true);
//                    for (int j = buttonIndex; i < playlistsButtons.length - 1; i++) {
//                        playlistsButtons[j].setDisable(true);
//                    }
                    break;
                }
                else {
                    playlistsButtons[i].playlist = playlistsButtons[i + 1].playlist;
                    playlistsButtons[i].setText(playlistsButtons[i].playlist.getPlaylistName());
                }
            }

            if (playlistsButtons[playlistsButtons.length - 1].playlist != null) {
                int playlistIndex = playlists.indexOf(
                        playlistsButtons[playlistsButtons.length - 1].playlist);
                System.out.println("Index is: " + playlistIndex);
                if (playlists.size() < playlistIndex + 2) {
                    playlistsButtons[playlistsButtons.length - 1].playlist = null;
                    playlistsButtons[playlistsButtons.length - 1].setText("");
                    playlistsButtons[playlistsButtons.length - 1].setDisable(true);
                }
                else {
                    playlistsButtons[playlistsButtons.length - 1].playlist =
                            playlists.get(playlistIndex + 1);
                    playlistsButtons[playlistsButtons.length - 1].setText(
                            playlistsButtons[playlistsButtons.length - 1].playlist.getPlaylistName());
                }
            }

            playlists.remove(currentButton.playlist);

        }
        else {
            System.out.println("Deleting failed");
        }
    }
}
