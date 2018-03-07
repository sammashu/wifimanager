package com.example.tpandroid;


import com.example.tpandroid.asyntask.CheckFrequencyAscTask;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class CheckFrequencyService extends Service {


	private WifiManager wifiManager;
	private int checkInterval, disconnectionTimeout;
	private boolean activatedNotification;
	private Handler handler = new Handler();


	
	
	private Runnable checkFrequencyRun = new Runnable() {
		
		@Override
		public void run() { // will execute the task every time we set
			CheckFrequencyAscTask checkFrequencyAscTask = new CheckFrequencyAscTask(wifiManager, disconnectionTimeout, activatedNotification, getBaseContext());
			checkFrequencyAscTask.execute();
			handler.postDelayed(this, checkInterval);
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
		SharedPreferences preferences = getSharedPreferences("SaveConfig", Context.MODE_PRIVATE);
		checkInterval = preferences.getInt("checkFrequence", 900000);
		disconnectionTimeout = preferences.getInt("disconnectionTimeout", 0);
		activatedNotification = preferences.getBoolean("activateNotification", true);
		Log.v("msg","interval dans onCreate service " + checkInterval);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.v("msg", "Start CheckFrequencyService");
		//checkFrequencyRun.run();
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v("msg", "kill CheckFrequencyService");
		//handler.removeCallbacks(checkFrequencyRun);
	}
}