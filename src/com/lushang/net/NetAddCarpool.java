package com.lushang.net;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.infos.Configs;

public class NetAddCarpool {

	public NetAddCarpool(String uid,String startX,String startY,String endX,
					String endY,String startName,String endName,String peopleNum,String leaveTime,
					final SuccessCallback successCallback,final FailCallback failCallback){
		
		new NetConnection(Configs.URL_ADD_CARPOOL, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
					successCallback.onSuccess();
			}
		}, new NetConnection.FailCallback() {
			
			@Override
			public void onFail() {
				failCallback.onFail();
			}
		},Configs.Uid,uid,
				Configs.START_X,startX,
				Configs.START_Y,startY,
				Configs.END_X,endX,
				Configs.END_Y,endY,
				Configs.START_NAME,startName,
				Configs.END_NAME,endName,
				Configs.PNUM,peopleNum,
				Configs.LEAVE_TIME,leaveTime);
	}
	
	public static interface SuccessCallback{
		void onSuccess();
	}
	public static interface FailCallback{
		void onFail();
	}
}
