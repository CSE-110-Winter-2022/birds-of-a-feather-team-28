package com.example.bof_group_28.utility.services;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.finder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.bof_group_28.activities.BirdsOfAFeatherActivity;
import com.example.bof_group_28.utility.classes.NearbyStudentsFinder;

public class NearbyStudentsService extends Service {

    public static final String SEARCHING_FOR_STUDENTS = "Searching for other students...";
    public static final String STOP_SEARCHING = "Stopping the search...";
    public static final String TAG = "BOF-TEAM-28";


    public NearbyStudentsService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(NearbyStudentsService.this, SEARCHING_FOR_STUDENTS, Toast.LENGTH_SHORT).show();

        synchronized (this) {
            finder.updateNearbyStudents();
            /*if (finder != null) {
                Log.d(TAG, "Finder publishing to Nearby");
                finder.publishToNearbyStudents();
                finder.updateNearbyStudents();
            }*/
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(NearbyStudentsService.this, STOP_SEARCHING, Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}