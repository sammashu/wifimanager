package com.example.tpandroid;

import com.example.tpandroid.asyntask.CheckFrequencyAscTask;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class BroadWifiService extends Service {

	private NetworkInfo mWifi;
	private WifiManager wifiManager;
	private Handler handler = new Handler();
	private int checkInterval, disconnectionTimeout;
	private boolean activatedNotification;


	
	
	private Runnable checkFrequencyRun = new Runnable() {
		
		@Override
		public void run() { // will execute the task every time we set
			CheckFrequencyAscTask checkFrequencyAscTask = new CheckFrequencyAscTask(wifiManager, disconnectionTimeout, activatedNotification, getBaseContext());
			checkFrequencyAscTask.execute();
			handler.postDelayed(this, checkInterval);
		}
	};
	
	private BroadcastReceiver thisReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				mWifi = intent
						.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
				if (mWifi.getType() == ConnectivityManager.TYPE_WIFI && !mWifi.isConnected()) {
					// Wifi is disconnected
					Log.d("msg","Wifi is disconnected: " + String.valueOf(mWifi));
					wifiManager.setWifiEnabled(false);
					
					//startService(new Intent(context, CheckFrequencyService.class));
					checkFrequencyRun.run();
					
				} else if (mWifi.getType() == ConnectivityManager.TYPE_WIFI && mWifi.isConnected()) {
					// Wifi is connected
					Log.d("msg", "Wifi is connected: " + String.valueOf(mWifi));
					//stopService(new Intent(context, CheckFrequencyService.class));	
					handler.removeCallbacks(checkFrequencyRun);
				}
			}
			
		}


	};

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		registerReceiver(thisReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		
		SharedPreferences preferences = getSharedPreferences("SaveConfig", Context.MODE_PRIVATE);
		checkInterval = preferences.getInt("checkFrequence", 900000);
		disconnectionTimeout = preferences.getInt("disconnectionTimeout", 0);
		activatedNotification = preferences.getBoolean("activateNotification", true);
		Log.v("msg","interval dans onCreate service " + checkInterval);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(thisReceiver);
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

}
