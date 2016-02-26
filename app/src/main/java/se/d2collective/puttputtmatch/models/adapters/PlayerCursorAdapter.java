package se.d2collective.puttputtmatch.models.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import se.d2collective.puttputtmatch.R;
import se.d2collective.puttputtmatch.activities.PlayersActivity;
import se.d2collective.puttputtmatch.database.commands.PlayerCommands;
import se.d2collective.puttputtmatch.database.tables.PlayerTableContract;
import se.d2collective.puttputtmatch.models.enums.PlayerActionEnums;
import se.d2collective.puttputtmatch.util.StringValidator;

/**
 * Created by msve on 2016-02-25.
 */
public class PlayerCursorAdapter extends CursorAdapter {
    private PlayerActionEnums mActionEnum;

    public PlayerCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.player_cursor_row, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView tvPlayerName = (TextView) view.findViewById(R.id.player);
        String name = cursor.getString(cursor.getColumnIndexOrThrow(PlayerTableContract.PlayerTable.COLUMN_NAME_PLAYER_NAME));
        tvPlayerName.setText(name);
        final long playerId = cursor.getLong(cursor.getColumnIndex("_id"));

        ImageView deletePlayerImageView = (ImageView) view.findViewById(R.id.delete_player);
        deletePlayerImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.delete_player_question)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                boolean success = new PlayerCommands(context).deletePlayerById(playerId);
                                if (!success) {
                                    AlertDialog.Builder errorDialog = new AlertDialog.Builder(context);
                                    errorDialog.setMessage("Something went wrong, try again.")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                    errorDialog.show();
                                } else {
                                    ((PlayersActivity) context).populateListItemsWithPlayers();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();
            }
        });

        ImageView editPlayerImageView = (ImageView) view.findViewById(R.id.edit_player);
        editPlayerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setTitle(R.string.edit_player_name);
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);

                builder.setView(input);

                builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = input.getText().toString();
                        if (StringValidator.isNameValid(name)) {
                            dialog.cancel();
                            boolean success = new PlayerCommands(context).editPlayerNameById(name, playerId);
                            if (!success) {
                                AlertDialog.Builder errorDialog = new AlertDialog.Builder(context);
                                errorDialog.setMessage("Something went wrong, try again.")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                errorDialog.show();
                            } else {
                                ((PlayersActivity) context).populateListItemsWithPlayers();
                            }
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }
}
