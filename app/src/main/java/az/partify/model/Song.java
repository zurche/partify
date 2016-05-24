package az.partify.model;

import java.io.Serializable;

/**
 * Created by az on 23/05/16.
 */
public class Song implements Serializable{
    public String songId;
    public String songArtist;
    public String songName;

    public Song() {}

    public Song(String songId, String songArtist, String songName) {
        this.songId = songId;
        this.songArtist = songArtist;
        this.songName = songName;
    }
}
