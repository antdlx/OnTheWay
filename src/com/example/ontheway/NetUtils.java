package com.example.ontheway;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 网络工具类的集合
 * @author Oathkeeper
 *
 */
public class NetUtils {
	
	/**
	 * 检查网络连接
	 * @param context
	 * @return
	 */
	public static boolean checkConnection(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isAvailable();
		}
		return false;
	}

	/**
	 * 检测是否wifi环境
	 * @param mContext
	 * @return
	 */
	public static boolean isWifi(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null && activeNetInfo.getTypeName().equals("WIFI")) {
			return true;
		}
		return false;
	}
	
	

	/**
	 * 从网络上获取bitmap图像
	 * @param url
	 * @return
	 * @throws IOException, 
	 */
	public static  Bitmap getBitmapFromUrl(String url) throws IOException{
		Bitmap bitmap=null;
			URL bitmapUrl;
			bitmapUrl = new URL( url);
			Log.i("tag","bitmapURL"+url);
			HttpURLConnection conn;
			conn = (HttpURLConnection) bitmapUrl.openConnection();
			conn.setConnectTimeout( 5000);//设置链接超时时间
			conn.setReadTimeout(1000); //设置读取超时时间
			//conn.setDoInput(true);  //如果打算进行输入，请设为true
		    conn.connect();
		    InputStream is = conn.getInputStream();  
		    bitmap= BitmapFactory.decodeStream(is); 
		    Log.i("tag", "bitmap"+bitmap);

		return bitmap;
	}
}
