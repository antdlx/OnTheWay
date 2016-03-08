package com.example.BDMap;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.example.adapters.AroundInfoAdapter;
import com.example.infos.AroundInfo;
import com.example.infos.Configs;
import com.example.infos.MyCarpoolMessageInfo;
import com.example.ontheway.R;
import com.lushang.net.GetAround;

public class AroundInformation extends Activity {
	private ListView list;
	private List<AroundInfo> listInfo = new ArrayList<AroundInfo>();
	private AroundInfoAdapter adapter ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_around_info);
		
		findViewById(R.id.btnBack).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		adapter = new AroundInfoAdapter(this);
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		
		Log.d("--get-around--", Configs.myLatitude+"****"+Configs.myLongtitude);
		
		new GetAround(Configs.myLatitude+"", Configs.myLongtitude+"", "5", new GetAround.SuccessCallBack() {
			
			@Override
			public void onSuccess(List<AroundInfo> lists) {
				adapter.addAll(lists);
			}
		}, new GetAround.FailCallBack() {
			
			@Override
			public void onFail() {
			}
		});
	}
}
