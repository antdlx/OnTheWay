package com.example.PersomalCenter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.bmob.im.demo.bean.User;
import com.example.BDMap.MyAutoCompleteTextViewAdapter;
import com.example.BDMap.MyCompleteTextViewInfo;
import com.example.infos.Configs;
import com.example.ontheway.R;
import com.lushang.net.AddRegularRoute;

public class RegularRouteActivity extends Activity{

	private PoiSearch mPoiSearch;
	private AutoCompleteTextView autoStart, autoEnd;
	private List<MyCompleteTextViewInfo> Mylist = new ArrayList<MyCompleteTextViewInfo>();
	private List<MyCompleteTextViewInfo> endMylist = new ArrayList<MyCompleteTextViewInfo>();
	private MyAutoCompleteTextViewAdapter adapter,endAdapter;
	private Button btn_upload;
	private double startX, startY, endX, endY;
	private String startName, endName;
	private boolean notClick=true;
	private MyCompleteTextViewInfo mctvi,endMctvi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_regularroute);

		initView();
		initPoiSearch();
	}

	private void initView() {
		
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					finish();
			}
		});
		
		MyCompleteTextViewInfo mctv = new MyCompleteTextViewInfo("ING", "ing",0.0,0.0);
		Mylist.add(mctv);
		
		autoStart = (AutoCompleteTextView) findViewById(R.id.auto_regular_start);
		adapter = new MyAutoCompleteTextViewAdapter(RegularRouteActivity.this, Mylist);
		autoStart.setAdapter(adapter);
		autoStart.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				Search(autoStart.getText().toString(),
						new OnGetPoiSearchResultListener() {

							@Override
							public void onGetPoiResult(PoiResult result) {
								if ((result == null
										|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND)&&notClick) {
									notClick=true;
									Toast.makeText(RegularRouteActivity.this,
											"对不起，您输入的位置搜索不到，请尝试再次输入",
											Toast.LENGTH_LONG).show();
									return;
								}
								// 检索结果正常返回
								if (result.error == SearchResult.ERRORNO.NO_ERROR) {
									notClick=true;
									List<MyCompleteTextViewInfo> Mylistx = new ArrayList<MyCompleteTextViewInfo>();
									for (int i = 0; i < result.getAllPoi().size(); i++) {
										mctvi = new MyCompleteTextViewInfo(result.getAllPoi().get(i).name,result.getAllPoi().get(i).address,
												result.getAllPoi().get(i).location.latitude,result.getAllPoi().get(i).location.longitude);
										Mylistx.add(mctvi);
									}
									adapter.AddAll(Mylistx);
									return;
								}
								// 检索词有岐义
								if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
									String strInfo = "在";
									for (CityInfo cityInfo : result
											.getSuggestCityList()) {
										strInfo += cityInfo.city;
										strInfo += ",";
									}
									strInfo += "找到结果";
									Toast.makeText(RegularRouteActivity.this,
											strInfo, Toast.LENGTH_LONG).show();
								}
							}

							@Override
							public void onGetPoiDetailResult(
									PoiDetailResult arg0) {
							}
						});
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		autoStart.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				notClick = false;
				Log.d("---VIEW---",adapter.getList().get(position).getTitle());
				startName = adapter.getList().get(position).getTitle();
				autoStart.setText(startName);
				startX =  adapter.getList().get(position).getX();
				startY =  adapter.getList().get(position).getY();
			}
		});
		
		MyCompleteTextViewInfo endMctv = new MyCompleteTextViewInfo("ING", "ing",0.0,0.0);
		endMylist.add(endMctv);
		
		autoEnd = (AutoCompleteTextView) findViewById(R.id.auto_regular_end);
		endAdapter = new MyAutoCompleteTextViewAdapter(RegularRouteActivity.this, endMylist);
		autoEnd.setAdapter(endAdapter);
		autoEnd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				Search(autoEnd.getText().toString(),
						new OnGetPoiSearchResultListener() {

							@Override
							public void onGetPoiResult(PoiResult result) {
								if ((result == null
										|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND)&&notClick) {
									notClick=true;
									Toast.makeText(RegularRouteActivity.this,
											"对不起，您输入的位置搜索不到，请尝试再次输入",
											Toast.LENGTH_LONG).show();
									return;
								}
								// 检索结果正常返回
								if (result.error == SearchResult.ERRORNO.NO_ERROR) {
									notClick=true;
									List<MyCompleteTextViewInfo> Mylistx = new ArrayList<MyCompleteTextViewInfo>();
									for (int i = 0; i < result.getAllPoi().size(); i++) {
										endMctvi = new MyCompleteTextViewInfo(result.getAllPoi().get(i).name,result.getAllPoi().get(i).address,
												result.getAllPoi().get(i).location.latitude,result.getAllPoi().get(i).location.longitude
												);
										Mylistx.add(endMctvi);
									}
									endAdapter.AddAll(Mylistx);
									return;
								}
								// 检索词有岐义
								if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
									String strInfo = "在";
									for (CityInfo cityInfo : result
											.getSuggestCityList()) {
										strInfo += cityInfo.city;
										strInfo += ",";
									}
									strInfo += "找到结果";
									Toast.makeText(RegularRouteActivity.this,
											strInfo, Toast.LENGTH_LONG).show();
								}
							}

							@Override
							public void onGetPoiDetailResult(
									PoiDetailResult arg0) {
							}
						});
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		autoEnd.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				notClick =false;
				Log.d("---VIEW2---",endAdapter.getList().get(position).getTitle());
				endName = endAdapter.getList().get(position).getTitle();
				autoEnd.setText(endName);
				endX =  endAdapter.getList().get(position).getX();
				endY =  endAdapter.getList().get(position).getY();
			}
		});
		
		btn_upload = (Button) findViewById(R.id.btn_regular_upload);
		btn_upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				 Log.d("---初始x---",startX+"***"+startY+"***"+ endX+"***"+endY+"***"+startName+"***"+ endName);
				 User user = BmobUser.getCurrentUser(RegularRouteActivity.this,User.class);
				new AddRegularRoute(Configs.T_UID, startX+"", startY+"", endX+"", endY+"", startName, endName, new AddRegularRoute.SuccessCallBack() {
					
					@Override
					public void onSuccess(String result) {
						if (result.equals("1")) {
							Toast.makeText(RegularRouteActivity.this, "已成功上传常用路线",Toast.LENGTH_LONG).show();
						}
					}
				}, new AddRegularRoute.FailCallBack() {
					
					@Override
					public void onFail() {
						
					}
				});
			}
		});
	}

	private void initPoiSearch() {
		mPoiSearch = PoiSearch.newInstance();
	}

	public void Search(String destination, OnGetPoiSearchResultListener listener) {
		mPoiSearch.setOnGetPoiSearchResultListener(listener);
		mPoiSearch.searchInCity(new PoiCitySearchOption().city(Configs.CITY)
				.keyword(destination).pageNum(10));
	}

	@Override
	protected void onDestroy() {
		mPoiSearch.destroy();
		super.onDestroy();
	}

}
