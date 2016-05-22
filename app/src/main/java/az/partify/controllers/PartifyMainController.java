package az.partify.controllers;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

import az.partify.screen_actions.PartifyMainScreenActions;
import az.partify.util.SharedPreferenceHelper;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by az on 22/05/16.
 */
public class PartifyMainController {

    private final PartifyMainScreenActions mPartifyScreenActions;
    private final SharedPreferenceHelper mSharedPreferenceHelper;
    private final SpotifyService mSpotifyService;

    public PartifyMainController(PartifyMainScreenActions partifyMainScreenActions) {
        mPartifyScreenActions = partifyMainScreenActions;
        mSharedPreferenceHelper = new SharedPreferenceHelper((Context) partifyMainScreenActions);

        SpotifyApi mSpotifyApi = new SpotifyApi();
        mSpotifyApi.setAccessToken(mSharedPreferenceHelper.getCurrentSpotifyToken());
        mSpotifyService = mSpotifyApi.getService();
    }

    public void onCreateParty() {
        getCurrentUser();
    }

    public void onSearchParty() {

    }

    private void getCurrentUser() {
        mSpotifyService.getMe(new Callback<UserPrivate>() {
            @Override
            public void success(UserPrivate userPrivate, Response response) {
                Log.d("USER", "Obtained User Information.");

                mSharedPreferenceHelper.saveCurrentUserId(userPrivate.id);

                createPlaylist(userPrivate.id, "PartifyPlaylist");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("USER", "FAILED to Obtain User Information.");
            }
        });
    }

    private void createPlaylist(String id, String playlistName) {
        HashMap<String, Object> playlistParams = new HashMap<String, Object>();
        playlistParams.put("name", playlistName);
        playlistParams.put("public", false);

        mSpotifyService.createPlaylist(id, playlistParams, new Callback<Playlist>() {
            @Override
            public void success(Playlist playlist, Response response) {
                Log.d("PLAYLIST", "Playlist Created successfully: " + playlist.name);

                mSharedPreferenceHelper.saveCurrentPlayListId(playlist.id);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("PLAYLIST", "Playlist Creation Failed.");
            }
        });
    }
}
