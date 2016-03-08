package com.example.BDMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.bmob.im.demo.ui.ChatmainActivity;
import com.example.BDMap.MyOrientationListener.onOrientationListener;
import com.example.PersomalCenter.PersonalCenterActivity;
import com.example.infos.AroundInfo;
import com.example.infos.Configs;
import com.example.ontheway.R;



/**
 * 
 * @author DLX
 *	拼车约车activity
 *
 */
public class BDMapActivity extends Activity  {

	private Context context;
	
	private MapView mapView = null;//整个View控件，而baidumap是一个对象图层而已
	private BaiduMap baiduMap = null;   
	
	//定位相关
	private LocationClient locationClient;
	private MyLocationListener locationListener;
	private boolean isFirstIn = true;//是否是第一次载入
	private double clatitude=0;
	private double clongitude=0;
	private LatLng currentLatLng;
	//自定义定位图标
	private BitmapDescriptor IconLocationArrow;  //bitmap 二进制位图
	//调用传感器
	private MyOrientationListener mOrientationListener;
	private float CX;
	
	//Marker相关
	private BitmapDescriptor IconCarpoolMarker;
	private View view;
	private LatLng l1;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在setContentView之前导入百度地图sdk
        SDKInitializer.initialize(getApplicationContext());  
        setContentView(R.layout.activity_bdmap);
        
        this.context = this;
        
       initView();
        
       initLocation();
       
       initMarkers();
       

    }

	/**
	 * 初始化marker
	 */
    private void initMarkers() {
    	//测试用marker坐标
		l1 = new LatLng(36.676,117.1442);
		//测试用marker icon
		IconCarpoolMarker = new BitmapDescriptorFactory().fromResource(R.drawable.carpool);
		//定义markerѡ覆盖选项zindex优先级为1~10的图层
		OverlayOptions op = new MarkerOptions().position(l1).icon(IconCarpoolMarker).zIndex(5);
		//显示marker
		baiduMap.addOverlay(op);
		//设置marker的点击监听器
		baiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker arg0) {
				//初始化Infowindow的View
				view =LayoutInflater.from(BDMapActivity.this).inflate(R.layout.infowindow,null);
				//ʵ初始化button并设置监听器
				view.findViewById(R.id.btn_chat).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//测试用toast
						Toast.makeText(BDMapActivity.this, "Click", Toast.LENGTH_LONG).show();
					}
				});
				//初始化infowindow
				InfoWindow infoWindow1 = new InfoWindow(view, l1, -30);
				//显示测试infowindow
				baiduMap.showInfoWindow(infoWindow1);
				return true;
			}
		});
		//设置百度地图的点击事件已达到在适当的时候隐藏infowindow的目的
		baiduMap.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}
			
			@Override
			public void onMapClick(LatLng arg0) {
				//隐藏infowindow
				baiduMap.hideInfoWindow();
			}
		});
	}

    /**
     * 初始化定位
     */
	private void initLocation() {
		locationClient = new LocationClient(this);
		locationListener = new MyLocationListener();
		//注册定位
		locationClient.registerLocationListener(locationListener);
		
		//设置client相关的设置
		LocationClientOption op = new LocationClientOption();
		op.setCoorType("bd09ll");  //坐标类型，百度坐标系
		op.setIsNeedAddress(true); //是否需要在result的回调函数中返回地址
		op.setOpenGps(true);
		op.setScanSpan(1000);  //每1秒刷新一次定位
		locationClient.setLocOption(op);
		
		//自定义定位的icon
		IconLocationArrow = BitmapDescriptorFactory.fromResource(R.drawable.map_arrow);
		mOrientationListener = new MyOrientationListener(context);
		mOrientationListener.setOrientationListener(new onOrientationListener() {
			@Override
			public void onOrientationChanged(float x) {
				CX=x;
			}
		});
	}

	/**
     * 初始化view
     */
    private void initView() {
		mapView = (MapView) findViewById(R.id.mapView);
		baiduMap = mapView.getMap();
		//通过mapStatusUpdate设置地图状态，由Factory生成改变后的状态  zoomto(15.0f)是初始缩放为500M
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
		baiduMap.setMapStatus(msu);
		
		findViewById(R.id.btnMyInformation).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i= new Intent(BDMapActivity.this,MyCarpoolMessage.class);
				startActivity(i);
			}
		});
		findViewById(R.id.btnAround).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i= new Intent(BDMapActivity.this,AroundInformation.class);
				startActivity(i);
			}
		});
		findViewById(R.id.btnAddCarpool).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(BDMapActivity.this,AddCarpoolActivity.class);
				startActivity(i);
			}
		});
		findViewById(R.id.btnMyFriends).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i  = new Intent(BDMapActivity.this,ChatmainActivity.class);
				startActivity(i);
			}
		});
		findViewById(R.id.btnPersonalCenter).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(BDMapActivity.this, PersonalCenterActivity.class);
				startActivity(i);
			}
		});
	}

	private class MyLocationListener implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation location) {
			Configs.CITY = location.getCity();
			MyLocationData data = new MyLocationData.Builder()   //再有较多需要初始化的选项时多用builder
			.direction(CX)							//ͨ当前的方向
			.accuracy(location.getRadius())   //定位精度  getRadius()半径
			.latitude(location.getLatitude())   //
			.longitude(location.getLongitude())   //
			.build();
			baiduMap.setMyLocationData(data);
			
			currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
			
			//设置定位的模式和自定义图标
			MyLocationConfiguration config = new MyLocationConfiguration(LocationMode.NORMAL, true, IconLocationArrow);
		  //  mode - 定位模式
		  //  enableDirection - 是否允许显示方向信息
		  //  customMarker - 自定义的图标
			baiduMap.setMyLocationConfigeration(config);
			
			clatitude = location.getLatitude();
			clongitude = location.getLongitude();
			
			Configs.myLatitude = clatitude;
			Configs.myLongtitude = clongitude;
			
			if (isFirstIn) {
				CenterMyLocation();
				isFirstIn = false;
				
				Toast.makeText(context, "当前位置：纬度"+location.getLatitude()+"经度"+location.getLongitude(), Toast.LENGTH_LONG).show();
			}
		}
    }
	/**
	 *把自己的位置定位到屏幕中央的方法
	 */
	private void CenterMyLocation() {
		LatLng latlng = new LatLng(clatitude, clongitude);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latlng);
		baiduMap.animateMapStatus(msu);
	}
	
	public void BtnMyLocationListener(View v){
		CenterMyLocation();
	}
    
	
	
@Override
protected void onStart() {
	super.onStart();
	//打开定位
	baiduMap.setMyLocationEnabled(true);
	//开启定位相关
	if (!locationClient.isStarted()) {
		locationClient.start();	
		mOrientationListener.start();
	}
}

@Override
protected void onStop() {
	super.onStop();
	//关闭定位
	baiduMap.setMyLocationEnabled(false);
	locationClient.stop();
	mOrientationListener.stop();
}
	
@Override
protected void onDestroy() {
	super.onDestroy();
	mapView.onDestroy();
}

@Override
protected void onResume() {
	super.onResume();
	mapView.onResume();
}

@Override
protected void onPause() {
	super.onPause();
	mapView.onPause();
}


}
