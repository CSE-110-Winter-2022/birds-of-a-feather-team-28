package com.example.bof_group_28.viewAdapters;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.TAG;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.databaseHandler;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.sessionManager;
import static com.example.bof_group_28.utility.classes.SessionManager.NO_SESSION;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bof_group_28.R;

import java.util.List;

public class SessionViewAdapter extends RecyclerView.Adapter<SessionViewAdapter.ViewHolder> {

    // Instance Variables
    private List<String> sessions;

    public Context c;

    public SessionViewAdapter(List<String> sessions, Context c) {
        super();
        this.c = c;
        this.sessions = sessions;
    }

    @NonNull
    @Override
    public SessionViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.session_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionViewAdapter.ViewHolder holder, int position) {
        holder.setSessionButton(sessions.get(position));
        holder.setContext(c);
    }

    @Override
    public int getItemCount() {
        return this.sessions.size();
    }

    public class ViewHolder
            extends RecyclerView.ViewHolder {
        private final Button sessionButton;
        private final Button deleteButton;
        private final Button renameButton;
        private Context c;

        ViewHolder(View itemView) {
            super(itemView);

            this.sessionButton = itemView.findViewById(R.id.session_button);
            sessionButton.setOnClickListener((view) -> {
                sessionManager.changeSession(sessionButton.getText().toString());
                ((Activity)c).finish();
            });

            this.deleteButton = itemView.findViewById(R.id.deleteSession);
            deleteButton.setOnClickListener((view) -> {
                if (sessionManager.getCurrentSession().equals(sessionButton.getText().toString())) {
                    Toast.makeText(c, "Can't delete a session you're in! Change sessions first.", Toast.LENGTH_LONG).show();

                } else {
                    sessionManager.deleteSession(sessionButton.getText().toString());


                    int pos = this.getLayoutPosition();
                    String seshName = sessions.get(pos);
                    sessions.remove(pos);
                    notifyItemRemoved(pos);
                    Toast.makeText(c, "Deleted Session: " + seshName, Toast.LENGTH_SHORT).show();
                }
            });

            this.renameButton = itemView.findViewById(R.id.renameSessionButton);
            renameButton.setOnClickListener((view) -> {
                if (sessionManager.getCurrentSession().equals(sessionButton.getText().toString())) {
                    Toast.makeText(c, "Can't rename a session you're in! Change sessions first.", Toast.LENGTH_LONG).show();
                } else {
                    showRenameSessionPrompt("Rename this session?", sessionButton.getText().toString(), getLayoutPosition());
                }
            });
        }

        public void setSessionButton(String session) {
            this.sessionButton.setText(session);
        }

        public void setContext(Context c) {
            this.c = c;
        }

        public void showRenameSessionPrompt(String message, String sessionToRename, int pos) {
            AlertDialog.Builder builder = new AlertDialog.Builder(c);
            final EditText input = new EditText(c);
            input.setText(sessionToRename);
            builder.setMessage(message)
                    .setPositiveButton("Rename Session", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.d(TAG, "User attempting to rename current session");
                            String inputName = input.getText().toString();
                            if (inputName.equals(NO_SESSION) || inputName.isEmpty()) {
                                Toast.makeText(c, "Invalid Session Name", Toast.LENGTH_SHORT).show();
                                showRenameSessionPrompt(message, sessionToRename, pos);
                                return;
                            }
                            if (sessionManager.sessionExists(inputName)) {
                                Toast.makeText(c, "Session Already Exists", Toast.LENGTH_SHORT).show();
                                showRenameSessionPrompt(message, sessionToRename, pos);
                                return;
                            }
                            if (inputName.length() > 30) {
                                Toast.makeText(c, "Session Name too Long", Toast.LENGTH_SHORT).show();
                                showRenameSessionPrompt(message, sessionToRename, pos);
                                return;
                            }
                            sessionManager.renameSessionFile(sessionToRename, inputName);

                            sessions.set(pos, inputName);
                            sessionButton.setText(inputName);
                            notifyItemChanged(pos);
                            Toast.makeText(c, "Renamed Session: " + sessionToRename + " to " + inputName, Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            builder.setView(input);
            builder.create().show();
        }
    }
}

