package com.example.bof_group_28.viewAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bof_group_28.R;

import java.util.List;

import model.db.CourseEntry;

public class EditCourseViewAdapter extends RecyclerView.Adapter<EditCourseViewAdapter.ViewHolder> {
    private final List<String> courses;
    //private final List<CourseEntry> courses;

    public EditCourseViewAdapter(List<String> courses) {
        super();
        this.courses = courses;
    }

    @NonNull
    @Override
    public EditCourseViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.class_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditCourseViewAdapter.ViewHolder holder, int position) {
        holder.setCourseName(courses.get(position));
    }

    @Override
    public int getItemCount() {
        return this.courses.size();
    }


    public static class ViewHolder
            extends RecyclerView.ViewHolder {
        private final TextView courseName;
        private String course;

        ViewHolder(View itemView) {
            super(itemView);
            this.courseName = itemView.findViewById(R.id.courseName);

        }

        public void setCourseName(String course) {
            this.course = course;
            this.courseName.setText(course);
        }
    }
}
