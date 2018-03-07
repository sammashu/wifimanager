package com.example.tpandroid.listenners;

import java.util.List;

import com.example.tpandroid.adapters.CustomSaveAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ListSaveListenner implements OnItemClickListener{
	Context context;
	CustomSaveAdapter customSaveAdapter;
	WifiManager wifiManager;
	List<WifiConfiguration> listConf;
	ListView listSaveView;
	public ListSaveListenner(Context context, CustomSaveAdapter customSaveAdapter, WifiManager wifiManager, List<WifiConfiguration> listConf, ListView listSaveView){
		this.context = context;
		this.customSaveAdapter = customSaveAdapter;
		this.wifiManager = wifiManager;
		this.listConf = listConf;
		this.listSaveView = listSaveView;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		Log.v("MytextView", "dans listenner save click");
		final WifiConfiguration wifiConfig  = (WifiConfiguration) customSaveAdapter.getItem(position);
		AlertDialog.Builder adbSave = new AlertDialog.Builder(context);
		adbSave.setTitle("Selection");
		adbSave.setMessage("Remove " + wifiConfig.SSID + wifiConfig.networkId + " from your list?");
		adbSave.setPositiveButton("Yes", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				wifiManager.removeNetwork(wifiConfig.networkId);
				wifiManager.saveConfiguration();
				listConf.clear();
				listConf = wifiManager.getConfiguredNetworks();
				customSaveAdapter.updateResults(listConf);
				listSaveView.setAdapter(customSaveAdapter);
			}
		});
		
		adbSave.setNegativeButton("No", null);
		adbSave.show();
	}


}
