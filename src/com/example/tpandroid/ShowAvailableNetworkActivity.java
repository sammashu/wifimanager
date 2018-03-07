package com.example.tpandroid;

import java.util.List;

import com.example.tpandroid.adapters.CustomSaveAdapter;
import com.example.tpandroid.listenners.ListSaveListenner;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.widget.ListView;

public class ShowAvailableNetworkActivity extends Activity {
	
	ListView listSaveView;
	WifiManager wifiManager;
	CustomSaveAdapter customSaveAdapter;
	List<WifiConfiguration> listConf;
	ListSaveListenner listSaveListenner;
	Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_available_network);
		listSaveView = (ListView) findViewById(R.id.listSave);
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);	
		listConf = wifiManager.getConfiguredNetworks();
		customSaveAdapter= new CustomSaveAdapter(ShowAvailableNetworkActivity.this, listConf);
		listSaveListenner = new ListSaveListenner(this, customSaveAdapter, wifiManager, listConf, listSaveView);
		listSaveView.setAdapter(customSaveAdapter);
		listSaveView.setOnItemClickListener(listSaveListenner);	
	}


	@Override
	protected void onStart() {
		super.onStart();
	}



	@Override
	protected void onPause() {
		super.onPause();
	}
	
	
	
	

}
