package com.example.tpandroid.adapters;

import java.util.List;

import com.example.tpandroid.R;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomSaveAdapter extends BaseAdapter {
	
	Context context;
	List<WifiConfiguration> listConf;
	
	public CustomSaveAdapter(Context context, List<WifiConfiguration> listConf){
		this.context = context;
		this.listConf = listConf;
	}
	
	class ViewHolder {
		TextView txtSSIDsave;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		LayoutInflater _inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		if(convertView == null){
			convertView = _inflater.inflate(R.layout.layout_list_save, null);
			viewHolder = new ViewHolder();
			viewHolder.txtSSIDsave = (TextView) convertView.findViewById(R.id.txtSSIDsave);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		WifiConfiguration wifiConfig = (WifiConfiguration) getItem(position);
		viewHolder.txtSSIDsave.setText(wifiConfig.SSID);
		return convertView;
	}
	
    public void updateResults(List<WifiConfiguration> results) {
        this.listConf = results;
        //Triggers the list update
        notifyDataSetChanged();
    }
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listConf.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listConf.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return listConf.indexOf(position);
	}

}
