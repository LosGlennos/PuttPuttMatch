package se.d2collective.puttputtmatch.activities;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import se.d2collective.puttputtmatch.R;
import se.d2collective.puttputtmatch.database.queries.MatchQueries;
import se.d2collective.puttputtmatch.models.adapters.MatchAdapter;

public class MatchActivity extends Activity {
    private int mTotalScore = 0;
    private TextView mTotalScoreView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        long matchId = getIntent().getLongExtra("matchId", 0);
        mTotalScoreView = (TextView) findViewById(R.id.match_total_difference);
        populateViewWithPlayers(matchId);
    }

    private void populateViewWithPlayers(long matchId) {
        Cursor matchPlayerCursor = new MatchQueries(getBaseContext()).getPlayersForMatch(matchId);

        RecyclerView matchRecyclerView = (RecyclerView) findViewById(R.id.matchRecyclerView);
        matchRecyclerView.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        matchRecyclerView.setLayoutManager(llm);

        MatchAdapter adapter = new MatchAdapter(this, matchPlayerCursor);
        matchRecyclerView.setAdapter(adapter);
    }

    public void changeScore(int amount) {
        mTotalScore += amount;
        String totalScoreString = mTotalScore > 0 ? "+" + mTotalScore : mTotalScore + "";
        mTotalScoreView.setText(totalScoreString);
    }

    public void addOneToTotalScore() {
        changeScore(1);
    }

    public void deductOneFromTotalScore() {
        changeScore(-1);
    }
}
