package com.ctbri.staroftoday.view;



import com.ctbri.staroftoday.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PopupWindow1 extends PopupWindow {

	private TextView btn_take_photo, btn_pick_photo;
	private View mMenuView;

	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	public PopupWindow1(Activity context,OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.alert_dialog, null);
		btn_take_photo = (TextView) mMenuView.findViewById(R.id.btn_take_photo);
		btn_pick_photo = (TextView) mMenuView.findViewById(R.id.btn_pick_photo);
		Typeface face = Typeface.createFromAsset (context.getAssets() , "fonts/Roboto-Regular.ttf" );  
		btn_take_photo.setTypeface (face);
		btn_pick_photo.setTypeface (face);
		
		//锟斤拷锟矫帮拷钮锟斤拷锟斤拷
		btn_pick_photo.setOnClickListener(itemsOnClick);
		btn_take_photo.setOnClickListener(itemsOnClick);
		//锟斤拷锟斤拷SelectPicPopupWindow锟斤拷View
		this.setContentView(mMenuView);
		//锟斤拷锟斤拷SelectPicPopupWindow锟斤拷锟斤拷锟斤拷锟斤拷目锟�
		this.setWidth(LayoutParams.FILL_PARENT);
		//锟斤拷锟斤拷SelectPicPopupWindow锟斤拷锟斤拷锟斤拷锟斤拷母锟�
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//锟斤拷锟斤拷SelectPicPopupWindow锟斤拷锟斤拷锟斤拷锟斤拷傻锟斤拷
		this.setFocusable(true);
		//实锟斤拷锟斤拷一锟斤拷ColorDrawable锟斤拷色为锟斤拷透锟斤拷
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		//锟斤拷锟斤拷SelectPicPopupWindow锟斤拷锟斤拷锟斤拷锟斤拷谋锟斤拷锟�
		this.setBackgroundDrawable(dw);
		//mMenuView锟斤拷锟絆nTouchListener锟斤拷锟斤拷锟叫断伙拷取锟斤拷锟斤拷位锟斤拷锟斤拷锟斤拷锟窖★拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟劫碉拷锟斤拷锟斤拷
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			@SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View v, MotionEvent event) {
				
				int height1 = mMenuView.findViewById(R.id.pop_layout).getTop();
				int height2 = mMenuView.findViewById(R.id.pop_layout).getBottom();
				int width1 = mMenuView.findViewById(R.id.pop_layout).getLeft();
				int width2 = mMenuView.findViewById(R.id.pop_layout).getRight();
				int y=(int) event.getY();
				int x=(int) event.getX();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y>height1||y<height2||x>width2||x<width1){
						dismiss();
					}
				}				
				return true;
			}
		});

	}

}
