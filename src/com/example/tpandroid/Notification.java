package com.example.tpandroid;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

public class Notification {

	private boolean activateNotification;
	private Context context;	
	
	public Notification(boolean activateNotification, Context context) {
		super();
		this.activateNotification = activateNotification;
		this.context = context;
	}

	public void show(String notTicker, String notContentText) {
		if (activateNotification) {
			final NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			final Intent launchNotifiactionIntent = new Intent();
			final PendingIntent pendingIntent = PendingIntent.getActivity(context,
					0, launchNotifiactionIntent,
					PendingIntent.FLAG_CANCEL_CURRENT);

			long[] pattern = { 10, 500, 200, 200, 200, 500 };
			Uri alarmSound = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

			NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setWhen(System.currentTimeMillis())
					.setTicker(notTicker).setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle("Wifi status")
					.setContentText(notContentText)
					.setContentIntent(pendingIntent).setVibrate(pattern)
					.setSound(alarmSound);

			notifManager.notify(5, builder.build());
		}
	}

}
