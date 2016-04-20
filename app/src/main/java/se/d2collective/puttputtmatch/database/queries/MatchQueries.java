package se.d2collective.puttputtmatch.database.queries;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import se.d2collective.puttputtmatch.database.DatabaseConnection;
import se.d2collective.puttputtmatch.database.tables.MatchPlayerTableContract;

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

    public Cursor getPlayersNotInMatch(long matchId) {
        SQLiteDatabase db = mDbConnection.getReadableDatabase();

        String sqlQuery = "SELECT " + MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_PLAYER_ID +
                            " FROM " + MatchPlayerTableContract.MatchPlayerTable.TABLE_NAME +
                            " WHERE " + MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_MATCH_ID + " = ?";
        Cursor players = db.rawQuery(sqlQuery, new String[]{String.valueOf(matchId)});

        ArrayList<String> playersInMatch = new ArrayList<>();

        while (players != null && players.moveToNext()) {
            playersInMatch.add(players.getString(players.getColumnIndex(MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_PLAYER_ID)));
        }

        String getPlayersNotInMatchQuery = "SELECT * FROM Players WHERE _id != ? AND _id != ? AND _id != ? AND _id != ? AND _id != ?";
        return db.rawQuery(getPlayersNotInMatchQuery, new String[] {playersInMatch.get(0), playersInMatch.get(1), playersInMatch.get(2), playersInMatch.get(3), playersInMatch.get(4)});
    }

    public String getPlayerPositionForMatchId(long matchId, long playerId) {
        SQLiteDatabase db = mDbConnection.getReadableDatabase();

        String sqlQuery = "SELECT " + MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_PLAYER_POSITION +
                            " FROM " + MatchPlayerTableContract.MatchPlayerTable.TABLE_NAME +
                            " WHERE " + MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_PLAYER_ID + " = ?" +
                            " AND " + MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_MATCH_ID + " = ?";
        Cursor playerPositionCursor = db.rawQuery(sqlQuery, new String[]{String.valueOf(playerId), String.valueOf(matchId)});
        if (playerPositionCursor != null && playerPositionCursor.moveToFirst()) {
            return playerPositionCursor.getString(playerPositionCursor.getColumnIndex(MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_PLAYER_POSITION));
        }
        return "";
    }
}
