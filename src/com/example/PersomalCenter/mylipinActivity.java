package com.example.PersomalCenter;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.adapters.mylipinListviewAdapter;
import com.example.ontheway.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class mylipinActivity extends Activity implements OnClickListener{
	private Button btn_back;
	private ListView lv_mylipin;
	protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylipin);
        btn_back=(Button) findViewById(R.id.btn_back);
        lv_mylipin=(ListView) findViewById(R.id.lv_mylipin);
        btn_back.setOnClickListener(this);
        String[]data={"iphone6"};
        int size=data.length;
    	ArrayList<HashMap<String, String>>listItem = new ArrayList<HashMap<String, String>>();
    	for(int i=0;i<size;i++){
    		HashMap<String, String> map = new HashMap<String, String>();
    	    map.put("data", data[i]); 
    	    listItem.add(map); 
    	    }  
    	mylipinListviewAdapter ItemAdapter= new mylipinListviewAdapter(this, listItem);  
    	lv_mylipin.setAdapter(ItemAdapter);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			Intent intent = new Intent();
			intent.setClass(this, PersonalCenterActivity.class);
			startActivity(intent);
			finish();
			break;
		
		default:
			break;
	}

	}}
