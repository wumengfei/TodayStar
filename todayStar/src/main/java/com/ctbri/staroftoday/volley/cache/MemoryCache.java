package com.ctbri.staroftoday.volley.cache;

import com.ctbri.staroftoday.volley.cache.util.LruCache;

import android.graphics.Bitmap;
/*
 * @ description:����ͼƬ���渨����
 */
public class MemoryCache extends BaseImageCache{
	public static final String TAG = MemoryCache.class.getSimpleName();
	LruCache<String, Bitmap> mLruCache;
	
	public MemoryCache() {		
		this(0.125f);
		priority = Integer.MAX_VALUE;
	}
	
	public MemoryCache(float scale) {
		// ��ȡӦ�ó����������ڴ�  
        int maxMemory = (int) Runtime.getRuntime().maxMemory();   
        log("����ڴ�"+maxMemory);
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
			log(url+"���ڴ���ȡ");
		}
		return res;
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		if(isCache){
			mLruCache.put(hashKeyForDisk(url), bitmap);
			log(url+"���ڴ滺��");
		}
	}

	@Override
	public void clear() {
		if(isCache){
			mLruCache.evictAll();
			log("����ڴ滺��");
		}
	}
}
