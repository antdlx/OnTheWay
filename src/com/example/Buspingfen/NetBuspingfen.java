package com.example.Buspingfen;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

import com.example.infos.Configs;
import com.lushang.net.NetConnection;

public class NetBuspingfen {
		public NetBuspingfen(String Uid,String Bnamme,String Date,String Hygiene,String Crowd,
				String Attitude,String Minute,String Suggestion,
				final SuccessCallback successCallback,final FailCallback failCallback ){
			new NetConnection(Configs.URL_test_grade_bus,new NetConnection.SuccessCallback() {

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
				},Configs.Uid,Uid,
						Configs.BNAMME,Bnamme,
						Configs.DATE,Date,
						Configs.HYGIENE,Hygiene,
						Configs.CROWD,Crowd,
						Configs.ATTITUDE,Attitude,
						Configs.MINUTE,Minute,
						Configs.SUGGESTION,Suggestion
	);
		

			}
		public static interface SuccessCallback{
			void onSuccess(String result);

		}
		public static interface FailCallback{
			void onFail();
		}
		}