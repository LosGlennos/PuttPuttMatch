package se.d2collective.puttputtmatch.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import se.d2collective.puttputtmatch.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void chooseOpponent(View view) {
        Intent chooseOpponentIntent = new Intent(this, OpponentActivity.class);
        startActivity(chooseOpponentIntent);
    }

    public void showPlayers(View view) {
        Intent showPlayersIntent = new Intent(this, PlayersActivity.class);
        startActivity(showPlayersIntent);
    }
}
