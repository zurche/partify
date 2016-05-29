package az.partify.screen_actions;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by az on 29/05/16.
 */
public interface SearchTrackScreenActions {
    void setTrackList(List<Track> tracks);
}
