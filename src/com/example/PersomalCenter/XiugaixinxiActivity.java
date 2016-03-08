package com.example.PersomalCenter;

import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;

import com.bmob.im.demo.bean.User;
import com.example.PersomalCenter.NetXiugaixinxi.SuccessCallback;
import com.example.Yidongbaoxiu.YDbaoxiuActivity;
import com.example.infos.Configs;
import com.example.infos.MyBaseInformation;
import com.example.ontheway.*;
import com.example.ontheway.R.id;




public class XiugaixinxiActivity extends Activity implements OnClickListener{
	private int columnIndex;
	private String videoPath;
	private String usage[] = new String[] { "拍照", "从相片中选择" };
	private Cursor cursor,cursor1;
	private int CAMERA = 0;
	private final String IMAGE_TYPE = "image/*";
	private int IMAGE_CODE = 1;
	private String[]sex = {"男","女"};
	private ImageButton ib_back;
	private ImageView iv_touxiang;
	private EditText et_nicheng;
	private EditText et_nianling;
	private Spinner spin_xingbie;
	private EditText et_city;
	private Bitmap camera_bitmap,album_bitmap;
	private Button btn_queding;
	@Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiugaixinxi);
        iv_touxiang = (ImageView) findViewById(R.id.iv_touxiang2);
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        et_nicheng = (EditText) findViewById(R.id.et_nicheng);
        et_nianling = (EditText) findViewById(R.id.et_nianling);
        et_city = (EditText) findViewById(R.id.et_city);
        btn_queding=(Button) findViewById(R.id.btn_queding);
        btn_queding.setOnClickListener(this);
        ib_back.setOnClickListener(this);
        iv_touxiang.setOnClickListener(this);
        initSpinner();
        new NetGetXiugaixinxi(Configs.T_UID, new NetGetXiugaixinxi.SuccessCallBack() {
			
			private List<MyBaseInformation> list;

			@Override
			public void onSuccess(List<MyBaseInformation> list) {
				// TODO Auto-generated method stub
				MyBaseInformation data=(MyBaseInformation) list;
				et_nicheng.setText(data.getnickname());
				et_city.setText(data.getcity());
				et_nianling.setText(data.getage());
				spin_xingbie.setSelection(data.getSex(), true);
				Bitmap bitmap2=convertToBitmap(data.getimg_url(), 150, 150);
				iv_touxiang.setImageBitmap(bitmap2);
				
				
			}
		}, new NetGetXiugaixinxi.FailCallBack() {
			
			@Override
			public void onFail() {
				// TODO Auto-generated method stub
				
			}
		});
}
	private void initSpinner(){
		spin_xingbie=(Spinner) findViewById(R.id.spin_xingbie);
		    	ArrayList<String> sex2 = new ArrayList<String>();
		    	for(int i=0;i<sex.length;i++){
		    		sex2.add(sex[i]);
		    	}
		    	ArrayAdapter<String> adapter_sex = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,sex2);
		    	adapter_sex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    	spin_xingbie.setAdapter(adapter_sex);
		    }
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			Intent intent =new Intent();
			intent.setClass(this, PersonalCenterActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.iv_touxiang2:
			init();
			break;
		case R.id.btn_queding:
			User user = BmobUser.getCurrentUser(XiugaixinxiActivity.this,User.class);
			String path;
			if(cursor.getString(columnIndex)==null){
				path=videoPath;
			}else{
				path=cursor.getString(columnIndex);
			}
			new NetXiugaixinxi(user.getUsername(), et_nicheng.getText().toString(), path, spin_xingbie.getSelectedItem().toString(), et_nianling.getText().toString(), et_city.getText().toString(),
					new NetXiugaixinxi.SuccessCallback() {
						
						public void onSuccess(String result) {
							// TODO Auto-generated method stub
							
							if (result.equals("1")){
								Toast.makeText(XiugaixinxiActivity.this, "修改成功", Toast.LENGTH_LONG).show();
							}else{
								Toast.makeText(XiugaixinxiActivity.this, "修改未成功", Toast.LENGTH_LONG).show();
							}
							
						}
					}, new NetXiugaixinxi.FailCallback() {
						
						@Override
						public void onFail() {
							// TODO Auto-generated method stub
							
						}
					});
			break;

		default:
			break;
		}// TODO Auto-generated method stub
		
	}
	private void init() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				XiugaixinxiActivity.this).setTitle("选择").setItems(usage,
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
			iv_touxiang.setImageBitmap(camera_bitmap);
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

				iv_touxiang.setImageBitmap(album_bitmap);

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
