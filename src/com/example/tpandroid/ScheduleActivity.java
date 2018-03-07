package com.example.tpandroid;


import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

public class ScheduleActivity extends Activity {

	private TimePicker timeDisconnection;
	private TimePicker timeConnection;
	private Button btnAllSet;
	private Button btnDisable;

	private int heureConn;
	private int minuteConn;
	private int heureDisconn;
	private int minuteDisconn;

	private boolean scheduleSet, stateAllSet = true, stateDisable = false;

	// /public boolean testrun;


	private String timeConn = "", timeDisc = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);

		timeDisconnection = (TimePicker) findViewById(R.id.timePickerDisconnection);
		timeConnection = (TimePicker) findViewById(R.id.timePickerConnection);
		btnAllSet = (Button) findViewById(R.id.btnAllset);
		btnDisable = (Button) findViewById(R.id.btnDisableSchedule);
		
		SharedPreferences preferences = getSharedPreferences("SaveConfig", Context.MODE_PRIVATE);
		stateAllSet = preferences.getBoolean("stateAllSet", true);
		stateDisable = preferences.getBoolean("stateDisable", false);
		btnAllSet.setEnabled(stateAllSet);
		btnDisable.setEnabled(stateDisable);



		btnAllSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				heureDisconn = timeDisconnection.getCurrentHour();
				minuteDisconn = timeDisconnection.getCurrentMinute();
				heureConn = timeConnection.getCurrentHour();
				minuteConn = timeConnection.getCurrentMinute();

				String mConn = (minuteConn < 10) ? "0" + minuteConn : ""
						+ minuteConn;
				String hConn = (heureConn < 10) ? "0" + heureConn : ""
						+ heureConn;
				String mDisc = (minuteDisconn < 10) ? "0" + minuteDisconn : ""
						+ minuteDisconn;
				String hDisc = (heureDisconn < 10) ? "0" + heureDisconn : ""
						+ heureDisconn;

				// activer le bouton Disable
				stateAllSet = false;
				stateDisable = true;
				btnAllSet.setEnabled(stateAllSet);
				btnDisable.setEnabled(stateDisable);

				// set valeur a true de ScheduleSet
				scheduleSet = true;

				Toast.makeText(getApplicationContext(),
						"Disconnection at : " + heureDisconn + " : " + mDisc,
						Toast.LENGTH_SHORT).show();
				Toast.makeText(getApplicationContext(),
						"Reconnection at : " + heureConn + " : " + mConn,
						Toast.LENGTH_SHORT).show();

				timeConn = hConn + ":" + mConn + ":00";
				timeDisc = hDisc + ":" + mDisc + ":00";
				saveSchedule();
				
				//stopService(new Intent(getBaseContext(), CheckFrequencyService.class));
				stopService(new Intent(getBaseContext(), BroadWifiService.class));

				startService(new Intent(getBaseContext(), ScheduleService.class));

				Log.v("msg", "apres le schedule");

			}
		});

		btnDisable.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// activer le bouton Disable

				stateDisable = false;
				stateAllSet = true;
				btnAllSet.setEnabled(stateAllSet);
				btnDisable.setEnabled(stateDisable);


				// set valeur a true de ScheduleSet
				scheduleSet = false;

				saveSchedule();
				// arreter le timer
				stopService(new Intent(getBaseContext(), ScheduleService.class));
				
				//demarer les autres services
				startService(new Intent(getBaseContext(), BroadWifiService.class));
				//stopService(new Intent(getBaseContext(), CheckFrequencyService.class));

				
			}
		});
	}

	public void saveSchedule() {

		SharedPreferences preferences = this.getSharedPreferences("SaveConfig", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("timeConn", timeConn);
		editor.putString("timeDisc", timeDisc);
		editor.putBoolean("scheduleSet", scheduleSet);
		editor.putBoolean("stateAllSet", stateAllSet);
		editor.putBoolean("stateDisable", stateDisable);
		editor.commit();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		saveSchedule();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		scheduleSet = preferences.getBoolean("scheduleSet", false);

		Log.v("msg", "Dans le On Resume Activity " + scheduleSet);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.schedule, menu);
		return true;
	}

	public String getTimeConn() {
		return timeConn;
	}

	public void setTimeConn(String timeConn) {
		this.timeConn = timeConn;
	}

	public String getTimeDisc() {
		return timeDisc;
	}

	public void setTimeDisc(String timeDisc) {
		this.timeDisc = timeDisc;
	}

}
