package az.partify.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import az.partify.R;
import az.partify.adapter.PartifyTracksAdapter;
import az.partify.controllers.PartyDetailsController;
import az.partify.model.PartifyTrack;
import az.partify.model.Party;
import az.partify.screen_actions.PartyDetailsScreenActions;
import az.partify.util.SharedPreferenceHelper;

public class PartyDetailsActivity extends AppCompatActivity implements PartyDetailsScreenActions {

    private ArrayList<PartifyTrack> mPartyTrackList;
    private ListView mPartySongListView;
    private PartifyTracksAdapter mTracksAdapter;
    private Button mAddTrackButton;
    private PartyDetailsController mPartyDetailsController;
    private Party mCurrentParty;
    private SharedPreferenceHelper mSharedPreferenceHelper;
    private boolean mIsCurrentPartyOwnedByCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_details);

        mSharedPreferenceHelper = new SharedPreferenceHelper(this);


        Bundle intentExtras = getIntent().getExtras();

        mCurrentParty = (Party) intentExtras.get(SearchPartyActivity.CURRENT_PARTY);

        mIsCurrentPartyOwnedByCurrentUser =
                mCurrentParty.hostId.equals(mSharedPreferenceHelper.getCurrentUserId());

        mPartyTrackList = mCurrentParty.trackList;

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mCurrentParty.name);
        }

        mPartyDetailsController = new PartyDetailsController(this, mCurrentParty);

        mAddTrackButton = (Button) findViewById(R.id.add_track_button);
        mAddTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPartyDetailsController.onUserClicksAddTrack();
            }
        });

        mAddTrackButton.setVisibility(
                mIsCurrentPartyOwnedByCurrentUser ?
                        View.GONE :
                        View.VISIBLE);

        mPartySongListView = (ListView) findViewById(R.id.party_song_list);
        mTracksAdapter = new PartifyTracksAdapter(mPartyTrackList, this, mIsCurrentPartyOwnedByCurrentUser);
        mPartySongListView.setAdapter(mTracksAdapter);
        mPartySongListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mIsCurrentPartyOwnedByCurrentUser) {
                    mPartyDetailsController.onHostClicksTrack(mTracksAdapter.getItem(position));
                    removeElementFromTracklist(position);
                    Toast.makeText(PartyDetailsActivity.this, "Track Added!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void removeElementFromTracklist(int position) {
        mPartyTrackList.remove(position);
        mTracksAdapter = new PartifyTracksAdapter(mPartyTrackList, PartyDetailsActivity.this, mIsCurrentPartyOwnedByCurrentUser);
        mPartySongListView.setAdapter(mTracksAdapter);
    }

    @Override
    public void showSearchTrackScreen() {
        Intent startSearchSongActivity = new Intent(this, SearchTrackActivity.class);
        startActivityForResult(startSearchSongActivity, CreatePartyActivity.SEARCH_SONG_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CreatePartyActivity.SEARCH_SONG_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PartifyTrack trackToAdd = (PartifyTrack) data.getExtras().get(SearchTrackActivity.TRACK);
                mPartyDetailsController.onUserAddedTrack(trackToAdd);
            }
        }
    }

    @Override
    public void updateCurrentTrackList(Party mCurrentParty) {
        mTracksAdapter = new PartifyTracksAdapter(mCurrentParty.trackList, PartyDetailsActivity.this, mIsCurrentPartyOwnedByCurrentUser);
        mPartySongListView.setAdapter(mTracksAdapter);
    }
}
