package com.example.bof_group_28;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import model.IPerson;
import model.db.AppDatabase;
import model.db.CourseEntry;
import model.db.CourseEntryDAO;
import model.db.Person;

public class MainActivity extends AppCompatActivity {
    protected RecyclerView personsRecyclerView;
    protected RecyclerView.LayoutManager personsLayoutManager;
    //protected PersonsViewAdapter personsViewAdapter;

    /*protected IPerson[] data = {
            new DummyPerson(0,"Jane Doe", new String[]{
                    "Likes cats.",
                    "Favorite color is blue."
            }),
            new DummyPerson(1,"John Public", new String[]{
                    "Likes dogs.",
                    "Favorite color is red."
            }),
            new DummyPerson(2,"Richard Roe", new String[]{
                    "Likes birds.",
                    "Favorite color is yellow."
            })
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppDatabase db = AppDatabase.singleton(getApplicationContext());
        db.clearAllTables();

        //List<? extends IPerson> persons = db.personWithCoursesDAO().getAll();
        //int newCourseId = db.courseEntryDAO().maxId() +1;
        //int personId = db.personWithCoursesDAO().count() +1;

        //TextView person1Text = (TextView) findViewById(R.id.person1);
        //person1Text.setText(persons.get(0).getCourses().get(0).subject);
        //set up recycler view
        //personsRecyclerView = findViewById(R.id.persons_view);

        //personsLayoutManager = new LinearLayoutManager(this);
        //personsRecyclerView.setLayoutManager(personsLayoutManager);

        //personsViewAdapter = new PersonsViewAdapter(persons);
        //personsRecyclerView.setAdapter(personsViewAdapter);
    }
}