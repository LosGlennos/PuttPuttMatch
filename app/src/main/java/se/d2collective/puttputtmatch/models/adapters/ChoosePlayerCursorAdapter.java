package se.d2collective.puttputtmatch.models.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import se.d2collective.puttputtmatch.R;
import se.d2collective.puttputtmatch.activities.ChoosePlayersActivity;
import se.d2collective.puttputtmatch.activities.PlayersActivity;
import se.d2collective.puttputtmatch.database.commands.PlayerCommands;
import se.d2collective.puttputtmatch.database.tables.PlayerTableContract;
import se.d2collective.puttputtmatch.models.enums.PlayerActionEnums;
import se.d2collective.puttputtmatch.util.StringValidator;

/**
 * Created by msve on 2016-02-25.
 */
public class ChoosePlayerCursorAdapter extends CursorAdapter {

    public ChoosePlayerCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.choose_player_cursor_row, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView tvPlayerName = (TextView) view.findViewById(R.id.player_choice_name);
        String name = cursor.getString(cursor.getColumnIndexOrThrow(PlayerTableContract.PlayerTable.COLUMN_NAME_PLAYER_NAME));
        tvPlayerName.setText(name);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox playerCheckBox = (CheckBox) v.findViewById(R.id.choose_player);
                playerCheckBox.performClick();
                ((ChoosePlayersActivity) context).showOrHideFab();
            }
        });
    }
}
