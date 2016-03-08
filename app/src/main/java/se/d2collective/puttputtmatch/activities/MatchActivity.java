package se.d2collective.puttputtmatch.activities;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import se.d2collective.puttputtmatch.R;
import se.d2collective.puttputtmatch.database.commands.MatchCommands;
import se.d2collective.puttputtmatch.database.queries.MatchQueries;
import se.d2collective.puttputtmatch.models.adapters.MatchAdapter;

public class MatchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        long matchId = getIntent().getLongExtra("matchId", 0);

        populateViewWithPlayers(matchId);
    }

    private void populateViewWithPlayers(long matchId) {
        Cursor matchPlayerCursor = new MatchQueries(getBaseContext()).getPlayersForMatch(matchId);

        RecyclerView matchRecyclerView = (RecyclerView) findViewById(R.id.matchRecyclerView);
        matchRecyclerView.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        matchRecyclerView.setLayoutManager(llm);

        MatchAdapter adapter = new MatchAdapter(getBaseContext(), matchPlayerCursor);
        matchRecyclerView.setAdapter(adapter);
    }
}
