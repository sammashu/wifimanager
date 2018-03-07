package com.example.tpandroid.asyntask;

import com.example.tpandroid.Notification;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

public class CheckFrequencyAscTask extends AsyncTask<Void, Void, Void> {

	private WifiManager wifiManager;
	private int disconnectionTimeout;
	private boolean activateNotification;
	private Context context;
	private Notification notification;

	public CheckFrequencyAscTask(WifiManager wifiManager,
			int disconnectionTimeout, boolean activateNotification, Context context) {
		this.wifiManager = wifiManager;
		this.disconnectionTimeout = disconnectionTimeout;
		this.activateNotification = activateNotification;
		this.context = context;		
		Log.v("msg", "check frency asc construct");
	}

	@Override
	// will do this until its finish than postexecute call
	protected Void doInBackground(Void... params) {
		wifiManager.setWifiEnabled(true);
		// sleep pour attendre que le wifi s'active
		try {
			Thread.sleep(disconnectionTimeout);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		notification = new Notification(activateNotification, context);
		if (disconnectionTimeout != 0) {
			if (wifiManager.getConnectionInfo().getNetworkId() == -1) {
				// ferme le wifi
				wifiManager.setWifiEnabled(false);
				notification.show("not connect", "Not connect");
				Log.v("msg", "no connection");
			}
			// si connection
			else {
				Log.v("msg", "connection");
				notification.show("connect to "+ wifiManager.getConnectionInfo().getSSID(), "connect");
				this.cancel(true);
			}
		}
	}

}
