package com.example.bof_group_28.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bof_group_28.R;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
    }

    public void onHomeButtonClicked(View view) {
        finish();
    }

    public void onViewClassesButtonClicked(View view) {
        //implement
    }

    public void onAddClassesButtonClicked(View view) {
        Intent intent = new Intent(this,AddClassActivity.class );
        startActivity(intent);
    }

    public void onEditClassesButtonClicked(View view) {
        //implement
    }

    public void onRemoveClassesButtonClicked(View view) {
        //implement
    }
}