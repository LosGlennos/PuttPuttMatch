package se.d2collective.puttputtmatch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import se.d2collective.puttputtmatch.R;
import se.d2collective.puttputtmatch.fragments.OrderPlayersFragment;

public class OrderPlayersActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_players);

        final OrderPlayersFragment orderPlayers = new OrderPlayersFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.player_order_list_container, orderPlayers, "fragment").commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.start_game_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long matchId = orderPlayers.startGame();
                Intent matchIntent = new Intent(OrderPlayersActivity.this, MatchActivity.class);
                matchIntent.putExtra("matchId", matchId);
                startActivity(matchIntent);
            }
        });
    }
}
