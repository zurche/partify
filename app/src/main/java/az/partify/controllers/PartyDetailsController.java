package az.partify.controllers;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import az.partify.model.PartifyTrack;
import az.partify.model.Party;
import az.partify.screen_actions.PartyDetailsScreenActions;

/**
 * Created by az on 29/05/16.
 */
public class PartyDetailsController {

    private final PartyDetailsScreenActions mPartyDetailsScreenActions;
    private Party mCurrentParty;

    public PartyDetailsController(PartyDetailsScreenActions partyDetailsScreenActions,
                                  Party currentParty) {
        mPartyDetailsScreenActions = partyDetailsScreenActions;
        mCurrentParty = currentParty;
    }

    public void onUserClicksAddTrack() {
        mPartyDetailsScreenActions.showSearchTrackScreen();
    }

    public void onUserAddedTrack(PartifyTrack trackToAdd) {
        updateParty(trackToAdd);
    }

    private void updateParty(final PartifyTrack trackToAdd) {
        mCurrentParty.addTrack(trackToAdd);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("parties");

        myRef.child(mCurrentParty.name).setValue(mCurrentParty, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                mPartyDetailsScreenActions.updateCurrentTrackList();
            }
        });
    }
}
