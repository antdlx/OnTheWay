package com.example.ontheway;

import com.nineoldandroids.view.ViewHelper;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class SlidingMenu extends HorizontalScrollView {
	private LinearLayout mWrapper;
	private ViewGroup mMenu;
	private ViewGroup mContent;
	private int mScreenWidth;
	private int mMenuWidth;
	private boolean isOpen;
	// dp
	private int mMenuRightPadding;
	private boolean once = false;
    private VelocityTracker mVelocityTracker;  
    private int mPointerId;  
    private int slidstate = 0;
	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}
	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.SlidingMenu, defStyle, 0);
		int n = ta.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = ta.getIndex(i);
			switch (attr) {
			case R.styleable.SlidingMenu_rightPadding:
				mMenuRightPadding = (int) ta.getDimension(attr,
						(int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 50, context
										.getResources().getDisplayMetrics()));
				break;
			}
		}
		ta.recycle();

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		mScreenWidth = outMetrics.widthPixels;
	}

	public SlidingMenu(Context context) {
		this(context, null);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (!once) {
			mWrapper = (LinearLayout) getChildAt(0);
			mMenu = (ViewGroup) mWrapper.getChildAt(0);
			mContent = (ViewGroup) mWrapper.getChildAt(1);
			mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth
					- mMenuRightPadding;
			mContent.getLayoutParams().width = mScreenWidth;
			once = true;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			this.scrollTo(mMenuWidth, 0);
		}
	}

	public boolean onTouchEvent(MotionEvent ev) {
		if (mVelocityTracker == null) { 
            mVelocityTracker = VelocityTracker.obtain();
        } 
		int action = ev.getAction();
		mVelocityTracker.addMovement(ev);
	    final VelocityTracker verTracker = mVelocityTracker; 
	    
		switch (action) {
		
		case MotionEvent.ACTION_DOWN:
			mPointerId = ev.getPointerId(0);  
			break;
			
		case MotionEvent.ACTION_MOVE:  
            verTracker.computeCurrentVelocity(1,9999999);  
            final float velocityX = verTracker.getXVelocity(mPointerId);  
    		if (velocityX >= 3.0&&slidstate==0) {
    		   slidstate = 2;
			}
    		if (velocityX <= -3.0&&slidstate==0) {
     		   slidstate = 1;
 			}
            break;
		case MotionEvent.ACTION_UP:
			releaseVelocityTracker();
			int scrollX = getScrollX();
			Log.d("slidstate", slidstate+"");
			switch (slidstate) {
			case 0:
				if (scrollX >= mScreenWidth / 2) {
					this.smoothScrollTo(mMenuWidth, 0);
					isOpen = false;
				} else {
					this.smoothScrollTo(0, 0);
					isOpen = true;
				}
				break;
            case 1:
            	this.smoothScrollTo(mMenuWidth, 0);
				isOpen = false;
				break;
            case 2:
            	this.smoothScrollTo(0, 0);
				isOpen = true;
				break;
			}
			slidstate=0;
			return true;
		}
		return super.onTouchEvent(ev);
	}
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		
		float scale = l * 1.0f / mMenuWidth; // 1.0~0
		float rightScale = 0.7f + 0.3f * scale;
		ViewHelper.setPivotX(mContent, 0);
		ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
		ViewHelper.setScaleX(mContent, rightScale);
		ViewHelper.setScaleY(mContent, rightScale);
		float leftScale = 1.0f - 0.3f*scale;
		float leftAlpha = 1.0f - 0.4f*scale;
		ViewHelper.setTranslationX(mMenu, (float)l*0.8f);
		ViewHelper.setScaleX(mMenu, leftScale);
		ViewHelper.setScaleY(mMenu, leftScale);
		ViewHelper.setAlpha(mMenu, leftAlpha);
	}
	public void toggle() {
		if (isOpen) {
			this.smoothScrollTo(mMenuWidth, 0);
			isOpen = false;
		} else {
			this.smoothScrollTo(0, 0);
			isOpen = true;
		}
	}
	private void releaseVelocityTracker() {  
        if(null != mVelocityTracker) {  
            mVelocityTracker.clear();  
            mVelocityTracker.recycle();  
            mVelocityTracker = null;  
        }  
    }  
}
