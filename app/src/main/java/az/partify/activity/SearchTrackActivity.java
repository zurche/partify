package az.partify.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import az.partify.R;
import az.partify.adapter.TracksAdapter;
import az.partify.controllers.SearchTrackController;
import az.partify.model.PartifyTrack;
import az.partify.screen_actions.SearchTrackScreenActions;
import kaaes.spotify.webapi.android.models.Track;

public class SearchTrackActivity extends AppCompatActivity implements SearchTrackScreenActions {

    private static final String TAG = SearchTrackActivity.class.getSimpleName();

    public static final String TRACK = "track";
    private ListView mTrackList;
    private TracksAdapter mTrackListAdapter;
    private SearchTrackController mSearchSongController;
    private EditText mSearchTrackField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_track);

        mSearchSongController = new SearchTrackController(this);

        mSearchTrackField = (EditText) findViewById(R.id.track_search_edit_text);
        mSearchTrackField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged");
                if (s.length() > 0) {
                    mSearchSongController.onSearch(mSearchTrackField.getText().toString());
                }
            }
        });

        mTrackList = (ListView) findViewById(R.id.track_list);
        mTrackList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track selectedTrack = (Track) mTrackList.getItemAtPosition(position);

                PartifyTrack partifyTrack = new PartifyTrack(
                        selectedTrack.name,
                        selectedTrack.artists.get(0).name,
                        "spotify:track:" + selectedTrack.id);

                Intent resultData = new Intent();
                resultData.putExtra(TRACK, partifyTrack);
                setResult(Activity.RESULT_OK, resultData);
                finish();
            }
        });
    }

    @Override
    public void setTrackList(List<Track> tracks) {
        mTrackListAdapter = new TracksAdapter(tracks, this);
        mTrackList.setAdapter(mTrackListAdapter);
    }
}
