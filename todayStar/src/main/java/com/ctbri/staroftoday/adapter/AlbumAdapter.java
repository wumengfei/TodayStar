 package com.ctbri.staroftoday.adapter;

import java.util.ArrayList;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import com.ctbri.staroftoday.bean.PicD;
import com.ctbri.staroftoday.view.NoScrollGridView;
import com.ctbri.staroftoday.R;


public class AlbumAdapter extends BaseAdapter {
	public ArrayList<PicD> list = new ArrayList<PicD>();
	public ArrayList<String> temp_urls = new ArrayList<String>();
	private Activity activity;
	private int mystate;
	private ViewHolder vh;
	private NoScrollGridAdapter nsga;
	private ImageLoader mImageLoader;
	public AlbumAdapter(Activity activity, RequestQueue mQueue,
			ImageLoader mImageLoader,ArrayList<PicD> alist) {
		this.list=alist;
		this.activity=activity;
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
		final PicD data = list.get(arg0);
		if (null == arg1) {
			arg1 = LayoutInflater.from(arg2.getContext()).inflate(
					R.layout.list_item_album, arg2, false);
			vh = new ViewHolder();			
			vh.tv = (TextView) arg1.findViewById(R.id.tv_list_item_album_date);	
			vh.nsgv = (NoScrollGridView) arg1.findViewById(R.id.gridview);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}
		vh.tv.setText(data.pictime);
		vh.face=Typeface.createFromAsset (activity.getAssets() , "fonts/Roboto-Regular.ttf" );
		vh.tv.setTypeface(vh.face);
		//final int pos=arg0;
		if (data.pics == null || data.pics.size() == 0) { // 没有图片资源就隐藏GridView
			vh.nsgv.setVisibility(View.GONE);
		}else {
			mystate=0;
			if(data.pics.size()>8){
				mystate=1;
			}
			nsga=new NoScrollGridAdapter(activity, data.pics,mystate,mImageLoader);
			vh.nsgv.setAdapter(nsga);
		}
		return arg1;
	}
}
