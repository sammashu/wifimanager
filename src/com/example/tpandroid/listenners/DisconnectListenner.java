package com.example.tpandroid.listenners;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class DisconnectListenner implements OnClickListener {
	
	private Context context;
	private final CharSequence lstChoixDisconnection[] = { "Disabled", "30 seconds",
			"1 minute", "2 minutes", "5 minutes" };
	private int disconnectionTimeout = 0, choixDisconTimeOut = 0;
	
	public DisconnectListenner(Context context){
		this.context = context;
		SharedPreferences preferences = context.getSharedPreferences("SaveConfig", Context.MODE_PRIVATE);
		choixDisconTimeOut = preferences.getInt("choixDisconTimeOut", 0);
	}
	

	

	@Override
	public void onClick(View v) {
		AlertDialog.Builder ab = new AlertDialog.Builder(
				context);
		ab.setTitle("Set the disconnection timeout");
		ab.setCancelable(false);
		ab.setSingleChoiceItems(lstChoixDisconnection,
				choixDisconTimeOut,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						// TODO Auto-generated method stub

						choixDisconTimeOut = which;
						switch (which) {
						case 0: // disabled
							disconnectionTimeout = 0;
							break;
						case 1: // 30 secondes
							disconnectionTimeout = 30000;
							break;
						case 2: // 1 minute
							disconnectionTimeout = 60000;
							break;
						case 3: // 2 minutes
							disconnectionTimeout = 120000;
							break;
						case 4: // 5 minutes
							disconnectionTimeout = 300000;
							break;

						}
						saveFrequency();
						dialog.dismiss();
						Toast.makeText(
								context,
								"Disconnection Timeout : " + which
										+ "---------"
										+ disconnectionTimeout,
								Toast.LENGTH_SHORT).show();
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
		  editor.putInt("disconnectionTimeout", disconnectionTimeout);
		  editor.putInt("choixDisconTimeOut", choixDisconTimeOut);
		  editor.commit();
		
	}
	
	
}
