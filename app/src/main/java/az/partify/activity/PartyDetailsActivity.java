package az.partify.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import az.partify.R;
import az.partify.adapter.PartifyTracksAdapter;
import az.partify.model.PartifyTrack;

public class PartyDetailsActivity extends AppCompatActivity {

    public static final String TRACK_LIST = "track_list";
    public static final String PARTY_NAME = "party_name";

    private List<PartifyTrack> mPartyTrackList;
    private String mPartyName;
    private ListView mPartySongListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_details);

        Bundle intentExtras = getIntent().getExtras();

        mPartyTrackList = (ArrayList<PartifyTrack>) intentExtras.get(TRACK_LIST);
        mPartyName = (String) intentExtras.get(PARTY_NAME);

        if (getSupportActionBar() != null && mPartyTrackList != null) {
            getSupportActionBar().setTitle(mPartyName);
        }

        mPartySongListView = (ListView) findViewById(R.id.party_song_list);
        PartifyTracksAdapter partifyTracksAdapter = new PartifyTracksAdapter(mPartyTrackList, this);
        mPartySongListView.setAdapter(partifyTracksAdapter);
    }
}
