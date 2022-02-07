package com.example.bof_group_28.activities;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.MessagesClient;
import com.google.android.gms.nearby.messages.MessagesOptions;
import com.google.android.gms.nearby.messages.NearbyPermissions;

public class NearbyMessagesActivity extends AppCompatActivity {

    private static final String TAG = "BOF-28";
    private Message userDetails;
    private MessageListener messageListener;
    private MessagesClient messagesClient;

    public NearbyMessagesActivity() {

        //TODO: User Details message should get user details (id, past classes, etc) and convert it to bytes for transmission
        this.userDetails = new Message("Test Message".getBytes());
        this.messageListener = createMessageListener();
        this.messagesClient = createMessagesClient();

        transmitData(userDetails, messageListener, messagesClient);
    }

    private MessageListener createMessageListener() {
        return new MessageListener() {
            @Override
            public void onFound(@NonNull Message message) {
                Log.d(TAG, "Found message: " + new String(message.getContent()));
            }

            @Override
            public void onLost(@NonNull Message message) {
                Log.d(TAG, "Lost sight of message: " + new String(message.getContent()));
            }
        };
    }

    private MessagesClient createMessagesClient() {
        MessagesClient messagesClient = Nearby.getMessagesClient(this, new MessagesOptions.Builder().setPermissions(NearbyPermissions.BLE).build());

        return messagesClient;
    }

    public void transmitData(Message userDetails, MessageListener msgListener, MessagesClient msgClient) {
        msgClient.publish(userDetails);
        msgClient.subscribe(msgListener);
    }

}
