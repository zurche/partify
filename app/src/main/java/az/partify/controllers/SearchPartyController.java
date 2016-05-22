package az.partify.controllers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import az.partify.activity.SearchPartyActivity;
import az.partify.model.Party;
import az.partify.screen_actions.SearchPartyScreenActions;

/**
 * Created by az on 22/05/16.
 */
public class SearchPartyController {

    private final Context mContext;

    public SearchPartyController(Context context) {
        mContext = context;
    }

    public void onRetrievePartyList(float latitude, float longitude, SearchPartyActivity searchPartyActivity) {
        final Party currentLocation = new Party();
        currentLocation.latitude = latitude;
        currentLocation.longitude = longitude;

        List<Address> addresses = null;

        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String streetAddress = "";
        if (addresses.size() == 1) {
            streetAddress = addresses.get(0).getAddressLine(0);
        }

        final ArrayList<Party> parties = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("parties");

        myRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot partySnapshot : dataSnapshot.getChildren()) {
                            Party party = partySnapshot.getValue(Party.class);
                            if(distanceFrom(currentLocation, party) < 100) {
                                parties.add(party);
                            }
                        }
                        ((SearchPartyScreenActions) mContext).refreshPartyListInScreen(parties);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Search", "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    public static float distanceFrom(Party pointA, Party pointB) {
        float lat1 = pointA.latitude;
        float lng1 = pointA.longitude;
        float lat2 = pointB.latitude;
        float lng2 = pointB.longitude;

        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
    }
}
