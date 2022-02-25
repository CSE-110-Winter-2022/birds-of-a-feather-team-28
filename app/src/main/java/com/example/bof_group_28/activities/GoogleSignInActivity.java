package com.example.bof_group_28.activities;

import static android.view.View.VISIBLE;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.TAG;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.databaseHandler;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bof_group_28.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

/**
 * Taken from official Google Docs on how to setup OAuth sign in
 */
public class GoogleSignInActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);

        // Setup a google sign in client
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestIdToken("46578921299-q9ad6neg98dbf0vvl2nuhba0u06bh6dp.apps.googleusercontent.com")
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    /**
     * Attempt to update the UI on succesful login
     * @param account the account
     */
    public void updateUI(GoogleSignInAccount account) {
        //FIXME: not called often enough? When you first login does not prompt a name change
        if (account !=  null) {
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                Log.v(TAG, "Signed in as " + personName);

                TextView editGoogleName = findViewById(R.id.editGoogleName);
                editGoogleName.setText(personName);
                editGoogleName.setVisibility(VISIBLE);


                Button confirmGoogleNameButton = findViewById(R.id.confirmGoogleNameButton);
                confirmGoogleNameButton.setVisibility(VISIBLE);

                Toast.makeText(this, "Please confirm your name.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * Sign in button on click listener
     * @param view the view
     */
    public void onSignInButton(View view) {
        System.out.println("clicked button");
        Log.v(TAG, "Clicked sign in button.");
        signIn();
    }

    /**
     * Attempt to sign in
     */
    private void signIn() {
        Log.v(TAG, "Attempting to sign in.");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 777);
    }

    /**
     * Handle recieving a login task
     * @param requestCode rqc
     * @param resultCode rc
     * @param data the intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 777) {
            // if login request, handle login
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    /**
     * Handle some sign in request
     * @param completedTask google sign in task
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    /**
     * Button to handle confirming ones name
     * @param view the view
     */
    public void onGoogleConfirmName(View view) {

        TextView editGoogleName = findViewById(R.id.editGoogleName);
        if (editGoogleName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Name cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Name confirmed!", Toast.LENGTH_SHORT).show();

        databaseHandler.updatePerson(user.getId(), editGoogleName.getText().toString(), user.getProfilePic());
        finish();
    }
}
