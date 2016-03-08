package com.example.PersomalCenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.infos.Configs;
import com.example.infos.MyBaseInformation;
import com.lushang.net.NetConnection;



public class NetGetXiugaixinxi {
	public List<MyBaseInformation>list = new ArrayList<MyBaseInformation>();
	
	public NetGetXiugaixinxi(String uid,final SuccessCallBack successCallBack,final FailCallBack failCallBack){
		new NetConnection(Configs.URL_GET_BASE_INFO, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				try {
					JSONArray array = new JSONArray(result);
					JSONObject jo;
					MyBaseInformation info;
					for (int i = 0; i < array.length(); i++) {
						jo = array.getJSONObject(i);
						info = new MyBaseInformation();
						info.setnickname(jo.getString(Configs.NICKNAME));
						info.setimg_url(jo.getString(Configs.IMG_URL));
						info.setsex(jo.getString(Configs.SEX));
						info.setage(jo.getString(Configs.AGE));
						info.setcity(jo.getString(Configs.CITY2));
						list.add(info);
			}
					successCallBack.onSuccess(list);
		}catch (JSONException e) {
			failCallBack.onFail();
			e.printStackTrace();}}}, new NetConnection.FailCallback() {
				
				@Override
				public void onFail() {
					// TODO Auto-generated method stub
					failCallBack.onFail();
				}
			},Configs.Uid,uid);
	}
	public static interface SuccessCallBack{
		void onSuccess(List<MyBaseInformation>list);
	}
	public static interface FailCallBack{
		void onFail();
	}
}
