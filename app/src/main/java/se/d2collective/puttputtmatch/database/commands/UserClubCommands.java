package se.d2collective.puttputtmatch.database.commands;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import se.d2collective.puttputtmatch.database.DatabaseConnection;
import se.d2collective.puttputtmatch.database.tables.UserClubTableContract;

public class UserClubCommands {
    private final DatabaseConnection mDbConnection;

    public UserClubCommands(Context context) {
        mDbConnection = DatabaseConnection.getInstance(context);
    }

    public boolean addUserClub(String name) {
        SQLiteDatabase db = mDbConnection.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserClubTableContract.UserClubTable.COLUMN_NAME_CLUB_NAME, name);

        return db.insert(UserClubTableContract.UserClubTable.TABLE_NAME, null, values) != -1;
    }
}
