package com.ctbri.staroftoday;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
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
import com.ctbri.staroftoday.fragment.FrontPageFragment;
import com.ctbri.staroftoday.fragment.RecommendFragment;
import com.ctbri.staroftoday.net.JsonRequest;
import com.ctbri.staroftoday.utils.Constants;
import com.ctbri.staroftoday.utils.SPUtil;
import com.ctbri.staroftoday.view.PagerSlidingTabStrip;
import com.ctbri.staroftoday.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import info.vividcode.android.zxing.CaptureActivity;

/**

 */
public class TeacherMainActivity extends FragmentActivity implements
OnClickListener  {

	private AlbumFragment albumFragment;
	private FrontPageFragment frontpageFragment;
	private RecommendFragment recommendFragment;
	private PagerSlidingTabStrip tabs;
	private DisplayMetrics dm;
	private RequestQueue mQueue;
	private static final String IMAGE_FILE_NAME = "temp.jpg";	
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	public static final int ADD_REQUEST_CODE = 3;
	public static final int EDIT_REQUEST_CODE = 4;
	public static final int QR_REQUEST_CODE = 5;
	private String imgUri;
	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setOverflowShowingAlways();
		mQueue = Volley.newRequestQueue(this);
		dm = getResources().getDisplayMetrics();
		ViewPager pager = (ViewPager) findViewById(R.id.id_viewpager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		tabs.setViewPager(pager);
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.myactionbar2);//自定义ActionBar布局
        TextView tittle=(TextView)actionBar.getCustomView().findViewById(R.id.tittle1);
        Typeface typeFace =Typeface.createFromAsset(getAssets(),"fonts/Roboto-Black.ttf");
        tittle.setTypeface(typeFace);
		setTabsValue();
		
		 if((MyApplication.cid.equals(null))||(MyApplication.kid.equals(null)))
			{
	        	Toast toast = Toast.makeText(getApplicationContext(),"tishi!!!!", Toast.LENGTH_LONG);  
	        	toast.setGravity(Gravity.CENTER, 0, 0);   
	        	toast.show();  
			}
		
	}

	
	private void setTabsValue() {
		tabs.setShouldExpand(true);
		tabs.setDividerColor(Color.WHITE);
		tabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 1, dm));
		tabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_PX, 3, dm));
		tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_PX, 35, dm));
		tabs.setTypeface(Typeface.createFromAsset (this.getAssets() , "fonts/Roboto-Regular.ttf" ), 0);
		tabs.setIndicatorColor(Color.parseColor("#5d5d5d"));
		tabs.setSelectedTextColor(Color.parseColor("#5d5d5d"));
		tabs.setTabBackground(0);
		tabs.setBackgroundColor(Color.WHITE);
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		private final String[] titles = { "家长推荐", "我的相册","我的推荐"};

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
				if (frontpageFragment == null) {
					frontpageFragment = new FrontPageFragment();
				}
				return frontpageFragment;
			case 1:
				if (albumFragment == null) {
					albumFragment = new AlbumFragment();
				}
				return albumFragment;
			case 2:
				if (recommendFragment == null) {
					recommendFragment = new RecommendFragment();
				}
				return recommendFragment;
			default:
				return null;
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
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
		case R.id.action_feed1:
			startActivity(new Intent(this, TeacherRecordActivity.class));
			break;
		case R.id.action_takephoto:
			Intent intentFromCapture = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
					.fromFile(new File(Environment
							.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
			startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
			break;
		case R.id.action_qrLogin:
			Log.d("qr", "qrLogin");
			Intent intent = new Intent(this, CaptureActivity.class);
			startActivityForResult(intent, QR_REQUEST_CODE);
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
					Intent i = new Intent(TeacherMainActivity.this, EditPicActivity.class);
					i.putExtra("uri", imgUri);
					i.putExtra("type", EditPicActivity.ADD);
					i.putExtra("page", 0);
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
											Toast.makeText(TeacherMainActivity.this,
													"登陆成功", Toast.LENGTH_SHORT)
													.show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(TeacherMainActivity.this,
                                                e.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(TeacherMainActivity.this,
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
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", true);
		/*灏嗘瘮渚嬭涓�16:9锛�
		intent.putExtra("aspectX", 16);
		intent.putExtra("aspectY", 9);
		intent.putExtra("outputX", 800);
		intent.putExtra("outputY", 450);
		intent.putExtra("scale", true);
		*/
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, RESULT_REQUEST_CODE);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);// 鍙嶆敞鍐孍ventBus
		
		}
	/*
	//涓哄脊鍑虹獥鍙ｅ疄鐜扮洃鍚�??
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