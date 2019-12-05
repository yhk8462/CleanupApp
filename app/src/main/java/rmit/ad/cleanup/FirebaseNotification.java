package rmit.ad.cleanup;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import java.util.Date;

public class FirebaseNotification {

    private Context mContext;

    public FirebaseNotification(Context mContext) {
        this.mContext = mContext;
    }

    public void showNotificationMessage(String title, String message, Intent intent) {
        createNotificationMessage(title, message, intent);
    }

    public void createNotificationMessage(String title, String message, Intent intent) {

        if (message == null) {
            return;
        }

        final int icon = R.mipmap.ic_launcher;

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                mContext);

        buildNotification(mBuilder, icon, title, message, resultPendingIntent);

    }

    private void buildNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, PendingIntent resultPendingIntent) {
        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setWhen(new Date().getTime())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(101, notification);
    }
}
