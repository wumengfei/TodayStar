package com.ctbri.staroftoday.volley.cache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.ctbri.staroftoday.utils.SDCardUtils;
import com.ctbri.staroftoday.volley.cache.util.DiskLruCache;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @ description:±æµÿÕº∆¨ª∫¥Ê∏®÷˙¿‡
 */
public class DiskCache extends BaseImageCache{
	public static final String TAG = DiskCache.class.getSimpleName();
	DiskLruCache mDiskLruCache;
	
	public DiskCache() {			   
		try {
			File cacheDir = new File(SDCardUtils.getSDCardPath()+"mycache/");  
		    if (!cacheDir.exists()) {  
		        cacheDir.mkdirs();  
		    } 	
			mDiskLruCache = DiskLruCache.open(cacheDir, 1, 1, 10 * 1024 * 1024);
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	@Override
	public Bitmap getBitmap(String url) {
		DiskLruCache.Snapshot snapShot;
		Bitmap res = null;
		try {
			snapShot = mDiskLruCache.get(hashKeyForDisk(url));
			if (snapShot != null) {  
		        InputStream is = snapShot.getInputStream(0);  
		        res = BitmapFactory.decodeStream(is); 
		        log(url+"¥””≤≈Ã÷–»°");
		    }  					
		} catch (IOException e) {
			log(url+"Œƒº˛º”‘ÿ¥ÌŒÛ");
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		if(!isCache) return;
		try {
			DiskLruCache.Editor editor = mDiskLruCache.edit(hashKeyForDisk(url));  
			if(editor!=null){
				OutputStream fOut = editor.newOutputStream(0);
				if(bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)){
					log(url+"”≤≈Ãª∫¥Ê");
					editor.commit();  
				}else{
					editor.abort(); 
				}			
				mDiskLruCache.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void clear() {
		if(!isCache) return;
		try {
			log("«Â≥˝”≤≈Ãª∫¥Ê");
			DiskLruCache.deleteContents(mDiskLruCache.getDirectory());
		} catch (IOException e) {			
			e.printStackTrace();
		}		
	}	
}
