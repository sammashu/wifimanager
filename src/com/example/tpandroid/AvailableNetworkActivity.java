package com.example.tpandroid;

import java.util.List;

import com.example.tpandroid.adapters.CustomNetworkAdapter;
import com.example.tpandroid.listenners.ListViewListenner;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

public class AvailableNetworkActivity extends Activity{

	TextView mainText;
	ListView listNetwork;
	ListViewListenner lstViewListener;

	WifiManager wifiManager;
	WifiReceiver receiverWifi;
	List<ScanResult> wifiList;
	CustomNetworkAdapter customAdp;



	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_available_network);
		//view component
		mainText = (TextView) findViewById(R.id.txtAvailableNetwork);
		listNetwork = (ListView) findViewById(R.id.listNetwork);
		lstViewListener = new ListViewListenner(this);
		lstViewListener.setListNetwork(listNetwork);
		
		//setting progressDialog
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(true);
        dialog.setMessage("Loading...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
		
		//listener
		listNetwork.setOnItemClickListener(lstViewListener);

		//thread
         
        Thread myThread = new Thread(new Runnable() {
            public void run() {
            	while(wifiList == null){
            		try {
						Thread.sleep(1000); //sleep the thread for 1 sec
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
                dialog.dismiss(); //remove dialog progress
            }
        });
        myThread.start();
        
		//network
		wifiManager= (WifiManager) getSystemService(Context.WIFI_SERVICE);
		lstViewListener.setWifiManager(wifiManager);
		receiverWifi = new WifiReceiver();
		registerReceiver(receiverWifi, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		wifiManager.startScan();
	}
	
	

	protected void onPause() {
		unregisterReceiver(receiverWifi);
		super.onPause();
	}

	protected void onResume() {
		registerReceiver(receiverWifi, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		super.onResume();
	}

	class WifiReceiver extends BroadcastReceiver { 
		public void onReceive(Context c, Intent intent) {
			Log.v("msg", "onReceive");
			wifiList = wifiManager.getScanResults(); // get all network
			customAdp = new CustomNetworkAdapter(AvailableNetworkActivity.this,
					wifiList);
			listNetwork.setAdapter(customAdp); 
			
		}

	}

}
