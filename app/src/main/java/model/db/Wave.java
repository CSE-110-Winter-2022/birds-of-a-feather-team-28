package model.db;

import java.util.UUID;

public class Wave {
    UUID senderID;
    UUID receiverID;

    public Wave (UUID senderID, UUID receiverID) {
        this.senderID = senderID;
        this.receiverID = receiverID;
    }

    public UUID getReceiverID() {
        return receiverID;
    }

    public UUID getSenderID() {
        return senderID;
    }
}