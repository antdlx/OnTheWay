/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2014年 mob.com. All rights reserved.
 */
package com.mob.smssdk;

import static cn.smssdk.framework.utils.R.getBitmapRes;
import static cn.smssdk.framework.utils.R.getIdRes;
import static cn.smssdk.framework.utils.R.getLayoutRes;
import static cn.smssdk.framework.utils.R.getStringRes;
import static cn.smssdk.framework.utils.R.getStyleRes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.UserInterruptException;
import cn.smssdk.framework.FakeActivity;

import com.bmob.im.demo.bean.User;
import com.example.ontheway.R;

/** 短信注册页面*/
public class RegisterPage extends FakeActivity implements OnClickListener,
		TextWatcher {

	// 默认使用中国区号
	private static final String DEFAULT_COUNTRY_ID = "42";

	private EventHandler callback;

	// 手机号码
	private EditText etPhoneNum;
	// 下一步按钮
	private Button btnNext;

	private String currentId;
	private String currentCode;
	private EventHandler handler;
	// 国家号码规则
	private HashMap<String, String> countryRules;
	private Dialog pd;
	private OnSendMessageHandler osmHandler;
	
	private String codex;
	private Context context;
	private EditText et_password,et_confirm_password;
	
	public RegisterPage(Context context){
		this.context = context;
	}

	public void setRegisterCallback(EventHandler callback) {
		this.callback = callback;
	}

	public void setOnSendMessageHandler(OnSendMessageHandler h) {
		osmHandler = h;
	}

	public void show(Context context) {
		super.show(context, null);
	}

	public void onCreate() {
		
		int resId = getLayoutRes(activity, "smssdk_regist_page");
		if (resId > 0) {
			activity.setContentView(R.layout.activity_register);
			currentId = DEFAULT_COUNTRY_ID;
			
			et_password = findViewById(R.id.et_password);
			et_confirm_password = findViewById(R.id.et_email);
			
			btnNext = (Button) activity.findViewById(R.id.btn_getSMS);

			etPhoneNum = (EditText) activity.findViewById(R.id.et_username);
			etPhoneNum.setText("");
			etPhoneNum.addTextChangedListener(this);
			etPhoneNum.requestFocus();
			if (etPhoneNum.getText().length() > 0) {
				btnNext.setEnabled(true);
				resId = getBitmapRes(activity, "smssdk_btn_enable");
				if (resId > 0) {
					btnNext.setBackgroundResource(resId);
				}
			}

			btnNext.setOnClickListener(this);

			handler = new EventHandler() {
				@SuppressWarnings("unchecked")
				public void afterEvent(final int event, final int result,
						final Object data) {
					runOnUIThread(new Runnable() {
						public void run() {
							if (pd != null && pd.isShowing()) {
								pd.dismiss();
							}
							if (result == SMSSDK.RESULT_COMPLETE) {
								if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
									// 请求支持国家列表
									onCountryListGot((ArrayList<HashMap<String, Object>>) data);
								}
								else
									if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
									// 请求验证码后，跳转到验证码填写页面
										Log.d("====","==========");
									afterVerificationCodeRequested();
								}
							} else {
								if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE
										&& data != null
										&& (data instanceof UserInterruptException)) {
									// 由于此处是开发者自己决定要中断发送的，因此什么都不用做
									return;
								}

								// 根据服务器返回的网络错误，给toast提示
								try {
									((Throwable) data).printStackTrace();
									Throwable throwable = (Throwable) data;

									JSONObject object = new JSONObject(
											throwable.getMessage());
									String des = object.optString("detail");
									if (!TextUtils.isEmpty(des)) {
										Toast.makeText(activity, des,
												Toast.LENGTH_SHORT).show();
										return;
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								// 如果木有找到资源，默认提示
								int resId = getStringRes(activity,
										"smssdk_network_error");
								if (resId > 0) {
									Toast.makeText(activity, resId,
											Toast.LENGTH_SHORT).show();
								}
							}
						}
					});
				}
			};
		}
	}

	public void onResume() {
		SMSSDK.registerEventHandler(handler);
	}

	public void onPause() {
		SMSSDK.unregisterEventHandler(handler);
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (s.length() > 0) {
			btnNext.setEnabled(true);
			int resId = getBitmapRes(activity, "smssdk_btn_enable");
			if (resId > 0) {
				btnNext.setBackgroundResource(resId);
			}
		} else {
			btnNext.setEnabled(false);
			int resId = getBitmapRes(activity, "smssdk_btn_disenable");
			if (resId > 0) {
				btnNext.setBackgroundResource(resId);
			}
		}
	}

	public void afterTextChanged(Editable s) {
	}

	public void onClick(View v) {
		if (TextUtils.isEmpty(etPhoneNum.getText().toString())) {
			Toast.makeText(context, R.string.toast_error_username_null, Toast.LENGTH_LONG).show();
			return;
		}

		if (TextUtils.isEmpty(et_password.getText().toString())) {
			Toast.makeText(context, R.string.toast_error_password_null, Toast.LENGTH_LONG).show();
			return;
		}

		if (et_password.getText().toString().equals(et_confirm_password.getText().toString())) {
		int id = v.getId();
			if (v.getId()==R.id.btn_getSMS) {
			// 请求发送短信验证码
			if (countryRules == null || countryRules.size() <= 0) {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				pd = CommonDialog.ProgressDialog(activity);
				if (pd != null) {
					pd.show();
				}
				SMSSDK.getSupportedCountries();
			} else {
				String phone = etPhoneNum.getText().toString().trim()
						.replaceAll("\\s*", "");
				String code = ""+86;
				checkPhoneNum(phone, code);
			}
		} 
	}else {
		Toast.makeText(context, "两次密码输入不一致", Toast.LENGTH_LONG).show();
	}
	}
	@SuppressWarnings("unchecked")
	public void onResult(HashMap<String, Object> data) {
		if (data != null) {
			int page = (Integer) data.get("page");
				if (page == 2) {
				// 验证码校验返回
				Object res = data.get("res");
				HashMap<String, Object> phoneMap = (HashMap<String, Object>) data
						.get("phone");
				if (res != null && phoneMap != null) {
					int resId = getStringRes(activity,
							"smssdk_your_ccount_is_verified");
					if (resId > 0) {
						Toast.makeText(activity, resId, Toast.LENGTH_SHORT)
								.show();
					}
					if (callback != null) {
						callback.afterEvent(
								SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE,
								SMSSDK.RESULT_COMPLETE, phoneMap);
					}
					finish();
				}
			}
		}
	}

	private void onCountryListGot(ArrayList<HashMap<String, Object>> countries) {
		// 解析国家列表
		for (HashMap<String, Object> country : countries) {
			String code = (String) country.get("zone");
			String rule = (String) country.get("rule");
			Log.d("---CODE----", code);
			Log.d("---RULE----", rule);
			if (TextUtils.isEmpty(code) || TextUtils.isEmpty(rule)) {
				continue;
			}

			if (countryRules == null) {
				countryRules = new HashMap<String, String>();
			}
			countryRules.put(code, rule);
		}
		// 检查手机号码
		String phone = etPhoneNum.getText().toString().trim()
				.replaceAll("\\s*", "");
		String code = ""+86;
		checkPhoneNum(phone, code);
	}

	/** 分割电话号码 */
	private String splitPhoneNum(String phone) {
		StringBuilder builder = new StringBuilder(phone);
		builder.reverse();
		for (int i = 4, len = builder.length(); i < len; i += 5) {
			builder.insert(i, ' ');
		}
		builder.reverse();
		return builder.toString();
	}

	/** 检查电话号码 */
	private void checkPhoneNum(final String phone, String code) {
		if (code.startsWith("+")) {
			code = code.substring(1);
		}

		if (TextUtils.isEmpty(phone)) {
			int resId = getStringRes(activity, "smssdk_write_mobile_phone");
			if (resId > 0) {
				Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
			}
			return;
		}

		String rule = countryRules.get(code);
		Pattern p = Pattern.compile(rule);
		Matcher m = p.matcher(phone);
		int resId = 0;
		if (!m.matches()) {
			resId = getStringRes(activity, "smssdk_write_right_mobile_phone");
			if (resId > 0) {
				Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
			}
			return;
		}
		//****************************************这里是按照需求自行修改的sdk**************************************************
		codex = code;
		User bmobUser =BmobUser.getCurrentUser(context,User.class);
		BmobQuery<User> query = new BmobQuery<User>();
		query.addWhereEqualTo("phoneNum", ""+phone);
		query.findObjects(context, new FindListener<User>() {
			
			@Override
			public void onSuccess(List<User> arg0) {
				Log.d("返回的list的大小", arg0.size()+"");
				if (arg0.size()==0) {
					showDialog(phone, codex);
					Toast.makeText(context, "成功与服务器绑定手机号", Toast.LENGTH_LONG).show();
				}else {
					showDialog(phone, codex);
					Toast.makeText(context, "该手机号已被绑定", Toast.LENGTH_LONG).show();
				}
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				Toast.makeText(context, "手机号与服务器绑定失败："+arg1, Toast.LENGTH_LONG).show();
			}
		});
		//******************************************************************************************************
	}

	/** 是否请求发送验证码，对话框 */
	public void showDialog(final String phone, final String code) {
		int resId = getStyleRes(activity, "CommonDialog");
		if (resId > 0) {
			final String phoneNum = "+" + code + " " + splitPhoneNum(phone);
			final Dialog dialog = new Dialog(getContext(), resId);
			resId = getLayoutRes(activity, "smssdk_send_msg_dialog");
			if (resId > 0) {
				dialog.setContentView(resId);
				resId = getIdRes(activity, "tv_phone");
				((TextView) dialog.findViewById(resId)).setText(phoneNum);
				resId = getIdRes(activity, "tv_dialog_hint");
				TextView tv = (TextView) dialog.findViewById(resId);
				resId = getStringRes(activity, "smssdk_make_sure_mobile_detail");
				if (resId > 0) {
					String text = getContext().getString(resId);
					tv.setText(Html.fromHtml(text));
				}
				resId = getIdRes(activity, "btn_dialog_ok");
				if (resId > 0) {
					((Button) dialog.findViewById(resId))
							.setOnClickListener(new OnClickListener() {
								public void onClick(View v) {
									// 跳转到验证码页面
									dialog.dismiss();

									if (pd != null && pd.isShowing()) {
										pd.dismiss();
									}
									pd = CommonDialog.ProgressDialog(activity);
									if (pd != null) {
										pd.show();
									}
									Log.e("verification phone ==>>", phone);
									SMSSDK.getVerificationCode(code,
											phone.trim(), osmHandler);
								}
							});
				}
				resId = getIdRes(activity, "btn_dialog_cancel");
				if (resId > 0) {
					((Button) dialog.findViewById(resId))
							.setOnClickListener(new OnClickListener() {
								public void onClick(View v) {
									dialog.dismiss();
								}
							});
				}
				dialog.setCanceledOnTouchOutside(true);
				dialog.show();
			}
		}
	}

	/** 请求验证码后，跳转到验证码填写页面 */
	private void afterVerificationCodeRequested() {
		String phone = etPhoneNum.getText().toString().trim()
				.replaceAll("\\s*", "");
		String code = ""+86;
		if (code.startsWith("+")) {
			code = code.substring(1);
		}
		Log.d("---CODE---", ""+code);
		Log.d("---PHONE---", ""+phone);
		String formatedPhone = "+" + code + " " + splitPhoneNum(phone);
		Log.d("---FORMAT---", ""+formatedPhone);
		// 验证码页面
		Log.d("---PASSWORD---", et_password.getText().toString());
		
		IdentifyNumPage page = new IdentifyNumPage();
		page.setPhone(phone, code, formatedPhone);
		page.showForResult(activity, null, this);
	}
}
