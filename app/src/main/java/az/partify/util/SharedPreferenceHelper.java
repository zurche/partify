package az.partify.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by az on 21/05/16.
 */
public class SharedPreferenceHelper {

    public static final String PREFERENCES = "PartifyPreferences";

    public static final String CURRENT_SPOTIFY_TOKEN_KEY = "CurrentSpotifyToken";
    public static final String CURRENT_USER_SPOTIFY_ID_KEY = "CurrentSpotifyID";
    public static final String CURRENT_PLAYLIST_ID_KEY = "CurrentPlaylistID";

    private Context mContext;
    private SharedPreferences mSharedPreferences;

    public SharedPreferenceHelper(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(PREFERENCES, 0);
    }

    public void saveSpotifyToken(String token) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(CURRENT_SPOTIFY_TOKEN_KEY, token);
        editor.apply();
    }

    public void saveCurrentUserId(String id) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(CURRENT_USER_SPOTIFY_ID_KEY, id);
        editor.apply();
    }

    public void saveCurrentPlayListId(String id) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(CURRENT_PLAYLIST_ID_KEY, id);
        editor.apply();
    }

    public String getCurrentSpotifyToken() {
        return mSharedPreferences.getString(CURRENT_SPOTIFY_TOKEN_KEY, "");
    }

    public String getCurrentUserId() {
        return mSharedPreferences.getString(CURRENT_USER_SPOTIFY_ID_KEY, "");
    }

    public String getCurrentPlaylistId() {
        return mSharedPreferences.getString(CURRENT_PLAYLIST_ID_KEY, "");
    }

}
