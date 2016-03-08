package com.example.Yidongbaoxiu;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.infos.Configs;
import com.lushang.net.NetConnection;

public class NetYDBaoxiu {
		public NetYDBaoxiu(String uid,String fault_line,String phone,String img_url,String fault_detail,
				final SuccessCallback successCallback,final FailCallback failCallback ){
			new NetConnection(Configs.URL_test_repair_tv, new NetConnection.SuccessCallback() {

				@Override
				public void onSuccess(String result) {
					try {
						JSONObject jb = new JSONObject(result);
						successCallback.onSuccess(result);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
					
				},new NetConnection.FailCallback() {
					
					@Override
					public void onFail() {
						failCallback.onFail();
					}
				},Configs.UID,uid,
						Configs.FAULT_LINE,fault_line,
						Configs.PHONE,phone,
						Configs.IMG_URL,img_url,
						Configs.FAULT_DETAIL,fault_detail
	);
		}
		public static interface SuccessCallback{
			void onSuccess(String result);

		}
		public static interface FailCallback{
			void onFail();
		}
		}


