package com.example.adapters;

import java.util.ArrayList;
import java.util.List;

import com.example.infos.DialogListviewInfo;
import com.example.ontheway.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DialogListviewAdapter extends BaseAdapter {
	
	private Context context;
	private List<DialogListviewInfo> list = new ArrayList<DialogListviewInfo>();
	
	public DialogListviewAdapter(Context context) {
		this.context = context;
	}
	
	public void addAll(List<DialogListviewInfo> data){
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
	public DialogListviewInfo getItem(int position) {
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
		DialogListviewInfo data = getItem(position);
		
		lc.getSpName().setText(data.getSpName());
		lc.getSpAddress().setText(data.getSpAddress());
		
		return convertView;
	}
	
	
	public class ListCell{
		
		private TextView spName,spAddress;
		
		public ListCell(TextView spName,TextView spAddress) {
			this.spName = spName;
			this.spAddress = spAddress;
		}
		public TextView getSpName() {
			return spName;
		}
		public TextView getSpAddress() {
			return spAddress;
		}
	}

}
