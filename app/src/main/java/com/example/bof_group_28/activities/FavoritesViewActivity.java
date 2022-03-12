package com.example.bof_group_28.activities;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.databaseHandler;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bof_group_28.R;
import com.example.bof_group_28.viewAdapters.StudentViewAdapter;

public class FavoritesViewActivity extends AppCompatActivity {

    RecyclerView favoritesRecyclerView;
    StudentViewAdapter favoritesViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites_layout);
        setContentView(R.layout.favorites_layout);

        favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        RecyclerView.LayoutManager favoritesLayoutManager = new LinearLayoutManager(this);
        favoritesRecyclerView.setLayoutManager(favoritesLayoutManager);
        favoritesViewAdapter = new StudentViewAdapter(databaseHandler.getFavorites());
        favoritesRecyclerView.setAdapter(favoritesViewAdapter);
    }


    public void favoritesGoBack(View view) {
        finish();
    }

}

