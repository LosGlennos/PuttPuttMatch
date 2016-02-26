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
import se.d2collective.puttputtmatch.activities.OpponentActivity;
import se.d2collective.puttputtmatch.database.commands.ClubCommands;
import se.d2collective.puttputtmatch.database.tables.ClubTableContract;
import se.d2collective.puttputtmatch.util.StringValidator;

/**
 * Created by msve on 2016-02-25.
 */
public class OpponentCursorAdapter extends CursorAdapter {
    public OpponentCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.club_cursor_row, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView tvPlayerName = (TextView) view.findViewById(R.id.club);
        String name = cursor.getString(cursor.getColumnIndexOrThrow(ClubTableContract.ClubTable.COLUMN_NAME_CLUB_NAME));
        tvPlayerName.setText(name);
        final long clubId = cursor.getLong(cursor.getColumnIndex("_id"));

        ImageView deleteClubImageView = (ImageView) view.findViewById(R.id.delete_club);
        deleteClubImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(R.string.delete_club_question)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        boolean success = new ClubCommands(context).deleteClubById(clubId);
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
                            ((OpponentActivity) context).populateListItemsWithClubs();
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

        ImageView editClubImageView = (ImageView) view.findViewById(R.id.edit_club);
        editClubImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.edit_club_name);
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);

                builder.setView(input);

                builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = input.getText().toString();
                        if (StringValidator.isNameValid(name)) {
                            dialog.cancel();
                            boolean success = new ClubCommands(context).editClubNameById(name, clubId);
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
                                ((OpponentActivity) context).populateListItemsWithClubs();
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
