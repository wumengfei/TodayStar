package com.ctbri.staroftoday;



import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ctbri.staroftoday.R;
import com.ctbri.staroftoday.bean.UpdateManager;
import com.ctbri.staroftoday.net.JsonRequest;
import com.ctbri.staroftoday.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends Activity  {
	//private RequestQueue mQueue;
	public static Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		setContentView(R.layout.activity_welcome);
		super.onCreate(savedInstanceState);
		//mQueue = Volley.newRequestQueue(this);
		context = this;

		UpdateManager manager = new UpdateManager( WelcomeActivity.this);
		manager.checkUpdate();
        
    }
/*	private void getVersion() {
		try {
			//包名是todaystar还是staroftoday
			mQueue.add(new JsonObjectRequest(Request.Method.GET, Constants.NEWLYUPDATE+"com.ctbri.todaystar",
					JsonRequest.updateVersion(), new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					// TODO
					try {
						String version=response.getJSONObject("data").getString("version");
						String version1=getCruVersion();
						Toast.makeText(WelcomeActivity.this, version1, Toast.LENGTH_SHORT)
						.show();
						if(version.equals(getCruVersion())){
							Toast.makeText(WelcomeActivity.this,version, Toast.LENGTH_SHORT)
						.show();
							Timer timer = new Timer();
					        TimerTask task = new TimerTask(){
					         @Override
					         public void run() {
					        	
									if(MyApplication.rid.equals("6"))
									{
										startActivity(new Intent(
												WelcomeActivity.this,
												TeacherMainActivity.class));
										finish();
									}else if(MyApplication.rid.equals("7"))
									{
										startActivity(new Intent(
												WelcomeActivity.this,
												ParentsMainActivity.class));
										finish();
									}else {
										startActivity(new Intent(
												WelcomeActivity.this,
												LoginActivity.class));
										finish();
									}
					         }
					        };
					        timer.schedule(task, 2000);
						}else{
							if(response.getJSONObject("data").getInt("updatemethod")==0){
								UpdateManager mUpdateManager = new UpdateManager(WelcomeActivity.this);  
								mUpdateManager.checkUpdate();
								//mUpdateManager.checkUpdateInfo(response.getJSONObject("data").getString("downloadurl"));
							}else if(response.getJSONObject("data").getInt("updatemethod")==1){
								UpdateManager mUpdateManager = new UpdateManager(WelcomeActivity.this);
								mUpdateManager.checkUpdate();
								//mUpdateManager.Update(response.getJSONObject("data").getString("downloadurl"));
							}
							
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}, null));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	public String getCruVersion() {
	      try {
	          PackageManager manager = this.getPackageManager();
	          PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
	          String version = info.versionName;
	         return version;
	     } catch (Exception e) {
	         e.printStackTrace();
	         return null;
	     }
	 }
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	/**
	 * 获取渠道号码
	 *
	 * @return
	 */
	public String getChannelValue() {
		String result = "";
		ApplicationInfo appInfo;
		PackageManager manager = this.getPackageManager();
		try {
			appInfo = manager.getApplicationInfo(this.getPackageName(),
					PackageManager.GET_META_DATA);
			result = appInfo.metaData.getString("CHANNEL_NAME");
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}


}
