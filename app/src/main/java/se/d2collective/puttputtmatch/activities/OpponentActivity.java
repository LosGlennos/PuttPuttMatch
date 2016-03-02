package se.d2collective.puttputtmatch.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import se.d2collective.puttputtmatch.models.adapters.OpponentCursorAdapter;
import se.d2collective.puttputtmatch.R;
import se.d2collective.puttputtmatch.database.commands.ClubCommands;
import se.d2collective.puttputtmatch.database.queries.ClubQueries;
import se.d2collective.puttputtmatch.models.enums.PlayerActionEnums;
import se.d2collective.puttputtmatch.util.StringValidator;

public class OpponentActivity extends AppCompatActivity {

    private ProgressDialog mSpinnerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Choose opponent");
        setContentView(R.layout.activity_opponent);

        mSpinnerDialog = ProgressDialog.show(OpponentActivity.this, "", "Loading. Please wait...", true);
        mSpinnerDialog.setCancelable(false);

        populateListItemsWithClubs();

        ListView clubList = (ListView) findViewById(R.id.clubList);
        clubList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clubName = ((TextView) view.findViewById(R.id.club)).getText().toString();
                showStartMatchDialog(id, clubName);
            }
        });
    }

    private void showStartMatchDialog(final long id, String clubName) {
        AlertDialog.Builder startMatchDialog = new AlertDialog.Builder(OpponentActivity.this);
        startMatchDialog.setMessage("Start match against " + clubName + "?");
        startMatchDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent choosePlayersIntent = new Intent(getBaseContext(), ChoosePlayersActivity.class);
                choosePlayersIntent.putExtra("clubId", id);
                startActivity(choosePlayersIntent);
            }
        });
        startMatchDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.opponent_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_club:
                showAddClubDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAddClubDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OpponentActivity.this);
        builder.setTitle(R.string.add_club);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        builder.setView(input);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                if (StringValidator.isNameValid(name)) {
                    boolean success = new ClubCommands(getBaseContext()).addClub(name);
                    if (!success) {
                        dialog.cancel();
                        AlertDialog.Builder errorDialog = new AlertDialog.Builder(getParent().getBaseContext());
                        errorDialog.setMessage("Something went wrong, try again.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        errorDialog.show();
                    } else {
                        populateListItemsWithClubs();
                        dialog.cancel();
                    }
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

    public void populateListItemsWithClubs() {
        Cursor clubCursor = new ClubQueries(getBaseContext()).getAllClubs();

        OpponentCursorAdapter adapter = new OpponentCursorAdapter(this, clubCursor);

        ListView playersList = (ListView) findViewById(R.id.clubList);
        playersList.setAdapter(adapter);

        while (clubCursor.moveToNext()) {
            adapter.changeCursor(clubCursor);
        }
        mSpinnerDialog.cancel();
    }
}
