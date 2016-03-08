package com.example.PersomalCenter;

import java.lang.ref.WeakReference;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.bmob.im.demo.bean.User;
import com.example.infos.Configs;
import com.example.infos.MyBaseInformation;
import com.example.ontheway.MainActivity;
import com.example.ontheway.NetUtils;
import com.example.ontheway.R;
import com.mob.smssdk.RegisterPage;




public class PersonalCenterActivity extends Activity implements OnClickListener, Callback{
	private ImageView iv_tupian;//��ͼƬ
	private TextView tv_jibenxinxi;//����Ϣ��
	private TextView tv_jibenxinxitwo;
	private TextView tv_jibenxinxi2;//����Ϣ��
	private Button btn_jilu;//ƴ��Լ����¼��ť
	private Button btn_luxian;//����•�߰�ť
	private Button btn_lipin;//�鿴�ҵ���Ʒ
	private Button btn_xiugai;//����Ϣ�޸İ�ť
	private Button btn_fanhui;//���������水ť
	private Button btn_myfriends;//�ҵĺ��Ѱ�ť
	private Button btn_personalcenter;//�������İ�ť
	private ImageView iv_touxiang;//ͷ��
	private ImageView iv_sexal;//ͷ��
	/**
	 * 绑定手机号
	 */
	private Button btn_bindPhoneNum;
	@Override
	protected  void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personalcenter);
		iv_tupian=(ImageView) findViewById(R.id.iv_tupian);
		tv_jibenxinxi=(TextView) findViewById(R.id.tv_jibenxinxi);
		tv_jibenxinxi2=(TextView) findViewById(R.id.tv_jibenxinxi2);
		tv_jibenxinxitwo=(TextView) findViewById(R.id.tv_jibenxinxitwo);
		btn_jilu=(Button) findViewById(R.id.btn_jilu);
		btn_luxian=(Button) findViewById(R.id.btn_luxian);
		btn_lipin=(Button) findViewById(R.id.btn_lipin);
		btn_xiugai=(Button) findViewById(R.id.btn_xiugai);
		btn_fanhui= (Button) findViewById(R.id.btn_fanhui);
		btn_myfriends=(Button) findViewById(R.id.btn_myfriends);
		btn_personalcenter=(Button) findViewById(R.id.btn_personalcenter);
		iv_touxiang=(ImageView) findViewById(R.id.iv_touxiang);
		iv_sexal=(ImageView) findViewById(R.id.iv_sexal);
		btn_jilu.setOnClickListener(this);
		iv_tupian.setOnClickListener(this);
		btn_luxian.setOnClickListener(this);
		btn_lipin.setOnClickListener(this);
		btn_xiugai.setOnClickListener(this);
		btn_fanhui.setOnClickListener(this);
		btn_myfriends.setOnClickListener(this);
		btn_personalcenter.setOnClickListener(this);
		iv_touxiang.setOnClickListener(this);
		new NetGetXiugaixinxi(Configs.T_UID, new NetGetXiugaixinxi.SuccessCallBack(){
			private List<MyBaseInformation> list;
			@Override
			public void onSuccess(List<MyBaseInformation> list) {
				// TODO Auto-generated method stub
				MyBaseInformation data=(MyBaseInformation) list;
				tv_jibenxinxi.setText(data.getnickname());
				String jibenxinxi2=data.getage()+"岁"+" "+data.getcity();
				tv_jibenxinxi2.setText(jibenxinxi2);
				int sex=data.getSex();
				if(sex==0){
					iv_sexal.setImageResource(R.drawable.sex_nan);
				}else if(sex==1){
					iv_sexal.setImageResource(R.drawable.sex_nv);
				}
				Bitmap bitmap=convertToBitmap(data.getimg_url(), 150, 150);
				iv_touxiang.setImageBitmap(bitmap);
			}
			
		}, new NetGetXiugaixinxi.FailCallBack() {

			@Override
			public void onFail() {
				// TODO Auto-generated method stub
				
			}});
