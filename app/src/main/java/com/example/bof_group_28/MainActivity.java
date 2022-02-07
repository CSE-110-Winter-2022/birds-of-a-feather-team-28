package com.example.bof_group_28;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

//import edu.ucsd.cse110.lab5_room.model.DummyPerson;
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
        List<? extends IPerson> persons = db.personWithCoursesDAO().getAll();

        int newCourseId = db.courseEntryDAO().maxId() +1;
        int personId = db.personWithCoursesDAO().count() +1;

        //Hardcoded DB Entry
        Person person = new Person(1, "John Doe");
        db.personWithCoursesDAO().insert(person);
        CourseEntry courseEntry = new CourseEntry(1,1,"2022", "WI22", "CSE", "110");
        db.courseEntryDAO().insert(courseEntry);
        CourseEntry courseEntry1 = new CourseEntry(2,1,"2022", "WI22", "CSE", "101");
        db.courseEntryDAO().insert(courseEntry1);

        person = new Person(2, "Jane Doe");
        db.personWithCoursesDAO().insert(person);
        courseEntry = new CourseEntry(3,3,"2022", "WI22", "CSE", "110");
        db.courseEntryDAO().insert(courseEntry);
        courseEntry = new CourseEntry(4,4,"2022", "WI22", "MGT", "181");
        db.courseEntryDAO().insert(courseEntry);

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