package az.partify.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import az.partify.R;
import az.partify.controllers.CreatePartyController;
import az.partify.screen_actions.CreatePartyScreenActions;

public class CreatePartyActivity extends AppCompatActivity implements
        CreatePartyScreenActions,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private CreatePartyController mCreatePartyController;
    private GoogleApiClient mGoogleApiClient;
    private ProgressBar mLocationProgressBar;
    private TextView mLocationLabel;
    private Button mCreatePartyButton;
    private EditText mPartyNameEditText;

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

        mPartyNameEditText = (EditText) findViewById(R.id.party_name_edit_text);
        mLocationProgressBar = (ProgressBar) findViewById(R.id.location_progress);
        mLocationLabel = (TextView) findViewById(R.id.location_label);
        mCreatePartyButton = (Button) findViewById(R.id.create_party);
        mCreatePartyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCreatePartyController.saveParty(mPartyNameEditText.getText().toString());
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
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {return;}

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
}
