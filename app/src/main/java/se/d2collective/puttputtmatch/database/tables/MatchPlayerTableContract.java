package se.d2collective.puttputtmatch.database.tables;

import android.provider.BaseColumns;

/**
 * Created by msve on 2016-02-26.
 */
public final class MatchPlayerTableContract {
    public MatchPlayerTableContract() { }

    public static abstract class MatchPlayerTable implements BaseColumns {
        public static final String TABLE_NAME = "MatchPlayer";
        public static final String COLUMN_NAME_MATCH_ID = "MatchId";
        public static final String COLUMN_NAME_PLAYER_ID = "PlayerId";
        public static final String COLUMN_NAME_PLAYER_DIFFERENCE = "PlayerScoreDifference";
        public static final String COLUMN_NAME_PLAYER_SCORE = "PlayerScore";
        public static final String COLUMN_NAME_PLAYER_POSITION = "PlayerPosition";
        public static final String COLUMN_NAME_PLAYER_SUBSTITUTED_TYPE = "PlayerSubstitutedType";
        public static final String COLUMN_NAME_PLAYER_FINISHED_PLAYING = "PlayerFinished";
    }
}
