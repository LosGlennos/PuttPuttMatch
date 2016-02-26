package se.d2collective.puttputtmatch.database.tables;

import android.provider.BaseColumns;

/**
 * Created by msve on 2016-02-24.
 */
public final class MatchTableContract {
    public MatchTableContract() {}

    public static abstract class MatchTable implements BaseColumns {
        public static final String TABLE_NAME = "Matches";
        public static final String COLUMN_NAME_OPPONENT_ID = "OpponentId";
        public static final String COLUMN_NAME_MATCH_ONGOING = "MatchOngoing";
    }
}
