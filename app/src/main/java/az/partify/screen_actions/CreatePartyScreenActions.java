package az.partify.screen_actions;

/**
 * Created by az on 22/05/16.
 */
public interface CreatePartyScreenActions {
    void updateLocationUI(String locationText);

    void showError(String error);

    void showPartyCreatedScreen();

    void showSearchSongScreen();
}
