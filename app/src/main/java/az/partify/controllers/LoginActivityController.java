package az.partify.controllers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.HashMap;

import az.partify.screen_actions.LoginScreenActions;
import az.partify.util.SharedPreferenceHelper;
import az.partify.util.SpotifyScope;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by az on 22/05/16.
 */
public class LoginActivityController {

    private static final String CLIENT_ID = "331d62158fe642528cf0b22a5a90aa12";
    private static final String REDIRECT_URI = "yourcustomprotocol://callback";
    public static final int REQUEST_CODE = 1;

    private final SharedPreferenceHelper mSharedPreferenceHelper;
    private LoginScreenActions mLoginActivityActions;
    private SpotifyService mSpotifyService;

    public LoginActivityController(LoginScreenActions loginActivityActions) {
        mLoginActivityActions = loginActivityActions;
        mSharedPreferenceHelper = new SharedPreferenceHelper((Context) loginActivityActions);
    }

    public void onLoginUserToSpotify() {
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);

        builder.setScopes(new String[]{
                SpotifyScope.USER_READ_PRIVATE,
                SpotifyScope.PLAYLIST_READ_COLLABORATIVE,
                SpotifyScope.PLAYLIST_MODIFY_PRIVATE,
                SpotifyScope.PLAYLIST_MODIFY_PUBLIC,
                SpotifyScope.USER_FOLLOW_MODIFY,
                SpotifyScope.PLAYLIST_READ_PRIVATE});

        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity((Activity) mLoginActivityActions, REQUEST_CODE, request);
    }

    public void onUserLoggedInSuccessfully(String accessToken) {
        mSharedPreferenceHelper.saveSpotifyToken(accessToken);

        SpotifyApi mSpotifyApi = new SpotifyApi();
        mSpotifyApi.setAccessToken(accessToken);
        mSpotifyService = mSpotifyApi.getService();

        mLoginActivityActions.showMainScreen();
    }



    public void onCheckIfUserIsLoggedIn() {
        if(mSharedPreferenceHelper.getCurrentSpotifyToken().length() != 0) {
            mLoginActivityActions.showMainScreen();
        }
    }
}
