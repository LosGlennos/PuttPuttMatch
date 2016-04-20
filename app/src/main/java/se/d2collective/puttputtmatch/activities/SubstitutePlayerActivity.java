package se.d2collective.puttputtmatch.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import se.d2collective.puttputtmatch.R;
import se.d2collective.puttputtmatch.database.commands.MatchCommands;
import se.d2collective.puttputtmatch.database.queries.MatchQueries;
import se.d2collective.puttputtmatch.database.queries.PlayerQueries;
import se.d2collective.puttputtmatch.database.tables.PlayerTableContract;
import se.d2collective.puttputtmatch.models.adapters.PlayerCursorAdapter;
import se.d2collective.puttputtmatch.models.adapters.SubstitutePlayersAdapter;

public class SubstitutePlayerActivity extends AppCompatActivity {
    private long mMatchId;
    private long mPlayerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_substitute_player);
        mMatchId = getIntent().getLongExtra("matchId", 0);
        mPlayerId = getIntent().getLongExtra("playerId", 0);
        ListView playersList = (ListView) findViewById(R.id.substitute_players_list);

        populateListWithPlayers(playersList);
        setListItemOnClick(playersList);
    }

    private void setListItemOnClick(ListView playersList) {
        playersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                TextView substitutePlayerTextView = (TextView) view.findViewById(R.id.substitute_player_name);
                String substitutePlayer = (String) substitutePlayerTextView.getText();
                Cursor playerToBeSubstitutedCursor = new PlayerQueries(SubstitutePlayerActivity.this.getBaseContext()).getPlayerById(mPlayerId);
                String playerToBeSubstituted = "";
                if (playerToBeSubstitutedCursor != null && playerToBeSubstitutedCursor.moveToFirst()) {
                    playerToBeSubstituted = playerToBeSubstitutedCursor.getString(playerToBeSubstitutedCursor.getColumnIndex(PlayerTableContract.PlayerTable.COLUMN_NAME_PLAYER_NAME));
                }
                AlertDialog.Builder switchPlayerDialog = new AlertDialog.Builder(SubstitutePlayerActivity.this);
                switchPlayerDialog.setMessage("Do you want to substitute " + playerToBeSubstituted + " with " + substitutePlayer + "?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean substitutionSuccess = new MatchCommands(SubstitutePlayerActivity.this.getBaseContext()).substitutePlayer(mMatchId, mPlayerId, id);
                                Intent matchActivity = new Intent(SubstitutePlayerActivity.this, MatchActivity.class);
                                matchActivity.putExtra("matchId", mMatchId);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                switchPlayerDialog.show();
            }
        });
    }

    private void populateListWithPlayers(ListView playersList) {
        Cursor playerNotInMatchCursor = new MatchQueries(getBaseContext()).getPlayersNotInMatch(mMatchId);

        SubstitutePlayersAdapter adapter = new SubstitutePlayersAdapter(this, playerNotInMatchCursor);

        playersList.setAdapter(adapter);

        while (playerNotInMatchCursor.moveToNext()) {
            adapter.changeCursor(playerNotInMatchCursor);
        }
    }
}
