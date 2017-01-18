package fr.istic.m2miage.heybuddy.firebase;

import android.content.Intent;
import android.util.Log;

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

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(remoteMessage.getData().size() > 0) {
            Log.e("FMS", remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(json);
            } catch (Exception e) {
                Log.e("FMS", "Exception: " + e.getMessage());
            }
        }
    }

    private void sendPushNotification(JSONObject json) {

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
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
