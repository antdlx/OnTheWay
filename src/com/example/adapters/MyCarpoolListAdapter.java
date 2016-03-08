package com.example.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.GetChars;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.infos.MyCarpoolMessageInfo;
import com.example.ontheway.R;

public class MyCarpoolListAdapter extends BaseAdapter {
	private Context context;
	private List<MyCarpoolMessageInfo> messageInfoList = new ArrayList<MyCarpoolMessageInfo>();

	public MyCarpoolListAdapter (Context context){
		this.context = context;
	}
	
	public Context getContext() {
		return context;
	}

	public void addAll(List<MyCarpoolMessageInfo> list_info){
		messageInfoList.addAll(list_info);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return messageInfoList.size();
	}

	@Override
	public MyCarpoolMessageInfo getItem(int position) {
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
					R.layout.listview_carpool_message, null);
			convertView.setTag(new ListCell((TextView) convertView
					.findViewById(R.id.tvStartPosition), (TextView) convertView
					.findViewById(R.id.tvEndPosition),(TextView) convertView
					.findViewById(R.id.tvPhone),(TextView) convertView
					.findViewById(R.id.tvConditionTime),(TextView) convertView
					.findViewById(R.id.tvPeopleCount)));
		}
		ListCell lc = (ListCell) convertView.getTag();
		MyCarpoolMessageInfo data = getItem(position);
		lc.getTvStartPositon().setText(data.getStrStartPositon());
		lc.getTvEndPosition().setText(data.getStrEndPosition());
		lc.getTvPhone().setText(data.getStrPhone());
		lc.getTvConditionTime().setText(data.getStrConditionTime());
		lc.getTvPeopleCount().setText(data.getStrPeopleCount());
		
		return convertView;
	}

	private class ListCell{
		private TextView tvStartPositon,tvEndPosition,tvPhone,
				tvConditionTime,tvPeopleCount;
		
		public ListCell(TextView tvStartPositon,TextView tvEndPosition,TextView tvPhone,TextView tvConditionTime,TextView tvPeopleCount){
			this.tvStartPositon = tvStartPositon;
			this.tvEndPosition = tvEndPosition;
			this.tvPhone = tvPhone;
			this.tvConditionTime = tvConditionTime;
			this.tvPeopleCount = tvPeopleCount;
		}

		public TextView getTvStartPositon() {
			return tvStartPositon;
		}

		public void setTvStartPositon(TextView tvStartPositon) {
			this.tvStartPositon = tvStartPositon;
		}

		public TextView getTvEndPosition() {
			return tvEndPosition;
		}

		public void setTvEndPosition(TextView tvEndPosition) {
			this.tvEndPosition = tvEndPosition;
		}

		public TextView getTvPhone() {
			return tvPhone;
		}

		public void setTvPhone(TextView tvPhone) {
			this.tvPhone = tvPhone;
		}

		public TextView getTvConditionTime() {
			return tvConditionTime;
		}

		public void setTvConditionTime(TextView tvConditionTime) {
			this.tvConditionTime = tvConditionTime;
		}

		public TextView getTvPeopleCount() {
			return tvPeopleCount;
		}

		public void setTvPeopleCount(TextView tvPeopleCount) {
			this.tvPeopleCount = tvPeopleCount;
		}
	}
}
