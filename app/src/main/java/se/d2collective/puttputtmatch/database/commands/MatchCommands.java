package se.d2collective.puttputtmatch.database.commands;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.Pair;

import java.util.List;

import se.d2collective.puttputtmatch.database.DatabaseConnection;

/**
 * Created by msve on 2016-03-02.
 */
public class MatchCommands {
    DatabaseConnection mDbConnection;

    public MatchCommands(Context context) {
        mDbConnection = new DatabaseConnection(context);
    }

    public boolean startGame(List<Pair<Long, String>> playersList, long opponentId) {
        //in progress
        SQLiteDatabase db = mDbConnection.getWritableDatabase();

        return true;
    }
}
