package com.ctbri.staroftoday;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ctbri.staroftoday.fragment.AlbumFragment;
import com.ctbri.staroftoday.fragment.RecommendFragment;
import com.ctbri.staroftoday.net.JsonRequest;
import com.ctbri.staroftoday.utils.AppJudger;
import com.ctbri.staroftoday.utils.SPUtil;
import com.ctbri.staroftoday.view.PagerSlidingTabStrip;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import info.vividcode.android.zxing.CaptureActivity;

/**

 */
public class ParentsMainActivity extends FragmentActivity implements
OnClickListener  {

	/**
	 * 相册界面的Fragment
	 */
	private AlbumFragment albumFragment;
	/**
	 * 推荐界面的Fragment
	 */
	private RecommendFragment recommendFragment;
	/**
	 * PagerSlidingTabStrip的实�?
	 */
	private PagerSlidingTabStrip tabs;

	/**
	 * 获取当前屏幕的密�?
	 */
	private DisplayMetrics dm;
	private static final String IMAGE_FILE_NAME = "temp.jpg";	
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	public static final int ADD_REQUEST_CODE = 3;
	public static final int EDIT_REQUEST_CODE = 4;
	private static final int QR_REQUEST_CODE = 5;

	private RequestQueue mQueue;
	private String imgUri;
	private MyPagerAdapter mad;
	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MobclickAgent.openActivityDurationTrack(false);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mQueue = Volley.newRequestQueue(this);
		setOverflowShowingAlways();
		dm = getResources().getDisplayMetrics();
		ViewPager pager = (ViewPager) findViewById(R.id.id_viewpager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		mad=new MyPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(mad);
		tabs.setViewPager(pager);
		setTabsValue();
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.myactionbar2);//自定义ActionBar布局
        TextView tittle=(TextView)actionBar.getCustomView().findViewById(R.id.tittle1);
        Typeface typeFace =Typeface.createFromAsset(getAssets(),"fonts/Roboto-Black.ttf");
        tittle.setTypeface(typeFace);
        
        if((MyApplication.cid.equals(null))||(MyApplication.kid.equals(null)))
		{
        	Toast toast = Toast.makeText(getApplicationContext(),"tishi!!!!", Toast.LENGTH_LONG);  
        	toast.setGravity(Gravity.CENTER, 0, 0);   
        	toast.show();  
		}
        
	}
