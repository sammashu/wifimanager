package com.example.tpandroid.listenners;

import com.example.tpandroid.CheckFrequencyService;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class CheckFrequencyListener implements OnClickListener {

	private Context context;
	private int checkFrequence = 900000, choixFrequency = 2;
	//private boolean frequencyModified = true;
	
	public CheckFrequencyListener(Context context) {
		super();
		this.context = context;
		SharedPreferences preferences = context.getSharedPreferences("SaveConfig", Context.MODE_PRIVATE);
		choixFrequency = preferences.getInt("choixFrequency", 2);
	}

	// liste de choix pour la deconnection et la frequence
	final CharSequence lstChoixFrequency[] = { "1 minute", "5 minutes",
			"15 minutes", "30 minutes", "60 minutes" };

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			AlertDialog.Builder ab = new AlertDialog.Builder(context);
			ab.setTitle("Set the check frequency");
			ab.setCancelable(false);
			ab.setSingleChoiceItems(lstChoixFrequency, choixFrequency,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							// TODO Auto-generated method stub

							choixFrequency = which;
							switch (which) {
							case 0: // 1 minute
								checkFrequence = 60000;
								break;
							case 1: // 5 minutes
								checkFrequence = 300000;
								break;
							case 2: // 15 minutes
								checkFrequence = 900000;
								break;
							case 3: // 30 minutes
								checkFrequence = 1800000;
								break;
							case 4: // 60 minutes
								checkFrequence = 3600000;
								break;

							}
							//frequencyModified = true;
							Toast.makeText(context,
									"Check frequency : " + checkFrequence,
									Toast.LENGTH_SHORT).show();
							saveFrequency();
							ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
							NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
							if(mWifi.isConnected()){
								context.stopService(new Intent(context, CheckFrequencyService.class));
							}else{
								context.startService(new Intent(context, CheckFrequencyService.class));	
							}
							dialog.dismiss();
						}
					});

			ab.setNegativeButton("Back",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
			AlertDialog a = ab.create();
			a.show();
		}
	

		public void saveFrequency(){
			
			  SharedPreferences preferences = context.getSharedPreferences("SaveConfig", Context.MODE_PRIVATE);
			  SharedPreferences.Editor editor = preferences.edit();
			  editor.putInt("checkFrequence", checkFrequence);
			  editor.putInt("choixFrequency", choixFrequency);
			  editor.commit();
			
		}
}
