package se.d2collective.puttputtmatch.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import se.d2collective.puttputtmatch.database.tables.ClubTableContract;
import se.d2collective.puttputtmatch.database.tables.MatchPlayerTableContract;
import se.d2collective.puttputtmatch.database.tables.MatchTableContract;
import se.d2collective.puttputtmatch.database.tables.PlayerTableContract;
import se.d2collective.puttputtmatch.database.tables.UserClubTableContract;

/**
 * Created by msve on 2016-02-24.
 */
public class DatabaseConnection extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "dbPuttPutt";
    public static final int DATABASE_VERSION = 3;
    public static DatabaseConnection sInstance;

    private DatabaseConnection(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseConnection getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseConnection(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createUserClubTableString());
        db.execSQL(createPlayersTableString());
        db.execSQL(createClubTableString());
        db.execSQL(createMatchTableString());
        db.execSQL(createMatchPlayerTableString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropTablesClubs());
        db.execSQL(dropTablesMatch());
        db.execSQL(dropTablesMatchPlayer());
        db.execSQL(dropTablesPlayers());
        onCreate(db);
    }

    private String createUserClubTableString() {
        return "CREATE TABLE IF NOT EXISTS " + UserClubTableContract.UserClubTable.TABLE_NAME + " (" +
                PlayerTableContract.PlayerTable._ID + " INTEGER PRIMARY KEY," +
                UserClubTableContract.UserClubTable.COLUMN_NAME_CLUB_NAME + " TEXT NOT NULL)";
    }

    private String createPlayersTableString() {
        return "CREATE TABLE IF NOT EXISTS " + PlayerTableContract.PlayerTable.TABLE_NAME + " (" +
                PlayerTableContract.PlayerTable._ID + " INTEGER PRIMARY KEY," +
                PlayerTableContract.PlayerTable.COLUMN_NAME_PLAYER_NAME + " TEXT NOT NULL)";
    }

    private String createClubTableString() {
        return "CREATE TABLE IF NOT EXISTS " + ClubTableContract.ClubTable.TABLE_NAME + " (" +
                ClubTableContract.ClubTable._ID + " INTEGER PRIMARY KEY," +
                ClubTableContract.ClubTable.COLUMN_NAME_CLUB_NAME + " TEXT NOT NULL)";
    }

    private String createMatchTableString() {
        return "CREATE TABLE IF NOT EXISTS " + MatchTableContract.MatchTable.TABLE_NAME + " (" +
                MatchTableContract.MatchTable._ID + " INTEGER PRIMARY KEY," +
                MatchTableContract.MatchTable.COLUMN_NAME_OPPONENT_ID + " INTEGER NOT NULL," +
                MatchTableContract.MatchTable.COLUMN_NAME_MATCH_ONGOING + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + MatchTableContract.MatchTable.COLUMN_NAME_OPPONENT_ID + ") REFERENCES " + ClubTableContract.ClubTable.TABLE_NAME + "(" + ClubTableContract.ClubTable._ID + "))";
    }

    private String createMatchPlayerTableString() {
        return "CREATE TABLE IF NOT EXISTS " + MatchPlayerTableContract.MatchPlayerTable.TABLE_NAME + " (" +
                MatchPlayerTableContract.MatchPlayerTable._ID + " INTEGER PRIMARY KEY," +
                MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_MATCH_ID + " INTEGER NOT NULL," +
                MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_PLAYER_ID + " INTEGER NOT NULL," +
                MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_PLAYER_POSITION + " INTEGER NOT NULL," +
                MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_PLAYER_DIFFERENCE + " INTEGER DEFAULT 0, " +
                MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_PLAYER_SCORE + " INTEGER, " +
                MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_PLAYER_SUBSTITUTED_TYPE + " INTEGER," +
                MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_PLAYER_FINISHED_PLAYING + " INTEGER NOT NULL," +
                "FOREIGN KEY(" + MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_MATCH_ID + ") REFERENCES " + MatchTableContract.MatchTable.TABLE_NAME + "(" + MatchTableContract.MatchTable._ID + ")," +
                "FOREIGN KEY(" + MatchPlayerTableContract.MatchPlayerTable.COLUMN_NAME_PLAYER_ID + ") REFERENCES " + PlayerTableContract.PlayerTable.TABLE_NAME + "(" + PlayerTableContract.PlayerTable._ID + "))";
    }

    private String dropTablesMatchPlayer() {
        return "DROP TABLE IF EXISTS " + MatchPlayerTableContract.MatchPlayerTable.TABLE_NAME;
    }

    private String dropTablesMatch() {
        return "DROP TABLE IF EXISTS " + MatchTableContract.MatchTable.TABLE_NAME;
    }

    private String dropTablesPlayers() {
        return "DROP TABLE IF EXISTS " + PlayerTableContract.PlayerTable.TABLE_NAME;
    }

    private String dropTablesClubs() {
        return "DROP TABLE IF EXISTS " + ClubTableContract.ClubTable.TABLE_NAME;
    }

    private String dropTablesUserClub() {
        return "DROP TABLE IF EXISTS " + UserClubTableContract.UserClubTable.TABLE_NAME;
    }
}
