package com.ctbri.staroftoday.bean;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ctbri.staroftoday.LoginActivity;
import com.ctbri.staroftoday.MyApplication;
import com.ctbri.staroftoday.ParentsMainActivity;
import com.ctbri.staroftoday.R;
import com.ctbri.staroftoday.TeacherMainActivity;
import com.ctbri.staroftoday.WelcomeActivity;
import com.ctbri.staroftoday.utils.Constants;

/**
 *@author coolszy
 *@date 2012-4-26
 *@blog http://blog.92coding.com
 */

public class UpdateManager
{
	private RequestQueue mQueue;

	
    /* ������ */
    private static final int DOWNLOAD = 1;
    /* ���ؽ��� */
    private static final int DOWNLOAD_FINISH = 2;
    
    private static final int RESPONSE_SUCCESS = 3;
    
    private static final int RESPONSE_ERROR = 4;
    
    /* ���ر���·�� */
    private String mSavePath;
    
    private String versionCode;
    
    private String serviceCode;
    
    private int updateMethod;
    
    private String downloadUrl;
    
    private boolean uresult = false;
    /* ��¼��������� */
    private int progress;
    /* �Ƿ�ȡ����� */
    private boolean cancelUpdate = false;

    private Activity mContext;
    /* ���½���� */
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;

    @SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
            // ��������
            case DOWNLOAD:
                // ���ý����λ��
                mProgress.setProgress(progress);
                break;
            case DOWNLOAD_FINISH:
                // ��װ�ļ�
                installApk();
                break;
            case RESPONSE_SUCCESS:
            	 if (uresult)
                 {
                     // ��ʾ��ʾ�Ի���
                     showNoticeDialog();
                 } else
                 {
//                     Toast.makeText(mContext, R.string.soft_update_no, Toast.LENGTH_LONG).show();
                 }
                break;
            case RESPONSE_ERROR:
            	  Toast.makeText(mContext, "服务器端更新错误", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
            }
        };
    };

    public UpdateManager(Activity context)
    {
        this.mContext = context;
    }

    /**
     * ����������
     */
    public void checkUpdate()
    {   
    	isUpdate();
    	
    }

    /**
     * �������Ƿ��и��°汾
     * 
     * @return
     */
    private void isUpdate()
    {
        Log.w("umeng", getChannelValue());
        // ��ȡ��ǰ����汾
    	versionCode = getVersionCode(mContext);
        Log.w("版本号", versionCode);
        //��ȡ����������汾
    	mQueue = Volley.newRequestQueue(mContext);
    	JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                //不改成斜杠的写法可能会报错
                Constants.NEWLYUPDATE+'/'+getChannelValue(), null,
   			new Listener<JSONObject>() {
   				@Override
   				public void onResponse(JSONObject result) {
   					try {
   						int state = result.getInt("state");
   						String message= result.getString("message");
   						if (state == 1) {
   							JSONObject data = result.getJSONObject("data");
   							serviceCode = data.getString("version");                
   							if (!serviceCode.equals(versionCode)) {
   								updateMethod=data.getInt("updatemethod");
   								downloadUrl=data.getString("downloadurl");
   								uresult=true;				   
							}else{
								jump();
							}
   							
   						}else {
							Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
							jump();
							
						} 
   						mHandler.sendEmptyMessage(RESPONSE_SUCCESS);
   					} catch (JSONException e) {
   						e.printStackTrace();
   						mHandler.sendEmptyMessage(RESPONSE_ERROR);
   					}
   					
   				}
   			
   			}, new ErrorListener() {

   				@Override
   				public void onErrorResponse(VolleyError error) {
   					mHandler.sendEmptyMessage(RESPONSE_ERROR);
   					jump();
   				}

   			});
   	mQueue.add(jsonObjectRequest);
   	
   //     return uresult;
    }
public void jump(){
	Timer timer = new Timer();
    TimerTask task = new TimerTask(){
     @Override
     public void run() {
		if(MyApplication.rid.equals("6"))
		{
			mContext.startActivity(new Intent(
					mContext,
					TeacherMainActivity.class));
			mContext.finish();
		}else if(MyApplication.rid.equals("7"))
		{
			mContext.startActivity(new Intent(
					mContext,
					ParentsMainActivity.class));
			mContext.finish();
		}else {
			mContext.startActivity(new Intent(
					mContext,
					LoginActivity.class));
			mContext.finish();
		}
     }
    };
    timer.schedule(task, 1000);
}
/**
 * ��ȡ����汾��
 * 
 * @param context
 * @return
 */
