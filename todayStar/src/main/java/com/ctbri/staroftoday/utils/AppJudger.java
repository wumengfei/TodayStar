package com.ctbri.staroftoday.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class AppJudger {
	public static boolean isAppInstalled(Context context,String packagename)
	{
	PackageInfo packageInfo;        
	try {
	            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
	         }catch (NameNotFoundException e) {
	            packageInfo = null;
	            e.printStackTrace();
	         }
	         if(packageInfo ==null){
	            //System.out.println("û�а�װ");
	            return false;
	         }else{
	            //System.out.println("�Ѿ���װ");
	            return true;
	        }
	}
}
