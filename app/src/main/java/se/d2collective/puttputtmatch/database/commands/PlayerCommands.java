package se.d2collective.puttputtmatch.database.commands;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import se.d2collective.puttputtmatch.database.DatabaseConnection;
import se.d2collective.puttputtmatch.database.tables.PlayerTableContract;

/**
 * Created by msve on 2016-02-25.
 */
public class PlayerCommands {
    DatabaseConnection mDbConnection;

    public PlayerCommands(Context context) {
        this.mDbConnection = new DatabaseConnection(context);
    }

    public boolean deletePlayerById(long id) {
        SQLiteDatabase db = mDbConnection.getWritableDatabase();

        String selection = PlayerTableContract.PlayerTable._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };

        return db.delete(PlayerTableContract.PlayerTable.TABLE_NAME, selection, selectionArgs) == 1;
    }

    public boolean editPlayerNameById(String name, long id) {
        SQLiteDatabase db = mDbConnection.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(PlayerTableContract.PlayerTable.COLUMN_NAME_PLAYER_NAME, name);

        String selection = PlayerTableContract.PlayerTable._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };

        return db.update(PlayerTableContract.PlayerTable.TABLE_NAME, values, selection, selectionArgs) == 1;
    }

    public boolean addPlayer(String name) {
        SQLiteDatabase db = mDbConnection.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PlayerTableContract.PlayerTable.COLUMN_NAME_PLAYER_NAME, name);

        return db.insert(PlayerTableContract.PlayerTable.TABLE_NAME, null, values) != -1;
    }
}
