package com.example.bof_group_28;

import static com.example.bof_group_28.BirdsOfAFeatherActivity.PREF_NAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class StudentViewAdapter extends RecyclerView.Adapter<StudentViewAdapter.ViewHolder> {
    private final List<Person> students;
    private BirdsOfAFeatherHandleNearbyStudents handler;

    public StudentViewAdapter(List<Person> students, BirdsOfAFeatherHandleNearbyStudents handler) {
        super();
        this.handler = handler;
        this.students = students;
    }

    @NonNull
    @Override
    public StudentViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.person_row, parent, false);

        return new ViewHolder(view, handler);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewAdapter.ViewHolder holder, int position) {
        holder.setPersonButton(students.get(position));
    }

    @Override
    public int getItemCount() {
        return this.students.size();
    }

    public void clear(){
        int size = students.size();
        students.clear();
        notifyItemRangeRemoved(0,size);
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {
        private final Button personButton;
        private Person student;

        ViewHolder(View itemView, BirdsOfAFeatherHandleNearbyStudents handler) {
            super(itemView);
            this.personButton = itemView.findViewById(R.id.person_button);

            personButton.setOnClickListener((view) -> {
                Context c = view.getContext();

                Intent intent = new Intent(c, StudentSelectedActivity.class);
                SharedPreferences preferences = c.getSharedPreferences(PREF_NAME, c.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("name", student.getName());
                Set<String> sharedCourses = new ArraySet<>();
                //@TODO: unfake
                for (CourseEntry course : handler.getStudentClassMapFaked(student).get(student)) {
                    sharedCourses.add(course.toString());
                }
                editor.putStringSet("courses", sharedCourses);
                editor.apply();

                view.getContext().startActivity(intent);
            });
        }

        public void setPersonButton(Person student) {
            this.student = student;
            this.personButton.setText(student.getName());
        }
    }
}
