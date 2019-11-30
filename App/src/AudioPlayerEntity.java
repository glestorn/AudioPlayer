import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AudioPlayerEntity {
    private Mediator mediator;
    private String filePath = "D:/LauncherMusic.wav";
//    private Long currentFrame;
    private Clip wavClip;
//    private String status;
//    private AudioInputStream audioInputStream;
    private String currentTrackFormat;
    private MediaPlayer mp3Player;

    public AudioPlayerEntity(Mediator mediator) {
        this.mediator = mediator;

        if (filePath.endsWith(".wav")) {
            try {
                currentTrackFormat = "wav";
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
                wavClip = AudioSystem.getClip();
                wavClip.open(audioInputStream);
                wavClip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            catch (UnsupportedAudioFileException | IOException |
                    LineUnavailableException ex) {
                ex.printStackTrace();
            }
        }
        else if (filePath.endsWith(".mp3")) {
            currentTrackFormat = "mp3";
            Media mp3Track = new Media(new File(filePath).toURI().toString());
            mp3Player = new MediaPlayer(mp3Track);
            mp3Player.play();
        }

//        try {
//            audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
//            clip = AudioSystem.getClip();
//            clip.open(audioInputStream);
//            clip.loop(Clip.LOOP_CONTINUOUSLY);
//        }
//        catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
//            ex.printStackTrace();
//        }
//
//        clip.start();


//        try {
//            FileInputStream file = new FileInputStream(filePath);
//            BufferedInputStream bufferedInputStream = new BufferedInputStream(file);
//            Player player = new Player(bufferedInputStream);
//            long totalLength = file.available();
//            player.play();
//        }
//        catch (IOException ex) {
//            ex.printStackTrace();
//        }


//        Media hit = new Media(new File(filePath).toURI().toString());
//        MediaPlayer mediaPlayer = new MediaPlayer(hit);
//        mediaPlayer.setAutoPlay(true);
//        mediaPlayer.play();
    }

    public void stopPlayTrack() {
        if (currentTrackFormat.equals("mp3")) {
            if (mp3Player.getStatus().equals(MediaPlayer.Status.PAUSED)) {
                mp3Player.play();
            }
            else {
                mp3Player.pause();
            }
        }
        else if (currentTrackFormat.equals("wav")) {
            if (wavClip.isRunning()) {
                wavClip.stop();
            }
            else {
                wavClip.start();
            }
        }
    }


}
