package com.example.Yidongbaoxiu;

import java.io.FileNotFoundException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;

import com.bmob.im.demo.bean.User;
import com.example.BDMap.AddCarpoolActivity;
import com.example.PersomalCenter.PersonalCenterActivity;
import com.example.PersomalCenter.XiugaixinxiActivity;
import com.example.ontheway.MainActivity;
import com.example.ontheway.R;
import com.example.ontheway.R.id;
import com.example.ontheway.R.layout;




public class YDbaoxiuActivity extends Activity implements OnClickListener,OnTouchListener{
	private String videoPath;
	private int columnIndex;
	private Cursor cursor,cursor1;
	private String usage[] = new String[] { "拍照", "从相片中选择" };
	private int CAMERA = 0;
	private final String IMAGE_TYPE = "image/*";
	private int IMAGE_CODE = 1;
	private Bitmap camera_bitmap,album_bitmap;
	private Button btn_return;
	private Button btn_fabubaoxiu;
	private Button btn_myfriends;
	private Button btn_personalcenter;
	private ImageView iv_guzhangpicture;
	private TextView tv_dianji;
	private TextView tv_seeall;
	private EditText et_phone;
	private EditText et_xianlu;
	private EditText et_detales;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ydtvbaoxiu);
        btn_return=(Button) findViewById(R.id.btn_return);
        btn_fabubaoxiu=(Button) findViewById(R.id.btn_fabubaoxiu);
        btn_myfriends=(Button) findViewById(R.id.btn_myfriends);
        btn_personalcenter=(Button) findViewById(R.id.btn_personalcenter);
        iv_guzhangpicture=(ImageView) findViewById(R.id.iv_guzhangpicture);
        tv_dianji=(TextView) findViewById(R.id.tv_dianji);
        tv_seeall=(TextView) findViewById(R.id.tv_seeall);
        et_detales=(EditText) findViewById(R.id.et_detales);
        et_phone=(EditText) findViewById(R.id.et_phone);
        et_xianlu=(EditText) findViewById(R.id.et_xianlu);
        btn_return.setOnTouchListener(this);
        tv_dianji.setOnTouchListener(this);
        btn_fabubaoxiu.setOnTouchListener(this);
        tv_dianji.setOnClickListener(this);
        btn_return.setOnClickListener(this);
        btn_personalcenter.setOnClickListener(this);
        btn_myfriends.setOnClickListener(this);
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
		
		case R.id.btn_fabubaoxiu: 
			User user = BmobUser.getCurrentUser(YDbaoxiuActivity.this,User.class);
			String path;
			if(cursor.getString(columnIndex)==null){
				path=videoPath;
			}else{
				path=cursor.getString(columnIndex);
			}
			if(path==null){
				Toast.makeText(YDbaoxiuActivity.this, "请上传故障图片", Toast.LENGTH_LONG).show();
			}else if(et_xianlu.getText().toString().equals("")){
				Toast.makeText(YDbaoxiuActivity.this, "请填写故障线路", Toast.LENGTH_LONG).show();
			}else if(et_phone.getText().toString().equals("")){
				Toast.makeText(YDbaoxiuActivity.this, "请填写联系电话", Toast.LENGTH_LONG).show();
			}else{
			 new NetYDBaoxiu(user.getUsername(), et_xianlu.getText().toString(), 
					 et_phone.getText().toString(), path, 
					 et_detales.getText().toString(), 
					 new NetYDBaoxiu.SuccessCallback() {
						
						public void onSuccess(String result) {
							if (result.equals("1")){
								Toast.makeText(YDbaoxiuActivity.this, "上传成功", Toast.LENGTH_LONG).show();
							}else{
								Toast.makeText(YDbaoxiuActivity.this, "上传未成功", Toast.LENGTH_LONG).show();
							}
							
						}
					},new NetYDBaoxiu.FailCallback() {
						
						@Override
						public void onFail() {
							// TODO Auto-generated method stub
							
						}
					});}
			break;
		case R.id.btn_myfriends:
			
			break;
		case R.id.btn_personalcenter:
			Intent intent2=new Intent();
			intent2.setClass(this, PersonalCenterActivity.class);
			startActivity(intent2);
			break;
		case R.id.tv_dianji:
			init();
			break;
		case R.id.tv_seeall:

		default:
			break;
		}
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.btn_return:
			int action=event.getAction();
			if(action==MotionEvent.ACTION_DOWN){
				btn_return.setBackgroundResource(R.drawable.backyes);
			}else if(action==MotionEvent.ACTION_UP){
				btn_return.setBackgroundResource(R.drawable.backno);
			}
			break;
		
		case R.id.tv_dianji:
			int action3=event.getAction();
			if(action3==MotionEvent.ACTION_DOWN){
				tv_dianji.setBackgroundColor(Color.BLUE);;
			}else if(action3==MotionEvent.ACTION_UP){
				tv_dianji.setBackgroundResource(R.drawable.biankuang4);
			}
			break;
		case R.id.btn_fabubaoxiu:
			int action4=event.getAction();
			if(action4==MotionEvent.ACTION_DOWN){
				btn_fabubaoxiu.setBackgroundResource(R.drawable.fabuyes);;
			}else if(action4==MotionEvent.ACTION_UP){
				btn_fabubaoxiu.setBackgroundResource(R.drawable.fabu);
			}
			break;

		default:
			break;
		}
		
		return false;
	}

	private void init() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				YDbaoxiuActivity.this).setTitle("选择").setItems(usage,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog,
							int which) {
						if (which == 0) {
							// 拍照
							Intent takePhoto = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							startActivityForResult(takePhoto, CAMERA);
						} else if (which == 1) {
							// 选择一张照片

							Intent getAlbum = new Intent(
									Intent.ACTION_GET_CONTENT);
							getAlbum.setType(IMAGE_TYPE);
							startActivityForResult(getAlbum, IMAGE_CODE);

						}
						dialog.dismiss();
					}
				});
		builder.show();
	};

		

protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);

	if (requestCode == CAMERA) {
		switch (resultCode) {
		case RESULT_OK: // 用户照相

			Bundle bundle = data.getExtras();
			// 获取相机返回的数据，并转换为图片格式
			camera_bitmap = (Bitmap) bundle.get("data");
			camera_bitmap = ThumbnailUtils.extractThumbnail(camera_bitmap,
					150, 150);
			iv_guzhangpicture.setImageBitmap(camera_bitmap);
	//		Uri uri = data.getData();
	//		cursor1 = this.getContentResolver().query(uri, null,
	//		null, null, null);
	//		if (cursor1.moveToFirst()) {
	//		videoPath = cursor1.getString(cursor1
	//		.getColumnIndex("_data"));
	//		}
			break;
		case RESULT_CANCELED: // 用户取消照相
			camera_bitmap=null;
			return;
		}

		// 显示图片
	}
	if (requestCode == IMAGE_CODE) {
		switch (resultCode) {
		case RESULT_OK:
			try {
				Uri uri = data.getData();
				if (uri == null) {
					return;
				}
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				cursor = getContentResolver().query(uri,
						filePathColumn, null, null, null);
				cursor.moveToFirst();
				columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				ContentResolver cr = this.getContentResolver();

				album_bitmap = BitmapFactory.decodeStream(cr
						.openInputStream(uri));
				if (album_bitmap == null) {
					
				}
				album_bitmap = ThumbnailUtils.extractThumbnail(
						album_bitmap, 150, 150);

				iv_guzhangpicture.setImageBitmap(album_bitmap);

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OutOfMemoryError e) {
			}
			break;
		case RESULT_CANCELED: // 用户取消选择gallery
			return;
		}
	}
}

	
	
	}
