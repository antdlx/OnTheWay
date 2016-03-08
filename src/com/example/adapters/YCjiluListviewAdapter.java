package com.example.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.infos.DialogListviewInfo;
import com.example.infos.MyCarpoolMessageInfo;
import com.example.ontheway.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class YCjiluListviewAdapter extends BaseAdapter {
	private List<MyCarpoolMessageInfo> messageInfoList = new ArrayList<MyCarpoolMessageInfo>();
	private Context context;
	
	public YCjiluListviewAdapter(Context context){
		this.context=context;
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
		// TODO Auto-generated method stub
		return messageInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return messageInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.listview_yuechejilu, null);
			convertView.setTag(new ListCell((TextView) convertView
					.findViewById(R.id.tv_time), (TextView) convertView
					.findViewById(R.id.tv_chufadi),(TextView) convertView
					.findViewById(R.id.tv_mudidi),(TextView) convertView
					.findViewById(R.id.tv_faqiren)));
		}
		ListCell lc = (ListCell) convertView.getTag();
		MyCarpoolMessageInfo data = (MyCarpoolMessageInfo) getItem(position);
		lc.gettv_chufadi().setText(data.getStrStartPositon());
		lc.gettv_mudidi().setText(data.getStrEndPosition());
		lc.gettv_time().setText(data.getStrConditionTime());
		lc.gettv_faqiren().setText(data.getStrPeopleCount());
		
		return convertView;
	}
	private class ListCell{
		private TextView tv_chufadi,tv_mudidi,tv_time,tv_faqiren;
		
		public ListCell(TextView tv_chufadi,TextView tv_mudidi,TextView tv_time,TextView tv_faqiren){
			this.tv_chufadi = tv_chufadi;
			this.tv_mudidi = tv_mudidi;
			this.tv_time = tv_time;
			this.tv_faqiren = tv_faqiren;
		}

		public TextView gettv_chufadi() {
			return tv_chufadi;
		}

		public void settv_chufadi(TextView tv_chufadi) {
			this.tv_chufadi = tv_chufadi;
		}

		public TextView gettv_mudidi() {
			return tv_mudidi;
		}

		public void settv_mudidi(TextView tv_mudidi) {
			this.tv_mudidi = tv_mudidi;
		}

		

		public TextView gettv_time() {
			return tv_time;
		}

		public void settv_time(TextView tv_time) {
			this.tv_time = tv_time;
		}

		public TextView gettv_faqiren() {
			return tv_faqiren;
		}

		public void settv_faqiren(TextView tv_faqiren) {
			this.tv_faqiren = tv_faqiren;
		}
	}
}

	
	


