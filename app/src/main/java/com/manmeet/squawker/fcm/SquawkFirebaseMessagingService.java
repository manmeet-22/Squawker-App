package com.manmeet.squawker.fcm;
//dobjJYEJTrU:APA91bEHFzsD7Cu4xfJknj1PBuvaA3DV6w_KWtqEED1Mz9tcyAXyUZABZ2gLAVC60Xo-yxS1vFGkLe6fQl2Uv3MnGKWo_Q8oArp8SdtWpNa9ZDLOroPHoz9Rgo0yWZbzPpNp-rnsCqPSG4jMRVbXeN4DyE94Ngp0TA
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.manmeet.squawker.MainActivity;
import com.manmeet.squawker.R;
import com.manmeet.squawker.provider.SquawkContract;
import com.manmeet.squawker.provider.SquawkProvider;

import java.util.Map;

import static android.content.ContentValues.TAG;

public class SquawkFirebaseMessagingService extends FirebaseMessagingService {
    private static final String JSON_KEY_AUTHOR = SquawkContract.COLUMN_AUTHOR;
    private static final String JSON_KEY_AUTHOR_KEY = SquawkContract.COLUMN_AUTHOR_KEY;
    private static final String JSON_KEY_MESSAGE = SquawkContract.COLUMN_MESSAGE;
    private static final String JSON_KEY_DATE = SquawkContract.COLUMN_DATE;

    private static final int NOTIFICATION_MAX_CHARACTERS = 30;
    private static String LOG_TAG = SquawkFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        if (data.size() >0 ){
            Log.d(TAG, "onMessageReceived: MSg data payload"+ data);
            sendNotification(data);
            insertSquawk(data);
        }
    }

    private void insertSquawk(final Map<String, String> data) {
        AsyncTask<Void,Void,Void> insertSquawkTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(SquawkContract.COLUMN_AUTHOR, data.get(JSON_KEY_AUTHOR));
                contentValues.put(SquawkContract.COLUMN_MESSAGE, data.get(JSON_KEY_MESSAGE).trim());
                contentValues.put(SquawkContract.COLUMN_DATE, data.get(JSON_KEY_DATE));
                contentValues.put(SquawkContract.COLUMN_AUTHOR_KEY, data.get(JSON_KEY_AUTHOR_KEY));
                getContentResolver().insert(SquawkProvider.SquawkMessages.CONTENT_URI,contentValues);
                return null;
            }
        };
        insertSquawkTask.execute();
    }


    private void sendNotification(Map<String, String> data) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        String author  = data.get(JSON_KEY_AUTHOR);
        String message = data.get(JSON_KEY_MESSAGE);

        if (message.length() > NOTIFICATION_MAX_CHARACTERS){
            message = message.substring(0,NOTIFICATION_MAX_CHARACTERS) + "\u2026";
        }

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_duck)
                .setContentTitle("New Squawk from "+ author)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());
    }
}
