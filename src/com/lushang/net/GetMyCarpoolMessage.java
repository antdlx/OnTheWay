package com.lushang.net;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.infos.Configs;
import com.example.infos.MyCarpoolMessageInfo;

public class GetMyCarpoolMessage {
	public List<MyCarpoolMessageInfo> list = new ArrayList<MyCarpoolMessageInfo>();
	
	public GetMyCarpoolMessage(String uid,final SuccessCallBack successCallBack,final FailCallBack failCallBack){
		new NetConnection(Configs.URL_GET_MYCARPOOL_MEASSAGE, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
				sdf = new SimpleDateFormat("yyyy-MM-dd  hh:mm");
				try {
					JSONArray array = new JSONArray(result);
					JSONObject jo;
					MyCarpoolMessageInfo info;
					for (int i = 0; i < array.length(); i++) {
						jo = array.getJSONObject(i);
						info = new MyCarpoolMessageInfo();
						d = new Date(Long.parseLong(jo.getString(Configs.LEAVE_TIME)));
						info.setStrConditionTime(sdf.format(d).toString());
						info.setStrStartPositon(jo.getString(Configs.START_NAME));
						info.setStrEndPosition(jo.getString(Configs.END_NAME));
						info.setStrPeopleCount(jo.getString(Configs.PNUM));
						info.setStrPhone(jo.getString(Configs.TEL));
						list.add(info);
					}
					successCallBack.onSuccess(list);
				
				} catch (JSONException e) {
					failCallBack.onFail();
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
	
private SimpleDateFormat sdf;
private Date d;

public static interface SuccessCallBack{
	void onSuccess(List<MyCarpoolMessageInfo>list);
}
public static interface FailCallBack{
	void onFail();
}
}
