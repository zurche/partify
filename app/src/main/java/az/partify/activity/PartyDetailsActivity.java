package az.partify.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import az.partify.R;
import az.partify.adapter.SongsAdapter;
import az.partify.model.Song;

public class PartyDetailsActivity extends AppCompatActivity {

    public static final String SONG_LIST = "song_list";
    public static final String PARTY_NAME = "party_name";

    private ArrayList<Song> mPartySongList;
    private String mPartyName;
    private ListView mPartySongListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_details);

        Bundle intentExtras = getIntent().getExtras();

        mPartySongList = (ArrayList<Song>) intentExtras.get(SONG_LIST);
        mPartyName = (String) intentExtras.get(PARTY_NAME);

        if (getSupportActionBar() != null && mPartySongList != null) {
            getSupportActionBar().setTitle(mPartyName);
        }

        mPartySongListView = (ListView) findViewById(R.id.party_song_list);

        SongsAdapter mSongsAdapter = new SongsAdapter(this, mPartySongList);

        mPartySongListView.setAdapter(mSongsAdapter);
    }
}
