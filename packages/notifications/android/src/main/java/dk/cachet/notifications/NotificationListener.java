package dk.cachet.notifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import androidx.annotation.RequiresApi;

/**
 * Notification listening service. Intercepts notifications if permission is
 * given to do so.
 */
@SuppressLint("OverrideAbstract")
@RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationListener extends NotificationListenerService {

  public static String NOTIFICATION_INTENT = "notification_event";
  public static String NOTIFICATION_PACKAGE_NAME = "notification_package_name";
  public static String NOTIFICATION_MESSAGE = "notification_message";
  public static String NOTIFICATION_TITLE = "notification_title";

  @RequiresApi(api = VERSION_CODES.KITKAT)
  @Override
  public void onNotificationPosted(StatusBarNotification notification) {
    if (notification == null) {
      // Nothing to do if the notification is null
      return;
    }

    String packageName = notification.getPackageName();
    // Check if packageName is null and assign default if necessary
    if (packageName == null) {
      packageName = DEFAULT_PACKAGE_NAME;
    }

    // Pass data from one activity to another.
    Intent intent = new Intent(NOTIFICATION_INTENT);
    intent.putExtra(NOTIFICATION_PACKAGE_NAME, packageName);

    // Retrieve extra object from notification to extract payload.
    Bundle extras = notification.getNotification().extras;
    if (extras == null) {
      return; // extras が null の場合は処理を中断
    }
    CharSequence title = extras.getCharSequence(Notification.EXTRA_TITLE);
    CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);

    // Convert CharSequence to String safely
    String titleStr = title != null ? title.toString() : "";
    String textStr = text != null ? text.toString() : "";

    intent.putExtra(NOTIFICATION_TITLE, titleStr);
    intent.putExtra(NOTIFICATION_MESSAGE, textStr);

    sendBroadcast(intent);
  }
}
