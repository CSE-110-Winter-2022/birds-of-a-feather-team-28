package com.example.bof_group_28.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.bof_group_28.R;

public class AddClassActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Spinner spinner;
    private static final String[] quarterNames =
            {"FA","WI", "SP", "SS1", "SS2", "SSS"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        spinner = (Spinner)findViewById(R.id.quarter_dd);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(AddClassActivity.this,
                android.R.layout.simple_spinner_item,quarterNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        // stuff
    }
    public void onDoneClicked(View view) {
        finish();
    }

    public void onAddClicked(View view){
        //add courses to the database through the fields
        finish();
    }

    @Override
    //https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

}
