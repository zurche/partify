package az.partify.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by az on 21/05/16.
 */
@IgnoreExtraProperties
public class Party {

    public float latitude;
    public float longitude;
    public String name;
    public ArrayList<PartifyTrack> songList;

    public Party() {

    }

    public Party(float latitude, float longitude, String name, ArrayList<PartifyTrack> songList) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.songList = songList;
    }
}
