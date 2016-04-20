package se.d2collective.puttputtmatch.database.queries;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import se.d2collective.puttputtmatch.database.DatabaseConnection;
import se.d2collective.puttputtmatch.database.tables.PlayerTableContract;

/**
 * Created by msve on 2016-02-25.
 */
public class PlayerQueries {
    private DatabaseConnection mDbConnection;

    public PlayerQueries(Context context) {
        mDbConnection = new DatabaseConnection(context);
    }

    public Cursor getAllPlayers() {
        SQLiteDatabase db = mDbConnection.getReadableDatabase();
        String[] projection = {
                PlayerTableContract.PlayerTable._ID,
                PlayerTableContract.PlayerTable.COLUMN_NAME_PLAYER_NAME
        };

        String sortOrder = PlayerTableContract.PlayerTable._ID + " ASC";

        return db.query(
                PlayerTableContract.PlayerTable.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }

    public Cursor getPlayerById(long id) {
        SQLiteDatabase db = mDbConnection.getReadableDatabase();

        String sqlQuery = "SELECT * FROM Players WHERE _id = ?";

        return db.rawQuery(sqlQuery, new String[]{String.valueOf(id)});
    }
}
