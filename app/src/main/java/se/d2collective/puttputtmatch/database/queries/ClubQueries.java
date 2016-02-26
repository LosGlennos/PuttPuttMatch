package se.d2collective.puttputtmatch.database.queries;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import se.d2collective.puttputtmatch.database.DatabaseConnection;
import se.d2collective.puttputtmatch.database.tables.ClubTableContract;
import se.d2collective.puttputtmatch.database.tables.PlayerTableContract;

/**
 * Created by msve on 2016-02-25.
 */
public class ClubQueries {
    private final DatabaseConnection mDbConnection;

    public ClubQueries(Context context) {
        mDbConnection = new DatabaseConnection(context);
    }

    public Cursor getAllClubs() {
        SQLiteDatabase db = mDbConnection.getReadableDatabase();
        String[] projection = {
                ClubTableContract.ClubTable._ID,
                ClubTableContract.ClubTable.COLUMN_NAME_CLUB_NAME
        };

        String sortOrder = ClubTableContract.ClubTable._ID + " ASC";

        return db.query(
                ClubTableContract.ClubTable.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }
}
