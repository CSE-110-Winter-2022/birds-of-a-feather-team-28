package com.example.bof_group_28.viewAdapters;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.TAG;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.databaseHandler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import com.example.bof_group_28.R;

import model.db.AppDatabase;
import model.db.CourseEntry;

/**
 * Manage Course View Adapter is the view adapter for managing courses menu
 */
public class ManageCourseViewAdapter extends RecyclerView.Adapter<ManageCourseViewAdapter.Viewholder> {

    private Context context;
    private ArrayList<CourseEntry> courseEntries;

    // Constructor
    public ManageCourseViewAdapter(Context context, ArrayList<CourseEntry> courseEntries) {
        this.context = context;
        this.courseEntries = courseEntries;

    }

    @NonNull
    @Override
    public ManageCourseViewAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_class_row, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageCourseViewAdapter.Viewholder holder, int position) {
        // to set data to textview of each card layout
        CourseEntry course = courseEntries.get(position);
        holder.currentCourse = course;
        holder.courseSubject.setText(course.subject);
        holder.courseNum.setText(course.courseNum);
        holder.courseQuarter.setText(course.quarter);
        holder.courseYear.setText(course.year);
        Log.v(TAG, "Bound Manage Course Item for " + course.toString());
    }

    @Override
    public int getItemCount() {
        // showing number of card items in recycler view.
        return courseEntries.size();
    }

    // View holder class for initializing of the manage class row card
    public class Viewholder extends RecyclerView.ViewHolder {
        private CourseEntry currentCourse;
        private TextView courseSubject, courseNum, courseQuarter, courseYear;
        private final Button deleteCourseButton;
        private final Button editCourseButton;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            this.deleteCourseButton = itemView.findViewById(R.id.deleteCourseButton);
            this.editCourseButton = itemView.findViewById(R.id.editCourseButton);

            // delete course button implementation
            deleteCourseButton.setOnClickListener((view) -> {
                // delete the item
                int pos = this.getLayoutPosition();
                databaseHandler.deleteCourse(currentCourse.courseId);
                courseEntries.remove(pos);
                notifyItemRemoved(pos);
                Toast.makeText(context, "Deleted Course: " + currentCourse.subject + " " + currentCourse.courseNum , Toast.LENGTH_SHORT).show();
            });

            // edit a course
            //TODO: IMPLEMENT
            editCourseButton.setOnClickListener((view) -> {
                Toast.makeText(context, "Editing!", Toast.LENGTH_SHORT).show();
            });

            // update variables with course data
            courseSubject = itemView.findViewById(R.id.courseSubject);
            courseNum = itemView.findViewById(R.id.courseNum);
            courseQuarter = itemView.findViewById(R.id.courseQuarter);
            courseYear= itemView.findViewById(R.id.courseYear);
        }
    }
}
