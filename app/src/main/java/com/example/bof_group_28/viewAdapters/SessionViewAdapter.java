package com.example.bof_group_28.viewAdapters;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.sessionManager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bof_group_28.R;

import java.util.List;

public class SessionViewAdapter extends RecyclerView.Adapter<SessionViewAdapter.ViewHolder> {

    // Instance Variables
    private final List<String> sessions;

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

    public static class ViewHolder
            extends RecyclerView.ViewHolder {
        private final Button sessionButton;
        private Context c;

        ViewHolder(View itemView) {
            super(itemView);

            this.sessionButton = itemView.findViewById(R.id.session_button);
            sessionButton.setOnClickListener((view) -> {
                sessionManager.changeSession(sessionButton.getText().toString());
                ((Activity)c).finish();
            });
        }

        public void setSessionButton(String session) {
            this.sessionButton.setText(session);
        }

        public void setContext(Context c) {
            this.c = c;
        }
    }
}

