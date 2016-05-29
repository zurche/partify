package az.partify.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import az.partify.R;
import az.partify.adapter.PartifyTracksAdapter;
import az.partify.adapter.TracksAdapter;
import az.partify.controllers.CreatePartyController;
import az.partify.model.PartifyTrack;
import az.partify.screen_actions.CreatePartyScreenActions;
import kaaes.spotify.webapi.android.models.Track;

public class CreatePartyActivity extends AppCompatActivity implements
        CreatePartyScreenActions,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final int SEARCH_SONG_REQUEST_CODE = 1;
    private CreatePartyController mCreatePartyController;
    private GoogleApiClient mGoogleApiClient;
    private ProgressBar mLocationProgressBar;
    private TextView mLocationLabel;
    private Button mCreatePartyButton;
    private EditText mPartyNameEditText;
    private Button mAddSongButton;
    private ListView mPartyTrackList;
    private PartifyTracksAdapter mTracksAdapter;
    private ArrayList<PartifyTrack> mCurrentTrackList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);

        mCreatePartyController = new CreatePartyController(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mTracksAdapter = new PartifyTracksAdapter(mCurrentTrackList, this);

        mPartyNameEditText = (EditText) findViewById(R.id.party_name_edit_text);
        mLocationProgressBar = (ProgressBar) findViewById(R.id.location_progress);
        mLocationLabel = (TextView) findViewById(R.id.location_label);
        mPartyTrackList = (ListView) findViewById(R.id.party_track_list);

        mCreatePartyButton = (Button) findViewById(R.id.create_party);
        mCreatePartyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCreatePartyController.onSaveParty(mPartyNameEditText.getText().toString(), mCurrentTrackList);
            }
        });

        mAddSongButton = (Button) findViewById(R.id.add_song_button);
        mAddSongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCreatePartyController.onAddSongButtonPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPartyTrackList.setAdapter(mTracksAdapter);
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null) {
            mCreatePartyController.getCurrentLocation(
                    (float) mLastLocation.getLatitude(), (float) mLastLocation.getLongitude(), this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Could not retrieve location\nCheck GPS", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Could not retrieve location\nCheck GPS", Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateLocationUI(String locationText) {
        mLocationProgressBar.setVisibility(View.INVISIBLE);
        mLocationLabel.setText(locationText);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPartyCreatedScreen() {
        Toast.makeText(this, "Party Created!", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    @Override
    public void showSearchSongScreen() {
        Intent startSearchSongActivity = new Intent(this, SearchTrackActivity.class);
        startActivityForResult(startSearchSongActivity, SEARCH_SONG_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH_SONG_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                PartifyTrack trackToAdd = (PartifyTrack) data.getExtras().get(SearchTrackActivity.TRACK);
                mCurrentTrackList.add(trackToAdd);
                mTracksAdapter = new PartifyTracksAdapter(mCurrentTrackList, CreatePartyActivity.this);
                mPartyTrackList.setAdapter(mTracksAdapter);
                mPartyTrackList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mCurrentTrackList.remove(position);
                        mTracksAdapter = new PartifyTracksAdapter(mCurrentTrackList, CreatePartyActivity.this);
                        mPartyTrackList.setAdapter(mTracksAdapter);
                    }
                });

            }
        }
    }
}
