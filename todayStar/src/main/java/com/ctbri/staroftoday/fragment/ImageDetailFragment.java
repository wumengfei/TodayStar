package com.ctbri.staroftoday.fragment;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ctbri.staroftoday.volley.cache.CombineCache;
import com.ctbri.staroftoday.R;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
/**
 *显示单张图片Fragment
 */
public class ImageDetailFragment extends Fragment {
	private String mImageUrl;
	private ImageView mImageView;
	//private ProgressBar progressBar;
	private PhotoViewAttacher mAttacher;
	private RequestQueue mQueue;
	private ImageLoader mImageLoader;
	public CombineCache mCombineCache;
	
	public static ImageDetailFragment newInstance(String imageUrl) {
		final ImageDetailFragment f = new ImageDetailFragment();
		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
		mImageView = (ImageView) v.findViewById(R.id.image);
		mAttacher = new PhotoViewAttacher(mImageView);

		mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {

			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				getActivity().finish();
			}
		});

		//progressBar = (ProgressBar) v.findViewById(R.id.loading);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mQueue = Volley.newRequestQueue(getActivity());
		mCombineCache=new CombineCache(getActivity());
		mImageLoader = new ImageLoader(mQueue, mCombineCache); 
		mImageLoader.get(mImageUrl, ImageLoader.getImageListener(mImageView,
				R.drawable.test, R.drawable.test));
	}
	@Override
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("ImageDetailScreen"); //统计页面，"ImageDetailScreen"为页面名称，可自定义
	}
	@Override
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("ImageDetailScreen"); 
	}
}
