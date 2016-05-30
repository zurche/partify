package az.partify.controllers;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import az.partify.model.PartifyTrack;
import az.partify.model.Party;
import az.partify.screen_actions.PartyDetailsScreenActions;
import az.partify.util.SharedPreferenceHelper;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by az on 29/05/16.
 */
public class PartyDetailsController {
    private static final String TAG = PartyDetailsController.class.getSimpleName();
    private final PartyDetailsScreenActions mPartyDetailsScreenActions;
    private final SharedPreferenceHelper mSharedPreferenceHelper;
    private final SpotifyService mSpotifyService;
    private Party mCurrentParty;

    public PartyDetailsController(PartyDetailsScreenActions partyDetailsScreenActions,
                                  Party currentParty) {
        mPartyDetailsScreenActions = partyDetailsScreenActions;

        mCurrentParty = currentParty;

        mSharedPreferenceHelper = new SharedPreferenceHelper((Context) mPartyDetailsScreenActions);

        SpotifyApi mSpotifyApi = new SpotifyApi();
        mSpotifyApi.setAccessToken(mSharedPreferenceHelper.getCurrentSpotifyToken());
        mSpotifyService = mSpotifyApi.getService();
    }

    public void onUserClicksAddTrack() {
        mPartyDetailsScreenActions.showSearchTrackScreen();
    }

    public void onUserAddedTrack(PartifyTrack trackToAdd) {
        mCurrentParty.addTrack(trackToAdd);

        updateParty();
    }

    private void updateParty() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("parties");

        myRef.child(mCurrentParty.name).setValue(mCurrentParty, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                mPartyDetailsScreenActions.updateCurrentTrackList(mCurrentParty);
            }
        });
    }

    public void onHostClicksTrack(final PartifyTrack trackToAdd) {
        HashMap parametersMap = new HashMap();
        parametersMap.put("uris", trackToAdd.mId);

        mSpotifyService.addTracksToPlaylist(
                mCurrentParty.hostId,
                mCurrentParty.playlistId,
                parametersMap,
                new HashMap<String, Object>(),
                new Callback<Pager<PlaylistTrack>>() {
                    @Override
                    public void success(Pager<PlaylistTrack> playlistTrackPager, Response response) {
                        removeTrackFromParty(trackToAdd);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "Adding Tracks to Playlist Failed.");
                    }
                });
    }

    private void removeTrackFromParty(PartifyTrack trackToAdd) {
        mCurrentParty.trackList.remove(trackToAdd);

        updateParty();
    }
}