private String getVersionCode(Context context)
{
    
    try
    {
        // ��ȡ����汾�ţ���ӦAndroidManifest.xml��android:versionCode
        versionCode = context.getPackageManager().getPackageInfo("com.ctbri.staroftoday", 0).versionName;
        
    } catch (NameNotFoundException e)
    {
        e.printStackTrace();
    }
    return versionCode;
}

    /**
     * ��ʾ������¶Ի���
     */
    private void showNoticeDialog()
    {
        // ����Ի���
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle(R.string.soft_update_title);
        builder.setMessage(R.string.soft_update_info);
        // ����
        builder.setPositiveButton(R.string.soft_update_updatebtn, new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                // ��ʾ���ضԻ���
                showDownloadDialog();
            }
        });
        // �Ժ����
        builder.setNegativeButton(R.string.soft_update_later, new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(updateMethod==1)
                {Toast.makeText(mContext, "����ǿ�Ƹ���,���������¡���ť", Toast.LENGTH_LONG).show();}
                else if(updateMethod==0)
            	{dialog.dismiss();
            	jump();}
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    /**
     * ��ʾ������ضԻ���
     */
    @SuppressLint("InflateParams")
	private void showDownloadDialog()
    {
        // ����������ضԻ���
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle(R.string.soft_updating);
        // �����ضԻ������ӽ����
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        builder.setView(v);
        // ȡ�����
        builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                // ����ȡ��״̬
                cancelUpdate = true;
                jump();
            }
        });
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        // �����ļ�
        downloadApk();
    }

    /**
     * ����apk�ļ�
     */
    private void downloadApk()
    {
 /****�����ǿ������߳����صģ�
  * ��Ҫѧ�ܸĳ�ʹ��volley�����ص����ַ�ʽ���͸�login��Щ����һ���ˣ�ͬʱע�͵�����Ĵ���*****/  	
    	
    	// �������߳��������
        new downloadApkThread().start();
    }

    /**
     * �����ļ��߳�
     * 
     * @author coolszy
     *@date 2012-4-26
     *@blog http://blog.92coding.com
     */
    private class downloadApkThread extends Thread
    {
        @Override
        public void run()
        {
            try
            {
                // �ж�SD���Ƿ���ڣ������Ƿ���ж�дȨ��
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                {
                    // ��ô洢����·��
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    mSavePath = sdpath + "download";
                    URL url = new URL(downloadUrl);
                    // ��������
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // ��ȡ�ļ���С
                    int length = conn.getContentLength();
                    // ����������
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // �ж��ļ�Ŀ¼�Ƿ����
                    if (!file.exists())
                    {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, "com.ctbri.todaystar.tvbox"+serviceCode+".apk");
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // ����
                    byte buf[] = new byte[1024];
                    // д�뵽�ļ���
                    do
                    {
                        int numread = is.read(buf);
                        count += numread;
                        // ��������λ��
                        progress = (int) (((float) count / length) * 100);
                        // ���½��
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0)
                        {
                            // �������
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // д���ļ�
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// ���ȡ���ֹͣ����.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            // ȡ�����ضԻ�����ʾ
            mDownloadDialog.dismiss();
        }
    };

    /**
     * ��װAPK�ļ�
     */
    private void installApk()
    {
        File apkfile = new File(mSavePath, "com.ctbri.todaystar"+serviceCode+".apk");
        if (!apkfile.exists())
        {
            return;
        }
        // ͨ��Intent��װAPK�ļ�
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }

    public String getChannelValue() {
        String result = "";
        ApplicationInfo appInfo;
        PackageManager manager = WelcomeActivity.context.getPackageManager();
        try {
            appInfo = manager.getApplicationInfo(WelcomeActivity.context.getPackageName(),
                    PackageManager.GET_META_DATA);
            result = appInfo.metaData.getString("CHANNEL_NAME");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
