package com.lushang.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

import com.example.infos.Configs;

/**
 * 
 * @author antdlx
 * @since 2015-4-26
 * 网络连接基类，其他后台接口类需调用此方法
 *
 */

public class NetConnection {
HttpClient httpclient;
	//利用不定长参数
	public NetConnection(final String url,final SuccessCallback successCallback,final FailCallback failCallbcak,final String ...kvs){
		
		new AsyncTask<Void, Integer, String>() {

			//doInBackground是AsyncTask的一个方法，必须实现
			@Override
			protected String doInBackground(Void... arg0) {
				
				httpclient = new DefaultHttpClient();
				
				List<NameValuePair> params = new ArrayList<NameValuePair>();	
				for (int i = 0; i < kvs.length; i+=2) {
					params.add(new BasicNameValuePair(kvs[i],kvs[i+1]));
				}
				HttpPost post = new HttpPost(url);
				try {
					post.setEntity(new UrlEncodedFormEntity(params,Configs.CHARSET));
					HttpResponse response = httpclient.execute(post);
					HttpEntity entity = response.getEntity();
					if (entity!=null) {
						String result = EntityUtils.toString(entity,"utf-8");
						System.out.println("Result:"+result);
						
						return result;
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return null;
			}
		
			
			//onPostExecute接收的是doInBackground的返回值
		@Override
		protected void onPostExecute(String result) {
			if (result!=null) {
				if (successCallback!=null) {
					successCallback.onSuccess(result);
				}else {
					failCallbcak.onFail();
				}
			}
			super.onPostExecute(result);
		}
		}.execute();
	}
	
	
	//两个回调方法
	public static interface SuccessCallback{
		 void onSuccess(String result);
	}
	public static interface FailCallback{
		void onFail();
	}
}