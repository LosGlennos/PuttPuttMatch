package se.d2collective.puttputtmatch.database.queries;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
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
        mDbConnection = DatabaseConnection.getInstance(context);
    }

    public Cursor getPlayersForMatch(long matchId) {
        SQLiteDatabase db = mDbConnection.getReadableDatabase();

        String sqlQuery = "SELECT * FROM MatchPlayer m JOIN Players p ON m.PlayerId = p._id WHERE m.MatchId=? ORDER BY m.PlayerPosition ASC";
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

    public boolean hasSubstituted(long matchId) {
        SQLiteDatabase db = mDbConnection.getReadableDatabase();
        String sqlQuery = "SELECT PlayerSubstitutedType FROM MatchPlayer WHERE MatchId LIKE " + matchId + " AND PlayerSubstitutedType NOT LIKE 0";
        return db.rawQuery(sqlQuery, null).getCount() != 0;
    }

    public boolean hasFinishedPlaying(long matchId, long playerId) {
        SQLiteDatabase db = mDbConnection.getReadableDatabase();
        String sqlQuery = "SELECT PlayerFinished FROM MatchPlayer WHERE MatchId LIKE " + matchId + " AND PlayerId LIKE " + playerId + " AND PlayerFinished LIKE 1";
        return db.rawQuery(sqlQuery, null).getCount() != 0;
    }

    public String getPlayerDifference(long matchId, long playerId) {
        SQLiteDatabase db = mDbConnection.getReadableDatabase();
        String sqlQuery = "SELECT PlayerScoreDifference FROM MatchPlayer WHERE MatchId = ? AND PlayerId = ?";
        Cursor playerDifferenceCursor = db.rawQuery(sqlQuery, new String[]{String.valueOf(matchId), String.valueOf(playerId)});
        if (playerDifferenceCursor != null && playerDifferenceCursor.moveToFirst()) {
            return playerDifferenceCursor.getString(playerDifferenceCursor.getColumnIndex(MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_PLAYER_DIFFERENCE));
        }
        return "0";
    }

    public String getPlayerScore(long matchId, long playerId) {
        SQLiteDatabase db = mDbConnection.getReadableDatabase();
        String sqlQuery = "SELECT PlayerScore FROM MatchPlayer WHERE MatchId = ? AND PlayerId = ?";
        Cursor playerScoreCursor = db.rawQuery(sqlQuery, new String[]{String.valueOf(matchId), String.valueOf(playerId)});
        if (playerScoreCursor != null && playerScoreCursor.moveToFirst()) {
            return playerScoreCursor.getString(playerScoreCursor.getColumnIndex(MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_PLAYER_SCORE));
        }
        return "";
    }
}
