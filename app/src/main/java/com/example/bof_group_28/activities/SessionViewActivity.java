package com.example.bof_group_28.activities;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.TAG;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.sessionManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bof_group_28.R;
import com.example.bof_group_28.viewAdapters.SessionViewAdapter;
import com.example.bof_group_28.viewAdapters.StudentViewAdapter;

public class SessionViewActivity extends AppCompatActivity {

    private RecyclerView sessionRecyclerView;
    private RecyclerView.LayoutManager sessionLayoutManager;
    private SessionViewAdapter sessionViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_view);

        sessionRecyclerView = findViewById(R.id.sessionRecyclerView);
        sessionLayoutManager = new LinearLayoutManager(this);
        sessionRecyclerView.setLayoutManager(sessionLayoutManager);
        sessionViewAdapter = new SessionViewAdapter(sessionManager.getSessionsList(), this);
        sessionRecyclerView.setAdapter(sessionViewAdapter);
    }

    public void onBackButtonClick(View view) {
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent=new Intent();
        setResult(0,intent);
        Log.d(TAG, "Update based on selected session");
        finish();
    }
}
