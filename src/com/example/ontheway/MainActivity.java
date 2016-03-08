package com.example.ontheway;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import cn.bmob.im.BmobChat;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.bmob.im.demo.CustomApplcation;
import com.bmob.im.demo.bean.User;
import com.bmob.im.demo.ui.ActivityBase;
import com.bmob.im.demo.ui.ChatmainActivity;
import com.bmob.im.demo.ui.LoginActivity;
import com.example.BDMap.BDMapActivity;
import com.example.BDMap.RouteActivity;
import com.example.Buspingfen.Buspingfen;
import com.example.PersomalCenter.PersonalCenterActivity;
import com.example.Yidongbaoxiu.YDbaoxiuActivity;
import com.example.infos.Configs;
import com.example.lottery.lottery;
import com.example.suggestion.suggestion;

public class MainActivity extends ActivityBase implements OnClickListener{
	//消息小红点提示
	ImageView msg_tips;
	// 侧滑菜单按钮
	private Button left_nopic, left_night, left_share, left_advice,
			left_update, left_clear, left_quit;
	private ImageView iv_yuechepinche;
	private ImageView iv_luckychoujiang;
	private ImageView iv_tvbaoxiu;
	private ImageView iv_buspinfen;
	private ImageView iv_searchluxian;
	private Button btn_myfriends;
	private Button btn_personalcenter;
	private Button btn_share;
	private Button btn_suggestion;
	//公告栏
	private ImageView[] imageviews=null;
	private ImageView imageview=null;
	private ViewPager advpager;
	private AtomicInteger what = new AtomicInteger(0);
	private boolean isContinue = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initNewMessageBroadCast();
		//消息小红点提示
		msg_tips = (ImageView) findViewById(R.id.msg_tips);
		// 主页面按钮
		iv_yuechepinche = (ImageView) findViewById(R.id.iv_yuechepinche);
		iv_luckychoujiang = (ImageView) findViewById(R.id.iv_luckychoujiang);
		iv_tvbaoxiu = (ImageView) findViewById(R.id.iv_tvbaoxiu);
		iv_buspinfen = (ImageView) findViewById(R.id.iv_buspinfen);
		iv_searchluxian = (ImageView) findViewById(R.id.iv_searchluxian);
		btn_myfriends = (Button) findViewById(R.id.btn_myfriends);
		btn_personalcenter = (Button) findViewById(R.id.btn_personalcenter);
		btn_share=(Button)findViewById(R.id.left_share);
		iv_yuechepinche.setOnClickListener(this);
		iv_luckychoujiang.setOnClickListener(this);
		iv_tvbaoxiu.setOnClickListener(this);
		iv_buspinfen.setOnClickListener(this);
		iv_searchluxian.setOnClickListener(this);
		btn_myfriends.setOnClickListener(this);
		btn_personalcenter.setOnClickListener(this);
		// 左侧菜单按钮
		left_nopic = (Button) findViewById(R.id.left_nopic);
		left_nopic.setOnClickListener(this);
		left_night = (Button) findViewById(R.id.left_night);
		left_night.setOnClickListener(this);
		left_share = (Button) findViewById(R.id.left_share);
		left_share.setOnClickListener(this);
		left_advice = (Button) findViewById(R.id.left_advice);
		left_advice.setOnClickListener(this);
		left_update = (Button) findViewById(R.id.left_update);
		left_update.setOnClickListener(this);
		left_clear = (Button) findViewById(R.id.left_clear);
		left_clear.setOnClickListener(this);
		left_quit = (Button) findViewById(R.id.left_quit);
		left_quit.setOnClickListener(this);
		btn_suggestion=(Button)findViewById(R.id.left_advice);
		btn_suggestion.setOnClickListener(this);
		//分享功能
		btn_share.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent=new Intent(Intent.ACTION_SEND);
				intent.setType("text/*");
				intent.putExtra(Intent.EXTRA_SUBJECT, "路上分享");
				intent.putExtra(Intent.EXTRA_TEXT, "www.baidu.com");
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Intent.createChooser(intent, getTitle()));
			}
			});
		initViewPager();
		initConfigs();
	}
	/**
	 * 初始化Configs中的一些变量
	 */
	private void initConfigs() {
		//初始化当前用户是否绑定手机号
		User user = BmobUser.getCurrentUser(MainActivity.this,User.class);
		BmobQuery<User> query = new BmobQuery<User>();
		query.addWhereEqualTo("username", user.getUsername());
		query.findObjects(MainActivity.this,new FindListener<User>() {
			
			@Override
			public void onSuccess(List<User> arg0) {
				if (arg0.get(0).getPhoneNum()==null) {
					Configs.weatherBinded=false;
					Log.d("初始化weatherBinded", "NULL");
				}else {
					Log.d("初始化weatherBinded", arg0.get(0).getPhoneNum());
					Configs.weatherBinded=true;
				}
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				
			}
		});
	}
	private void initViewPager(){
		advpager = (ViewPager) findViewById(R.id.vp_gonggaolan);
		ViewGroup group = (ViewGroup) findViewById(R.id.viewgroup);
		//存放广告
		List<View>advPics = new ArrayList<View>();
		ImageView img1 = new ImageView(this);
		img1.setBackgroundResource(R.drawable.yindaoye1);
		advPics.add(img1);
		ImageView img2 = new ImageView(this);
		img2.setBackgroundResource(R.drawable.yindaoye2);
		advPics.add(img2);
		ImageView img3 = new ImageView(this);
		img3.setBackgroundResource(R.drawable.neikuang);
		advPics.add(img3);
		ImageView img4 = new ImageView(this);
		img4.setBackgroundResource(R.drawable.yindaoye4);
		advPics.add(img4);
		//对imageviews进行填充
		imageviews = new ImageView[advPics.size()];
		//小圆点
		for(int i = 0 ; i < advPics.size() ; i++){
			imageview = new ImageView(this);
			imageview.setLayoutParams(new LayoutParams(15, 15));
			imageview.setPadding(0, 0, 15, 0);
			imageviews[i]=imageview;
			if(i==0){
				imageviews[i].setBackgroundResource(R.drawable.yuandianyes);
			}else{
				imageviews[i].setBackgroundResource(R.drawable.yuandianno);
			}
			group.addView(imageviews[i]);
		}
		advpager.setAdapter(new AdvAdapter(advPics));
		advpager.setOnPageChangeListener(new GuidePageChangeListener());
		advpager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					isContinue=false;
					break;
				case MotionEvent.ACTION_UP:
					isContinue=true;
					break;
				default:
					isContinue=true;
					break;
				}
				return false;
			}
		});
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					if(isContinue){
						viewHandle.sendEmptyMessage(what.get());
						whatOption();
					}
				}
			}
		}).start();
	}
	
	private void whatOption(){
		what.incrementAndGet();
		if(what.get()>imageviews.length-1){
			what.getAndAdd(-4);
		}
		try{
			Thread.sleep(2000);
		}catch(InterruptedException e){
			
		}
	}
	private final Handler viewHandle = new Handler(){
		public void handleMessage(Message msg){
			advpager.setCurrentItem(msg.what);
			super.handleMessage(msg);
		}
	};
	final class GuidePageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			what.getAndSet(arg0);
			for(int i =0 ; i < imageviews.length ; i++){
				imageviews[arg0].setBackgroundResource(R.drawable.yuandianyes);
				if(arg0 != i){
					imageviews[i].setBackgroundResource(R.drawable.yuandianno);
				}
			}
		}
		
	}
	class AdvAdapter extends PagerAdapter{
		private List<View>views;
		public AdvAdapter(List<View>views){
			this.views=views;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return this.views.size();
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager)container).addView(views.get(position),0);
			return views.get(position);
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==arg1;
		}
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager)container).removeView(views.get(position));
		}

	}

	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_yuechepinche:
			Intent intent_BDMapActivity = new Intent();
			intent_BDMapActivity.setClass(this, BDMapActivity.class);
			startActivity(intent_BDMapActivity);
			break;
		case R.id.iv_luckychoujiang:
			Intent intent2 = new Intent();
			intent2.setClass(this, lottery.class);
			startActivity(intent2);
			break;
		case R.id.iv_tvbaoxiu:
			Intent intent3 = new Intent();
			intent3.setClass(this, YDbaoxiuActivity.class);
			startActivity(intent3);
			break;
		case R.id.iv_buspinfen:
			Intent intent4 = new Intent();
			intent4.setClass(this, Buspingfen.class);
			startActivity(intent4);
			break;
		case R.id.iv_searchluxian:
			Intent intent_RouteActivity = new Intent();
			intent_RouteActivity.setClass(this, RouteActivity.class);
			startActivity(intent_RouteActivity);
			break;
		case R.id.btn_myfriends:
			Intent intent5 = new Intent();
			intent5.setClass(this, ChatmainActivity.class);
			startActivity(intent5);
			break;
		case R.id.btn_personalcenter:
			Intent intent7 = new Intent();
			intent7.setClass(this, PersonalCenterActivity.class);
			startActivity(intent7);
			break;
		
		case R.id.left_nopic:
            Toast.makeText(this, "敬请期待！", Toast.LENGTH_SHORT).show();
			break;
		case R.id.left_night:
            Toast.makeText(this, "敬请期待！", Toast.LENGTH_SHORT).show();
			break;
		case R.id.left_share:
            Toast.makeText(this, "敬请期待！", Toast.LENGTH_SHORT).show();
			break;
		case R.id.left_advice:
			Intent intent8 = new Intent();
			intent8.setClass(this, suggestion.class);
			startActivity(intent8);
			break;
		case R.id.left_update:
            Toast.makeText(this, "敬请期待！", Toast.LENGTH_SHORT).show();
			break;
		case R.id.left_clear:
            Toast.makeText(this, "敬请期待！", Toast.LENGTH_SHORT).show();
			break;
		case R.id.left_quit:
			CustomApplcation.getInstance().logout();
			finish();
			startActivity(new Intent(MainActivity.this, LoginActivity.class));
			break;

		default:
			break;
		}// TODO Auto-generated method stub

	}
	
NewBroadcastReceiver  newReceiver;
	
	private void initNewMessageBroadCast(){
		BmobChat.getInstance(this).startPollService(30);
		newReceiver = new NewBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(BmobConfig.BROADCAST_NEW_MESSAGE);
		intentFilter.setPriority(3);
		registerReceiver(newReceiver, intentFilter);
	}
	
	private class NewBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			
			String from = intent.getStringExtra("fromId");
			String msgId = intent.getStringExtra("msgId");
			String msgTime = intent.getStringExtra("msgTime");
			Log.v("主类接收到fromId", from);
			Log.v("主类接收到msgId", msgId);
			Log.v("主类接收到msgTime", msgTime); 
			CustomApplcation.getInstance().getMediaPlayer().start();
			msg_tips.setVisibility(View.VISIBLE);
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			unregisterReceiver(newReceiver);
		} catch (Exception e) {
		}
		//取消定时检测服务
		BmobChat.getInstance(this).stopPollService();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//小圆点提示
		if(BmobDB.create(this).hasUnReadMsg()){
			msg_tips.setVisibility(View.VISIBLE);
		}else{
			msg_tips.setVisibility(View.GONE);
		}
	}

}

