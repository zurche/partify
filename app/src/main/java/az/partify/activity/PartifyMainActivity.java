package az.partify.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import az.partify.R;
import az.partify.controllers.PartifyMainController;
import az.partify.screen_actions.PartifyMainScreenActions;

public class PartifyMainActivity extends AppCompatActivity implements PartifyMainScreenActions {

    private Button mCreatePartyButton;
    private Button mSearchPartyButton;

    private PartifyMainController mPartifyMainController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partify_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPartifyMainController = new PartifyMainController(this);

        mCreatePartyButton = (Button) findViewById(R.id.create_party_button);
        mSearchPartyButton = (Button) findViewById(R.id.search_party_button);

        mCreatePartyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPartifyMainController.onCreateParty();
            }
        });

        mSearchPartyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPartifyMainController.onSearchParty();
            }
        });
    }

}
