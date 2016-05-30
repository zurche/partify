package az.partify.controllers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import az.partify.model.PartifyTrack;
import az.partify.model.Party;
import az.partify.screen_actions.CreatePartyScreenActions;
import az.partify.util.SharedPreferenceHelper;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by az on 22/05/16.
 */
public class CreatePartyController {
    private static final String TAG = CreatePartyController.class.getSimpleName();
    private final CreatePartyScreenActions mCreatePartyScreenActions;
    private final SharedPreferenceHelper mSharedPreferenceHelper;
    private final SpotifyService mSpotifyService;
    private String mStreetAddress;
    private float mLatitude;
    private float mLongitude;
    private Party mCurrentParty;

    public CreatePartyController(CreatePartyScreenActions createPartyScreenActions) {
        mCreatePartyScreenActions = createPartyScreenActions;
        mSharedPreferenceHelper = new SharedPreferenceHelper((Context) createPartyScreenActions);

        SpotifyApi mSpotifyApi = new SpotifyApi();
        mSpotifyApi.setAccessToken(mSharedPreferenceHelper.getCurrentSpotifyToken());
        mSpotifyService = mSpotifyApi.getService();
    }


    public void onSaveParty(String partyName, ArrayList<PartifyTrack> trackList) {
        if (partyName.trim().length() == 0) {
            mCreatePartyScreenActions.showError("Party Name Invalid");
        } else {
            mCurrentParty = new Party(mLatitude, mLongitude, partyName, trackList);

            createSpotifyPlaylist();
        }
    }

    private void createSpotifyPlaylist() {
        mSpotifyService.getMe(new Callback<UserPrivate>() {
            @Override
            public void success(UserPrivate userPrivate, Response response) {
                Log.d(TAG, "Obtained User Information.");

                mSharedPreferenceHelper.saveCurrentUserId(userPrivate.id);

                createPlaylist(userPrivate.id);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "FAILED to Obtain User Information.");
            }
        });
    }

    private void createPlaylist(final String userId) {
        HashMap<String, Object> playlistParams = new HashMap<String, Object>();
        playlistParams.put("name", mCurrentParty.name);
        playlistParams.put("public", true);

        mSpotifyService.createPlaylist(userId, playlistParams, new Callback<Playlist>() {
            @Override
            public void success(Playlist playlist, Response response) {
                Log.d(TAG, "Playlist Created successfully: " + playlist.name);

                mSharedPreferenceHelper.saveCurrentPlayListId(playlist.id);

                mCurrentParty.setPlaylistId(playlist.id);

                addTracksToSpotifyPlaylist(userId, playlist.id);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "Playlist Creation Failed.");
            }
        });
    }

    private void addTracksToSpotifyPlaylist(String userId, String playlistId) {

        StringBuffer tracksParams = new StringBuffer();

        for (PartifyTrack track : mCurrentParty.trackList) {
            tracksParams.append(track.mId).append(",");
        }

        HashMap trackMap = new HashMap();
        trackMap.put("uris", tracksParams.toString());


        mSpotifyService.addTracksToPlaylist(
                userId,
                playlistId,
                trackMap,
                new HashMap<String, Object>(),
                new Callback<Pager<PlaylistTrack>>() {
                    @Override
                    public void success(Pager<PlaylistTrack> playlistTrackPager, Response response) {
                        saveNewParty(mCurrentParty);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "Adding Tracks to Playlist Failed.");
                    }
                });
    }

    private void saveNewParty(Party tmpParty) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("parties");

        myRef.child(tmpParty.name).setValue(tmpParty, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                mCreatePartyScreenActions.showPartyCreatedScreen();
            }
        });
    }

    public void getCurrentLocation(float latitude, float longitude, Context context) {
        mLatitude = latitude;
        mLongitude = longitude;

        List<Address> addresses = null;

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses.size() == 1) {
            mStreetAddress = addresses.get(0).getAddressLine(0);
        }

        mCreatePartyScreenActions.updateLocationUI(mStreetAddress);
    }


    public void onAddSongButtonPressed() {
        mCreatePartyScreenActions.showSearchSongScreen();
    }
}
