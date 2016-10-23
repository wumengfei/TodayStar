package com.ctbri.staroftoday.adapter;

import java.util.ArrayList;

import com.android.volley.toolbox.ImageLoader;
import com.ctbri.staroftoday.ImagePagerActivity;
import com.ctbri.staroftoday.bean.PicData;
import com.ctbri.staroftoday.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

@SuppressLint("ViewHolder")
public class NoScrollGridAdapter1 extends BaseAdapter {

	private Context ctx;
	private int mystate;
	private final ImageLoader mImageLoader;
	public ArrayList<PicData> list = new ArrayList<PicData>();
	private ArrayList<String> imageUrls= new ArrayList<String>();
	private ArrayList<String> imageIds= new ArrayList<String>();
	private ArrayList<String> imageInfos= new ArrayList<String>();
	private ArrayList<String> dates= new ArrayList<String>();
	private ArrayList<Integer> imageTyps= new ArrayList<Integer>();
	
	public NoScrollGridAdapter1(Context ctx, ArrayList<PicData> list,int mystate,ImageLoader mImageLoader) {
		this.ctx = ctx;
		this.list = list;
		this.mImageLoader=mImageLoader;
		this.mystate=mystate;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mystate==0){
			return list == null ? 0 : list.size();
		}else if(mystate==1){
			return 9;
		}else{
			return list.size()+1;
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate(ctx, R.layout.item_gridview, null);
		final int pos=position;
		ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);
		if(mystate==1 && position==8){
			imageView.setImageResource(R.drawable.ofm_setting_icon);
			imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mystate=2;
					NoScrollGridAdapter1.this.notifyDataSetChanged();
				}
			});
		}else if(mystate==2 && position==list.size()){
				imageView.setImageResource(R.drawable.agree);
				imageView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mystate=1;
						NoScrollGridAdapter1.this.notifyDataSetChanged();
					}
				});
		}else{
			mImageLoader.get(list.get(position).getUrl(), ImageLoader.getImageListener(imageView,
					R.drawable.test, R.drawable.test));
			imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					initData();
					imageBrower(pos);
				}
			});
		}
		return view;
	}
	protected void imageBrower(int position) {
		Intent intent = new Intent(ctx, ImagePagerActivity.class);
		// 鍥剧墖url,涓轰簡婕旂ず杩欓噷浣跨敤甯搁噺锛屼竴鑸粠鏁版嵁搴撲腑鎴栫綉缁滀腑鑾峰彇
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, imageUrls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_IDS, imageIds);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INFOS, imageInfos);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_TYPS, imageTyps);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_DATES, dates);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		ctx.startActivity(intent);
	}
	public void initData(){
		for(int i=0;i<list.size();i++){
			imageUrls.add(list.get(i).getUrl());
			imageIds.add(list.get(i).getId());
			imageInfos.add(list.get(i).getInfo());
			dates.add(list.get(i).getTime());
			imageTyps.add(list.get(i).getIshown());
		}
	}
}
