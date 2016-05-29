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
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by az on 22/05/16.
 */
public class CreatePartyController {

    private final CreatePartyScreenActions mCreatePartyScreenActions;
    private final SharedPreferenceHelper mSharedPreferenceHelper;
    private final SpotifyService mSpotifyService;
    private String mStreetAddress;
    private float mLatitude;
    private float mLongitude;

    public CreatePartyController(CreatePartyScreenActions createPartyScreenActions) {
        mCreatePartyScreenActions = createPartyScreenActions;
        mSharedPreferenceHelper = new SharedPreferenceHelper((Context) createPartyScreenActions);

        SpotifyApi mSpotifyApi = new SpotifyApi();
        mSpotifyApi.setAccessToken(mSharedPreferenceHelper.getCurrentSpotifyToken());
        mSpotifyService = mSpotifyApi.getService();
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

    public void onSaveParty(String partyName, ArrayList<PartifyTrack> trackList) {
        if (partyName.trim().length() == 0) {
            mCreatePartyScreenActions.showError("Party Name Invalid");
        } else {
            Party newParty = new Party(mLatitude, mLongitude, partyName, trackList);
            saveNewParty(newParty);
        }
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

    public void onAddSongButtonPressed() {
        mCreatePartyScreenActions.showSearchSongScreen();
    }
}
