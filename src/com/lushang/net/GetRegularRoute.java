package com.lushang.net;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.infos.Configs;
import com.lushang.net.AddRegularRoute.FailCallBack;
import com.lushang.net.AddRegularRoute.SuccessCallBack;

public class GetRegularRoute {
	public GetRegularRoute(String uid,final SuccessCallBack successCallBack,final FailCallBack failCallBack){

		new NetConnection(Configs.URL_SET_REGULAR_ROUTE, new NetConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				try {
					JSONArray array = new JSONArray(result);
					JSONObject jo;
					for (int i = 0; i < array.length(); i++) {
						
					}
					successCallBack.onSuccess();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new NetConnection.FailCallback() {

			@Override
			public void onFail() {
				failCallBack.onFail();
			}
		}, Configs.Uid,uid);
	}

	public static interface SuccessCallBack{
		void onSuccess();
	}
	public static interface FailCallBack{
		void onFail();
	}
}
