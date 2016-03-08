package se.d2collective.puttputtmatch.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.woxthebox.draglistview.DragItem;
import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;
import java.util.List;

import se.d2collective.puttputtmatch.R;
import se.d2collective.puttputtmatch.database.commands.MatchCommands;
import se.d2collective.puttputtmatch.models.Player;
import se.d2collective.puttputtmatch.models.adapters.OrderPlayersAdapter;

/**
 * Created by msve on 2016-03-02.
 */
public class OrderPlayersFragment extends android.support.v4.app.Fragment {
    private ArrayList<Player> mItemArray;
    private DragListView mDragListView;
    private OrderPlayersAdapter mOrderPlayersAdapter;
    private long mOpponentId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mItemArray = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_order_players, container, false);
        mDragListView = (DragListView) view.findViewById(R.id.player_order_list);
        mDragListView.getRecyclerView().setVerticalScrollBarEnabled(true);
        mDragListView.setDragListListener(new DragListView.DragListListener() {
            @Override
            public void onItemDragStarted(int position) {
            }

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition) {
            }
        });

        mItemArray = (ArrayList) getActivity().getIntent().getExtras().get("players");
        mOpponentId = getActivity().getIntent().getLongExtra("clubId", 0);

        setupListRecyclerView();
        return view;
    }

    private void setupListRecyclerView() {
        ArrayList<Pair<Long, String>> playerPairList = new ArrayList<>();
        for (Player player : mItemArray) {
            playerPairList.add(new Pair<>(player.getId(), player.getName()));
        }
        mDragListView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        mOrderPlayersAdapter = new OrderPlayersAdapter(playerPairList, R.layout.player_order_row, R.id.player_order_row);
        mDragListView.setAdapter(mOrderPlayersAdapter, true);
        mDragListView.setCanDragHorizontally(false);
        mDragListView.setCustomDragItem(new MyDragItem(getActivity().getBaseContext(), R.layout.player_order_row));
    }

    public long startGame() {
        List<Pair<Long, String>> playersList = mOrderPlayersAdapter.getItemList();
        return new MatchCommands(getActivity().getBaseContext()).startGame(playersList, mOpponentId);
    }

    private static class MyDragItem extends DragItem {

        public MyDragItem(Context context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        public void onBindDragView(View clickedView, View dragView) {
            CharSequence text = ((TextView) clickedView.findViewById(R.id.player_order_name)).getText();
            ((TextView) dragView.findViewById(R.id.player_order_name)).setText(text);
            dragView.setBackgroundColor(Color.WHITE);
        }
    }
}
