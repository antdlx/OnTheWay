package com.lushang.net;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.Toast;

import com.example.infos.AroundInfo;
import com.example.infos.Configs;
import com.example.infos.MyCarpoolMessageInfo;

public class GetAround {
	
	public List<AroundInfo> list = new ArrayList<AroundInfo>();
	public AroundInfo info;
	public SimpleDateFormat sdf;
	public Date d;
	
	public GetAround(String coo_x,String coo_y,String radius,final SuccessCallBack successCallBack,final FailCallBack failCallBack ){
		new NetConnection(Configs.URL_GET_AROUND, new NetConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				if (result.equals("0")) {
					System.out.println("失败了。。。");
				}else{
				try {
					JSONArray array = new JSONArray(result);
					JSONObject jo;
					sdf = new SimpleDateFormat("yyyy-MM-dd  hh:mm");
					for (int i = 0; i < array.length(); i++) {
						jo = array.getJSONObject(i);
						info = new AroundInfo();
						d = new Date(Long.parseLong(jo.getString(Configs.LEAVE_TIME)));
						info.setStGoTime(sdf.format(d).toString());
						info.setStrStartPositon(jo.getString(Configs.START_NAME));
						info.setStrEndPosition(jo.getString(Configs.END_NAME));
						info.setStrPeopleCount(jo.getString(Configs.PNUM));
						info.setStrPhone(jo.getString(Configs.TEL));
						list.add(info);
					}
					successCallBack.onSuccess(list);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				}
			}
		}, new NetConnection.FailCallback() {

			@Override
			public void onFail() {
				failCallBack.onFail();
			}
		}, Configs.COO_X,coo_x,
				Configs.COO_Y,coo_y,
				Configs.RADIUS,radius);
	}

	public static interface SuccessCallBack{
		void onSuccess(List<AroundInfo> lists);
	}
	public static interface FailCallBack{
		void onFail();
	}
}