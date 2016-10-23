package com.ctbri.staroftoday.adapter;


import java.util.LinkedList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.ctbri.staroftoday.bean.Insertinfo;
import com.ctbri.staroftoday.R;


/**
 * @ description:推荐适配
 */
public class ParentRecordAdapter extends BaseAdapter {
	public LinkedList<Insertinfo> list = new LinkedList<Insertinfo>();
	public Activity activity;
	private PopupWindow popupWindow;
	
	
	public ParentRecordAdapter(Activity activity,LinkedList<Insertinfo> list) {
		this.activity = activity;
		this.list = list;
	}
	
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder vh;
		final Insertinfo data = list.get(arg0);
		if (null == arg1) {
			arg1 = LayoutInflater.from(arg2.getContext()).inflate(
					R.layout.list_item_record, arg2, false);
			vh = new ViewHolder();			
			vh.tv = (TextView) arg1.findViewById(R.id.record_date);
			vh.tv2 = (TextView) arg1.findViewById(R.id.record_state);
			vh.face=Typeface.createFromAsset (activity.getAssets() , "fonts/Roboto-Regular.ttf" );
			vh.tv.setTypeface(vh.face);
			vh.tv2.setTypeface(vh.face);
			switch(data.ishown){
			case 0:
				vh.tv2.setText("已拒绝");
				break;
			case 1:
				vh.tv2.setText("已同意");
				break;
			default:
				break;
		}
			vh.tv1 = (TextView) arg1.findViewById(R.id.record_detail);
			vh.tv1.setTypeface(vh.face);
			vh.tv1.setTag(arg0);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}
		vh.tv.setText(data.itime);		
		vh.tv1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPopupWindow(data.itime,data.remark);
			}
		
		});	
		arg1.setClickable(true);
		arg1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPopupWindow(data.itime,data.remark);
			}
		});
		return arg1;		
	}
	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	private void showPopupWindow(String time,String remark) {
		final View mMenuView = LayoutInflater.from(activity).inflate(
                R.layout.popupwindow1, null);
		TextView insert_record_date = (TextView) mMenuView.findViewById(R.id.insert_record_date);
		TextView insert_record_reason = (TextView) mMenuView.findViewById(R.id.insert_record_reason);
		LinearLayout ll=(LinearLayout) mMenuView.findViewById(R.id.pop_layout1);
		insert_record_date.setText(time);
		insert_record_reason.setText(remark);
		WindowManager wm = (WindowManager) activity
                .getSystemService(Context.WINDOW_SERVICE);
		LayoutParams lp=ll.getLayoutParams();
		lp.height=wm.getDefaultDisplay().getHeight()/2;
		lp.width= wm.getDefaultDisplay().getWidth()*3/4;
		ll.setLayoutParams(lp);
		popupWindow = new PopupWindow(mMenuView,
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);		
		mMenuView.setOnTouchListener(new OnTouchListener() {			
			@SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View v, MotionEvent event) {				
				int height1 = mMenuView.findViewById(R.id.pop_layout1).getTop();
				int height2 = mMenuView.findViewById(R.id.pop_layout1).getBottom();
				int width1 = mMenuView.findViewById(R.id.pop_layout1).getLeft();
				int width2 = mMenuView.findViewById(R.id.pop_layout1).getRight();
				int y=(int) event.getY();
				int x=(int) event.getX();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y>height1||y<height2||x>width2||x<width1){
						popupWindow.dismiss();
					}
				}				
				return true;
			}
		});
		popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
			@Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        popupWindow.setBackgroundDrawable(dw);
        // 设置好参数之后再show
        popupWindow.showAtLocation(activity.findViewById(R.id.record_main), Gravity.CENTER, 0, 0);
		}
	}
	


