package se.d2collective.puttputtmatch.database.tables;

import android.provider.BaseColumns;

/**
 * Created by msve on 2016-02-24.
 */
public final class PlayerTableContract {
    public PlayerTableContract() {}

    public static abstract class PlayerTable implements BaseColumns {
        public static final String TABLE_NAME = "Players";
        public static final String COLUMN_NAME_PLAYER_NAME = "Name";
    }
}
