package fr.istic.m2miage.heybuddy.firebase;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import fr.istic.m2miage.heybuddy.activities.MainActivity;
import fr.istic.m2miage.heybuddy.notification.NotificationsManager;

/**
 * Created by mahdi on 18/01/17.
 */

public class MessagingService extends FirebaseMessagingService {

    private final String SENDER_ID = "1000335333670";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(remoteMessage.getData().size() > 0) {
            Log.e("FMS", remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                showNotification(json);
            } catch (Exception e) {
                Log.e("FMS", "Exception: " + e.getMessage());
            }
        }
    }

    private void sendMessage(String to, String from, String title, String message) {

        FirebaseMessaging fm = FirebaseMessaging.getInstance();

        fm.send(new RemoteMessage.Builder(SENDER_ID + "@gcm.googleapis.com")
                .addData("to", to)
                .addData("from", from)
                .addData("title", title)
                .addData("message",message)
                .build());
    }

    private void showNotification(JSONObject json) {
        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("from") + " " + data.getString("title");
            String message = data.getString("message");

            NotificationsManager mNotificationManager = new NotificationsManager(getApplicationContext());

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            mNotificationManager.showSmallNotification(title, message, intent);

        } catch (JSONException e) {
            Log.e("FMS", "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e("FMS", "Exception: " + e.getMessage());
        }
    }
}
