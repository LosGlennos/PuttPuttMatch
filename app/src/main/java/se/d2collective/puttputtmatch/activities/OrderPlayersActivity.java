package se.d2collective.puttputtmatch.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;

import se.d2collective.puttputtmatch.R;
import se.d2collective.puttputtmatch.database.queries.PlayerQueries;
import se.d2collective.puttputtmatch.fragments.OrderPlayersFragment;
import se.d2collective.puttputtmatch.models.adapters.OrderPlayersAdapter;
import se.d2collective.puttputtmatch.models.adapters.PlayerCursorAdapter;

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
                orderPlayers.startGame();
            }
        });
    }
}
