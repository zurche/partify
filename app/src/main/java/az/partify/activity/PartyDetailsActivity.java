package az.partify.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import az.partify.R;
import az.partify.adapter.PartifyTracksAdapter;
import az.partify.controllers.PartyDetailsController;
import az.partify.model.PartifyTrack;
import az.partify.model.Party;
import az.partify.screen_actions.PartyDetailsScreenActions;

public class PartyDetailsActivity extends AppCompatActivity implements PartyDetailsScreenActions {

    private ArrayList<PartifyTrack> mPartyTrackList;
    private ListView mPartySongListView;
    private PartifyTracksAdapter mTracksAdapter;
    private Button mAddTrackButton;
    private PartyDetailsController mPartyDetailsController;
    private Party mCurrentParty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_details);


        Bundle intentExtras = getIntent().getExtras();

        mCurrentParty = (Party) intentExtras.get(SearchPartyActivity.CURRENT_PARTY);

        mPartyTrackList = mCurrentParty.trackList;

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mCurrentParty.name);
        }

        mPartyDetailsController = new PartyDetailsController(this, mCurrentParty);

        mAddTrackButton = (Button) findViewById(R.id.add_song_button);
        mAddTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPartyDetailsController.onUserClicksAddTrack();
            }
        });

        mPartySongListView = (ListView) findViewById(R.id.party_song_list);
        mTracksAdapter = new PartifyTracksAdapter(mPartyTrackList, this);
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
    public void updateCurrentTrackList() {
        mTracksAdapter = new PartifyTracksAdapter(mPartyTrackList, PartyDetailsActivity.this);
        mPartySongListView.setAdapter(mTracksAdapter);
    }
}
