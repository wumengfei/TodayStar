package com.ctbri.staroftoday.adapter;

import java.util.ArrayList;

import com.android.volley.toolbox.ImageLoader;
import com.ctbri.staroftoday.ImagePagerActivity;
import com.ctbri.staroftoday.bean.Pics;
import com.ctbri.staroftoday.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

@SuppressLint("ViewHolder")
public class NoScrollGridAdapter extends BaseAdapter {

	private Context ctx;
	private int mystate;
	private final ImageLoader mImageLoader;
	public ArrayList<Pics> list = new ArrayList<Pics>();
	private ArrayList<String> imageUrls= new ArrayList<String>();
	private ArrayList<String> imageIds= new ArrayList<String>();
	private ArrayList<String> imageInfos= new ArrayList<String>();
	private ArrayList<String> dates= new ArrayList<String>();
	private ArrayList<Integer> imageTyps= new ArrayList<Integer>();
	
	public NoScrollGridAdapter(Context ctx, ArrayList<Pics> pics,int mystate,ImageLoader mImageLoader) {
		this.ctx = ctx;
		this.list = pics;
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
					NoScrollGridAdapter.this.notifyDataSetChanged();
				}
			});
		}else if(mystate==2 && position==list.size()){
				imageView.setImageResource(R.drawable.agree);
				imageView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mystate=1;
						NoScrollGridAdapter.this.notifyDataSetChanged();
					}
				});
		}else{
			//ImageLoader.getInstance().displayImage(list.get(position).purl, imageView, options);
			mImageLoader.get(list.get(pos).purl, ImageLoader.getImageListener(imageView,
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
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
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
			imageUrls.add(list.get(i).purl);
			imageIds.add(list.get(i).id);
			imageInfos.add(list.get(i).pname);
			dates.add(list.get(i).ctime);
			imageTyps.add(list.get(i).ishown);
		}
	}
}
