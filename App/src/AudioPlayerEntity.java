import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayerEntity {
    private Mediator mediator;
    private String filePath = "D:/TheAgedMan.mp3";
//    private String filePath = "D:/LauncherMusic.wav";
//    private Long currentFrame;
    private Clip wavClip;
//    private String status;
//    private AudioInputStream audioInputStream;
    private String currentTrackFormat;
    private MediaPlayer mp3Player;

    private float MINIMUM_VOLUME = -40;

    public AudioPlayerEntity(Mediator mediator) {
        this.mediator = mediator;

        if (filePath.endsWith(".wav")) {
            try {
                currentTrackFormat = "wav";
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
                wavClip = AudioSystem.getClip();
                wavClip.open(audioInputStream);
                wavClip.loop(Clip.LOOP_CONTINUOUSLY);
                FloatControl volume = (FloatControl)wavClip.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(volume.getMaximum());
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
            mp3Player.setVolume(1);
            mp3Player.play();
        }
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

    public void setVolume(double newVolume) {
        if (currentTrackFormat.equals("mp3")) {
            System.out.println(newVolume);
            mp3Player.setVolume(newVolume); // from 0 to 1
        }
        else if (currentTrackFormat.equals("wav")) {
            FloatControl volume = (FloatControl)wavClip.getControl(FloatControl.Type.MASTER_GAIN);
            float newVolumeValue;
            if (newVolume >= 0.5) {
                newVolumeValue = (float)((newVolume - 0.5) * 2);
                volume.setValue(newVolumeValue * volume.getMaximum());
            }
            else {
                newVolumeValue = (float)(newVolume * 2);
                float middleVolume = newVolumeValue * MINIMUM_VOLUME;
                System.out.println("Middle Volume: " + middleVolume);
                volume.setValue((1 - newVolumeValue) * MINIMUM_VOLUME);
            }
        }
    }

    public void setTrackTime(double newTime) {
        if (currentTrackFormat.equals("mp3")) {
            System.out.println(newTime);
            Duration someDuration = new Duration(mp3Player.getStopTime().toMillis() * newTime);
            System.out.println(someDuration.toMillis());
            mp3Player.stop();
            mp3Player = new MediaPlayer(new Media(new File(filePath).toURI().toString()));
            mp3Player.setStartTime(someDuration);
            mp3Player.play();
            System.out.println(mp3Player.getCurrentTime());
            System.out.println(mp3Player.getStopTime());
        } else if (currentTrackFormat.equals("wav")) {
            System.out.println("Frame length: " + wavClip.getFrameLength());
            System.out.println("Frame position: " + wavClip.getFramePosition());
            wavClip.setFramePosition((int)(wavClip.getFrameLength() * newTime));
            System.out.println("Frame position after change: " + wavClip.getFramePosition());
        }
    }


    public String getCurrentTime() {
        StringBuilder currentTime = new StringBuilder();
        int minutes = 0;
        int seconds = 0;
        if (currentTrackFormat.equals("mp3")) {
            double allSeconds = mp3Player.getCurrentTime().toMillis() / 1000;
            minutes = (int)(allSeconds / 60);
            seconds = (int)(allSeconds % 60);
        }
        else if (currentTrackFormat.equals("wav")) {
            double allSeconds = (double)wavClip.getMicrosecondPosition() / 1000000;
            minutes = (int)(allSeconds / 60);
            seconds = (int)(allSeconds % 60);
        }
        if (minutes < 100) {
            currentTime.append(" ");
        }
        if (minutes < 10) {
            currentTime.append(" ");
        }
        currentTime.append(minutes);
        currentTime.append(":");
        if (seconds < 10) {
            currentTime.append(0);
        }
        currentTime.append(seconds);
        return currentTime.toString();
    }

    public double getCurrentSliderPosition() {
        double newValue = 0;
        if (currentTrackFormat.equals("mp3")) {
            newValue = mp3Player.getCurrentTime().toMillis()
                    / mp3Player.getStopTime().toMillis();
        } else if (currentTrackFormat.equals("wav")) {
            newValue = (double)wavClip.getFramePosition()
                    / (double)wavClip.getFrameLength();
        }
        return newValue;
    }
    //TODO try to make Strategy pattern
}
