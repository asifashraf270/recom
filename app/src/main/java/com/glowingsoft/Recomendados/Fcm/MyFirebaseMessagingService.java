package com.glowingsoft.Recomendados.Fcm;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.glowingsoft.Recomendados.Buyer.Activities.BottomNavigationActivity;
import com.glowingsoft.Recomendados.GlobalClass;
import com.glowingsoft.Recomendados.R;
import com.glowingsoft.Recomendados.Seller.ActivitiesSeller.BottomNavigationSellerActivity;
import com.glowingsoft.Recomendados.Seller.Chat.UserChatMessages;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.util.List;
import java.util.Random;

import me.leolin.shortcutbadger.ShortcutBadger;


/*
 *
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    Intent intent = null;
    int badgeCount = 0;
    int value = 0;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            GlobalClass.getInstance().storeBadgeValue(badgeCount);
            if (GlobalClass.getInstance().returnConvertionalId() == null) {
                ShortcutBadger.applyCount(MyFirebaseMessagingService.this, GlobalClass.getInstance().returnCount());
                intent = new Intent(this, BottomNavigationActivity.class);
                intent.putExtra("type", 2);///2 for notification android
                intent.putExtra("conversation_id", remoteMessage.getData().get("conversation_id"));
                CreateNOti(remoteMessage);
            } else {
                if (GlobalClass.getInstance().returnConvertionalId() != remoteMessage.getData().get("conversation_id")) {
                    badgeCount = ++badgeCount;
                    Log.d("value", badgeCount + "");
                    GlobalClass.getInstance().storeBadgeValue(badgeCount);
                }
            }
        }
    }

    public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null) {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void CreateNOti(RemoteMessage remoteMessage) {
        // Check if message contains a notification payload.
        String message = remoteMessage.getData().get("title") + "\n" + remoteMessage.getData().get("message");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        int requestID = (int) System.currentTimeMillis();

        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(String.valueOf(getApplicationContext().getString(R.string.app_name)))
                .setContentText(message)
                .setVibrate(new long[]{1000, 1000})
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            mBuilder.setColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
        } else {
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(requestID, mBuilder.build());


    }

//    public boolean checkApp(RemoteMessage mRemoteMsg) {
//
//
//        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
//        for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
//            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//                for (String activeProcess : processInfo.pkgList) {
//                    if (activeProcess.equals(this.getPackageName())) {
//                        //If your app is the process in foreground
//                        Log.d("notification", "app is in foreground");
//
//                        String topActivity = SharedHelper.getKey(getApplicationContext(), "topActivity");
//                        if (topActivity.equals("com.app.kolloki.Chat.ConversationActivity")) {
//                            return true;
//                        }
//                        if (topActivity.equals("com.app.kolloki.Chat.UserChatMessages")) {
//                            //First check current logged in user
//                            //add messages in batch
//                            String conversationID = SharedHelper.getKey(getApplicationContext(), "conversaationID");
//                            String conversationIDNoti = mRemoteMsg.getData().get("your_custom_data_key");
//
//                            try {
//                                if (conversationID.equals(conversationIDNoti)) {
//                                    return true;
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                return true;
//                            }
//                            totalBatch =SharedHelper.getInt(getApplicationContext(), "newmessages");
//
//                            totalBatch = totalBatch + 1;
//
//
//                            SharedHelper.putInt(getApplicationContext(), "newmessages", totalBatch);
//
//
//                            return true;
//                        } else {
//                            return false;
//                        }
//                    } else {
//                        return false;
//                    }
//                }
//            }
//        }
//        return false;
//
//    }

//    public void updateBatch(RemoteMessage mRemoteMsg) {
//        try {
//            SharedHelper.putInt(getApplicationContext(), "messagesBatch", badgeCount);
//            ShortcutBadger.applyCount(this, badgeCount); //for 1.1.4+
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    //    private void saveBatchForHomeScreen(RemoteMessage mRemoteMsg) {
//        try {
//            Log.d("response mRemoteMsg", mRemoteMsg.getData().toString());
////            int batch = Integer.parseInt(mRemoteMsg.getData().get("badge"));
//
//
//
//            SharedHelper.putInt(getApplicationContext(), "messagesBatch", badgeCount);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    private void sendNotification(RemoteMessage remoteMessage) {
//        RemoteMessage.Notification notification = remoteMessage.getNotification();
//        Intent intent = new Intent(this, .class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(notification.getTitle())
//                .setContentText(notification.getBody())
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0, notificationBuilder.build());
//    }
}