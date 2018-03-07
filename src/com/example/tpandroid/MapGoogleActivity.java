package com.example.tpandroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

public class MapGoogleActivity extends FragmentActivity {

	private GoogleMap googleMap;
	private double latitude;
	private double longitude;
	private NetworkInfo mWifi;
	private ConnectivityManager connManager;
	private ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	private WifiInfo wifiInfo;
	private WifiManager wifiManager;
	private LocationManager locManager;
	private LocationListener locListen;
	private String w_bssid = null;
	private double latitudePos;
	private double longitudePos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locListen = new LocationListenner();
		locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListen);
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		googleMap = mapFragment.getMap();
		mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		wifiInfo = wifiManager.getConnectionInfo();
		
	    if (android.os.Build.VERSION.SDK_INT > 9) {
	        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	        StrictMode.setThreadPolicy(policy);
	      }
	    
	    httpGetData();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	// function sending data to http post
	public void httpSending() {
		@SuppressWarnings("unused")
		InputStream is = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost("http://www.terrytoryproductions.com/insertCoord.php");
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			Log.v("msg","execute http");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			Log.v("msg", "Error in http connection"+e.toString());
			e.printStackTrace();
		}
	}
	
	//get data from the server
	public void httpGetData(){
		String result = null;
		InputStream is = null;
		StringBuilder sb = null;
		
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://www.terrytoryproductions.com/selectCoord.php");
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// read data and built a string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");
			String line = "0";
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}
		
		
		// paring data
		JSONArray jArray;
		try {
			jArray = new JSONArray(result);
			JSONObject json_data = null;
			for (int i = 0; i < jArray.length(); i++) {
				json_data = jArray.getJSONObject(i);
				w_bssid = json_data.getString("bssid");
				latitudePos = json_data.getDouble("lat");
				longitudePos = json_data.getDouble("lng");
				
			    googleMap.addMarker(new MarkerOptions()
		        .position(new LatLng(latitudePos, longitudePos))
		        .title(w_bssid)
		        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
			}

		} catch (JSONException e1) {
			Toast.makeText(getBaseContext(), "No location Found", Toast.LENGTH_LONG)
					.show();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}

	// get location only if any wifi connection
	public class LocationListenner implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			Log.v("msg","change location");
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			if (mWifi.isConnected()) {
				nameValuePairs.add(new BasicNameValuePair("w_bssid", wifiInfo.getBSSID().toString()));			
				nameValuePairs.add(new BasicNameValuePair("w_lat", String.valueOf(latitude)));
				nameValuePairs.add(new BasicNameValuePair("w_lng", String.valueOf(longitude)));
				httpSending();
				httpGetData();
			}
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
			locManager.removeUpdates(locListen);

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

	}

}
