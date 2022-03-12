package com.example.bof_group_28.viewAdapters;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.PREF_NAME;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.TAG;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.databaseHandler;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.sessionManager;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bof_group_28.activities.BirdsOfAFeatherActivity;
import com.example.bof_group_28.utility.classes.Converters;
import com.example.bof_group_28.utility.classes.DownloadImageTask;
import com.example.bof_group_28.utility.classes.NearbyStudentsHandler;
import com.example.bof_group_28.R;
import com.example.bof_group_28.activities.StudentSelectedActivity;

import java.util.List;
import java.util.Set;

import model.db.CourseEntry;
import model.db.PersonWithCourses;

/**
 * The student view adapter handles a view of nearby students with similar classes
 */
public class StudentViewAdapter extends RecyclerView.Adapter<StudentViewAdapter.ViewHolder> {

    public static final String PFP = "pfp";

    // Instance Variables
    public final List<PersonWithCourses> students;

    // Constants
    public static final String SELECTED_STUDENT_NAME = "selectedName";
    public static final String SELECTED_STUDENT_COURSES = "selectedCourses";

    /**
     * Construct a student view adapter with a nearby handler and list of students
     * @param students list of students to show
     * @param handler nearby handler
     */
    public StudentViewAdapter(List<PersonWithCourses> students) {
        super();
        this.students = students;
    }

    @NonNull
    @Override
    public StudentViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.person_row, parent, false);

        return new ViewHolder(view);
    }

    /**
     * Setup the person button and shared courses count
     * @param holder the holder
     * @param position the position of student to bind to in array
     */
    @Override
    public void onBindViewHolder(@NonNull StudentViewAdapter.ViewHolder holder, int position) {
        holder.setPersonButton(students.get(position));
        holder.setFavoriteButton(students.get(position));

        if (sessionManager.getPeople().contains(students.get(position))) {
            Log.d(TAG, "Setting shared course count for " + students.get(position).getName() + " to " + databaseHandler.sharedCoursesCount(students.get(position).getId()));
            holder.setSharedCoursesCount(databaseHandler.sharedCoursesCount(students.get(position).getId()));
        } else {
            Log.d(TAG, "Defaulting shared course count for " + students.get(position).getName() + " to 0 as they were not in UUID LIST");
            holder.setSharedCoursesCount(0);
        }

        holder.setProfilePicture(students.get(position).getProfilePic());
    }

    @Override
    public int getItemCount() {
        return this.students.size();
    }

    /**
     * Clear the student view adapter
     */
    public void clear(){
        int size = students.size();
        students.clear();
        notifyItemRangeRemoved(0,size);
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {
        private final Button personButton;
        private final ImageButton favoriteButton;
        private PersonWithCourses student;
        private final TextView classCount;
        private String pfp;

        /**
         * Construct a Recycler View Item with onClick to select a student
         * @param itemView the item
         */
        ViewHolder(View itemView) {
            super(itemView);

            // shared count
            this.classCount = itemView.findViewById(R.id.sharedClassesCount);



            // onclick to select a student
            this.personButton = itemView.findViewById(R.id.session_button);
            personButton.setOnClickListener((view) -> {
                Context c = view.getContext();

                Intent intent = new Intent(c, StudentSelectedActivity.class);
                SharedPreferences preferences = c.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                // share their name as an extra
                editor.putString(SELECTED_STUDENT_NAME, student.getName());

                // get the shared courses as a set, and pass as an extra
                Set<String> sharedCourses = new ArraySet<>();
                for (CourseEntry course : databaseHandler.getSharedCourses(student.getId())) {
                    sharedCourses.add(course.toString());
                }
                editor.putStringSet(SELECTED_STUDENT_COURSES, sharedCourses);

                if (pfp != null) {
                    intent.putExtra(PFP, pfp);
                }

                editor.apply();

                view.getContext().startActivity(intent);
            });

            //Should be refactored
            this.favoriteButton = itemView.findViewById(R.id.favorite);

            favoriteButton.setOnClickListener((view) -> {
                boolean currFavStatus = databaseHandler.getFavStatus(student.getId());
                databaseHandler.setFavStatus(student.getId(), !currFavStatus);
                Log.v("StudentViewAdapter:", "Favorited: " + currFavStatus);
                setFavoriteButton(this.student);
            });

        }

        public void setPersonButton(PersonWithCourses student) {
            this.student = student;
            this.personButton.setText(student.getName());
        }

        public void setSharedCoursesCount(int courses) {
            classCount.setText(courses + " shared courses.");
        }

        public void setFavoriteButton(PersonWithCourses student) {
            if (databaseHandler.getFavStatus(student.getId())) {
                favoriteButton.setImageResource(R.drawable.button_on);
            }
            else {
                favoriteButton.setImageResource(R.drawable.button_off);
            }
        }

        /**
         * Set the profile picture
         * @param pfp byte array
         */
        public void setProfilePicture(String pfp) {
            this.pfp = pfp;
            new DownloadImageTask((ImageView) itemView.findViewById(R.id.smallProfilePicture)).execute(pfp);
        }
    }
}
