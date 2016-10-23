 package com.ctbri.staroftoday.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ctbri.staroftoday.bean.GroupData;
import com.ctbri.staroftoday.net.JsonRequest;
import com.ctbri.staroftoday.utils.Constants;
import com.ctbri.staroftoday.view.NoScrollGridView;
import com.ctbri.staroftoday.R;



public class FrontPageAdapter extends BaseAdapter {
	public LinkedList<GroupData> list = new LinkedList<GroupData>();
	public ArrayList<String> temp_urls = new ArrayList<String>();
	private Activity activity;
	private int mystate=0;
	private ViewHolder vh;
	private NoScrollGridAdapter1 nsga;
	private RequestQueue mQueue;
	private ImageLoader mImageLoader;
	public FrontPageAdapter(Activity activity, RequestQueue mQueue,
			ImageLoader mImageLoader,LinkedList<GroupData> list) {
		this.list=list;
		this.activity=activity;
		this.mQueue=mQueue;
		this.mImageLoader=mImageLoader;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		final GroupData data = list.get(arg0);
		if (null == arg1) {
			arg1 = LayoutInflater.from(arg2.getContext()).inflate(
					R.layout.list_item_frontpage, arg2, false);
			vh = new ViewHolder();			
			vh.tv = (TextView) arg1.findViewById(R.id.tv_list_item_frontpage_name);	
			vh.tv2 = (TextView) arg1.findViewById(R.id.tv_list_item_frontpage_agree);	
			vh.tv1 = (TextView) arg1.findViewById(R.id.tv_list_item_frontpage_disagree);
			vh.tv1.setTag(arg0);
			//vh.iv=(ImageView) arg1.findViewById(R.id.headphoto);
			vh.nsgv = (NoScrollGridView) arg1.findViewById(R.id.gridview1);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}
		//mImageLoader.get(data.getUserHeadPic(), ImageLoader.getImageListener(vh.iv,
		//		R.drawable.test, R.drawable.test));
		vh.tv.setText(data.getUsername());
		vh.face=Typeface.createFromAsset (activity.getAssets() , "fonts/Roboto-Regular.ttf" );
		vh.tv.setTypeface(vh.face);
		//final int pos=arg0;
		if (list == null || list.size() == 0) { // 娌℃湁鍥剧墖璧勬簮灏遍殣钘廏ridView
			vh.nsgv.setVisibility(View.GONE);
		}else {
			if(list.size()>8){
				mystate=1;
			}
			nsga=new NoScrollGridAdapter1(activity, data.getPic() , mystate,mImageLoader);
			vh.nsgv.setAdapter(nsga);
		}
		vh.tv1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					int pos=(Integer) v.getTag();
					list.remove(pos);
					FrontPageAdapter.this.notifyDataSetChanged();					
					mQueue.add(new JsonObjectRequest(Method.POST, Constants.HOST+"denyPlay", 
							JsonRequest.denyPlayRequest(data.getId()), new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							// TODO
							Toast.makeText(activity, "驳回成功", Toast.LENGTH_SHORT)
							.show();
						}
					}, null));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		vh.tv2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Calendar c = Calendar.getInstance(Locale.CHINA);
				DatePickerDialog datePicker = new DatePickerDialog(
						activity,
						new OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, final int year,
									final int monthOfYear, final int dayOfMonth) {
								new Builder(activity)
										.setTitle("提示")
										.setMessage(
												"是否要把当前照片组插播到" + year + "/"
														+ (monthOfYear+1) + "/"
														+ dayOfMonth)
										.setPositiveButton("确定",
												new DialogInterface.OnClickListener() {

													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														try {
															mQueue.add(new JsonObjectRequest(
																	Method.POST, Constants.HOST+"insertPlay", 
																	JsonRequest.insertPlayRequest(data.getId(),String.valueOf(year)+"-"+String.valueOf(monthOfYear+1)+"-"+String.valueOf(dayOfMonth)),
																	new Listener<JSONObject>() {
																		@Override
																		public void onResponse(
																				JSONObject response) {
																			// TODO
																			Log.d("RES", response.toString());
																			Toast.makeText(activity, "插播成功", Toast.LENGTH_SHORT)
																			.show();
																		}
																	}, null));
														} catch (JSONException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
													}
												})
										.setNegativeButton("取消",
												new DialogInterface.OnClickListener() {

													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														dialog.dismiss();
													}
												}).create().show();
								Toast.makeText(
										activity,
										year + "year " + (monthOfYear+1) + "month "
												+ dayOfMonth + "day",
										Toast.LENGTH_SHORT).show();
							}
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
						c.get(Calendar.DATE));
				datePicker.show();
			}
		});
		return arg1;
	}
}
