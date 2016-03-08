package se.d2collective.puttputtmatch.database.commands;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.Pair;

import java.util.List;

import se.d2collective.puttputtmatch.database.DatabaseConnection;
import se.d2collective.puttputtmatch.database.tables.MatchPlayerTableContract;
import se.d2collective.puttputtmatch.database.tables.MatchTableContract;

/**
 * Created by msve on 2016-03-02.
 */
public class MatchCommands {
    DatabaseConnection mDbConnection;

    public MatchCommands(Context context) {
        mDbConnection = new DatabaseConnection(context);
    }

    public long startGame(List<Pair<Long, String>> playersList, long opponentId) {
        //in progress
        SQLiteDatabase db = mDbConnection.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MatchTableContract.MatchTable.COLUMN_NAME_OPPONENT_ID, opponentId);
        values.put(MatchTableContract.MatchTable.COLUMN_NAME_MATCH_ONGOING, 1);

        long matchId = db.insert(MatchTableContract.MatchTable.TABLE_NAME, null, values);

        if (matchId != -1) {
            values.clear();
            for (int i = 0; i < playersList.size(); i++) {
                values.put(MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_PLAYER_ID, playersList.get(i).first);
                values.put(MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_MATCH_ID, matchId);
                values.put(MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_PLAYER_FINISHED_PLAYING, 0);
                values.put(MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_PLAYER_POSITION, i + 1);
                long matchPlayerId = db.insert(MatchPlayerTableContract.MatchPlayerTable.TABLE_NAME, null, values);
                if (matchPlayerId != -1) {
                    values.clear();
                } else {
                    db.delete(MatchPlayerTableContract.MatchPlayerTable.TABLE_NAME, MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_MATCH_ID + "=" + matchId, null);
                    db.delete(MatchTableContract.MatchTable.TABLE_NAME, MatchTableContract.MatchTable._ID + "=" + matchId, null);
                    return -1;
                }
            }
        } else {
            return -1;
        }

        return matchId;
    }
}
