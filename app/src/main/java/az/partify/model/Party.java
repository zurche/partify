package az.partify.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by az on 21/05/16.
 */
@IgnoreExtraProperties
public class Party implements Serializable {

    public float latitude;
    public float longitude;
    public String name;
    public ArrayList<PartifyTrack> trackList;
    public String playlistId;
    public String hostId;

    public Party() {

    }

    public Party(float latitude, float longitude, String name, ArrayList<PartifyTrack> trackList) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.trackList = trackList;
    }

    public void addTrack(PartifyTrack trackToAdd) {
        trackList.add(trackToAdd);
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }
}
