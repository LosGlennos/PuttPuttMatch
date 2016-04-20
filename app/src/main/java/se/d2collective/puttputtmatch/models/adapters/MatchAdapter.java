package se.d2collective.puttputtmatch.models.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import se.d2collective.puttputtmatch.R;
import se.d2collective.puttputtmatch.activities.MatchActivity;
import se.d2collective.puttputtmatch.activities.SubstitutePlayerActivity;
import se.d2collective.puttputtmatch.database.commands.MatchCommands;
import se.d2collective.puttputtmatch.database.tables.MatchPlayerTableContract;
import se.d2collective.puttputtmatch.database.tables.PlayerTableContract;

/**
 * Created by msve on 2016-03-08.
 */
public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {
    CursorAdapter mCursorAdapter;
    Context mContext;
    MatchCommands mMatchCommands;

    public MatchAdapter(Context context, Cursor c) {
        mContext = context;
        mMatchCommands = new MatchCommands(context);
        mCursorAdapter = new CursorAdapter(mContext, c, 0) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.player_match_content, parent, false);
            }

            @Override
            public void bindView(View view, final Context context, Cursor cursor) {
                TextView textView = (TextView) view.findViewById(R.id.match_view_player_name);
                String playerName = cursor.getString(cursor.getColumnIndexOrThrow(PlayerTableContract.PlayerTable.COLUMN_NAME_PLAYER_NAME));
                textView.setText(playerName);

                FloatingActionButton playerFabPlus = (FloatingActionButton) view.findViewById(R.id.fab_player_plus);
                FloatingActionButton playerFabMinus = (FloatingActionButton) view.findViewById(R.id.fab_player_minus);
                final TextView playerScore = (TextView) view.findViewById(R.id.match_view_player_difference);
                final long playerId = cursor.getLong(cursor.getColumnIndexOrThrow(MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_PLAYER_ID));
                final long matchId = cursor.getLong(cursor.getColumnIndexOrThrow(MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_MATCH_ID));

                playerFabPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    int playerDifference = mMatchCommands.updateScoreForPlayer(matchId, playerId, 1);
                    String scoreString = playerDifference > 0 ? "+" + playerDifference : playerDifference + "";
                    playerScore.setText(scoreString);
                    ((MatchActivity)mContext).addOneToTotalScore();
                    }
                });

                playerFabMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int playerDifference = mMatchCommands.updateScoreForPlayer(matchId, playerId, -1);
                        String scoreString = playerDifference > 0 ? "+" + playerDifference : playerDifference + "";
                        playerScore.setText(scoreString);
                        ((MatchActivity)mContext).deductOneFromTotalScore();
                    }
                });

                LinearLayout playerOptions = (LinearLayout) view.findViewById(R.id.match_player_options);
                playerOptions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("Select an option")
                                .setItems(R.array.match_player_options, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0) {
                                            Intent substitutePlayerIntent = new Intent(mContext, SubstitutePlayerActivity.class);
                                            substitutePlayerIntent.putExtra("matchId", matchId);
                                            substitutePlayerIntent.putExtra("playerId", playerId);
                                            mContext.startActivity(substitutePlayerIntent);
                                        } else if (which == 1) {
                                            //Complete match
                                        }
                                    }
                                });
                        Dialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View v1;

        public ViewHolder(View itemView) {
            super(itemView);
            v1 = itemView.findViewById(R.id.matchRecyclerView);
        }
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Passing the binding operation to cursor loader
        mCursorAdapter.getCursor().moveToPosition(position);
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Passing the inflater job to the cursor-adapter
        View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        return new ViewHolder(v);
    }
}
