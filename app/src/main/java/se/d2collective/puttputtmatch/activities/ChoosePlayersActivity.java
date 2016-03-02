package se.d2collective.puttputtmatch.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import se.d2collective.puttputtmatch.R;
import se.d2collective.puttputtmatch.database.queries.PlayerQueries;
import se.d2collective.puttputtmatch.models.Player;
import se.d2collective.puttputtmatch.models.adapters.ChoosePlayerCursorAdapter;

public class ChoosePlayersActivity extends AppCompatActivity {
    private ProgressDialog mSpinnerDialog;
    private ArrayList<Player> mPlayersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_players);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Choose players");

        mSpinnerDialog = ProgressDialog.show(ChoosePlayersActivity.this, "", "Loading. Please wait...", true);
        mSpinnerDialog.setCancelable(false);

        mPlayersList = new ArrayList<>();

        final long opponentId = getIntent().getLongExtra("clubId", 0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent orderPlayersIntent = new Intent(getBaseContext(), OrderPlayersActivity.class);
                orderPlayersIntent.putExtra("players", mPlayersList);
                orderPlayersIntent.putExtra("clubId", opponentId);
                startActivity(orderPlayersIntent);
            }
        });

        fab.hide();
        populateListItemsWithPlayers();
    }

    public void populateListItemsWithPlayers() {
        Cursor playerCursor = new PlayerQueries(getBaseContext()).getAllPlayers();

        ChoosePlayerCursorAdapter adapter = new ChoosePlayerCursorAdapter(this, playerCursor);

        ListView playersList = (ListView) findViewById(R.id.player_choices);
        playersList.setAdapter(adapter);

        while (playerCursor.moveToNext()) {
            adapter.changeCursor(playerCursor);
        }
        mSpinnerDialog.cancel();
    }

    public void showOrHideFab() {
        ListView listView = (ListView) findViewById(R.id.player_choices);
        int numberOfPlayersChosen = 0;

        for(int i = 0; i < listView.getCount(); i++) {
            View v = listView.getChildAt(i);
            CheckBox cb = (CheckBox) v.findViewById(R.id.choose_player);
            if (cb.isChecked()) {
                numberOfPlayersChosen = numberOfPlayersChosen + 1;
            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (numberOfPlayersChosen == 5) {
            fab.show();
        } else {
            fab.hide();
        }
    }

    public void addOrDeletePlayerFromList(long playerId, String name) {
        Player player = new Player(playerId, name);

        if (mPlayersList.contains(player)) {
            for (int i = 0; i < mPlayersList.size(); i++) {
                Player playerInList = mPlayersList.get(i);
                if (playerInList.getId() == playerId) {
                    mPlayersList.remove(i);
                    break;
                }
            }
        } else {
            mPlayersList.add(player);
        }
    }
}
