package model.db;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.TAG;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.databaseHandler;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.io.File;
import java.util.Arrays;

@Database(entities = {Person.class, CourseEntry.class}, version = 1)
@TypeConverters({UUIDConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase singletonInstance;

    public static AppDatabase singleton(Context context) {
        if (singletonInstance == null) {
            singletonInstance = Room.databaseBuilder(context, AppDatabase.class, "appDatabase.db")
                    //.createFromAsset("starter-persons.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return singletonInstance;
    }

    public abstract PersonWithCoursesDAO personWithCoursesDAO();
    public abstract CourseEntryDAO  courseEntryDAO();
}