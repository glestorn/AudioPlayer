import java.util.ArrayList;

public class Playlist {
    private ArrayList<String> tracksPaths;
    private ArrayList<String> tracksNames;
    private String filePath;
    private String playlistName;

    public Playlist(String playlistName, String filePath) {
        this.playlistName = playlistName;
        this.filePath = filePath;
    }

    public String getPlaylistName() {
        return playlistName;
    }
}
