package com.ctbri.staroftoday.utils;

import android.os.Environment;

public class Constants {
	
	public static final String HOST = "http://ts.youxt.cn/api/phone/";
	public static final String UPDATE = "http://ts.youxt.cn/api/update/";
	public static final String NEWLYUPDATE = "http://update.youxt.cn/api/app/com.ctbri.staroftoday";
	public static final String POST ="http://ts.youxt.cn";
	public static final String YXT = "http://www.youxt.cn";
	public static String FOLDER_ROOT = Environment.getExternalStorageDirectory().getPath() + "/StarOfToday/";
	public static final String FOLDER_EXCEPTION = FOLDER_ROOT + "exception_log/";

}
