package com.example.BDMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.ontheway.R;

public class popupwindow_mins extends PopupWindow{
	
	private View conentView ;
	private Activity activity;

	 public popupwindow_mins(final Activity context) {
		 activity = context;
         LayoutInflater inflater = (LayoutInflater) context
                             .getSystemService(Context. LAYOUT_INFLATER_SERVICE);
         //自定义布局
          conentView = inflater.inflate(R.layout.popupwindow_mins, null);
          int h = context.getWindowManager().getDefaultDisplay().getHeight();
          int w = context.getWindowManager().getDefaultDisplay().getWidth();
          // 设置SelectPicPopupWindow的View
          this.setContentView(conentView );
          // 设置SelectPicPopupWindow弹出窗体的宽
          this.setWidth(w/7);
          // 设置SelectPicPopupWindow弹出窗体的高
          this.setHeight(LayoutParams.WRAP_CONTENT);
          // 设置SelectPicPopupWindow弹出窗体可点击
          this.setFocusable(true);
          this.setOutsideTouchable(true);
          // 刷新状态，否则无效
          this.update();
          // 实例化一个ColorDrawable颜色为半透明
         ColorDrawable dw = new ColorDrawable(0000000000);
          // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
          this.setBackgroundDrawable(dw);
          // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
          // 设置SelectPicPopupWindow弹出窗体动画效果
          this.setAnimationStyle(R.style.AnimationPreview1);

}

public void showPopupWindow(View parent) {
          if (!this .isShowing()) {
                    this.showAsDropDown(parent, 0, 0);
         } else {
                    this.dismiss();
         }
}

public ListView getListView(){
	
	String mins [] = new String[60];
	int j=0;
	for (int i = 0; i < 60; i++) {
		j+=5;
		mins[i] = String.valueOf(j);
	}
	
	ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,R.layout.item,mins);
	ListView list =(ListView) conentView.findViewById(R.id.list);
	list.setAdapter(adapter);
	
	return list;
}
}
