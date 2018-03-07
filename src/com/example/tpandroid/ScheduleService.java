package com.example.tpandroid;

import java.text.SimpleDateFormat;
import java.util.Date;



import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ScheduleService extends Service {

	private WifiManager wifiManager;
	private Handler handler = new Handler();
	private String timeConn = "", timeDisc = "", currentTime = "", notTicker, notContentText;
	private boolean activateNotification;
	private Notification notification = new Notification(true, this);
	
private Runnable ScheduleRun = new Runnable() {
		
		@Override
		public void run() { // will execute the task every time we set
			Date d = new Date();
			SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
			currentTime = f.format(d);
			Log.v("msg", currentTime);
			
			
			// si les notifications sont active
			// si temps actuel est egal au temps de connection
			if (currentTime.equals(timeConn)) {
				// si le wifi est ferme
				if (!wifiManager.isWifiEnabled()) {
					// dire a user que la connection est faite

					Toast.makeText(getBaseContext(), "Wifi was connected",
							Toast.LENGTH_SHORT).show();
					// activer le wifi
					wifiManager.setWifiEnabled(true);
					// set ticker et contentText notification
					
					  notContentText = "Wifi was connected at " + timeConn;
					  notTicker = "Wifi was connected";
					 
				} else {
					// dire a user que deja deconnecte
					Toast.makeText(getBaseContext(),
							"Wifi is already connected", Toast.LENGTH_SHORT)
							.show();
					// set ticker et contentText notification
					
					  notContentText = "Wifi is already connected"; 
					  notTicker =  "Wifi is already connected";
					 
				}
				// send notification
				notification.show(notTicker, notContentText);
			}
			// si temps actuel est egal au temps de deconnection
			if (currentTime.equals(timeDisc)) {
				// si le wifi est connecte
				if (wifiManager.isWifiEnabled()) {
					// dire au user que le wifi s'est deconnecte
					Toast.makeText(getBaseContext(),
							"Wifi was disconnected", Toast.LENGTH_SHORT).show();
					// deconnecter le wifi
					wifiManager.setWifiEnabled(false);
					// set ticker et contentText notification
					
					  notContentText = "Wifi was disconnected at " + timeDisc;
					  notTicker = "Wifi was disconnected";
					 
				} else {
					// dire au user que le wifi est deja deconnecte
					Toast.makeText(getBaseContext(),
							"Wifi is already disconnected", Toast.LENGTH_SHORT)
							.show();
					// set ticker et contentText notification
					
					  notContentText = "Wifi is already disconnected";
					  notTicker = "Wifi is already disconnected";
					 
				}
				// send notification
				notification.show(notTicker, notContentText);
			}

			handler.postDelayed(this, 1000);
			
		}
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		
		ScheduleActivity sa = new ScheduleActivity();
		timeConn = sa.getTimeConn();
		timeDisc = sa.getTimeDisc();

		SharedPreferences preferences = getSharedPreferences("SaveConfig", Context.MODE_PRIVATE);
		timeConn = preferences.getString("timeConn", "");
		timeDisc = preferences.getString("timeDisc", "");
		activateNotification = preferences.getBoolean("activateNotification", false);
		
		Log.v("msg", "timeConn : " + timeConn);
		Log.v("msg", "timeDisc : " + timeDisc);
		Log.v("msg", "activateNotification : " + activateNotification);
	}
	
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.v("msg", "Start ScheduleService");

		ScheduleRun.run();
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v("msg", "kill ScheduleService");
		handler.removeCallbacks(ScheduleRun);
	}

}
