package com.ctbri.staroftoday.volley.cache;

import com.ctbri.staroftoday.volley.cache.util.LruCache;

import android.graphics.Bitmap;
/*
 * @ description:本地图片缓存辅助类
 */
public class MemoryCache extends BaseImageCache{
	public static final String TAG = MemoryCache.class.getSimpleName();
	LruCache<String, Bitmap> mLruCache;
	
	public MemoryCache() {		
		this(0.125f);
		priority = Integer.MAX_VALUE;
	}
	
	public MemoryCache(float scale) {
		// 获取应用程序最大可用内存  
        int maxMemory = (int) Runtime.getRuntime().maxMemory();   
        log("最大内存"+maxMemory);
        int cacheSize = (int) (scale*maxMemory);  
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {  
            @Override  
            protected int sizeOf(String key, Bitmap bitmap) {  
                return bitmap.getByteCount(); 
            }  
        };  
	}
	
	@Override
	public Bitmap getBitmap(String url) {
		Bitmap res = mLruCache.get(hashKeyForDisk(url));		
		if(res!=null){
			log(url+"从内存中取");
		}
		return res;
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		if(isCache){
			mLruCache.put(hashKeyForDisk(url), bitmap);
			log(url+"向内存缓存");
		}
	}

	@Override
	public void clear() {
		if(isCache){
			mLruCache.evictAll();
			log("清除内存缓存");
		}
	}
}
