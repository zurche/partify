package az.partify.controllers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import az.partify.screen_actions.LoginScreenActions;
import az.partify.util.SharedPreferenceHelper;
import az.partify.util.SpotifyScope;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by az on 22/05/16.
 */
public class LoginActivityController {

    private static final String TAG = LoginActivityController.class.getSimpleName();

    private static final String CLIENT_ID = "331d62158fe642528cf0b22a5a90aa12";
    private static final String REDIRECT_URI = "yourcustomprotocol://callback";
    public static final int REQUEST_CODE = 1;

    private final SharedPreferenceHelper mSharedPreferenceHelper;
    private Context mContext;

    public LoginActivityController(Context context) {
        mContext = context;
        mSharedPreferenceHelper = new SharedPreferenceHelper(context);
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
        AuthenticationClient.openLoginActivity((Activity) mContext, REQUEST_CODE, request);
    }

    public void onUserLoggedInSuccessfully(String accessToken) {
        mSharedPreferenceHelper.saveSpotifyToken(accessToken);

        SpotifyApi mSpotifyApi = new SpotifyApi();
        mSpotifyApi.setAccessToken(accessToken);
        SpotifyService spotifyService = mSpotifyApi.getService();

        spotifyService.getMe(new Callback<UserPrivate>() {
            @Override
            public void success(UserPrivate userPrivate, Response response) {
                Log.d(TAG, "Obtained User Information.");

                mSharedPreferenceHelper.saveCurrentUserId(userPrivate.id);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "FAILED to Obtain User Information.");
            }
        });

        ((LoginScreenActions) mContext).showMainScreen();
    }

    public void onCheckIfUserIsLoggedIn() {
        if(mSharedPreferenceHelper.getCurrentSpotifyToken().length() != 0) {
            ((LoginScreenActions) mContext).showMainScreen();
        }
    }

    public void onRefreshCurrentLocation() {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        System.out.println("");
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        System.out.println("");
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        System.out.println("");
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        System.out.println("");
                    }
                });
    }
}
