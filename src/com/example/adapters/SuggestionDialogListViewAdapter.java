package com.example.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.infos.SuggestionDialogInfo;
import com.example.ontheway.R;


public class SuggestionDialogListViewAdapter extends BaseAdapter {
	
	private Context context;
	private List<SuggestionDialogInfo> list = new ArrayList<SuggestionDialogInfo>();
	
	public SuggestionDialogListViewAdapter(Context context) {
		this.context = context;
	}
	
	public void addAll(List<SuggestionDialogInfo> data){
		this.list.addAll(data);
		notifyDataSetChanged();
	}

	public Context getContext(){
		return context;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public SuggestionDialogInfo getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView==null||convertView.getTag()==null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_dialoglistview, null);
			convertView.setTag(new ListCell((TextView)convertView.findViewById(R.id.tvTitle), (TextView)convertView.findViewById(R.id.tvAddress)));
		}
		
		ListCell lc = (ListCell) convertView.getTag();
		SuggestionDialogInfo data = getItem(position);
		
		lc.getSpKey().setText(data.getKey());
		lc.getSpAddress().setText(data.getCityAndDistric());
		
		return convertView;
	}
	
	
	public class ListCell{
		
		private TextView spKey,spAddress;
		
		public ListCell(TextView spKey,TextView spAddress) {
			this.spKey = spKey;
			this.spAddress = spAddress;
		}
		public TextView getSpKey() {
			return spKey;
		}
		public TextView getSpAddress() {
			return spAddress;
		}
	}

}
