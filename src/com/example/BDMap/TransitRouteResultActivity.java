package com.example.BDMap;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.example.adapters.ExpandableListviewAdapter;
import com.example.infos.Configs;
import com.example.infos.DialogListviewInfo;
import com.example.infos.ExpandableviewGInfo;
import com.example.ontheway.R;

public class TransitRouteResultActivity extends Activity implements
		OnGetRoutePlanResultListener {

	private DialogListviewInfo info;
	private LatLng currentLatlng;
	private LatLng destinationLatlng;
	private String destination;

	// About RoutePlan
	private RoutePlanSearch routeSearch;
	private List<ExpandableviewGInfo> groupInfos = new ArrayList<ExpandableviewGInfo>();
	private List<List<String>> childString = new ArrayList<List<String>>();
	private String GBusName;
	private ExpandableviewGInfo exGinfo;
	private char[] Cinfo;
	private boolean onlyOneBus = false;

	// About ExpandableListView
	private ExpandableListviewAdapter exAdapter;
	private ExpandableListView exList;

	//About waiting
		private Dialog waitingDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_transitroute);

		// 获取坐标信息
		Intent i = getIntent();
		info = (DialogListviewInfo) i.getSerializableExtra("info");
		currentLatlng = new LatLng(i.getDoubleExtra("clatitude", 0.0),
				i.getDoubleExtra("clongitude", 0.0));
		destinationLatlng = new LatLng(info.getSpLatitude(),
				info.getSpLongitude());
		TextView tvDestination = (TextView) findViewById(R.id.tvDestination);
		destination = i.getStringExtra("destination");
		tvDestination.setText(destination);

		initRoutePlanSearch();
		
		initWaitingDialog();
	}

	private void initWaitingDialog() {
		View v = LayoutInflater.from(TransitRouteResultActivity.this).inflate(R.layout.dialog_waiting, null);
		waitingDialog = new Dialog(TransitRouteResultActivity.this, R.style.MyDialog);
		waitingDialog.setContentView(v);
		
		waitingDialog.show();
	}

	/**
	 * 初始化用于呈现公交结果的expandableView
	 */
	private void initExpandableView() {
		exList = (ExpandableListView) findViewById(R.id.exlistView_transitResult);
		exAdapter = new ExpandableListviewAdapter(
				TransitRouteResultActivity.this);
		exList.setGroupIndicator(null);
		exList.setAdapter(exAdapter);
		exAdapter.addAllGroup(groupInfos);
		exAdapter.addAllChild(childString);
	}

	/**
	 * 初始化路径规划模块
	 */
	private void initRoutePlanSearch() {
		routeSearch = RoutePlanSearch.newInstance();
		routeSearch.setOnGetRoutePlanResultListener(this);

		PlanNode stNode = PlanNode.withLocation(currentLatlng);
		PlanNode edNode = PlanNode.withLocation(destinationLatlng);

		System.out.println("起始位置" + currentLatlng.latitude + "*"
				+ currentLatlng.longitude);
		System.out.println("终止位置" + destinationLatlng.latitude + "*"
				+ destinationLatlng.longitude);

		routeSearch.transitSearch(new TransitRoutePlanOption().from(stNode).city(Configs.CITY).to(edNode));
	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {
		System.out.println("KKKKKKKKKKKKKKKKKKK"+result.error);
		waitingDialog.hide();
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(TransitRouteResultActivity.this, "抱歉，未找到结果"+result.error,
					Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			result.getSuggestAddrInfo();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			// 初始化groupString
			for (int i = 0; i < result.getRouteLines().size(); i++) {

				Log.i("线路数", result.getRouteLines().size() + "");

				if (result.getRouteLines().get(i).getAllStep().get(1)
						.getVehicleInfo().getTitle() != null) {
					GBusName = result.getRouteLines().get(i).getAllStep()
							.get(1).getVehicleInfo().getTitle();
					System.out.println("2222222222222222222222222222222222"
							+ GBusName);
				}

				// 初始化childString,注意ChildStringx必须在这里声明，体会一下
				List<String> childStringx = new ArrayList<String>();
				char[] Cinfox = new char[8];

				for (int j = 0; j < result.getRouteLines().get(i).getAllStep()
						.size(); j++) {

					Log.i("子线路数", result.getRouteLines().get(i).getAllStep()
							.size()
							+ "*" + j);

					// 自定义的筛选该线路全部公交name的方法
					Cinfo = result.getRouteLines().get(i).getAllStep().get(j)
							.getInstructions().toCharArray();
					for (int ci = 2; ci < Cinfo.length && j > 1; ci++) {
						if (Cinfo[0] == '乘' && Cinfo[1] == '坐'
								&& Cinfo[ci] != '路') {
							Cinfox[ci - 2] = Cinfo[ci];
							onlyOneBus = true;
						} else {
							break;
						}
					}
					childStringx.add(result.getRouteLines().get(i).getAllStep()
							.get(j).getInstructions());
					System.out.println("333333333333333333333333333333"
							+ childStringx.get(j));
				}

				if (onlyOneBus) {
					GBusName += " / " + String.valueOf(Cinfox) + "路";
					onlyOneBus = false;
				}

				exGinfo = new ExpandableviewGInfo(GBusName, result
						.getRouteLines().get(i).getDistance(), result
						.getRouteLines().get(i).getDuration(), i + 1);
				groupInfos.add(exGinfo);

				childString.add(childStringx);
				System.out.println("oooooooooooooooooooooooooooooooooooooo");
			}

			initExpandableView();
		}
	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		
	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult arg0) {

	}
	@Override
	protected void onDestroy() {
		waitingDialog.dismiss();
		routeSearch.destroy();
		super.onDestroy();
	}
}
