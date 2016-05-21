package az.partify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.ConnectionStateCallback;

import java.util.HashMap;

import az.partify.util.SharedPreferenceHelper;
import az.partify.util.SpotifyScope;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements ConnectionStateCallback {

    private static final String CLIENT_ID = "331d62158fe642528cf0b22a5a90aa12";
    private static final String REDIRECT_URI = "yourcustomprotocol://callback";
    private static final int REQUEST_CODE = 1;
    private SharedPreferenceHelper mSharedPreferenceHelper;
    private SpotifyService mSpotifyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferenceHelper = new SharedPreferenceHelper(this);

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);

        builder.setScopes(new String[]{
                SpotifyScope.USER_READ_PRIVATE,
                SpotifyScope.PLAYLIST_READ_COLLABORATIVE,
                SpotifyScope.PLAYLIST_MODIFY_PRIVATE,
                SpotifyScope.PLAYLIST_MODIFY_PUBLIC,
                SpotifyScope.PLAYLIST_READ_PRIVATE});

        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {

                String accessToken = response.getAccessToken();
                mSharedPreferenceHelper.saveSpotifyToken(accessToken);

                SpotifyApi mSpotifyApi = new SpotifyApi();
                mSpotifyApi.setAccessToken(accessToken);
                mSpotifyService = mSpotifyApi.getService();

                getCurrentUser();
            }
        }
    }

    private void getCurrentUser() {
        mSpotifyService.getMe(new Callback<UserPrivate>() {
            @Override
            public void success(UserPrivate userPrivate, Response response) {
                Log.d("USER", "Obtained User Information.");

                mSharedPreferenceHelper.saveCurrentUserId(userPrivate.id);

                createPlaylist(userPrivate.id, "PartifyPlaylist");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("USER", "FAILED to Obtain User Information.");
            }
        });
    }

    private void createPlaylist(String id, String playlistName) {
        HashMap<String, Object> playlistParams = new HashMap<String, Object>();
        playlistParams.put("name", playlistName);
        playlistParams.put("public", false);

        mSpotifyService.createPlaylist(id, playlistParams, new Callback<Playlist>() {
            @Override
            public void success(Playlist playlist, Response response) {
                Log.d("PLAYLIST", "Playlist Created successfully: " + playlist.name);

                mSharedPreferenceHelper.saveCurrentPlayListId(playlist.id);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("PLAYLIST", "Playlist Creation Failed.");
            }
        });
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
