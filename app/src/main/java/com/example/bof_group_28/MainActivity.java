package com.example.bof_group_28;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import model.db.AppDatabase;
import model.IPerson;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppDatabase db = AppDatabase.singleton(getApplicationContect());
        List<? extends IPerson> persons = db.personsWithNotesDao().getAll();

        final Button buttonHideName = findViewById(R.id.hideName);
        buttonHideName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView textView = (TextView) findViewById(R.id.textViewName);
                textView.setVisibility(View.INVISIBLE);
            }
        });
        final Button buttonShowName = findViewById(R.id.showName);
        buttonShowName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView textView = (TextView) findViewById(R.id.textViewName);
                textView.setVisibility(View.VISIBLE);
            }
        });

    }
}