package model.db;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.TAG;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.databaseHandler;
import static com.example.bof_group_28.utility.classes.SessionManager.DEFAULT_SESSION_NAME;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.io.File;
import java.util.Arrays;

@Database(entities = {Person.class, CourseEntry.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase singletonInstance;
    private static String currentDatabase;

    public static AppDatabase singleton(Context context, String database) {
        if (singletonInstance == null || !database.equals(currentDatabase)) {
            Log.v(TAG, "Generated database from " + database + ".db");
            singletonInstance = Room.databaseBuilder(context, AppDatabase.class, database + ".db")
                    //.createFromAsset("starter-persons.db")
                    .allowMainThreadQueries()
                    .build();
        }
        currentDatabase = database;
        Log.v(TAG, "New database stored at: " + context.getDatabasePath(currentDatabase + ".db").getPath());
        return singletonInstance;
    }

    public static void renameCurrentDatabase(Context context, String name) {
        /*singletonInstance.close();
        singletonInstance = null;
        File dbFile = context.getDatabasePath(currentDatabase + ".db");
        String path = dbFile.getPath().substring(0, dbFile.getPath().length() - (currentDatabase + ".db").length());
        dbFile.renameTo(new File(path + name + ".db"));
        Log.v(TAG, "Renamed Database to " + path + name + ".db");*/

        singletonInstance.close();
        System.out.println("MASTER1: " + databaseHandler.db.getOpenHelper().getDatabaseName());
        // ??? context.deleteDatabase(DEFAULT_SESSION_NAME);
        destroyInstance();
        //singletonInstance.close();
        singletonInstance = Room.databaseBuilder(context, AppDatabase.class, name + ".db")
                .createFromAsset(DEFAULT_SESSION_NAME + ".db")
                .allowMainThreadQueries()
                .build();
        Log.v(TAG, "Created new Database " + name + ".db");
    }

    public static void destroyInstance() {
        singletonInstance = null;
    }

    public abstract PersonWithCoursesDAO personWithCoursesDAO();
    public abstract CourseEntryDAO courseEntryDAO();
}