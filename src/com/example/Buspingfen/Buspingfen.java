package com.example.Buspingfen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;

import com.bmob.im.demo.bean.User;
import com.example.Buspingfen.NetBuspingfen.FailCallback;
import com.example.PersomalCenter.PersonalCenterActivity;
import com.example.Yidongbaoxiu.YDbaoxiuActivity;
import com.example.ontheway.MainActivity;
import com.example.ontheway.R;
import com.example.ontheway.R.id;
import com.example.ontheway.R.layout;




public class Buspingfen extends Activity implements OnClickListener,OnTouchListener{
	private String Date;
	private String sdate;
	private int Month,Day,Year;
	private String[]bus={"119","118","117","116","115"};
	private String[]month={"01","02","03","04","05","06","07","08","09","10","11","12"};
	private String[]day={"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
	private String[]hour={"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};
	private String rbs1,rbs2,rbs3;//三个选择的radiogroup的值
	private Spinner spin_choosebus;
	private Spinner spin_choosemonth;
	private Spinner spin_chooseday;
	private Spinner spin_choosehour;
	private Button btn_return;
	private Button btn_fabu;
	private Button btn_myfriends;
	private Button btn_personalcenter;
	private RadioGroup rg_busenvironment;
	private RadioGroup rg_busyongji;
	private RadioGroup rg_driver;
	private EditText et_jianyi;
	private EditText et_minutes;
	
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buspingfen);
        btn_return=(Button) findViewById(R.id.btn_return);
        btn_fabu=(Button) findViewById(R.id.btn_fabu);
        btn_myfriends=(Button) findViewById(R.id.btn_myfriends);
        btn_personalcenter=(Button) findViewById(R.id.btn_personalcenter);
        rg_busenvironment=(RadioGroup) findViewById(R.id.rg_busenvironment);
        rg_busyongji=(RadioGroup) findViewById(R.id.rg_busyongji);
        rg_driver=(RadioGroup) findViewById(R.id.rg_driver);
        et_minutes=(EditText) findViewById(R.id.et_minutes);
        et_jianyi=(EditText) findViewById(R.id.et_jianyi);
        btn_fabu.setOnClickListener(this);
        btn_myfriends.setOnClickListener(this);
        btn_personalcenter.setOnClickListener(this);
        btn_return.setOnClickListener(this);
        rg_busenvironment.setOnClickListener(this);
        rg_busyongji.setOnClickListener(this);
        rg_driver.setOnClickListener(this);
        rg_busenvironment.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				RadioButton rb1=(RadioButton) findViewById(rg_busenvironment.getCheckedRadioButtonId());
				rbs1=rb1.getText().toString();
				
			}
		});
        rg_busyongji.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        	public void onCheckedChanged(RadioGroup group, int checkedId) {
        		// TODO Auto-generated method stub
        		RadioButton rb2=(RadioButton) findViewById(rg_busyongji.getCheckedRadioButtonId());
        		rbs2=rb2.getText().toString();
        		
        	}
        });
        rg_driver.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        	public void onCheckedChanged(RadioGroup group, int checkedId) {
        		// TODO Auto-generated method stub
        		RadioButton rb3=(RadioButton) findViewById(rg_driver.getCheckedRadioButtonId());
        		rbs3=rb3.getText().toString();
        		
        	}
        });
        et_jianyi.setOnClickListener(this);
        et_minutes.setOnClickListener(this);
        et_jianyi.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int editStart ;
		    private int editEnd ;
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				temp=s;
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				editStart = et_jianyi.getSelectionStart();
		        editEnd = et_jianyi.getSelectionEnd();
		        if (et_jianyi.toString().getBytes().length > 255) {
	                Toast.makeText(Buspingfen.this,
	                        "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT)
	                        .show();
	                s.delete(editStart-1, editEnd);
	                int tempSelection = editStart;
	                et_jianyi.setText(s);
	                et_jianyi.setSelection(tempSelection);
				
			}
			}});
        spin_chooseBus();
        spin_chooseDay();
        spin_chooseHour();
        spin_chooseMonth();
        
	}


    private void spin_chooseBus(){
    	spin_choosebus=(Spinner) findViewById(R.id.spin_choosebus);
    	ArrayList<String> buses = new ArrayList<String>();
    	for(int i=0;i<bus.length;i++){
    		buses.add(bus[i]);
    	}
    	ArrayAdapter<String> adapter_bus = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,buses);
    	adapter_bus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spin_choosebus.setAdapter(adapter_bus);
    }
    private void spin_chooseMonth(){
    	spin_choosemonth=(Spinner) findViewById(R.id.spin_choosemonth);
    	ArrayList<String> monthes = new ArrayList<String>();
    	for(int i=0;i<month.length;i++){
    		monthes.add(month[i]);
    	}
    	ArrayAdapter<String> adapter_month = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,monthes);
    	adapter_month.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spin_choosemonth.setAdapter(adapter_month);
    }
    private void spin_chooseDay(){
    	spin_chooseday=(Spinner) findViewById(R.id.spin_chooseday);
    	ArrayList<String> days = new ArrayList<String>();
    	
    		for(int i=0;i<day.length;i++){
        		days.add(day[i]);}
    	
    	ArrayAdapter<String> adapter_day = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,days);
    	adapter_day.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spin_chooseday.setAdapter(adapter_day);
    }
    private void spin_chooseHour(){
    	spin_choosehour=(Spinner) findViewById(R.id.spin_choosehour);
    	ArrayList<String> hours = new ArrayList<String>();
    	for(int i=0;i<hour.length;i++){
    		hours.add(hour[i]);
    	}
    	ArrayAdapter<String> adapter_hour = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,hours);
    	adapter_hour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spin_choosehour.setAdapter(adapter_hour);
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_return:
			Intent intent=new Intent();
			intent.setClass(this,MainActivity.class);
			startActivity(intent);
			finish();
			break;

		case R.id.btn_fabu:
			User user = BmobUser.getCurrentUser(Buspingfen.this,User.class);
			if(spin_choosemonth.getSelectedItem().toString().equals("")||spin_chooseday.getSelectedItem().toString().equals("")||spin_choosehour.getSelectedItem().toString().equals("")){
				Toast.makeText(Buspingfen.this, "请选择时间", Toast.LENGTH_LONG).show();
			}else{
			dates();
			}
			if(spin_choosebus.getSelectedItem().toString().equals("")){
				Toast.makeText(Buspingfen.this, "请选择线路", Toast.LENGTH_LONG).show();
			}
			if(rbs1.equals("")||rbs2.equals("")||rbs3.equals("")||et_minutes.getText().toString().equals("")){
				Toast.makeText(Buspingfen.this, "请完成您的评分", Toast.LENGTH_LONG).show();
			}
			new NetBuspingfen(user.getUsername(), spin_choosebus.getSelectedItem().toString(), Date, rbs1, rbs2, rbs3, et_minutes.getText().toString(), et_jianyi.getText().toString(),
					new NetBuspingfen.SuccessCallback() {
						
						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub
							if (result.equals("1")){
								Toast.makeText(Buspingfen.this, "上传成功", Toast.LENGTH_LONG).show();
							}else{
								Toast.makeText(Buspingfen.this, "上传未成功", Toast.LENGTH_LONG).show();
							}
							
						
						}
					},new NetBuspingfen.FailCallback() {
						
						@Override
						public void onFail() {
							// TODO Auto-generated method stub
							
						}
					});
			et_minutes.setText("");
			et_jianyi.setText("");
			Toast.makeText(this, "发布成功", Toast.LENGTH_LONG).show();
			break;
		case R.id.btn_myfriends:
			
			break;
		case R.id.btn_personalcenter:
			Intent intent2=new Intent();
			intent2.setClass(this, PersonalCenterActivity.class);
			startActivity(intent2);
			break;
		case R.id.rg_busenvironment:
			
			break;
		case R.id.rg_busyongji:
			
			break;
		case R.id.rg_driver:
			
			break;
		case R.id.et_jianyi:
			CharSequence jianyi=et_jianyi.getText();
			break;
		case R.id.et_minutes:
			CharSequence minutes=et_minutes.getText();
			break;

		default:
			break;
		}
	}


	private void dates() {
		int ho=0;
		Calendar c = Calendar.getInstance();
		Year=c.get(Calendar.YEAR);
		sdate=Year+spin_choosemonth.getSelectedItem().toString()+"-"+spin_chooseday.getSelectedItem().toString();
		sdate+=" 00:00:00";
		String str = spin_choosehour.getSelectedItem().toString();
		if(str != null && str.length() > 0) {
		ho = Integer.parseInt(str);	
	}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d;
		try {
			d = sdf.parse(sdate);
			long l = d.getTime();
			l+=(ho*3600);
			Date = String.valueOf(l);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_return:
			int action=event.getAction();
			if(action==MotionEvent.ACTION_DOWN){
				btn_return.setBackgroundResource(R.drawable.backyes);
			}else if(action==MotionEvent.ACTION_UP){
				btn_return.setBackgroundResource(R.drawable.backno);
			}
			break;

		case R.id.btn_fabu:
			int action3=event.getAction();
			if(action3==MotionEvent.ACTION_DOWN){
				btn_fabu.setBackgroundResource(R.drawable.tijiao2);
			}else if(action3==MotionEvent.ACTION_UP){
				btn_fabu.setBackgroundResource(R.drawable.tijiao);
			}
			break;

		default:
			break;
		}
		return false;
	}
}
