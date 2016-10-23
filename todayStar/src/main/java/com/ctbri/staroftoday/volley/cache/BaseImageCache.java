package com.ctbri.staroftoday.volley.cache;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import com.android.volley.toolbox.ImageLoader.ImageCache;



import android.util.Log;
/**
 * @ description:??????????????
 */
public abstract class BaseImageCache implements ImageCache{
	public int priority = 1;
	protected boolean isCache = true;
	private final static boolean debug = true;
	public final static String TAG = "cacheDebug";
	public BitmapFilter mBitmapFilter = new BitmapFilter(){

		@Override
		public boolean isFilter() {
			return false;
		}		
	};
	
	/** 
     * ???MD5????????key????????????? 
     */  
    public static String hashKeyForDisk(String key) {
    	long s = System.currentTimeMillis();
    	log("md5 start:"+s);
        String cacheKey;  
        try {  
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");  
            mDigest.update(key.getBytes());  
            cacheKey = bytesToHexString(mDigest.digest());  
        } catch (NoSuchAlgorithmException e) {  
            cacheKey = String.valueOf(key.hashCode());  
        }  
        long e = System.currentTimeMillis();
        log("md5 during "+(e-s));        
        log("md5 end:"+e);
        return cacheKey;  
    }  
	
    private static String bytesToHexString(byte[] bytes) {  
        StringBuilder sb = new StringBuilder();  
        for (int i = 0; i < bytes.length; i++) {  
            String hex = Integer.toHexString(0xFF & bytes[i]);  
            if (hex.length() == 1) {  
                sb.append('0');  
            }  
            sb.append(hex);  
        }  
        return sb.toString();  
    }  
    
    public abstract void clear();
    
    public static void log(String tag,String msg){
    	if(debug) Log.i(tag, msg);
    }
    
    public static void log(String msg){
    	if(debug) Log.i(TAG, msg);
    }
    
    protected interface BitmapFilter{
    	public boolean isFilter();
    }
    
    public void setBitmapFilter(BitmapFilter mBitmapFilter){
    	this.mBitmapFilter = mBitmapFilter;
    }
}
