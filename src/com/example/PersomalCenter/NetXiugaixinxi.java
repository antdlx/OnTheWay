package com.example.PersomalCenter;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.infos.Configs;
import com.lushang.net.NetConnection;


public class NetXiugaixinxi {
	public NetXiugaixinxi(String uid,String nickname,String img_url,String sex,String age,
			String city,final SuccessCallback successCallback,final FailCallback failCallback) {
		new NetConnection(Configs.URL_CHANGE_BASE, new NetConnection.SuccessCallback(){

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				try {
					JSONObject jb = new JSONObject(result);
					successCallback.onSuccess(result);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}},new NetConnection.FailCallback() {
				public void onFail() {
					failCallback.onFail();
				}
			},Configs.UID,uid,
				Configs.NICKNAME,nickname,
				Configs.IMG_URL,img_url,
				Configs.SEX,sex,
				Configs.AGE,age,
				Configs.CITY2,city);
	}
	public static interface SuccessCallback{
		void onSuccess(String result);
	}
	public static interface FailCallback{
		void onFail();
	}
}
