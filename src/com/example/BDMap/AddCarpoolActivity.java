package com.example.BDMap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import com.example.infos.Configs;
import com.example.ontheway.R;
import com.lushang.net.NetAddCarpool;

public class AddCarpoolActivity extends Activity {

	private PoiSearch mPoiSearch;
	private AutoCompleteTextView autoStart, autoEnd;
	private List<MyCompleteTextViewInfo> Mylist = new ArrayList<MyCompleteTextViewInfo>();
	private List<MyCompleteTextViewInfo> endMylist = new ArrayList<MyCompleteTextViewInfo>();
	private MyAutoCompleteTextViewAdapter adapter,endAdapter;
	private Button btn_upload;
	private double startX, startY, endX, endY;
	private String startName, endName;
	private TextView tv_people,tv_hour,tv_min;
	private int peopleNum=3,hours,mins;
	private String sdate;
	private int month,day,year;
	private String leave_time;
	private boolean notClick=true;
	private MyCompleteTextViewInfo mctvi,endMctvi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_add_carpool);

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
		
		autoStart = (AutoCompleteTextView) findViewById(R.id.auto_start);
		adapter = new MyAutoCompleteTextViewAdapter(AddCarpoolActivity.this, Mylist);
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
									Toast.makeText(AddCarpoolActivity.this,
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
									Toast.makeText(AddCarpoolActivity.this,
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
		
		autoEnd = (AutoCompleteTextView) findViewById(R.id.auto_end);
		endAdapter = new MyAutoCompleteTextViewAdapter(AddCarpoolActivity.this, endMylist);
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
									Toast.makeText(AddCarpoolActivity.this,
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
									Toast.makeText(AddCarpoolActivity.this,
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
		
		tv_people = (TextView) findViewById(R.id.tv_people);
		tv_people.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final popupwindow_people popup_people = new popupwindow_people(AddCarpoolActivity.this);
				popup_people.getTwoPeople().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						peopleNum = 2;
						tv_people.setText(peopleNum+"");
						popup_people.dismiss();
					}
				});
				popup_people.getThreePeople().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						peopleNum =3;
						tv_people.setText(peopleNum+"");
						popup_people.dismiss();
					}
				});
				popup_people.getFourPeople().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						peopleNum = 4;
						tv_people.setText(peopleNum+"");
						popup_people.dismiss();
					}
				});
				popup_people.showPopupWindow(tv_people);
			}
		});
		
		tv_hour = (TextView) findViewById(R.id.tv_hour);
		tv_hour.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final popupwindow_hour popup_hour = new popupwindow_hour(AddCarpoolActivity.this);
				ListView listView = popup_hour.getListView();
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						hours = (++arg2);
						tv_hour.setText(hours+"");
						popup_hour.dismiss();
					}
				});
				popup_hour.showPopupWindow(tv_hour);
			}
		});
		
		tv_min = (TextView) findViewById(R.id.tv_minutes);
		tv_min.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final popupwindow_mins popup_mins = new popupwindow_mins(AddCarpoolActivity.this);
				ListView listView = popup_mins.getListView();
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						mins = (++arg2)*5;
						tv_min.setText(mins+"");
						popup_mins.dismiss();
					}
				});
				popup_mins.showPopupWindow(tv_min);
			}
		});
		
		btn_upload = (Button) findViewById(R.id.btn_upload);
		btn_upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				 GetLeaveTime();
				 Log.d("---初始x---",startX+"***"+startY+"***"+ endX+"***"+endY+"***"+startName+"***"+ endName+"***"+peopleNum+"***"+leave_time);
				 User user = BmobUser.getCurrentUser(AddCarpoolActivity.this,User.class);
				 new NetAddCarpool(Configs.T_UID, startX+"", startY+"", endX+"", endY+"", startName, endName, peopleNum+"", leave_time,
						 new NetAddCarpool.SuccessCallback() {
							
							@Override
							public void onSuccess() {
								Toast.makeText(AddCarpoolActivity.this, "您已成功上传~", Toast.LENGTH_LONG).show();
							}
						}, new NetAddCarpool.FailCallback() {
							
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
	
	/**
	 * 得到出发的时间
	 */
	public void GetLeaveTime(){
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		if (month<10) {
			sdate = year+"-0"+month;
		}else {
			sdate = year+"-"+month;
		}
		if (day<10) {
			sdate+="-0"+day;
		}else {
			sdate+="-"+day;
		}
		sdate+=" 00:00:00";
		Log.d("日期",sdate);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d;
		try {
			d = sdf.parse(sdate);
			long l = d.getTime();
			l+=(hours*3600+mins*60);
			leave_time = String.valueOf(l);
			Log.d("时间戳",leave_time);
			} catch (ParseException e) {
			e.printStackTrace();
			}
	}
}
