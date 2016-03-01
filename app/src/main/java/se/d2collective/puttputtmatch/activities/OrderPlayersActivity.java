package se.d2collective.puttputtmatch.activities;

import android.database.Cursor;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;

import se.d2collective.puttputtmatch.R;
import se.d2collective.puttputtmatch.database.queries.PlayerQueries;
import se.d2collective.puttputtmatch.models.adapters.PlayerCursorAdapter;

public class OrderPlayersActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_players);

        Cursor players = new PlayerQueries(this).getAllPlayers();
        ArrayList<Pair<Long, String>> playersList = new ArrayList<>();


        DragListView playerOrder = (DragListView) findViewById(R.id.player_order_list);
        playerOrder.setDragListListener(new DragListView.DragListListener() {
            @Override
            public void onItemDragStarted(int position) {

            }

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition) {

            }
        });
    }
}