/*
	private static void setActionBar(Activity activity) {
		// TODO Auto-generated method stub
		//隐藏掉系统本身的标题		
	        int titleId = activity.getResources().getIdentifier("action_bar_title", "id", "android");
	        if (titleId<=0)return;
	        TextView tV = (TextView)activity.findViewById(titleId);
	        tV.setTypeface(Typeface.createFromAsset (activity.getAssets() , "fonts/Roboto-Black.ttf" ));
	        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
	        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) tV.getLayoutParams();
	        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
	        txvPars.width = metrics.widthPixels+10;
	        tV.setLayoutParams(txvPars);
	        tV.setGravity(Gravity.CENTER);
	}
*/
	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值�??
	 */
	private void setTabsValue() {
		// 设置Tab是自动填充满屏幕�?
		tabs.setShouldExpand(true);
		// 设置Tab的分割线是�?�明�?
		tabs.setDividerColor(Color.WHITE);
		// 设置Tab底部线的高度
		tabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 1, dm));
		// 设置Tab Indicator的高�?
		tabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_PX, 3, dm));
		// 设置Tab标题文字的大小字�?
		tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_PX, 35, dm));
		tabs.setTypeface(Typeface.createFromAsset (this.getAssets() , "fonts/Roboto-Regular.ttf" ), 0);
		// 设置Tab Indicator的颜�?
		tabs.setIndicatorColor(Color.parseColor("#5d5d5d"));
		// 设置选中Tab文字的颜�? (这是我自定义的一个方�?)
		tabs.setSelectedTextColor(Color.parseColor("#5d5d5d"));
		// 取消点击Tab时的背景�?
		tabs.setTabBackground(0);
		tabs.setBackgroundColor(Color.WHITE);
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		private final String[] titles = { "相册", "推荐"};

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
					albumFragment = new AlbumFragment();
				return albumFragment;
			case 1:
					recommendFragment = new RecommendFragment();
				return recommendFragment;
			default:
				return null;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}

	private void setOverflowShowingAlways() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			menuKeyField.setAccessible(true);
			menuKeyField.setBoolean(config, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_album:
			Intent intentFromGallery = new Intent();
			if(getSDKVersionNumber()<19){
				intentFromGallery.setType("image/*");
				intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
			}else{
				intentFromGallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			}			
			startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
			break;
		case R.id.action_feed:
			/****判断是否绑定班级，若未绑定，不能进行插播请求****/
			if((MyApplication.cid.equals(null))||(MyApplication.kid.equals(null)))
			{  Dialog alertDialog = new AlertDialog.Builder(this).
		    setTitle("温馨提示！").
		    setMessage("在幼儿园班级相册中插播图片，需班级教师批准，请先在幼学堂中绑定幼儿园及班级后使用此功能！").
	//	    setIcon(R.drawable.logo).
		    setPositiveButton("前去绑定", new DialogInterface.OnClickListener() {
		     
		     @Override
		     public void onClick(DialogInterface dialog, int which) {
		      // TODO Auto-generated method stub
		    	/***判断若安装了幼学堂，就打开幼学堂，若未安装，则跳转到浏览器幼学堂下载页面*******/
		    	//已安装，打开程序，需传入参数包名："com.skype.android.verizon" 
		    	 if(AppJudger.isAppInstalled(ParentsMainActivity.this, "com.ctbri.youxt")){ 
		    	                 Intent i = new Intent(); 
		    	                 ComponentName cn = new ComponentName("com.ctbri.youxt", 
		    	                         "com.ctbri.youxt.activity.SplashActivity"); 
		    	                 i.setComponent(cn); 
		    	                 startActivityForResult(i, RESULT_OK);    
		    	             } 
		    	 //未安装，跳转至market下载该程序 
		    	 else { 
		    	                 Uri uri = Uri.parse("market://details?id=com.ctbri.youxt");//id为包名 
		    	                 Intent it = new Intent(Intent.ACTION_VIEW, uri); 
		    	                 startActivity(it); 
		    	             }

		     }
		    }).
		    setNegativeButton("取消", new DialogInterface.OnClickListener() {
		     
		     @Override
		     public void onClick(DialogInterface dialog, int which) {
		      // TODO Auto-generated method stub
		    	
		     }
		    }).
		    create();
		  alertDialog.show();
	        	
			}
		else{startActivity(new Intent(this, ParentRecordActivity.class));}
			break;
		case R.id.action_qrLogin:
			Log.d("qr","qrLogin");
			Toast.makeText(this, "go into qrlogin", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this, CaptureActivity.class);
			startActivityForResult(intent, QR_REQUEST_CODE);
			break;

		case R.id.action_takephoto:
			Intent intentFromCapture = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
					.fromFile(new File(Environment
							.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
			startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
			break;
		case R.id.action_exit:
			SPUtil sp = SPUtil.getInstance(this);
			sp.clear();
			startActivity(new Intent(this, LoginActivity.class));
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@SuppressWarnings("deprecation")
	public static int getSDKVersionNumber() {  
	    int sdkVersion;  
	    try {  
	        sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);  
	    } catch (NumberFormatException e) {  
	        sdkVersion = 0;  
	    }  
	    return sdkVersion;  
	}  
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_CANCELED) {
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				startPhotoZoom(Uri.fromFile(new File(Environment
						.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					Intent i = new Intent(ParentsMainActivity.this, EditPicActivity.class);
					i.putExtra("uri", imgUri);
					i.putExtra("type", EditPicActivity.ADD);
					startActivityForResult(i, requestCode);
				}
				break;
			case QR_REQUEST_CODE:
				String result = data.getExtras().getString("SCAN_RESULT");
				Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

				try {
					mQueue.add(new JsonObjectRequest(
							"http://login.youxt.cn/api/qrlogin/"+result, JsonRequest
							.loginRequest(MyApplication.userName, MyApplication.passWord),
							new Response.Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject result) {
									int state;
									String message;
									try {
										state = result.getInt("state");
										message = result.getString("message");
										if (state == 1) {
											JSONObject data = result.getJSONObject("data");
											Toast.makeText(ParentsMainActivity.this,
													"登陆成功", Toast.LENGTH_SHORT)
													.show();
										}
									} catch (JSONException e) {
										e.printStackTrace();
										Toast.makeText(ParentsMainActivity.this,
												e.getMessage(),
												Toast.LENGTH_SHORT).show();
									}
								}
							}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Toast.makeText(ParentsMainActivity.this,
									error.getMessage(),
									Toast.LENGTH_SHORT).show();
						}
					}));
				} catch (JSONException e) {
					e.printStackTrace();
				}


				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	@SuppressLint("SimpleDateFormat") 
	public void startPhotoZoom(Uri uri) {
		File f = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"今日之星");
		if (!f.exists())
			f.mkdirs();
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile = new File(f.getPath() + File.separator + "IMG_"
				+ timeStamp + ".jpg");
		imgUri = mediaFile.getPath();
		Log.d("uri:", imgUri);
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", true);
		intent.putExtra("aspectX", 16);
		intent.putExtra("aspectY", 9);
		intent.putExtra("outputX", 800);
		intent.putExtra("outputY", 450);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, RESULT_REQUEST_CODE);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);// 反注册EventBus
		
		}
	/*
	//为弹出窗口实现监听类
    private OnItemClickListener  itemsOnClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			switch(position){
				case 0:
					break;
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				default:
					break;
			}
		}
	};
	*/
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
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
	
}