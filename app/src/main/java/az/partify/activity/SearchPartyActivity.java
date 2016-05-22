package az.partify.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import az.partify.R;
import az.partify.adapter.PartiesAdapter;
import az.partify.controllers.SearchPartyController;
import az.partify.model.Party;
import az.partify.screen_actions.SearchPartyScreenActions;

public class SearchPartyActivity extends AppCompatActivity implements SearchPartyScreenActions {

    private SearchPartyController mSearchPartyController;
    private ListView mPartiesListView;
    private ProgressBar mLoadingPartiesProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_party);

        mSearchPartyController = new SearchPartyController(this);

        mPartiesListView = (ListView) findViewById(R.id.parties_list);
        mLoadingPartiesProgress = (ProgressBar) findViewById(R.id.loading_parties_list);

        mSearchPartyController.onRetrievePartyList();
    }

    @Override
    public void refreshPartyListInScreen(ArrayList<Party> parties) {
        PartiesAdapter mPartiesAdapter = new PartiesAdapter(this, parties);

        mPartiesListView.setAdapter(mPartiesAdapter);

        mLoadingPartiesProgress.setVisibility(View.GONE);
    }
}
