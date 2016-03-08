package com.example.BDMap;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.example.adapters.DialogListviewAdapter;
import com.example.adapters.SuggestionDialogListViewAdapter;
import com.example.infos.Configs;
import com.example.infos.DialogListviewInfo;
import com.example.infos.SuggestionDialogInfo;
import com.example.ontheway.R;

public class RouteActivity extends Activity implements
		OnGetPoiSearchResultListener,OnGetSuggestionResultListener{
	// About Ui
	private Button btnBack, btnSearch;
	private TextView edDestination;

	// Anout poi
	private PoiSearch poiSearch;
	private PoiResult currentResult;
	private ListView list;
	private DialogListviewAdapter adapter;
	private String destination;

	// About Location
	private LatLng currentLatlng;
	private LocationClient locationClient;
	private MyLocationListenerX locationListener;

	// About history
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;
	private int spIndex;
	private String[] strHistory;
	private String[] EmptyStrHistory = new String[0];
	private ArrayAdapter<String> arrayAdapter;
	private ListView historyList;
	
	//About waiting
	private Dialog waitingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_route);

		initView();
		initMyLocation();
		initPoiSearch();
	}

	/**
	 * 定位当前位置坐标
	 */
	private void initMyLocation() {
		locationClient = new LocationClient(RouteActivity.this);
		locationListener = new MyLocationListenerX();
		//注册定位
		locationClient.registerLocationListener(locationListener);
		
		//设置client相关的设置
		LocationClientOption op = new LocationClientOption();
		op.setCoorType("bd09ll");  //坐标类型，百度坐标系
		op.setIsNeedAddress(true); //是否需要在result的回调函数中返回地址
		op.setOpenGps(true);
		op.setScanSpan(60000);  //每1分钟刷新一次定位
		locationClient.setLocOption(op);
	}

	private void initView() {
		View v = LayoutInflater.from(RouteActivity.this).inflate(R.layout.dialog_waiting, null);
		waitingDialog = new Dialog(RouteActivity.this, R.style.MyDialog);
		waitingDialog.setContentView(v);

		edDestination = (TextView) findViewById(R.id.edDestination);

		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//建议查询
				destination = edDestination.getText().toString();
				SuggestionSearch mSuggestionSearch = SuggestionSearch.newInstance();
				mSuggestionSearch.setOnGetSuggestionResultListener(RouteActivity.this);
				mSuggestionSearch.requestSuggestion((new SuggestionSearchOption().keyword(destination).city(Configs.CITY)));
				
				// history 相关
				spIndex = mSharedPreferences.getInt("index", 0);
				mEditor.putInt("index", ++spIndex);
				mEditor.putString("history" + spIndex, destination);
				System.out.println("目的地" + destination);
				mEditor.commit();
				arrayAdapter.notifyDataSetChanged();
				
				showWaitingDialog();
			}
		});
		

		// 初始化history相关
		mSharedPreferences = getSharedPreferences("History", MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
		historyList = (ListView) findViewById(R.id.HistoryList);
		// 点击历史记录自动填充
		historyList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				edDestination.setText(strHistory[position]);
			}
		});
		spIndex = mSharedPreferences.getInt("index", 0);
		System.out.println("SP数量" + spIndex);
		// 实现倒序显示
		if (spIndex <= 7 && spIndex != 0) {
			strHistory = new String[spIndex];
			for (int i = spIndex - 1; i >= 0; i--) {
				strHistory[i] = mSharedPreferences.getString("history"
						+ (spIndex - i), "空");
			}
		} else {
			if (spIndex != 0) {
				strHistory = new String[7];
				for (int i = 6; i >= 0; i--) {
					strHistory[i] = mSharedPreferences.getString("history" + (spIndex-i),
							"空7");
				}
			} else {
				strHistory = new String[0];
			}
		}
		arrayAdapter = new ArrayAdapter<String>(RouteActivity.this,
				android.R.layout.simple_list_item_1, strHistory);
		historyList.setAdapter(arrayAdapter);

		Button btnClearHistory = (Button) findViewById(R.id.btnClearHistory);
		btnClearHistory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mEditor.clear().commit();
				strHistory = EmptyStrHistory;
				arrayAdapter = new ArrayAdapter<String>(RouteActivity.this,
						android.R.layout.simple_list_item_1, EmptyStrHistory);
				historyList.setAdapter(arrayAdapter);
				System.out.println("删除edit");
			}
		});
	}

	/**
	 * 生成载入中请等待的dialog
	 */
	protected void showWaitingDialog() {
		waitingDialog.show();
	}

	/**
	 * 初始化poi检索模块
	 */
	private void initPoiSearch() {
		poiSearch = PoiSearch.newInstance();
		poiSearch.setOnGetPoiSearchResultListener(this);
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {
	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		// 没找到location
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(RouteActivity.this, "对不起，您输入的位置搜索不到，请尝试再次输入", Toast.LENGTH_LONG).show();
			return;
		}
		// 检索结果正常返回
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			currentResult = result;
			// 显示一个自定义Dialog来装载poi检索结果
			if (result.getAllPoi().size() > 1) {
				showMyDialog();
			} else {
				// 初始化dialog中的listView，并使用序列化传递必要的数据给路线规划
				DialogListviewInfo dialogInfo = new DialogListviewInfo(result.getAllPoi().get(0).name,result.getAllPoi().get(0).address, result.getAllPoi().get(0).location.latitude, result.getAllPoi().get(0).location.longitude);
				Intent i = new Intent(RouteActivity.this,
						TransitRouteResultActivity.class);
				i.putExtra("info", dialogInfo);
				i.putExtra("clatitude", currentLatlng.latitude);
				i.putExtra("clongitude", currentLatlng.longitude);
				i.putExtra("destination", destination);
				startActivity(i);
			}
			return;
		}
		// 检索词有岐义
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

			String strInfo = "在";
			for (CityInfo cityInfo : result.getSuggestCityList()) {
				strInfo += cityInfo.city;
				strInfo += ",";
			}
			strInfo += "找到结果";
			Toast.makeText(RouteActivity.this, strInfo, Toast.LENGTH_LONG)
					.show();
		}
	}

	/**
	 * 显示自定义dialog
	 */
	private void showMyDialog() {

		if (currentResult.getAllPoi().get(0).address != "") {
			// 初始化dialogView
			View v = LayoutInflater.from(RouteActivity.this).inflate(
					R.layout.dialog_mydialog, null);

			List<DialogListviewInfo> dialogData = new ArrayList<DialogListviewInfo>();
			// 加载信息
			for (int i = 0; i < currentResult.getAllPoi().size(); i++) {
				System.out.println(currentResult.getAllPoi().get(i).name);
				dialogData.add(new DialogListviewInfo(currentResult.getAllPoi().get(i).name,
						currentResult.getAllPoi().get(i).address,
						currentResult.getAllPoi().get(i).location.latitude,
						currentResult.getAllPoi().get(i).location.longitude));
				Log.d("KKKKKKKKK", currentResult.getAllPoi().get(i).address);
			}

			list = (ListView) v.findViewById(R.id.listView_DialoglistView);
			adapter = new DialogListviewAdapter(RouteActivity.this);
			list.setAdapter(adapter);
			adapter.addAll(dialogData);
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent i = new Intent(RouteActivity.this,TransitRouteResultActivity.class);
					DialogListviewInfo dialogInfo = adapter.getItem(position);
					i.putExtra("info", dialogInfo);
					i.putExtra("clatitude", currentLatlng.latitude);
					i.putExtra("clongitude", currentLatlng.longitude);
					i.putExtra("destination", destination);
					startActivity(i);
				}
			});

			// 初始化dialog
			Dialog dialog = new Dialog(RouteActivity.this, R.style.MyDialog);
			dialog.setContentView(v);

			Window dialogWindow = dialog.getWindow();
			WindowManager.LayoutParams manager = dialogWindow.getAttributes();
			int WIDTH = getResources().getDisplayMetrics().widthPixels;
			int HEIGHT = getResources().getDisplayMetrics().heightPixels;

			manager.width = (WIDTH / 10) * 9;

			dialogWindow.setAttributes(manager);

			dialog.show();

			// 点击外部隐藏
			dialog.setCanceledOnTouchOutside(true);
		} 
	else {
			Toast.makeText(RouteActivity.this, "请输入有效地址", Toast.LENGTH_LONG)
					.show();
		}
	}
	
	/**
	 * 开启定位
	 */
	@Override
	protected void onStart() {
		super.onStart();
		if (!locationClient.isStarted()) {
			locationClient.start();	
		}
	}
	
	/**
	 * 关闭定位
	 */
	@Override
	protected void onStop() {
		super.onStop();
		locationClient.stop();
	}
	
	@Override
	protected void onDestroy() {
		waitingDialog.dismiss();
		poiSearch.destroy();
		super.onDestroy();
	}
	
	/**
	 * 
	 * @author DLX
	 *				自定义的定位监听器
	 */
	private class MyLocationListenerX implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location!=null) {
				Configs.mLocation = location;
				currentLatlng = new LatLng(location.getLatitude(), location.getLongitude());
				Log.d("当前坐标", "latitude : "+location.getLatitude()+"  longtitude : "+location.getLongitude());
			}
		}
    }

	@Override
	public void onGetSuggestionResult(final SuggestionResult res) {
		for (int j = 0; j < res.getAllSuggestions().size(); j++) {
    		System.out.println(res.getAllSuggestions().get(j).key+"  <-key  "+res.getAllSuggestions().get(j).city+"  <-city  "+res.getAllSuggestions().get(j).district+"  <-district");
		}
		//隐藏加载Dialog
		waitingDialog.hide();
		
		if (res.getAllSuggestions().size()!=0) {
			// 初始化dialogView
			View sv = LayoutInflater.from(RouteActivity.this).inflate(
					R.layout.dialog_mydialog, null);

			List<SuggestionDialogInfo> suggestionDialogInfos = new ArrayList<SuggestionDialogInfo>();
			// 加载信息
			for (int i = 0; i < res.getAllSuggestions().size(); i++) {
				suggestionDialogInfos.add(new SuggestionDialogInfo(res.getAllSuggestions().get(i).key,
						(res.getAllSuggestions().get(i).city+"   "+res.getAllSuggestions().get(i).district)
						));
			}
			sAdapter = new SuggestionDialogListViewAdapter(RouteActivity.this);
			list = (ListView) sv.findViewById(R.id.listView_DialoglistView);
			list.setAdapter(sAdapter);
			sAdapter.addAll(suggestionDialogInfos);
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Log.d("---检索---",res.getAllSuggestions().get(position).key );
					poiSearch.searchInCity((new PoiCitySearchOption())
							.city(Configs.CITY)
							.keyword(res.getAllSuggestions().get(position).key.trim())
							.pageNum(10));	
				}
			});

			// 初始化dialog
			Dialog dialog = new Dialog(RouteActivity.this, R.style.MyDialog);
			dialog.setContentView(sv);

			Window dialogWindow = dialog.getWindow();
			WindowManager.LayoutParams manager = dialogWindow.getAttributes();
			int WIDTH = getResources().getDisplayMetrics().widthPixels;
			int HEIGHT = getResources().getDisplayMetrics().heightPixels;

			manager.width = (WIDTH / 10) * 9;

			dialogWindow.setAttributes(manager);

			dialog.show();

			// 点击外部隐藏
			dialog.setCanceledOnTouchOutside(true);
		}
	}
		private SuggestionDialogListViewAdapter sAdapter;
}