package com.example.lottery;

import com.example.ontheway.R;

import android.app.Activity;
import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.ImageView;

public class lottery extends Activity implements SensorEventListener{
	private SensorManager mSensorManager;
    private Vibrator vibrator;
    ImageView ivlottery;
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery);
        ivlottery=(ImageView)findViewById(R.id.ivlottery);
        //获取传感器管理服务
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //震动
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);

}
	@Override
	public void onResume(){
		  super.onResume();
		  //加速度传感器
		  mSensorManager.registerListener(this, 
		  mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
		  SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	protected void onStop(){
	  mSensorManager.unregisterListener(this);
	  super.onStop();
	}

	@Override
	protected void onPause(){
	  mSensorManager.unregisterListener(this);
	  super.onPause();
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		int sensorType = event.sensor.getType();
		  float[] values = event.values;  
		  if(sensorType == Sensor.TYPE_ACCELEROMETER){
		   if((Math.abs(values[0])>14||Math.abs(values[1])>14||Math.abs(values[2])>14)){		   
		    ivlottery.setImageResource(R.drawable.lotteryresult);
		    vibrator.vibrate(500);
		
	}
		  }}}
