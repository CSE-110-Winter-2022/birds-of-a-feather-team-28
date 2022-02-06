package com.example.bof_group_28;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CourseViewAdapter extends RecyclerView.Adapter<CourseViewAdapter.ViewHolder> {
    private final List<String> courses;

    public CourseViewAdapter(List<String> courses) {
        super();
        this.courses = courses;
    }

    @NonNull
    @Override
    public CourseViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.class_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewAdapter.ViewHolder holder, int position) {
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
