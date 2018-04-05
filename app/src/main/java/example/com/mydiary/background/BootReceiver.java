package example.com.mydiary.background;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import example.com.mydiary.R;
import example.com.mydiary.database.DBOps;
import example.com.mydiary.utils.Constants;
import example.com.mydiary.utils.Router;
import example.com.mydiary.view.EntryActivity;
import example.com.mydiary.view.HomeActivity;

/**
 * Created by saikr on 04-04-2018.
 */

public class BootReceiver extends BroadcastReceiver {
    private DBOps database = new DBOps();
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationIntent = new Intent(context, EntryActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(context)
                        .setContentTitle(context.getText(R.string.notification_title))
                        .setContentText("Boot Receiver")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, notification.build());

        Intent newIntent = new Intent(context, HomeActivity.class);
        intent.putExtra(Constants.Companion.getCHECK_PASSWORD_EXTRA(),true);
        newIntent.putExtra(Constants.Companion.getADD_BUTTON_CLICKED(),0);
        context.startActivity(newIntent);

        int[] time;
        time = database.getNotifyTime();
        Toast.makeText(context, "Broadcast Received", Toast.LENGTH_SHORT).show();
        Intent backgroundIntent = new Intent(context, NotificationService.class);
        intent.putExtra(Constants.Companion.getNOTIFY_HRS(),time[0]);
        intent.putExtra(Constants.Companion.getNOTIFY_MINS(),time[1]);
        backgroundIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startService(backgroundIntent);
    }
}
