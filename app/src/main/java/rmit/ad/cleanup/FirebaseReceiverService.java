package rmit.ad.cleanup;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseReceiverService extends FirebaseMessagingService {

    private FirebaseNotification firebaseNotification;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage == null) {
            return;
        }

        firebaseNotification.showNotificationMessage(remoteMessage.getData().get("title"),
                remoteMessage.getData().get("body"), null);

    }
}
