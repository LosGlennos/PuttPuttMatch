package se.d2collective.puttputtmatch.database.queries;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import se.d2collective.puttputtmatch.database.DatabaseConnection;

/**
 * Created by msve on 2016-03-08.
 */
public class MatchQueries {
    private final DatabaseConnection mDbConnection;

    public MatchQueries(Context context) {
        mDbConnection = new DatabaseConnection(context);
    }

    public Cursor getPlayersForMatch(long matchId) {
        SQLiteDatabase db = mDbConnection.getReadableDatabase();

        String sqlQuery = "SELECT * FROM MatchPlayer m JOIN Players p ON m.PlayerId = p._id WHERE m.MatchId=?";
        return db.rawQuery(sqlQuery, new String[]{String.valueOf(matchId)});
    }
}
