package az.partify.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import az.partify.R;
import az.partify.controllers.SearchPartyController;
import az.partify.screen_actions.SearchPartyScreenActions;

public class SearchPartyActivity extends AppCompatActivity implements SearchPartyScreenActions {

    private SearchPartyController mSearchPartyController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_party);

        mSearchPartyController = new SearchPartyController(this);
    }
}
