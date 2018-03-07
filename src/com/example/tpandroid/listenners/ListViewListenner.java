package com.example.tpandroid.listenners;

import java.util.List;

import com.example.tpandroid.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ListViewListenner implements OnItemClickListener {

	Context context;
	ListView listNetwork;
	WifiManager wifiManager;
	WifiInfo wifiInfo;
	WifiConfiguration wifiConfig;

	public ListViewListenner(Context context) {
		this.context = context;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long id) {

		final TextView SSID = (TextView) view.findViewById(R.id.txtSsId);
		final TextView capability = (TextView) view.findViewById(R.id.txtCapabilities);
		String ssid;
		// check wifi state
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE); // current connectivity 
		final NetworkInfo mWifi = cm
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI); // get info from the current connectivity
		if (mWifi.isConnected()) { // check wifi if connect to someone or not
			wifiInfo = wifiManager.getConnectionInfo();
			ssid = wifiInfo.getSSID().replaceAll("\"", ""); // get the network ssid connected
		} else {
			wifiInfo = null;
			ssid = null;
		}

		if (ssid == null || !ssid.equals(SSID.getText().toString())) {
			AlertDialog.Builder adbPass = new AlertDialog.Builder(context);
			adbPass.setTitle("Connection Wifi");
			adbPass.setMessage("You have Select : " + SSID.getText().toString()
					+ "\n\n Password:");

			final EditText inputPass = new EditText(context);

			adbPass.setView(inputPass);
			adbPass.setPositiveButton("Connect",
					new DialogInterface.OnClickListener() {
						// take care off connecting to wifi
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String connPass = inputPass.getText().toString();
							wifiConfig = new WifiConfiguration();
							if(capability.getText().toString().indexOf("WPA") > 0){
							// wpa and wpa2 configuration for connecting internet
								Log.v("msg","dans wpa");
								wifiConfig.SSID = "\"" + SSID.getText() + "\"";
								wifiConfig.preSharedKey = "\"" + connPass + "\"";
	
								wifiConfig.allowedGroupCiphers
										.set(WifiConfiguration.GroupCipher.TKIP);
								wifiConfig.allowedGroupCiphers
										.set(WifiConfiguration.GroupCipher.CCMP);
								wifiConfig.allowedKeyManagement
										.set(WifiConfiguration.KeyMgmt.WPA_PSK);
								wifiConfig.allowedPairwiseCiphers
										.set(WifiConfiguration.PairwiseCipher.TKIP);
								wifiConfig.allowedPairwiseCiphers
										.set(WifiConfiguration.PairwiseCipher.CCMP);
								wifiConfig.allowedProtocols
										.set(WifiConfiguration.Protocol.RSN);
	
								wifiManager.addNetwork(wifiConfig); // put network and save it
								List<WifiConfiguration> list = wifiManager
										.getConfiguredNetworks();
								for (WifiConfiguration i : list) {
									if (i.SSID != null && i.SSID.equals("\""+ SSID.getText().toString()+ "\"")) {
										wifiManager.disconnect(); // disconnect current wifi
										wifiManager.enableNetwork(i.networkId, true); // connect to new wifi
										wifiManager.reconnect(); 
										break;
									}
								}
							}else if(capability.getText().toString().indexOf("WEP") > 0){
								// wep configuration for connecting internet
								Log.v("msg","dans wep");
								 wifiConfig.SSID = "\"" + SSID.getText() + "\"";
								 wifiConfig.wepKeys[0] = "\"" + connPass + "\"";
								 wifiConfig.wepTxKeyIndex = 0;
								 wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
								 wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
								 
								wifiManager.addNetwork(wifiConfig);  // put network and save it
								List<WifiConfiguration> list = wifiManager.getConfiguredNetworks(); // take all network that has been save
									for (WifiConfiguration i : list) {
										if (i.SSID != null && i.SSID.equals("\""+ SSID.getText().toString()+ "\"")) {
											wifiManager.disconnect(); // disconnect current wifi
											wifiManager.enableNetwork(i.networkId, true); // connect to new wifi
											wifiManager.reconnect();
											break;
										}
									}
							}else{
								Log.v("msg","dans else"); // alert box if network not recognize
								AlertDialog.Builder adbError = new AlertDialog.Builder(context);
								adbError.setTitle("Error!");
								adbError.setMessage("This network is not support");
								adbError.setPositiveButton("Ok", null);
								adbError.show();
							}
						}
					});
			adbPass.show();
		} else if (ssid.equals(SSID.getText().toString())) {
			AlertDialog.Builder adbPass = new AlertDialog.Builder(context);
			adbPass.setTitle("Connection Wifi");
			adbPass.setMessage("Already Connected");
			adbPass.setPositiveButton("Ok", null);
			adbPass.show();
		}

	}

	public void setListNetwork(ListView listNetwork) {
		this.listNetwork = listNetwork;
	}

	public void setWifiManager(WifiManager wifiManager) {
		this.wifiManager = wifiManager;
	}

	
	

}
