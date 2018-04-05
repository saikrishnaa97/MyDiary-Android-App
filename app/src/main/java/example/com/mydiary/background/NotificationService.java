package example.com.mydiary.background;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import java.util.Date;
import java.util.Timer;

import example.com.mydiary.R;
import example.com.mydiary.database.DBOps;
import example.com.mydiary.utils.Constants;
import example.com.mydiary.view.EntryActivity;
import timber.log.Timber;

/**
 * Created by saikr on 04-04-2018.
 */

public class NotificationService extends Service {

    private static int hours,minutes;
    private DBOps database = new DBOps();
    private boolean isNotify = false;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // TODO Auto-generated method stub
        Intent restartServiceTask = new Intent(getApplicationContext(),this.getClass());
        restartServiceTask.setPackage(getPackageName());
        restartServiceTask.putExtra(Constants.Companion.getNOTIFY_HRS(),hours);
        restartServiceTask.putExtra(Constants.Companion.getNOTIFY_MINS(),minutes);
        PendingIntent restartPendingIntent =PendingIntent.getService(getApplicationContext(), 1,restartServiceTask, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager myAlarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        myAlarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartPendingIntent);

        super.onTaskRemoved(rootIntent);

    }

    @Override
    @Deprecated
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        try {
            hours = intent.getIntExtra(Constants.Companion.getNOTIFY_HRS(),21);
            minutes = intent.getIntExtra(Constants.Companion.getNOTIFY_MINS(), 0);
        } catch (Exception e) {
            Timber.e(e.getMessage());
        }
        Toast.makeText(getApplicationContext(), "Service Started" + hours + ":" + minutes, Toast.LENGTH_SHORT).show();
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    int hrsNow, minsNow;
                    hrsNow = new Date().getHours();
                    if (hours == hrsNow) {
                        minsNow = new Date().getMinutes();
                        if (minutes == minsNow && !isNotify && !database.checkToday()) {
                            isNotify = true;
                            Intent notificationIntent = new Intent(getApplicationContext(), EntryActivity.class);
                            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PendingIntent pendingIntent =
                                    PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
                            NotificationCompat.Builder notification =
                                    new NotificationCompat.Builder(getApplicationContext())
                                            .setContentTitle(getText(R.string.notification_title))
                                            .setContentText(getText(R.string.notification_message))
                                            .setSmallIcon(R.drawable.ic_launcher_background)
                                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                            .setContentIntent(pendingIntent);

                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                            notificationManager.notify(1, notification.build());
                        }
                    } else {
                        isNotify = false;
                    }
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        Timber.e(e.getMessage());
                    }
                }
            }
        });
        thread.start();

//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                int hrsNow,minsNow;
//                hrsNow = new Date().getHours();
//                if(hours == hrsNow){
//                    minsNow = new Date().getMinutes();
//                    if(minutes == minsNow){
//                        Intent notificationIntent = new Intent(getApplicationContext(), EntryActivity.class);
//                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        PendingIntent pendingIntent =
//                                PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
//                        NotificationCompat.Builder notification =
//                                new NotificationCompat.Builder(getApplicationContext())
//                                        .setContentTitle(getText(R.string.notification_title))
//                                        .setContentText(getText(R.string.notification_message))
//                                        .setSmallIcon(R.drawable.ic_launcher_background)
//                                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                                        .setContentIntent(pendingIntent);
//
//                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
//                        notificationManager.notify(1,notification.build());
//                    }
//                }
//            }
//        }, 1000);
    }
}
