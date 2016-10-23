package com.ctbri.staroftoday.adapter;


import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ctbri.staroftoday.bean.InsertResult;
import com.ctbri.staroftoday.bean.Insertinfo;
import com.ctbri.staroftoday.net.JsonRequest;
import com.ctbri.staroftoday.utils.Constants;
import com.ctbri.staroftoday.R;


/**
 * @ description:�Ƽ�����
 */
public class TeacherRecordAdapter extends BaseAdapter {
	public LinkedList<InsertResult> list = new LinkedList<InsertResult>();
	public Activity activity;
	private PopupWindow popupWindow;
	//private int myIndex = 0; 
	
	public TeacherRecordAdapter(Activity activity,LinkedList<InsertResult> list) {
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
		final Insertinfo data = list.get(arg0).insertinfo;
		if (null == arg1) {
			arg1 = LayoutInflater.from(arg2.getContext()).inflate(
					R.layout.list_item_record1, arg2, false);		
			vh = new ViewHolder();		
			vh.face=Typeface.createFromAsset (activity.getAssets() , "fonts/Roboto-Regular.ttf" );
			vh.tv = (TextView) arg1.findViewById(R.id.record_date);
			vh.tv.setTypeface(vh.face);
			vh.tv2 = (TextView) arg1.findViewById(R.id.record_state);
			vh.tv2.setTypeface(vh.face);
			switch(data.ishown){
				case 0:
					vh.tv2.setText("同意");
					break;
				case 1:
					vh.tv2.setText("拒绝");
					break;
				default:
					break;
			}
	
			vh.tv1 = (TextView) arg1.findViewById(R.id.record_detail);
			vh.tv1.setTypeface(vh.face);
			vh.tv1.setTag(arg0);
			vh.tv3=(TextView) arg1.findViewById(R.id.record_name);
			vh.tv3.setTypeface(vh.face);
			vh.tv3.setText(list.get(arg0).parentname);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}
		vh.tv.setText(data.itime);
		//final int i=Integer.parseInt(vh.tv1.getTag().toString());
		final int index = arg0;
		vh.tv1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//int i=Integer.parseInt(v.getTag().toString()); ԭʦ�ܵĴ���ˢ���д?����
				//Log.i("xiaofang", "index = " + index); 
				showPopupWindow(index);
			}
		
		});	
		arg1.setClickable(true);
		arg1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//int i=Integer.parseInt(v.getTag().toString());
				showPopupWindow(index);
			}
		});
		return arg1;		
	}
	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	private void showPopupWindow(int i) {
		final int pos=i;
		final View mMenuView = LayoutInflater.from(activity).inflate(
                R.layout.popupwindow2, null);
		TextView insert_record_date = (TextView) mMenuView.findViewById(R.id.insert_record_date);
		TextView insert_record_reason = (TextView) mMenuView.findViewById(R.id.insert_record_reason);
		insert_record_date.setText(list.get(i).insertinfo.itime);
		insert_record_reason.setText(list.get(i).insertinfo.remark);
		TextView insert_disagree = (TextView) mMenuView.findViewById(R.id.insert_disagree);
		TextView insert_agree = (TextView) mMenuView.findViewById(R.id.insert_agree);
		RelativeLayout ll=(RelativeLayout) mMenuView.findViewById(R.id.pop_layout2);
		insert_agree.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {				
				insertAgree(pos);		
				}

			
			});
		insert_disagree.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {				
				insertDisagree(pos);		
				}
			});
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
				int height1 = mMenuView.findViewById(R.id.pop_layout2).getTop();
				int height2 = mMenuView.findViewById(R.id.pop_layout2).getBottom();
				int width1 = mMenuView.findViewById(R.id.pop_layout2).getLeft();
				int width2 = mMenuView.findViewById(R.id.pop_layout2).getRight();
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
        // ���úò���֮����show
        popupWindow.showAtLocation(activity.findViewById(R.id.record_main), Gravity.CENTER, 0, 0);
	}
		protected void insertDisagree(int pos) {
			try {
				RequestQueue mQueue = Volley.newRequestQueue(activity);
				mQueue.add(new JsonObjectRequest(Method.GET, Constants.HOST+"insertDeny/"+list.get(pos).insertinfo.id, 
						JsonRequest.updateVersion(), new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// TODO
						try {
							Toast.makeText(activity,response.getString("message"), Toast.LENGTH_SHORT)
							.show();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, null));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		protected void insertAgree(int pos) {
			try {
				RequestQueue mQueue = Volley.newRequestQueue(activity);
				mQueue.add(new JsonObjectRequest(
						Method.POST, Constants.HOST+"insertPlay", 
						JsonRequest.insertPlayRequest1(list.get(pos).insertinfo.gid,list.get(pos).insertinfo.itime),
						new Listener<JSONObject>() {
							@Override
							public void onResponse(
									JSONObject response) {
								// TODO
								Log.d("RES", response.toString());
								try {
									Toast.makeText(activity, response.getString("message"), Toast.LENGTH_SHORT)
									.show();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}, null));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	


