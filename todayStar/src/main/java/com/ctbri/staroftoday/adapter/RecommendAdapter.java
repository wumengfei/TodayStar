package com.ctbri.staroftoday.adapter;


import java.util.ArrayList;
import java.util.LinkedList;


import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import com.ctbri.staroftoday.ImagePagerActivity;
import com.ctbri.staroftoday.MyApplication;
import com.ctbri.staroftoday.bean.RecommendData;
import com.ctbri.staroftoday.net.JsonRequest;
import com.ctbri.staroftoday.utils.Constants;
import com.ctbri.staroftoday.R;


/**
 * @ description:�Ƽ�����
 */
public class RecommendAdapter extends BaseAdapter {
	private final RequestQueue mQueue;
	private final ImageLoader mImageLoader;
	public LinkedList<RecommendData> list = new LinkedList<RecommendData>();
	public Activity activity;
	
	private ArrayList<String> imageUrls= new ArrayList<String>();
	private ArrayList<String> imageIds= new ArrayList<String>();
	private ArrayList<String> imageInfos= new ArrayList<String>();
	private ArrayList<String> dates= new ArrayList<String>();
	private ArrayList<Integer> imageTyps= new ArrayList<Integer>();
	
	public RecommendAdapter(Activity activity,RequestQueue mQueue, ImageLoader mImageLoader, LinkedList<RecommendData> list) {
		this.activity = activity;
		this.mQueue = mQueue;
		this.mImageLoader = mImageLoader;
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
		final RecommendData data = list.get(arg0);
		if (null == arg1) {
			arg1 = LayoutInflater.from(arg2.getContext()).inflate(
					R.layout.list_item_recommend, arg2, false);
			vh = new ViewHolder();			
			vh.face=Typeface.createFromAsset (activity.getAssets() , "fonts/Roboto-Regular.ttf" );
			vh.tv = (TextView) arg1.findViewById(R.id.tv_list_item_recommend_info);
			vh.tv.setTypeface(vh.face);
			vh.iv = (ImageView) arg1.findViewById(R.id.iv_list_item_recommend_pic);
			vh.iv.setTag(arg0);
			vh.tv2 = (TextView) arg1.findViewById(R.id.b_list_item_recommend_cancel);
			vh.tv2.setTypeface(vh.face);
			vh.tv2.setTag(arg0);
			//vh.tv1 = (TextView) arg1.findViewById(R.id.b_list_item_recommend_edit);
			//vh.tv1.setTypeface(vh.face);
			//vh.tv1.setTag(arg0);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}
		vh.tv.setText(data.getInfo());
		
		mImageLoader.get(data.getUrl(), ImageLoader.getImageListener(vh.iv,
				R.drawable.test, R.drawable.test));		
		/*
		vh.tv1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				edit();
			}

			private void edit() {
				// TODO Auto-generated method stub
				Intent i = new Intent(activity, EditPicActivity.class);
				i.putExtra("uri", data.getUrl());
				i.putExtra("type", EditPicActivity.EDIT);
				i.putExtra("id", data.getId());
				activity.startActivityForResult(i, AlbumFragment.EDIT_REQUEST_CODE);
			}
		});
		*/
		vh.iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				initData();
				int i=Integer.parseInt(v.getTag().toString());
				imageBrower(i);
			}
		
		});
		vh.tv2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg1) {
				try {
					delete(arg1);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

			private void delete(View v) throws JSONException {
				// TODO Auto-generated method stub
				int pos=(Integer) v.getTag();
				list.remove(pos);
				//Toast.makeText(RecommendAdapter.this, list.size(), Toast.LENGTH_SHORT).show();
				RecommendAdapter.this.notifyDataSetChanged();
				mQueue.add(new JsonObjectRequest(Constants.HOST+"cancelRec", JsonRequest
						.cancelRecommendRequest(data.getId(),MyApplication.uid), new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// TODO
						Log.d("RES","删除推荐的状态="+response.toString());
						int state = 0;
						try {
							state = response.getInt("state");
							switch (state) {
							case 1:
								Toast.makeText(activity, "推荐已删除", Toast.LENGTH_SHORT)
								.show();
								break;
							default:
								response.getString("message");
								break;
							}
						} catch (JSONException e) {
							e.printStackTrace();
							e.getMessage();
						}	
					}
				}, null));
			}
		});
		return arg1;		
	}
	protected void imageBrower(int imagePos) {
		Intent intent = new Intent(activity, ImagePagerActivity.class);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, imageUrls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_IDS, imageIds);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INFOS, imageInfos);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_TYPS, imageTyps);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_DATES, dates);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, imagePos);
		activity.startActivity(intent);
	}
	public void initData(){
		imageUrls.clear();
		imageIds.clear();
		imageInfos.clear();
		dates.clear();
		imageTyps.clear();
		for(int i=0;i<list.size();i++){
			imageUrls.add(list.get(i).getUrl());
			imageIds.add(list.get(i).getId());
			imageInfos.add(list.get(i).getInfo());
			dates.add(list.get(i).getTime());
			imageTyps.add(2);
		}
	}
	}
	


