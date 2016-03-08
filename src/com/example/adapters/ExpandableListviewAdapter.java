package com.example.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.infos.ExpandableviewGInfo;
import com.example.ontheway.R;

public class ExpandableListviewAdapter extends BaseExpandableListAdapter {

	private List<ExpandableviewGInfo> group = new ArrayList<ExpandableviewGInfo>();
	private List<List<String>> child = new ArrayList<List<String>>();

	private Context context;
	private int textIndex=0;

	public ExpandableListviewAdapter(Context context) {
		this.context = context;
	}

	public void addAllGroup(List<ExpandableviewGInfo> g) {
		group.addAll(g);
		notifyDataSetChanged();
	}

	public void addAllChild(List<List<String>> c) {
		child.addAll(c);
		notifyDataSetChanged();
	}

	public Context getContext() {
		return context;
	}

	@Override
	public int getGroupCount() {
		return group.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return child.get(groupPosition).size();
	}

	@Override
	public ExpandableviewGInfo getGroup(int groupPosition) {
		return group.get(groupPosition);
	}

	@Override
	public String getChild(int groupPosition, int childPosition) {
		return (child.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.listview_group, null);
			convertView.setTag(new GroupListCell((TextView) convertView
					.findViewById(R.id.tvBusNames), (TextView) convertView
					.findViewById(R.id.tvNum), (TextView) convertView
					.findViewById(R.id.tvDetail)));
		}

		GroupListCell glc = (GroupListCell) convertView.getTag();
		ExpandableviewGInfo Groupinfos = getGroup(groupPosition);

		glc.getTvBusNames().setText(Groupinfos.getBusNames());
		glc.getTvNum().setText(Groupinfos.getNum());
		glc.getTvDetail().setText(Groupinfos.getDuration()+"/"+Groupinfos.getDistance());

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.listview_child, null);
			convertView.setTag(new ChildListCell((TextView) convertView
					.findViewById(R.id.tvChild)));
		}

		ChildListCell clc = (ChildListCell) convertView.getTag();
		String ChildText = getChild(groupPosition, childPosition);

		clc.getTvCihld().setText(ChildText);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	public class GroupListCell {
		private TextView tvBusNames, tvNum, tvDetail;

		public GroupListCell(TextView tvBusNames, TextView tvNum, TextView tvDetail) {
			this.tvBusNames = tvBusNames;
			this.tvNum = tvNum;
			this.tvDetail = tvDetail;
		}

		public TextView getTvNum() {
			return tvNum;
		}

		public TextView getTvDetail() {
			return tvDetail;
		}

		public TextView getTvBusNames() {
			return tvBusNames;
		}
	}

	public class ChildListCell {
		private TextView tvChild;

		public ChildListCell(TextView tvChild) {
			this.tvChild = tvChild;
		}

		public TextView getTvCihld() {
			return tvChild;
		}
	}
}
