package com.ctbri.staroftoday;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Calendar;
import java.util.TimeZone;

import com.ctbri.staroftoday.utils.Constants;
import com.ctbri.staroftoday.utils.SPUtil;

import android.app.Application;
import android.content.pm.PackageInfo;

/**
 * @ description:����
 */
public class MyApplication extends Application implements UncaughtExceptionHandler {
	public static final String KEY_USERNAME = "username";
	public static final String KEY_PASSWORD = "password";
	/** �û�Id */
	public static final String KEY_USERID = "uid";
	/** �׶�԰Id */
	public static final String KEY_KID = "kid";
	/** ��Ƭ��ݸ��¼�� */
	public static final String KEY_PICTIME = "pictime";
	/** ��Ƭˢ�¼�� */
	public static final String KEY_FRESHTIME = "freshtime";
	/** �༶Id */
	public static final String KEY_CID = "cid";
	/** ְ��Id */
	public static final String KEY_RID = "rid";
	/** �ϴθ���ʱ�� */
	public static final String KEY_LASTPICTIME = "lastpictime";
	/** ��Ƭ��� */
	public static final String KEY_NUMBER = "number";
	/** ��Ƭ���� */

	public static final String KEY_SIZE = "size";
	public static String uid;
	public static String kid;
	public static String cid;
	public static String rid;
	public static String userName;
	public static String passWord;
	public static int count = 5;
	public static SPUtil sp;
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		SPUtil sp = SPUtil.getInstance(this);
		userName=sp.getString(MyApplication.KEY_USERNAME, "");
		passWord = sp.getString(MyApplication.KEY_PASSWORD, "");
		uid=sp.getString(MyApplication.KEY_USERID, "");
		rid=sp.getString(MyApplication.KEY_RID, "");
		cid=sp.getString(MyApplication.KEY_CID, "");
		kid=sp.getString(MyApplication.KEY_KID, "");
				}
	
	@Override
	public void uncaughtException(Thread thread, final Throwable ex) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				saveErrorLog2Sdcard(ex);
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		}).start();
	}

	/**
	 * ���쳣��Ϣд��SD��
	 * 
	 * @param ex
	 */
	private void saveErrorLog2Sdcard(Throwable ex) {
		try {
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			int minute = cal.get(Calendar.MINUTE);
			int second = cal.get(Calendar.SECOND);
			File dir = new File(Constants.FOLDER_EXCEPTION);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(dir, "exception.log");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file, true);
			String exceptionHeader = "********************at " + year + "/" + month + "/" + day + "  " + hour + ":"
					+ minute + ":" + second + "  ***************\r\n</br>";
			fos.write(exceptionHeader.getBytes());
			ex.printStackTrace(new PrintStream(fos));
			fos.flush();
			fos.close();
			try {
				PackageInfo pi = getPackageManager().getPackageInfo(this.getPackageName(), 0);
				exceptionHeader = exceptionHeader + "version:" + pi.versionCode + ",versionName:" + pi.versionName
						+ "\n";
			} catch (Exception e) {
				e.printStackTrace();
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ex.printStackTrace();
			ex.printStackTrace(new PrintStream(baos));
			exceptionHeader = exceptionHeader + baos.toString();
			exceptionHeader = exceptionHeader.replaceAll("\n", "</br>");
			// Api.getInstance(getApplicationContext()).uploadCrashInfo(exceptionHeader,
			// "lims@ctbri.com.cn,liucongbj@163.com,tianmingxin1991@163.com",
			// Constants.APIID_UPLOADCREASHINFO);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
