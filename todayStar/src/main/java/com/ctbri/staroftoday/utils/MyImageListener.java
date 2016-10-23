package com.ctbri.staroftoday.utils;

import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;

/**
 * @author limen
 * @date 2015年9月6日上午10:25:30
 * @description
 */
	public class MyImageListener implements ImageListener {
		private final ImageView iv;
		private final int defaultIcon;

		public MyImageListener(ImageView iv, int defaultIcon) {
			this.iv = iv;
			this.defaultIcon = defaultIcon;
		}

		@Override
		public void onErrorResponse(VolleyError error) {
			iv.setImageResource(defaultIcon);
		}

		@Override
		public void onResponse(ImageContainer response, boolean isImmediate) {
			iv.setImageBitmap(response.getBitmap());
		}
	}
