package se.d2collective.puttputtmatch.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import se.d2collective.puttputtmatch.R;
import se.d2collective.puttputtmatch.database.commands.UserClubCommands;
import se.d2collective.puttputtmatch.database.queries.UserClubQueries;
import se.d2collective.puttputtmatch.database.tables.UserClubTableContract;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Cursor userClubCursor = new UserClubQueries(getBaseContext()).getUserClub();

        while (userClubCursor.moveToNext()) {
            String name = userClubCursor.getString(userClubCursor.getColumnIndex(UserClubTableContract.UserClubTable.COLUMN_NAME_CLUB_NAME));

            if (name != null) {
                Intent mainActivity = new Intent(this, MainActivity.class);
                startActivity(mainActivity);
            }
        }
    }

    public void saveClubName(View view) {
        String name = ((EditText)(findViewById(R.id.club_name_input))).getText().toString();

        if (name.isEmpty()) {
            AlertDialog.Builder errorDialog = new AlertDialog.Builder(getBaseContext());
            errorDialog.setMessage("You need to specify a name")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            errorDialog.show();
        } else {
            new UserClubCommands(getBaseContext()).addUserClub(name);
            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
        }
    }
}
