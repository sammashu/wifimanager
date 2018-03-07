package com.example.tpandroid.adapters;

import java.util.List;

import com.example.tpandroid.R;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomNetworkAdapter extends BaseAdapter {
	
	Context context;
	List<ScanResult> listItems;

	public CustomNetworkAdapter(Context context, List<ScanResult> listItems) {
		super();
		this.context = context;
		this.listItems = listItems;
	}
	
	private class ViewHolder{
		TextView txt_BSSID;
		TextView txt_capabilities;
		TextView txt_frequency;
		TextView txt_level;
		TextView txt_SSID;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder = null;
		
		LayoutInflater _mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		if(view == null){
			view = _mInflater.inflate(R.layout.layout_list_scan, null); //associate xml with a view
			holder = new ViewHolder();
			holder.txt_BSSID = (TextView) view.findViewById(R.id.txtBssId);
			holder.txt_capabilities = (TextView) view.findViewById(R.id.txtCapabilities);
			holder.txt_frequency = (TextView) view.findViewById(R.id.txtfrequency);
			holder.txt_level = (TextView) view.findViewById(R.id.txtlevel);
			holder.txt_SSID = (TextView) view.findViewById(R.id.txtSsId);
			view.setTag(holder);
		}else{
			holder = (ViewHolder) view.getTag();
		}
		
		ScanResult rowItem = (ScanResult) getItem(position);
		holder.txt_BSSID.setText(rowItem.BSSID);
		holder.txt_frequency.setText(""+rowItem.frequency);
		holder.txt_capabilities.setText(rowItem.capabilities);
		holder.txt_level.setText(""+rowItem.level);
		holder.txt_SSID.setText(rowItem.SSID);
		
		return view;
	}
	
	@Override
	public int getCount() {
		return listItems.size();
		//return (Integer) null;
	}

	@Override
	public Object getItem(int position) {
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return listItems.indexOf(position);
	}

}
