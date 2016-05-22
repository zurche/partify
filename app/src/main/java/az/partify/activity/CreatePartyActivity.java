package az.partify.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import az.partify.R;
import az.partify.controllers.CreatePartyController;
import az.partify.screen_actions.CreatePartyScreenActions;

public class CreatePartyActivity extends AppCompatActivity implements CreatePartyScreenActions {

    private CreatePartyController mCreatePartyController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);

        mCreatePartyController = new CreatePartyController(this);
    }
}
