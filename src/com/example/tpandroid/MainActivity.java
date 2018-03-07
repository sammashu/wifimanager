package com.example.tpandroid;

import java.util.List;
import java.util.Locale;
import java.util.Timer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.tpandroid.listenners.CheckFrequencyListener;
import com.example.tpandroid.listenners.DisconnectListenner;

public class MainActivity extends Activity {

	private static final String FILENAME = "WifiManagerConfiguration";
	private Button btnShow, btnAvailable, btnCheckFrequency, btnSchedule,
			btnDisconnectTime, btnHelp, btnMap;
	private ToggleButton TbOnOff;
	private CheckBox chkNotification;
	private boolean activateNotification = true, scheduleSet,
			frequencyModified = true, disconnectModified = true,
			changeSchedule = false;// a changer par un bouton
	private int checkFrequence = 900000, disconnectionTimeout = 0,
			choixFrequency = 2, choixDisconTimeOut = 0;
	private String timeConn = "", timeDisc = "", currentTime = "",
			notTicker = "", notContentText = "";
	final Handler handler = new Handler();
	private WifiManager wifiManager;
	// private WifiDataReceiver wifiDataReceiver;
	private NetworkInfo mWifi;

	// Variables globales
	private boolean connected;

	// Variables pour listener
	private CheckFrequencyListener checkFrequencyListener;
	private DisconnectListenner disconnectListenner;

	// Timer
	private Timer timer = new Timer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// mettre la main sur l'ensemble des composants
		btnShow = (Button) findViewById(R.id.btnShow);
		btnAvailable = (Button) findViewById(R.id.btnAvailable);
		btnCheckFrequency = (Button) findViewById(R.id.btnCheckFrequency);
		btnSchedule = (Button) findViewById(R.id.btnSchedule);
		btnDisconnectTime = (Button) findViewById(R.id.btnDisconnectionTimeout);
		chkNotification = (CheckBox) findViewById(R.id.chkNotification);
		btnHelp = (Button) findViewById(R.id.btnHelp);
		TbOnOff = (ToggleButton) findViewById(R.id.toggleButtonOnOff);
		btnMap = (Button) findViewById(R.id.btnMap);

		// set les notification a false;
		chkNotification.setChecked(true);
		activateNotification = true;

		// set TBOnOff a true au depart;
		TbOnOff.setChecked(true);

		// WIFI manager
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// SetRegister
		// wifiDataReceiver = new WifiDataReceiver();
		// registerReceiver(wifiDataReceiver, new IntentFilter(
		// ConnectivityManager.CONNECTIVITY_ACTION));

		// **********
		// set listener
		// **********

		// ***************
		// Disconnection Timeout
		// ***************
		disconnectListenner = new DisconnectListenner(this);
		btnDisconnectTime.setOnClickListener(disconnectListenner);

		// ***************
		// Check Frequency
		// ***************
		checkFrequencyListener = new CheckFrequencyListener(this);
		btnCheckFrequency.setOnClickListener(checkFrequencyListener);

