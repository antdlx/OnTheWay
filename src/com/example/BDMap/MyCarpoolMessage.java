package com.example.BDMap;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.example.adapters.MyCarpoolListAdapter;
import com.example.infos.Configs;
import com.example.infos.MyCarpoolMessageInfo;
import com.example.ontheway.R;
import com.lushang.net.GetMyCarpoolMessage;

public class MyCarpoolMessage extends Activity {
	private ListView list;
	private List<MyCarpoolMessageInfo> listInfo = new ArrayList<MyCarpoolMessageInfo>();
	private MyCarpoolListAdapter adapter ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_carpool_message);
		findViewById(R.id.btnBack).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		list = (ListView) findViewById(R.id.list);
		adapter = new MyCarpoolListAdapter(this);
		list.setAdapter(adapter);
		
		new GetMyCarpoolMessage(Configs.T_UID, new GetMyCarpoolMessage.SuccessCallBack() {
			
			@Override
			public void onSuccess(List<MyCarpoolMessageInfo> list) {
				adapter.addAll(list);
			}
		}, new GetMyCarpoolMessage.FailCallBack() {
			
			@Override
			public void onFail() {
			}
		});
	}

}