//		btn_bindPhoneNum = (Button) findViewById(R.id.btn_bindPhone);
//		btn_bindPhoneNum.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_jilu:
			Intent intent2=new Intent();
			intent2.setClass(this,YueCheJiLuActivity.class);
			startActivity(intent2);
			break;
		case R.id.btn_luxian:
			Intent intent3=new Intent();
			intent3.setClass(this,RegularRouteActivity.class);
			startActivityForResult(intent3, 3);
			break;
		case R.id.btn_lipin:
			Intent intent6=new Intent();
			intent6.setClass(this,mylipinActivity.class);
			startActivity(intent6);
			break;
		case R.id.btn_xiugai://�޸Ļ���Ϣ
		Intent intent4=new Intent();
		intent4.setClass(this,XiugaixinxiActivity.class);
		startActivity(intent4);
		break;
		case R.id.btn_fanhui://����������
			Intent intent=new Intent();
			intent.setClass(this,MainActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.btn_myfriends://���غ����б�
			if (NetUtils.checkConnection(PersonalCenterActivity.this)) {
				initSDK();
				RegisterPage registerPage = new RegisterPage(PersonalCenterActivity.this);
				registerPage.setRegisterCallback(new EventHandler() {
					public void afterEvent(int event, int result, Object data) {
						// 解析注册结果
						if (result == SMSSDK.RESULT_COMPLETE) {
							Toast.makeText(PersonalCenterActivity.this, "恭喜您，您已成功验证", Toast.LENGTH_LONG).show();
							Log.d("得到的data", data.toString());
							try {
								JSONObject jb =new JSONObject(data.toString());
								Log.d("得到的JSON", jb.getString("phone").toString());
								User cuser = BmobUser.getCurrentUser(PersonalCenterActivity.this,User.class);
								cuser.setPhoneNum(jb.getString("phone").toString());
								cuser.update(PersonalCenterActivity.this, new UpdateListener() {

									@Override
									public void onSuccess() {
										Toast.makeText(PersonalCenterActivity.this, "已将手机号成功绑定",Toast.LENGTH_LONG
												).show();
										Configs.weatherBinded = true;
									}
									@Override
									public void onFailure(int arg0, String arg1) {
										Toast.makeText(PersonalCenterActivity.this, "手机号绑定错误："+arg1,Toast.LENGTH_LONG
												).show();
									}
								});
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				});
				registerPage.show(this);
			}else {
				Toast.makeText(PersonalCenterActivity.this, "请检查网络连接", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.btn_personalcenter://���ظ�������
			Intent intent1=new Intent();
			intent1.setClass(this,PersonalCenterActivity.class);
			startActivity(intent1);
			finish();
			break;
		case R.id.iv_touxiang://�޸�ͷ��
			break;

			//绑定手机号的相关逻辑
//		case R.id.btn_bindPhone:
//			if (NetUtils.checkConnection(PersonalCenterActivity.this)) {
//				initSDK();
//				RegisterPage registerPage = new RegisterPage(PersonalCenterActivity.this);
//				registerPage.setRegisterCallback(new EventHandler() {
//					public void afterEvent(int event, int result, Object data) {
//						// 解析注册结果
//						if (result == SMSSDK.RESULT_COMPLETE) {
//							Toast.makeText(PersonalCenterActivity.this, "恭喜您，您已成功验证", Toast.LENGTH_LONG).show();
//							Log.d("得到的data", data.toString());
//							try {
//								JSONObject jb =new JSONObject(data.toString());
//								Log.d("得到的JSON", jb.getString("phone").toString());
//								User cuser = BmobUser.getCurrentUser(PersonalCenterActivity.this,User.class);
//								cuser.setPhoneNum(jb.getString("phone").toString());
//								cuser.update(PersonalCenterActivity.this, new UpdateListener() {
//
//									@Override
//									public void onSuccess() {
//										Toast.makeText(PersonalCenterActivity.this, "已将手机号成功绑定",Toast.LENGTH_LONG
//												).show();
//										Configs.weatherBinded = true;
//									}
//									@Override
//									public void onFailure(int arg0, String arg1) {
//										Toast.makeText(PersonalCenterActivity.this, "手机号绑定错误："+arg1,Toast.LENGTH_LONG
//												).show();
//									}
//								});
//							} catch (JSONException e) {
//								e.printStackTrace();
//							}
//						}
//					}
//				});
//				registerPage.show(this);
//			}else {
//				Toast.makeText(PersonalCenterActivity.this, "请检查网络连接", Toast.LENGTH_LONG).show();
//			}
//			break;
		default:
			break;
		}

	}

	
	// 填写从短信SDK应用后台注册得到的APPKEY
	private static String APPKEY = "70099f838a10";

	// 填写从短信SDK应用后台注册得到的APPSECRET
	private static String APPSECRET = "73bda53c18dd1c8971ca1a604c051f79";

	private boolean ready;
	private Dialog pd;
	private void initSDK() {
		// 初始化短信SDK
		SMSSDK.initSDK(this, APPKEY, APPSECRET);
		final Handler handler = new Handler(this);
		EventHandler eventHandler = new EventHandler() {
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		};
		// 注册回调监听接口
		SMSSDK.registerEventHandler(eventHandler);
		ready = true;
	}

	public boolean handleMessage(Message msg) {
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}

		int event = msg.arg1;
		int result = msg.arg2;
		Object data = msg.obj;
		if (event == SMSSDK.EVENT_SUBMIT_USER_INFO) {
			// 短信注册成功后，返回MainActivity
			if (result == SMSSDK.RESULT_COMPLETE) {
				Toast.makeText(this, R.string.smssdk_user_info_submited, Toast.LENGTH_SHORT).show();
			} else {
				((Throwable) data).printStackTrace();
			}
		} 
		return false;
	}

	protected void onDestroy() {
		if (ready) {
			// 销毁回调监听接口
			SMSSDK.unregisterAllEventHandler();
		}
		super.onDestroy();
	}
	public Bitmap convertToBitmap(String path, int w, int h) {
	    BitmapFactory.Options opts = new BitmapFactory.Options();
	    // 设置为ture只获取图片大小
	    opts.inJustDecodeBounds = true;
	    opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
	    // 返回为空
	    BitmapFactory.decodeFile(path, opts);
	    int width = opts.outWidth;
	    int height = opts.outHeight;
	    float scaleWidth = 0.f, scaleHeight = 0.f;
	    if (width > w || height > h) {
	        // 缩放
	        scaleWidth = ((float) width) / w;
	        scaleHeight = ((float) height) / h;
	    }
	    opts.inJustDecodeBounds = false;
	    float scale = Math.max(scaleWidth, scaleHeight);
	    opts.inSampleSize = (int)scale;
	    WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
	    return Bitmap.createScaledBitmap(weak.get(), w, h, true);
	}
}
