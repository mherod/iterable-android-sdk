package com.iterable.iterableapi;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;

import java.util.Map;

public class IterableHmsMessageService extends HmsMessageService {

    static final String TAG = "itblHMSMessagingService";

    @Override
    public final void onMessageReceived(RemoteMessage remoteMessage) {
        handleMessageReceived(this, remoteMessage);
    }

    @Override
    public final void onNewToken(String s) {
        handleTokenRefresh(this);
    }

    /**
     * Handles receiving an incoming push notification from the intent.
     * <p>
     * Call this from a custom {@link HmsMessageService} to pass Iterable push messages to
     * Iterable SDK for tracking and rendering
     *
     * @param remoteMessage Remote message received from Firebase in
     *                      {@link HmsMessageService#onMessageReceived(RemoteMessage)}
     * @return Boolean indicating whether it was an Iterable message or not
     */
    public static boolean handleMessageReceived(@NonNull Context context, @NonNull RemoteMessage remoteMessage) {
        final Map<String, String> messageData = remoteMessage.getDataOfMap();

        if (null == messageData || messageData.isEmpty()) {
            return false;
        }

        IterableLogger.d(TAG, "Message data payload: " + remoteMessage.getData());
        // Check if message contains a notification payload.
        if (null != remoteMessage.getNotification()) {
            IterableLogger.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        Bundle extras = IterableNotificationHelper.mapToBundle(messageData);

        if (!IterableNotificationHelper.isIterablePush(extras)) {
            IterableLogger.d(TAG, "Not an Iterable push message");
            return false;
        }

        if (!IterableNotificationHelper.isGhostPush(extras)) {
            if (!IterableNotificationHelper.isEmptyBody(extras)) {
                IterableLogger.d(TAG, "Iterable push received " + messageData);
                IterableNotificationBuilder notificationBuilder = IterableNotificationHelper.createNotification(
                        context.getApplicationContext(), extras);
                new IterableNotificationManager().execute(notificationBuilder);
            } else {
                IterableLogger.d(TAG, "Iterable OS notification push received");
            }
        } else {
            IterableLogger.d(TAG, "Iterable ghost silent push received");

            String notificationType = extras.getString("notificationType");
            if (null != notificationType) {
                if (notificationType.equals("InAppUpdate")) {
                    IterableApi.getInstance().getInAppManager().syncInApp();
                } else if (notificationType.equals("InAppRemove")) {
                    String messageId = extras.getString("messageId");
                    if (null != messageId) {
                        IterableApi.getInstance().getInAppManager().removeMessage(messageId);
                    }
                }
            }
        }
        return true;
    }

    /**
     * Handles token refresh
     * Call this from a custom {@link HmsMessageService} to register the new token with Iterable
     *
     * @param context
     */
    public static void handleTokenRefresh(Context context) {
        String registrationToken = HmsInstanceId.getInstance(context).getToken();
        IterableLogger.d(TAG, "New Firebase Token generated: " + registrationToken);
        IterableApi.getInstance().registerForPush();
    }

    /**
     * Checks if the message is an Iterable ghost push or silent push message
     *
     * @param remoteMessage Remote message received from Firebase in
     *                      {@link HmsMessageService#onMessageReceived(RemoteMessage)}
     * @return Boolean indicating whether the message is an Iterable ghost push or silent push
     */
    public static boolean isGhostPush(RemoteMessage remoteMessage) {
        final Map<String, String> messageData = remoteMessage.getDataOfMap();

        if (null == messageData || messageData.isEmpty()) {
            return false;
        }

        Bundle extras = IterableNotificationHelper.mapToBundle(messageData);
        return IterableNotificationHelper.isGhostPush(extras);
    }
}

