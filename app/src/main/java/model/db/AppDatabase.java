package model.db;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Person.class, CourseEntry.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase singletonInstance;
    public static String currentDatabase;

    public static AppDatabase singleton(Context context, String database) {
        if (singletonInstance == null || !database.equals(currentDatabase)) {
            singletonInstance = Room.databaseBuilder(context, AppDatabase.class, database + ".db")
                    //.createFromAsset("starter-persons.db")
                    .allowMainThreadQueries()
                    .build();
        }
        currentDatabase = database;
        return singletonInstance;
    }
    public abstract PersonWithCoursesDAO personWithCoursesDAO();
    public abstract CourseEntryDAO courseEntryDAO();
}