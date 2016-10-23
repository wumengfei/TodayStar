package com.ctbri.staroftoday.volley.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

public class CombineCache extends BaseImageCache{
	
	HashMap<String,BaseImageCache> cacheMap = new HashMap<String,BaseImageCache>();
	List<BaseImageCache> cacheArray = new ArrayList<BaseImageCache>();
	List<BaseImageCache> nullCacheArray = new ArrayList<BaseImageCache>();
	
	public CombineCache(Context context) {
			addCache(new MemoryCache());
			//addCache(new DiskCache());取消硬盘缓存方案，因为volley内部已经实现了这一级缓存
			addCache(new LocalFileCache(context,Config.RGB_565,400,400));//本地图片缩放方案
	}
	
	@Override
	public Bitmap getBitmap(String url) {		
		Bitmap res = null;
		for(BaseImageCache cache:cacheArray){
			res = cache.getBitmap(url);
			if(res!=null){
				putPriorityBitmap(url,res);
				return res;	
			}else nullCacheArray.add(cache);
		}
		return res;		
	}
	
	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		for(BaseImageCache cache:cacheArray){
			if(!cache.mBitmapFilter.isFilter())
				cache.putBitmap(url,bitmap);			
		}
	}
		   
    public void clear(){
    	for(BaseImageCache cache:cacheArray){
			cache.clear();
		}
    }
    
    private void putPriorityBitmap(String url, Bitmap bitmap){
    	for(BaseImageCache cache:nullCacheArray){
    		if(!cache.mBitmapFilter.isFilter())
    			cache.putBitmap(url,bitmap);			
		}
    	nullCacheArray.clear();
    }
    
    public void addCache(BaseImageCache cache){
    	cacheMap.put(cache.getClass().getSimpleName(), cache);
    	cacheArray.add(cache);
    	Collections.sort(cacheArray,new Comparator<BaseImageCache>(){

			@Override
			public int compare(BaseImageCache lhs, BaseImageCache rhs) {
				return lhs.priority>rhs.priority?-1:1;
			}    						
    	});
    }
    
    public void removeCache(BaseImageCache cache){
    	cacheMap.remove(cache.getClass().getSimpleName());
    	for(BaseImageCache mcache:cacheArray){
			if(mcache==cache){				
				cacheArray.remove(cache);
				return;
			}
		}
    }
    
    public void clearCache(){
    	cacheMap.clear();
    	cacheArray.clear();
    }
}