		// ***************
		// Schedule
		// ***************
		btnSchedule.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this,
						ScheduleActivity.class));
			}
		});

		// ***************
		// Help
		// ***************
		btnHelp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this, HelpActivity.class));
			}
		});

		// ***************
		// Available network
		// ***************
		// ****
		// btnAvailable
		// ****
		btnAvailable.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (wifiManager.isWifiEnabled()) {
					Log.v("msg", "" + wifiManager.isWifiEnabled());
					startActivity(new Intent(MainActivity.this,
							AvailableNetworkActivity.class));
				} else {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							MainActivity.this);
					adb.setTitle("Error!");
					adb.setMessage("Wifi not enable.");
					adb.setPositiveButton("Ok", null);
					adb.show();
				}
			}

		});

		// ****
		// btnShow
		// ****
		btnShow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (wifiManager.isWifiEnabled()) {
					Log.v("msg", "" + wifiManager.isWifiEnabled());
					startActivity(new Intent(MainActivity.this,
							ShowAvailableNetworkActivity.class));
				} else {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							MainActivity.this);
					adb.setTitle("Error!");
					adb.setMessage("Wifi not enable.");
					adb.setPositiveButton("Ok", null);
					adb.show();
				}
			}
		});

		// ****
		// CHKNotification
		// ****
		chkNotification
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (chkNotification.isChecked() == true) {
							activateNotification = true;
							Toast.makeText(getApplicationContext(),
									"Notification activated",
									Toast.LENGTH_SHORT).show();
						} else {
							activateNotification = false;
							Toast.makeText(getApplicationContext(),
									"Notification deactivated",
									Toast.LENGTH_SHORT).show();
						}
					}
				});

		// ****
		// Toggle On off
		// ****
		TbOnOff.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					btnShow.setEnabled(true);
					btnAvailable.setEnabled(true);
					btnCheckFrequency.setEnabled(true);
					btnSchedule.setEnabled(true);
					btnDisconnectTime.setEnabled(true);
					chkNotification.setEnabled(true);

				} else {
					btnShow.setEnabled(false);
					btnAvailable.setEnabled(false);
					btnCheckFrequency.setEnabled(false);
					btnSchedule.setEnabled(false);
					btnDisconnectTime.setEnabled(false);
					chkNotification.setEnabled(false);

				}
			}
		});

		// to map view

		btnMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!manager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
					buildAlertMessageNoGps();
				} else {
					Intent intent = new Intent(MainActivity.this,
							MapGoogleActivity.class);
					startActivity(intent);
				}

			}
		});

	}

	@Override
	protected void onStart() {
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if(!mWifi.isConnected()) {
			wifiManager.setWifiEnabled(true);
			startService(new Intent(this, CheckFrequencyService.class));
		}
		super.onStart();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			SharedPreferences preferences = getSharedPreferences("SaveConfig",
					Context.MODE_PRIVATE);
			checkFrequence = preferences.getInt("checkFrequence", 900000);
			choixFrequency = preferences.getInt("choixFrequency", 2);
			disconnectionTimeout = preferences
					.getInt("disconnectionTimeout", 0);
			choixDisconTimeOut = preferences.getInt("choixDisconTimeOut", 0);
			Log.v("msg", "checkFrequence : " + checkFrequence);
			Log.v("msg", "disconnectionTimeout : " + disconnectionTimeout);
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		// ***********
		// Save donnees
		// ***********

		// try {
		// FileOutputStream fos = openFileOutput(FILENAME,
		// Context.MODE_PRIVATE);
		//
		// fos.write((checkFrequence + "\n").getBytes());
		// fos.write((disconnectionTimeout + "\n").getBytes());
		// fos.write((choixDisconTimeOut + "\n").getBytes());
		// fos.write((choixFrequency + "\n").getBytes());
		// fos.write((activateNotification + "\n").getBytes());
		// fos.write((scheduleSet + "\n").getBytes());
		// fos.write((timeConn + "\n").getBytes());
		// fos.write((timeDisc + "\n").getBytes());
		//
		// fos.close();
		//
		// Log.v("save", "" + getFileStreamPath(FILENAME));
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	@Override
	protected void onResume() {
		startService(new Intent(getBaseContext(), BroadWifiService.class));
		super.onResume();

		// ***********
		// Recuperer donnees
		// ***********

		// String collected = "";
		// String[] value;
		// FileInputStream fis = null;
		// try {
		// fis = openFileInput(FILENAME);
		// byte[] dataArray = new byte[fis.available()];
		// while (fis.read(dataArray) != -1) {
		// collected = new String(dataArray);
		// }
		// fis.close();
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		//
		// //get all value from save file
		//
		// value = collected.split("\n");
		// if(value[0] != ""){
		// checkFrequence = Integer.parseInt(value[0]);
		//
		// disconnectionTimeout = Integer.parseInt(value[1]);
		// choixDisconTimeOut = Integer.parseInt(value[2]);
		// choixFrequency = Integer.parseInt(value[3]);
		// activateNotification = Boolean.parseBoolean(value[4]);
		// scheduleSet = Boolean.parseBoolean(value[5]);
		// if(value.length == 8){
		// timeConn = value[6];
		// timeDisc = value[7];
		// }
		// }
		//
		// if(changeSchedule==true){
		// scheduleSet = true;
		// }
		// registerReceiver(wifiDataReceiver, new IntentFilter(
		// ConnectivityManager.CONNECTIVITY_ACTION));
		//
		// if(connected==true){
		//
		// }
	}

	@Override
	protected void onDestroy() {
		Log.v("msg", "Ondestroy de main");
		super.onDestroy();
	}

	// CHECK VALUE FROM OTHER ACTIVITY
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 6) {
			if (resultCode == Activity.RESULT_OK) {
				String mC = "" + data.getStringExtra("minuteConn");
				String hC = "" + data.getStringExtra("heureConn");

				timeConn = hC + ":" + mC + ":00";

				String mD = "" + data.getStringExtra("minuteDisconn");
				String hD = "" + data.getStringExtra("heureDisconn");
				timeDisc = hD + ":" + mD + ":00";

				scheduleSet = data.getBooleanExtra("scheduleSet", false);
				changeSchedule = true;
			}
		}

	}

	/*
	 * public void notification(String notTicker, String notContentText) { if
	 * (activateNotification) { final NotificationManager notifManager =
	 * (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	 * final Intent launchNotifiactionIntent = new Intent(); final PendingIntent
	 * pendingIntent = PendingIntent.getActivity(this, 0,
	 * launchNotifiactionIntent, PendingIntent.FLAG_CANCEL_CURRENT);
	 * 
	 * long[] pattern = { 10, 500, 200, 200, 200, 500 }; Uri alarmSound =
	 * RingtoneManager .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	 * 
	 * NotificationCompat.Builder builder = new NotificationCompat.Builder(
	 * this).setWhen(System.currentTimeMillis())
	 * .setTicker(notTicker).setSmallIcon(R.drawable.ic_launcher)
	 * .setContentTitle("Wifi status") .setContentText(notContentText)
	 * .setContentIntent(pendingIntent).setVibrate(pattern)
	 * .setSound(alarmSound);
	 * 
	 * notifManager.notify(5, builder.build()); } }
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
	    Configuration config = new Configuration();
	    switch (item.getItemId()) {
	    case R.id.USA:
	     config.locale = Locale.ENGLISH;
	     break;
	    case R.id.French:
	     config.locale = Locale.CANADA_FRENCH;
	     break;
	    default:
	     config.locale = Locale.ENGLISH;
	     break;
	    }
	    getResources().updateConfiguration(config, null);
	   
		return super.onOptionsItemSelected(item);
	}

	private void buildAlertMessageNoGps() { // alert
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Your Mobile network location seems to be disabled, do you want to enable it?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int id) {
								startActivity(new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							final int id) {
						dialog.cancel();
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();
	}

}
