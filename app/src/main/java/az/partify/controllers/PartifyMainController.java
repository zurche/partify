package az.partify.controllers;

import az.partify.screen_actions.PartifyMainScreenActions;

/**
 * Created by az on 22/05/16.
 */
public class PartifyMainController {

    private final PartifyMainScreenActions mPartifyScreenActions;

    public PartifyMainController(PartifyMainScreenActions partifyMainScreenActions) {
        mPartifyScreenActions = partifyMainScreenActions;
    }

    public void onCreateParty() {
        mPartifyScreenActions.showCreatePartyScreen();
    }

    public void onSearchParty() {
        mPartifyScreenActions.showSearchPartyScreen();
    }

}
