package com.example.BDMap;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MyOrientationListener implements SensorEventListener {

	private SensorManager mSensorManager;
	private Context context;
	private Sensor mSensor;
	
	private float lastX;
	
	public MyOrientationListener(Context context){
		this.context = context;
	}
	
	@SuppressWarnings("deprecation")
	public void start(){
		//初始化传感器管理器
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		//检测手机是否支持传感器
		if (mSensorManager!=null) {
			mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		}
		if (mSensor!=null) {
			//注册传感器
			mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
		}
	}
	
	
	public void stop(){
		mSensorManager.unregisterListener(this);
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			float x = event.values[SensorManager.DATA_X];
			//防止过于频繁的修改
			if (Math.abs(x-lastX)>1.0) {
				if (mOrientationListener!=null) {
					mOrientationListener.onOrientationChanged(x);
				}
			}
			lastX = x;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		//当精度改变的时候，暂时不需要
	}
	
	
	public void setOrientationListener(onOrientationListener mOrientationListener) {
		this.mOrientationListener = mOrientationListener;
	}


	private onOrientationListener mOrientationListener;
	//方向改变的回调接口
	public interface onOrientationListener{
		void onOrientationChanged(float x);
	}

}
