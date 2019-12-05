import javafx.scene.text.Font;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


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

    public String getFilePath() {
        return filePath;
    }

    public void addFiles(List<File> files) {
        JSONArray tracks = new JSONArray();
        try {
            JSONParser jsonParser = new JSONParser();
            tracks = (JSONArray) jsonParser.parse(new FileReader(filePath));
        }
        catch (org.json.simple.parser.ParseException ex) {
            System.out.println("File is empty");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        for (File file : files) {
            JSONObject newObject = new JSONObject();
            newObject.put("path", file.getAbsolutePath());
            newObject.put("name", file.getName());
            if (!tracks.contains(newObject)) {
                tracks.add(newObject);
            }
        }
        FileWriter fileToWrite = null;
        try {
            fileToWrite = new FileWriter(filePath);
            fileToWrite.write(tracks.toJSONString());
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                fileToWrite.flush();
                fileToWrite.close();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public ArrayList<TrackButton> getTracks() {
        JSONArray tracks = new JSONArray();
        try {
            JSONParser jsonParser = new JSONParser();
            tracks = (JSONArray) jsonParser.parse(new FileReader(filePath));
        }
        catch (org.json.simple.parser.ParseException ex) {
            System.out.println("File is empty");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        ArrayList<TrackButton> trackButtons = new ArrayList<>();
        for (int i = 0; i < tracks.size(); i++) {
            JSONObject object = (JSONObject)tracks.get(i);
            TrackButton temp = new TrackButton();
            temp.path = object.get("path").toString();
            temp.track = object.get("name").toString().substring(0,
                    object.get("name").toString().indexOf("."));
            temp.setText(temp.track);
            temp.setStyle("-fx-border-width: 0;" +
                "-fx-background-color: whitedef");
            temp.setFont(new Font(20));
            temp.setMinWidth(400);
            trackButtons.add(temp);
            System.out.println("Track path is: " + temp.path);
            System.out.println("Track name is: " +temp.track);
        }

        return trackButtons;
    }
}
