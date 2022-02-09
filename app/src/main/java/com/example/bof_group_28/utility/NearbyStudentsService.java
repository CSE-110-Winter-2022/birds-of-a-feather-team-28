package com.example.bof_group_28.utility.classes;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.nearby.Nearby;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NearbyStudentsService extends Service {

    public static final String SEARCHING_FOR_STUDENTS = "Searching for other students...";
    public static final String STOP_SEARCHING = "Stopping the search...";
    public static final String TAG = "BOF-TEAM-28";

    ExecutorService executor = Executors.newSingleThreadExecutor();

    public NearbyStudentsService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(NearbyStudentsService.this, SEARCHING_FOR_STUDENTS, Toast.LENGTH_SHORT).show();

        executor.submit(() -> {
            synchronized (this) {
                NearbyStudentsFinder nearbyStudentsFinder = new NearbyStudentsFinder(this);
                nearbyStudentsFinder.updateNearbyStudents();
            }
        });

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