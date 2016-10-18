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
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.math.MathContext;

import se.d2collective.puttputtmatch.R;
import se.d2collective.puttputtmatch.activities.MatchActivity;
import se.d2collective.puttputtmatch.activities.SubstitutePlayerActivity;
import se.d2collective.puttputtmatch.database.commands.MatchCommands;
import se.d2collective.puttputtmatch.database.queries.MatchQueries;
import se.d2collective.puttputtmatch.database.tables.MatchPlayerTableContract;
import se.d2collective.puttputtmatch.database.tables.PlayerTableContract;
import se.d2collective.puttputtmatch.util.StringValidator;

/**
 * Created by msve on 2016-03-08.
 */
public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {
    CursorAdapter mCursorAdapter;
    Context mContext;
    MatchCommands mMatchCommands;
    MatchQueries mMatchQueries;
    LinearLayout mOverlay;
    View mView;

    public MatchAdapter(Context context, Cursor c) {
        mContext = context;
        mMatchCommands = new MatchCommands(context);
        mMatchQueries = new MatchQueries(context);
        mCursorAdapter = new CursorAdapter(mContext, c, 0) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.player_match_content, parent, false);
            }

            @Override
            public void bindView(View view, final Context context, Cursor cursor) {
                mView = view;
                mOverlay = (LinearLayout) view.findViewById(R.id.overlay);
                TextView textView = (TextView) view.findViewById(R.id.match_view_player_name);
                String playerName = cursor.getString(cursor.getColumnIndexOrThrow(PlayerTableContract.PlayerTable.COLUMN_NAME_PLAYER_NAME));
                textView.setText(playerName);

                FloatingActionButton playerFabPlus = (FloatingActionButton) view.findViewById(R.id.fab_player_plus);
                FloatingActionButton playerFabMinus = (FloatingActionButton) view.findViewById(R.id.fab_player_minus);
                final TextView playerScore = (TextView) view.findViewById(R.id.match_view_player_difference);
                final long playerId = cursor.getLong(cursor.getColumnIndexOrThrow(MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_PLAYER_ID));
                final long matchId = cursor.getLong(cursor.getColumnIndexOrThrow(MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_MATCH_ID));

                boolean hasFinishedPlaying = mMatchQueries.hasFinishedPlaying(matchId, playerId);

                initPlayerScore(view, matchId, playerId);

                if (hasFinishedPlaying) {
                    disableView(view);
                } else {
                    enableView(view);
                }

                this.notifyDataSetChanged();

                playerFabPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updatePlayerScore(playerId, matchId, playerScore, 1);
                    }
                });

                playerFabMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updatePlayerScore(playerId, matchId, playerScore, -1);
                    }
                });

                LinearLayout playerOptions = (LinearLayout) view.findViewById(R.id.match_player_options);
                playerOptions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPlayerOptions(matchId, playerId, v);
                    }
                });
            }
        };
    }

    private void initPlayerScore(View view, long matchId, long playerId) {
        String playerDifference = mMatchQueries.getPlayerDifference(matchId, playerId);
        String playerScore = mMatchQueries.getPlayerScore(matchId, playerId);

        TextView playerScoreView = (TextView) view.findViewById(R.id.match_player_score);
        TextView playerDifferenceView = (TextView) view.findViewById(R.id.match_view_player_difference);

        if (!"".equals(playerScore)) {
            playerScoreView.setText("Score: " + playerScore);
        }

        if (Integer.parseInt(playerDifference) > 0) {
            playerDifference = "+" + playerDifference;
        }

        playerDifferenceView.setText(playerDifference);
    }

    private void updatePlayerScore(long playerId, long matchId, TextView playerScore, int difference) {
        int playerDifference = mMatchCommands.updateScoreForPlayer(matchId, playerId, difference);
        String scoreString = playerDifference > 0 ? "+" + playerDifference : playerDifference + "";
        playerScore.setText(scoreString);
        ((MatchActivity) mContext).updateTotalScore(difference);
    }

    private void showPlayerOptions(final long matchId, final long playerId, final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle("Select an option")
                .setItems(R.array.match_player_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                boolean hasSubstituted = new MatchQueries(mContext).hasSubstituted(matchId);
                                if (hasSubstituted) {
                                    final AlertDialog.Builder alreadySubstitutedBuilder = new AlertDialog.Builder(mContext);
                                    alreadySubstitutedBuilder.setTitle("Error!");
                                    alreadySubstitutedBuilder.setMessage("Player already substituted this match!");
                                    alreadySubstitutedBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    alreadySubstitutedBuilder.show();
                                } else {
                                    Intent substitutePlayerIntent = new Intent(mContext, SubstitutePlayerActivity.class);
                                    substitutePlayerIntent.putExtra("matchId", matchId);
                                    substitutePlayerIntent.putExtra("playerId", playerId);
                                    mContext.startActivity(substitutePlayerIntent);
                                }
                                break;
                            case 1:
                                AlertDialog.Builder completeMatchDialog = new AlertDialog.Builder(mContext);
                                completeMatchDialog.setTitle("Specify score:");
                                final EditText input = new EditText(mContext);
                                input.setInputType(InputType.TYPE_CLASS_NUMBER);

                                completeMatchDialog.setView(input);

                                completeMatchDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int score = Integer.parseInt(input.getText().toString());
                                        boolean success = new MatchCommands(mContext).finishGameForPlayer(playerId, matchId, score);
                                        TextView playerScoreView = (TextView) view.getRootView().findViewById(R.id.match_player_score);
                                        playerScoreView.setText("Score: " + score);

                                        if (success) {
                                            disableView((RelativeLayout)((RelativeLayout) view.getParent()).getParent());
                                        }
                                    }
                                });

                                completeMatchDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                completeMatchDialog.show();
                                break;
                            default:
                                break;
                        }
                    }
                });
        AlertDialog dialog = builder.create();

        dialog.show();
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

    private void disableView(View parentView) {
        LinearLayout overlay = (LinearLayout) parentView.findViewById(R.id.overlay);
        overlay.setVisibility(View.VISIBLE);

        FloatingActionButton plusButton = (FloatingActionButton) parentView.findViewById(R.id.fab_player_plus);
        FloatingActionButton minusButton = (FloatingActionButton) parentView.findViewById(R.id.fab_player_minus);
        LinearLayout playerOptions = (LinearLayout) parentView.findViewById(R.id.match_player_options);

        playerOptions.setEnabled(false);
        plusButton.setEnabled(false);
        minusButton.setEnabled(false);
    }

    private void enableView(View parentView) {
        LinearLayout overlay = (LinearLayout) parentView.findViewById(R.id.overlay);
        overlay.setVisibility(View.INVISIBLE);

        FloatingActionButton plusButton = (FloatingActionButton) parentView.findViewById(R.id.fab_player_plus);
        FloatingActionButton minusButton = (FloatingActionButton) parentView.findViewById(R.id.fab_player_minus);
        LinearLayout playerOptions = (LinearLayout) parentView.findViewById(R.id.match_player_options);

        playerOptions.setEnabled(true);
        plusButton.setEnabled(true);
        minusButton.setEnabled(true);
    }
}
