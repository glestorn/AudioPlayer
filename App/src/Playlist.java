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

            if (tracks.toJSONString() != null) {
                System.out.println(tracks.toString());
                System.out.println(tracks.get(0).toString());
            }
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
            tracks.add(newObject);
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
}
