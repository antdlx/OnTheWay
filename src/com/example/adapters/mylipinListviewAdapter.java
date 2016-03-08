package com.example.adapters;

import java.util.HashMap;
import java.util.List;

import com.example.ontheway.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class mylipinListviewAdapter extends BaseAdapter{
	private List<HashMap<String,String>>list;
	private Context context;
	
	public mylipinListviewAdapter(Context context,List<HashMap<String,String>>list){
		this.context=context;
		this.list=list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		 return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
			LayoutInflater mInflater = LayoutInflater.from(context);
			View view = null;
			view = mInflater.inflate(R.layout.listview_mylipin, null);
			TextView mylipin = (TextView) view.findViewById(R.id.tv_lipin);
			String content=list.get(position).get("data");
			String []items=content.split(",");
			mylipin.setText(items[0]);
		return view;
	}

}
