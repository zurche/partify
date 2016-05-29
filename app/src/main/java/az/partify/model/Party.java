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
}
