package com.lushang.net;

import com.example.infos.Configs;

public class AddRegularRoute {
	
	public AddRegularRoute(String uid,String start_x,String start_y,String end_x,String end_y,String start_name,String end_name,
			final SuccessCallBack successCallBack,final FailCallBack failCallBack){
		
		new NetConnection(Configs.URL_SET_REGULAR_ROUTE, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
				successCallBack.onSuccess(result);
			}
		}, new NetConnection.FailCallback() {
			
			@Override
			public void onFail() {
				failCallBack.onFail();
			}
		}, Configs.Uid,uid,
				Configs.START_X,start_x,
				Configs.START_Y,start_y,
				Configs.END_X,end_x,
				Configs.END_Y,end_y,
				Configs.START_NAME,start_name,
				Configs.END_NAME,end_name);
	}

public static interface SuccessCallBack{
	void onSuccess(String result);
}
public static interface FailCallBack{
	void onFail();
}
}
