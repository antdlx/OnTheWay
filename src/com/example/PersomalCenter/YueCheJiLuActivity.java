package com.example.PersomalCenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.adapters.MyCarpoolListAdapter;
import com.example.adapters.YCjiluListviewAdapter;
import com.example.infos.Configs;
import com.example.infos.MyCarpoolMessageInfo;
import com.example.ontheway.*;
import com.example.ontheway.R.id;
import com.lushang.net.GetMyCarpoolMessage;




public class YueCheJiLuActivity extends Activity implements OnClickListener{
	private ImageButton ib_back;
	private ListView lv_yuechejilu;
	private YCjiluListviewAdapter adapter;
	@Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yuechejilu);
        ib_back=(ImageButton) findViewById(R.id.ib_back);
        lv_yuechejilu=(ListView) findViewById(R.id.lv_yuechejilu);
        
	
        adapter = new YCjiluListviewAdapter(this);
		lv_yuechejilu.setAdapter(adapter);
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
        
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			Intent intent = new Intent();
			intent.setClass(this, PersonalCenterActivity.class);
			startActivity(intent);
			finish();
			break;
		
		default:
			break;
		}// TODO Auto-generated method stub
		
	}
}
