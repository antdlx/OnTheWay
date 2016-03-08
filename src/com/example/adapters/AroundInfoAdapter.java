package com.example.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.infos.AroundInfo;
import com.example.infos.MyCarpoolMessageInfo;
import com.example.ontheway.R;

public class AroundInfoAdapter extends BaseAdapter{
	private Context context;
	private List<AroundInfo> messageInfoList = new ArrayList<AroundInfo>();

	public AroundInfoAdapter (Context context){
		this.context = context;
	}
	
	public Context getContext() {
		return context;
	}

	public void addAll(List<AroundInfo> list_info){
		messageInfoList.addAll(list_info);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return messageInfoList.size();
	}

	@Override
	public AroundInfo getItem(int position) {
		return messageInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.listview_around_info_cell, null);
			convertView.setTag(new ListCell((TextView) convertView
					.findViewById(R.id.tvStartPosition), (TextView) convertView
					.findViewById(R.id.tvEndPosition),(TextView) convertView
					.findViewById(R.id.tvPhone),(TextView) convertView
					.findViewById(R.id.tvPeopleCount),(TextView)convertView.findViewById(R.id.tvGoTime)));
		}
		ListCell lc = (ListCell) convertView.getTag();
		AroundInfo data = getItem(position);
		lc.getTvStartPositon().setText(data.getStrStartPositon());
		lc.getTvEndPosition().setText(data.getStrEndPosition());
		lc.getTvPhone().setText(data.getStrPhone());
		lc.getTvPeopleCount().setText(data.getStrPeopleCount());
		lc.getTvGoTime().setText(data.getStGoTime());
		
		return convertView;
	}

	private class ListCell{
		private TextView tvStartPositon,tvEndPosition,tvPhone,tvPeopleCount,tvGoTime;
		
		public ListCell(TextView tvStartPositon,TextView tvEndPosition,TextView tvPhone,TextView tvPeopleCount
				,TextView tvGoTime){
			this.tvStartPositon = tvStartPositon;
			this.tvEndPosition = tvEndPosition;
			this.tvPhone = tvPhone;
			this.tvPeopleCount = tvPeopleCount;
			this.tvGoTime = tvGoTime;
		}

		public TextView getTvGoTime() {
			return tvGoTime;
		}

		public TextView getTvStartPositon() {
			return tvStartPositon;
		}

		public TextView getTvEndPosition() {
			return tvEndPosition;
		}

		public TextView getTvPhone() {
			return tvPhone;
		}

		public TextView getTvPeopleCount() {
			return tvPeopleCount;
		}

	}
}
