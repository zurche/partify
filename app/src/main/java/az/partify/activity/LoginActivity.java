package az.partify.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.common.api.GoogleApiClient;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.ConnectionStateCallback;

import az.partify.R;
import az.partify.controllers.LoginActivityController;
import az.partify.screen_actions.LoginScreenActions;

public class LoginActivity extends AppCompatActivity implements
        ConnectionStateCallback,
        LoginScreenActions {

    private LoginActivityController mLoginActivityController;

    private Button mLoginButton;
    private ProgressBar mLoadingLoginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginActivityController = new LoginActivityController(this);

        mLoginActivityController.onRefreshCurrentLocation();

        mLoginButton = (Button) findViewById(R.id.login_button);
        mLoadingLoginProgressBar = (ProgressBar) findViewById(R.id.loading_login);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginActivityController.onLoginUserToSpotify();

                mLoadingLoginProgressBar.setVisibility(View.VISIBLE);
                mLoginButton.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mLoadingLoginProgressBar.setVisibility(View.GONE);
        mLoginButton.setVisibility(View.VISIBLE);

        mLoginActivityController.onCheckIfUserIsLoggedIn();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == LoginActivityController.REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {

                String accessToken = response.getAccessToken();
                mLoginActivityController.onUserLoggedInSuccessfully(accessToken);
            }
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("LoginActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("LoginActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        Log.d("LoginActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("LoginActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("LoginActivity", "Received connection message: " + message);
    }

    @Override
    public void showMainScreen() {
        Intent loadMainPartifyScreen = new Intent(this, PartifyMainActivity.class);
        startActivity(loadMainPartifyScreen);
        finish();
    }
}
