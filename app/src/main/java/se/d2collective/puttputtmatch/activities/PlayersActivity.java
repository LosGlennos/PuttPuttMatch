package se.d2collective.puttputtmatch.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import se.d2collective.puttputtmatch.models.adapters.PlayerCursorAdapter;
import se.d2collective.puttputtmatch.R;
import se.d2collective.puttputtmatch.database.commands.PlayerCommands;
import se.d2collective.puttputtmatch.database.queries.PlayerQueries;
import se.d2collective.puttputtmatch.models.enums.PlayerActionEnums;
import se.d2collective.puttputtmatch.util.StringValidator;

public class PlayersActivity extends AppCompatActivity {
    private ProgressDialog mSpinnerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        setTitle("Players");

        mSpinnerDialog = ProgressDialog.show(PlayersActivity.this, "", "Loading. Please wait...", true);
        mSpinnerDialog.setCancelable(false);

        populateListItemsWithPlayers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.player_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_player:
                showAddPlayerDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void populateListItemsWithPlayers() {
        Cursor playerCursor = new PlayerQueries(getBaseContext()).getAllPlayers();

        PlayerCursorAdapter adapter = new PlayerCursorAdapter(this, playerCursor);

        ListView playersList = (ListView) findViewById(R.id.playerList);
        playersList.setAdapter(adapter);

        while (playerCursor.moveToNext()) {
            adapter.changeCursor(playerCursor);
        }
        mSpinnerDialog.cancel();
    }

    public void showAddPlayerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayersActivity.this);
        builder.setTitle(R.string.add_player);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        builder.setView(input);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                if (StringValidator.isNameValid(name)) {
                    addPlayerToDatabase(input.getText().toString());
                    dialog.cancel();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }



    public void addPlayerToDatabase(String name) {
        mSpinnerDialog = ProgressDialog.show(PlayersActivity.this, "", "Loading. Please wait...", true);
        mSpinnerDialog.setCancelable(false);

        boolean success = new PlayerCommands(getBaseContext()).addPlayer(name);

        if (success) {
            populateListItemsWithPlayers();
        } else {
            AlertDialog.Builder errorDialog = new AlertDialog.Builder(getBaseContext());
            errorDialog.setMessage("Something went wrong, try again.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            errorDialog.show();
        }
    }
}
