package se.d2collective.puttputtmatch.models;

import java.io.Serializable;

/**
 * Created by msve on 2016-03-02.
 */
public class Player implements Serializable{
    private String mName;
    private long mId;

    public Player(long id, String name) {
        this.mId = id;
        this.mName = name;
    }

    public String getName() {
        return mName;
    }

    public long getId() {
        return mId;
    }
}
