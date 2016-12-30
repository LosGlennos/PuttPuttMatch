package se.d2collective.puttputtmatch.database.queries;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import se.d2collective.puttputtmatch.database.DatabaseConnection;
import se.d2collective.puttputtmatch.database.tables.UserClubTableContract;

/**
 * Created by Martin on 2016-12-30.
 */

public class UserClubQueries {
    private final DatabaseConnection mDbConnection;

    public UserClubQueries(Context context) {
        mDbConnection = DatabaseConnection.getInstance(context);
    }

    public Cursor getUserClub() {
        SQLiteDatabase db = mDbConnection.getReadableDatabase();

        String sqlQuery = "SELECT * FROM UserClub";
        return db.rawQuery(sqlQuery, new String[] {});
    }
}
