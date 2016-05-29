package az.partify.controllers;

import android.content.Context;
import android.util.Log;

import az.partify.screen_actions.SearchTrackScreenActions;
import az.partify.util.SharedPreferenceHelper;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by az on 29/05/16.
 */
public class SearchTrackController {
    private final SearchTrackScreenActions mSearchTrackScreenActions;
    private final SharedPreferenceHelper mSharedPreferenceHelper;
    private final SpotifyService mSpotifyService;
    private static final String TAG = SearchTrackController.class.getSimpleName();

    public SearchTrackController(SearchTrackScreenActions searchTrackActivity) {
        mSearchTrackScreenActions = searchTrackActivity;
        mSharedPreferenceHelper = new SharedPreferenceHelper((Context) searchTrackActivity);

        SpotifyApi mSpotifyApi = new SpotifyApi();
        mSpotifyApi.setAccessToken(mSharedPreferenceHelper.getCurrentSpotifyToken());
        mSpotifyService = mSpotifyApi.getService();
    }

    public void onSearch(String trackName) {
        mSpotifyService.searchTracks(trackName, new Callback<TracksPager>() {
            @Override
            public void success(TracksPager tracksPager, Response response) {
                Log.d(TAG, "Track Search success: " + response.toString());
                mSearchTrackScreenActions.setTrackList(tracksPager.tracks.items);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
