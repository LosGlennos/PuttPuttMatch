package se.d2collective.puttputtmatch.database.tables;

import android.provider.BaseColumns;

/**
 * Created by msve on 2016-02-24.
 */
public final class ClubTableContract {
    public ClubTableContract() {}

    public static abstract class ClubTable implements BaseColumns {
        public static final String TABLE_NAME = "Clubs";
        public static final String COLUMN_NAME_CLUB_NAME = "Name";
    }
}
