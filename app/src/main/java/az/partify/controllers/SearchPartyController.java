package az.partify.controllers;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import az.partify.model.Party;
import az.partify.screen_actions.SearchPartyScreenActions;

/**
 * Created by az on 22/05/16.
 */
public class SearchPartyController {

    private final SearchPartyScreenActions mSearchPartyScreenActions;

    public SearchPartyController(SearchPartyScreenActions searchPartyScreenActions) {
        mSearchPartyScreenActions = searchPartyScreenActions;
    }

    public void onRetrievePartyList() {
        final ArrayList<Party> parties = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("parties");

        myRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot partySnapshot : dataSnapshot.getChildren()) {
                            Party party = partySnapshot.getValue(Party.class);
                            parties.add(party);
                        }
                        mSearchPartyScreenActions.refreshPartyListInScreen(parties);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Search", "getUser:onCancelled", databaseError.toException());
                    }
                });
    }
}
