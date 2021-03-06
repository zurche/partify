package az.partify.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import az.partify.R;
import az.partify.adapter.PartiesAdapter;
import az.partify.controllers.SearchPartyController;
import az.partify.model.PartifyTrack;
import az.partify.model.Party;
import az.partify.screen_actions.SearchPartyScreenActions;

public class SearchPartyActivity extends AppCompatActivity implements
        SearchPartyScreenActions,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = SearchPartyActivity.class.getSimpleName();
    public static final String CURRENT_PARTY = "party";

    private SearchPartyController mSearchPartyController;
    private ListView mPartiesListView;
    private ProgressBar mLoadingPartiesProgress;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_party);

        mSearchPartyController = new SearchPartyController(this);

        mPartiesListView = (ListView) findViewById(R.id.parties_list);
        mLoadingPartiesProgress = (ProgressBar) findViewById(R.id.loading_parties_list);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mPartiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Party selectedParty = (Party) mPartiesListView.getItemAtPosition(position);

                if(selectedParty.trackList == null) {
                    selectedParty.trackList = new ArrayList<PartifyTrack>();
                }

                Intent startPartyDetailsScreen = new Intent(SearchPartyActivity.this,
                        PartyDetailsActivity.class);

                startPartyDetailsScreen.putExtra(CURRENT_PARTY, selectedParty);

                startActivity(startPartyDetailsScreen);
            }
        });

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
    public void refreshPartyListInScreen(ArrayList<Party> parties) {
        PartiesAdapter mPartiesAdapter = new PartiesAdapter(this, parties);
        mPartiesListView.setAdapter(mPartiesAdapter);
        mLoadingPartiesProgress.setVisibility(View.GONE);
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
            mSearchPartyController.onRetrievePartyList(
                    (float) mLastLocation.getLatitude(),
                    (float) mLastLocation.getLongitude(), this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended called with value: " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed called with value: " + connectionResult);
    }
}
