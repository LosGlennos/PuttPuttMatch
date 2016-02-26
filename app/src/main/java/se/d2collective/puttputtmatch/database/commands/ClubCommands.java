package se.d2collective.puttputtmatch.database.commands;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import se.d2collective.puttputtmatch.database.DatabaseConnection;
import se.d2collective.puttputtmatch.database.tables.ClubTableContract;
import se.d2collective.puttputtmatch.database.tables.PlayerTableContract;

/**
 * Created by msve on 2016-02-25.
 */
public class ClubCommands {
    private DatabaseConnection mDbConnection;

    public ClubCommands(Context context) {
        this.mDbConnection = new DatabaseConnection(context);
    }

    public boolean deleteClubById(long id) {
        SQLiteDatabase db = mDbConnection.getWritableDatabase();

        String selection = ClubTableContract.ClubTable._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };

        return db.delete(ClubTableContract.ClubTable.TABLE_NAME, selection, selectionArgs) == 1;
    }

    public boolean addClub(String name) {
        SQLiteDatabase db = mDbConnection.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ClubTableContract.ClubTable.COLUMN_NAME_CLUB_NAME, name);

        return db.insert(ClubTableContract.ClubTable.TABLE_NAME, null, values) != -1;
    }

    public boolean editClubNameById(String name, long id) {
        SQLiteDatabase db = mDbConnection.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(ClubTableContract.ClubTable.COLUMN_NAME_CLUB_NAME, name);

        String selection = ClubTableContract.ClubTable._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };

        return db.update(ClubTableContract.ClubTable.TABLE_NAME, values, selection, selectionArgs) == 1;
    }

}
