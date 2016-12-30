package se.d2collective.puttputtmatch.database.tables;

import android.provider.BaseColumns;

public class UserClubTableContract {
    public UserClubTableContract() { }

    public static abstract class UserClubTable implements BaseColumns {
        public static final String TABLE_NAME = "UserClub";
        public static final String COLUMN_NAME_CLUB_NAME = "Name";
    }
}
