package com.example.bof_group_28.viewAdapters;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.PREF_NAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bof_group_28.activities.BirdsOfAFeatherActivity;
import com.example.bof_group_28.utility.classes.NearbyStudentsHandler;
import com.example.bof_group_28.R;
import com.example.bof_group_28.activities.StudentSelectedActivity;

import java.util.List;
import java.util.Set;

import model.db.CourseEntry;
import model.db.PersonWithCourses;

//bookkeeping

public class StudentViewAdapter extends RecyclerView.Adapter<StudentViewAdapter.ViewHolder> {
    private final List<PersonWithCourses> students;
    private final NearbyStudentsHandler handler;

    public static final String SELECTED_STUDENT_NAME = "selectedName";
    public static final String SELECTED_STUDENT_COURSES = "selectedCourses";

    public StudentViewAdapter(List<PersonWithCourses> students, NearbyStudentsHandler handler) {
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
        holder.setSharedCoursesCount(handler.getStudentClassMap().get(students.get(position)).size());
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
        private PersonWithCourses student;
        private final TextView classCount;

        ViewHolder(View itemView, NearbyStudentsHandler handler) {
            super(itemView);

            this.classCount = itemView.findViewById(R.id.sharedClassesCount);

            this.personButton = itemView.findViewById(R.id.person_button);
            personButton.setOnClickListener((view) -> {
                Context c = view.getContext();

                Intent intent = new Intent(c, StudentSelectedActivity.class);
                SharedPreferences preferences = c.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString(SELECTED_STUDENT_NAME, student.getName());
                Set<String> sharedCourses = new ArraySet<>();
                if (handler.getStudentClassMap() != null && handler.getStudentClassMap().containsKey(student)) {
                    for (CourseEntry course : handler.getStudentClassMap().get(student)) {
                        sharedCourses.add(course.toString());
                    }
                } else {
                    Log.e(BirdsOfAFeatherActivity.TAG, "ERROR! Student button pressed for student that does not exist in map.");
                }
                editor.putStringSet(SELECTED_STUDENT_COURSES, sharedCourses);
                editor.apply();

                view.getContext().startActivity(intent);
            });
        }

        public void setPersonButton(PersonWithCourses student) {
            this.student = student;
            this.personButton.setText(student.getName());
        }

        public void setSharedCoursesCount(int courses) {
            classCount.setText(courses + " shared courses.");
        }
    }
}
