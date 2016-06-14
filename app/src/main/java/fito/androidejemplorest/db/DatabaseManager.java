package fito.androidejemplorest.db;

import android.content.Context;

/**
 * Created by fito on 8/12/15.
 */
public class DatabaseManager {
    static private DatabaseManager instance;

    static public void init(Context ctx) {
        if (null==instance) {
            instance = new DatabaseManager(ctx);
        }
    }

    static public DatabaseManager getInstance() {
        return instance;
    }

    private DatabaseHelper helper;
    private DatabaseManager(Context ctx) {
        helper = new DatabaseHelper(ctx);
    }

    public DatabaseHelper getHelper() {
        return helper;
    }
}
