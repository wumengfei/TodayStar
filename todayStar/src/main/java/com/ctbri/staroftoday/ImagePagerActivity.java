package com.ctbri.staroftoday;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ctbri.staroftoday.fragment.ImageDetailFragment;
import com.ctbri.staroftoday.net.JsonRequest;
import com.ctbri.staroftoday.utils.Constants;
import com.ctbri.staroftoday.view.HackyViewPager;
import com.ctbri.staroftoday.R;
import com.umeng.analytics.MobclickAgent;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 图片查看
 */
public class ImagePagerActivity extends FragmentActivity {
	private static final String STATE_POSITION = "STATE_POSITION";
	public static final String EXTRA_IMAGE_INDEX = "image_index"; 
	public static final String EXTRA_IMAGE_URLS = "image_urls";
	public static final String EXTRA_IMAGE_IDS = "image_ids";
	public static final String EXTRA_IMAGE_INFOS = "image_infos";
	public static final String EXTRA_IMAGE_TYPS = "image_typs";
	public static final String EXTRA_IMAGE_DATES = "image_dates";
	public static final int  EDIT_REQUEST_CODE=0;
	public static final int  DELETE_STATE=1;
	public static  int deleteState;
	private HackyViewPager mPager;
	private int pagerPosition,deletePosition;
	private TextView indicator,image_date;
	private ArrayList<String> urls=new ArrayList<String>();
	private ArrayList<String> ids=new ArrayList<String>();
	private ArrayList<String> infos=new ArrayList<String>();
	private ArrayList<String> dates=new ArrayList<String>();
	private ArrayList<Integer> typs=new ArrayList<Integer>();
	private RequestQueue mQueue;
	private ImagePagerAdapter1 mAdapter;
	private Typeface face;
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		MobclickAgent.openActivityDurationTrack(false);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_detail_pager);
		mQueue= Volley.newRequestQueue(this);
		
		setOverflowShowingAlways();
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.my_actionbar);//�Զ���ActionBar����
        TextView tittle=(TextView)actionBar.getCustomView().findViewById(R.id.tittle);
        Typeface typeFace =Typeface.createFromAsset(getAssets(),"fonts/Roboto-Black.ttf");
        tittle.setTypeface(typeFace);
        ImageButton bt_back=(ImageButton)actionBar.getCustomView().findViewById(R.id.back);
        bt_back.setOnClickListener(new OnClickListener() {//�����¼�
            @Override
            public void onClick(View arg0) {         
            finish(); 
            }
        });
        
		RelativeLayout rl=(RelativeLayout)findViewById(R.id.bg);
		rl.setOnClickListener(new OnClickListener() {//�����¼�
            @Override
            public void onClick(View arg0) {         
            finish(); 
            }
        });
		
		pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);	
		deletePosition=pagerPosition;
		urls =getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);
		ids = getIntent().getStringArrayListExtra(EXTRA_IMAGE_IDS);
		infos = getIntent().getStringArrayListExtra(EXTRA_IMAGE_INFOS);
		typs = getIntent().getIntegerArrayListExtra(EXTRA_IMAGE_TYPS);
		dates= getIntent().getStringArrayListExtra(EXTRA_IMAGE_DATES);
		mPager = (HackyViewPager) findViewById(R.id.pager);
		mAdapter = new ImagePagerAdapter1(getSupportFragmentManager());//, urls);
		mPager.setAdapter(mAdapter);
		indicator = (TextView) findViewById(R.id.indicator);
		image_date=(TextView) findViewById(R.id.image_date);
		face=Typeface.createFromAsset (this.getAssets() , "fonts/Roboto-Regular.ttf" );
		indicator.setTypeface(face);
		image_date.setTypeface(face);
		indicator.setTypeface(face);
		image_date.setTypeface(face);
		indicator.setText(infos.get(pagerPosition));
		String ctime=dates.get(pagerPosition);
		ctime = ctime.substring(0,ctime.length()-3);
		String dtime=TimeStamp2Date(ctime);	
		String day=dtime.substring(8, 10);
		String month=dtime.substring(5, 7);
		String year=dtime.substring(0, 4);
		image_date.setText(year+"-"+month+"-"+day);
		//CharSequence text = getString(R.string.viewpager_indicator, 1, mPager.getAdapter().getCount());
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				//CharSequence text = getString(R.string.viewpager_indicator, arg0 + 1, mPager.getAdapter().getCount());
				deletePosition=arg0;
				indicator.setTypeface(face);
				image_date.setTypeface(face);
				indicator.setText(infos.get(arg0));
				String ctime=dates.get(arg0);
				ctime = ctime.substring(0,ctime.length()-3);
				String dtime=TimeStamp2Date(ctime);	
				String day=dtime.substring(8, 10);
				String month=dtime.substring(5, 7);
				String year=dtime.substring(0, 4);
				image_date.setText(year+"-"+month+"-"+day);
			}

		});
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		mPager.setCurrentItem(pagerPosition);
		deleteState=0;
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter1 extends FragmentStatePagerAdapter {

		//public ArrayList<String> fileList;

		public ImagePagerAdapter1(FragmentManager fm/*, ArrayList<String> fileList*/) {
			super(fm);
			//this.fileList = urls;
		}
		@Override
		public int getCount() {
			//return fileList == null ? 0 : fileList.size();
			return urls == null ? 0 : urls.size();
		}
		@Override
	    public int getItemPosition(Object object) {
			/*
			if(deleteState==DELETE_STATE){
				deleteState=0;
		        return POSITION_NONE;		        
			}else{
				return super.getItemPosition(object) ;
			}
			*/
			return POSITION_NONE;		  
	    }
		@Override
		public Fragment getItem(int position) {
			//String url = fileList.get(position);
			String url = urls.get(position);
			return ImageDetailFragment.newInstance(url);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.photo_menu, menu);
		return true;
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.clear();
		MenuInflater inflater = this.getMenuInflater();
		switch (typs.get(deletePosition)) {
		case 0:
			inflater.inflate(R.menu.photo_menu1, menu);
			break;
		default:
			inflater.inflate(R.menu.photo_menu, menu);
			break;
		}
		return super.onPrepareOptionsMenu(menu);
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
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.photo_cancel:
			try {				
				cancel(deletePosition);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			break;
		case R.id.photo_recommend:
			try {
				recommend(deletePosition);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			break;
		case R.id.photo_delete:
			delete(deletePosition);					
			break;
		case R.id.photo_delete1:
			delete(deletePosition);					
			break;
		case R.id.photo_edit:
			Intent i = new Intent(this, EditPicActivity.class);
			i.putExtra("uri", urls.get(deletePosition));
			i.putExtra("type", EditPicActivity.EDIT);
			i.putExtra("id", ids.get(deletePosition));
			i.putExtra("page", 1);
			i.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
			i.putExtra(ImagePagerActivity.EXTRA_IMAGE_IDS, ids);
			i.putExtra(ImagePagerActivity.EXTRA_IMAGE_INFOS, infos);
			i.putExtra(ImagePagerActivity.EXTRA_IMAGE_TYPS, typs);
			i.putExtra(ImagePagerActivity.EXTRA_IMAGE_DATES, dates);
			i.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, deletePosition);
			this.startActivity(i);		
			break;
		case R.id.photo_edit1:
			Intent i1 = new Intent(this, EditPicActivity.class);
			i1.putExtra("uri", urls.get(deletePosition));
			i1.putExtra("type", EditPicActivity.EDIT);
			i1.putExtra("id", ids.get(deletePosition));
			i1.putExtra("page", 1);
			i1.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
			i1.putExtra(ImagePagerActivity.EXTRA_IMAGE_IDS, ids);
			i1.putExtra(ImagePagerActivity.EXTRA_IMAGE_INFOS, infos);
			i1.putExtra(ImagePagerActivity.EXTRA_IMAGE_TYPS, typs);
			i1.putExtra(ImagePagerActivity.EXTRA_IMAGE_DATES, dates);
			i1.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, deletePosition);
			this.startActivity(i1);		
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	private void cancel(int pos) throws JSONException {
		// TODO Auto-generated method stub
		//String url=urls.get(pos);
		String id=ids.get(pos);
		typs.set(pos,0);
		mQueue.add(new JsonObjectRequest(Constants.HOST+"cancelRec", JsonRequest
				.cancelRecommendRequest(id,MyApplication.uid), new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// TODO
				Log.d("RES","ȡ���Ƽ�="+response.toString());
				int state = 0;
				try {
					state = response.getInt("state");
					switch (state) {
					case 1:
						Toast.makeText(ImagePagerActivity.this, "取消推荐成功", Toast.LENGTH_SHORT)
						.show();
						break;
					default:
						response.getString("message");
						break;
					}
				} catch (JSONException e) {
					e.printStackTrace();
					e.getMessage();
				}	
			}
		}, null));
	}
	//****************************
	private void recommend(int pos) throws JSONException {
		// TODO Auto-generated method stub
		typs.set(pos,2);
		mQueue.add(new JsonObjectRequest(Constants.HOST+"addRec", JsonRequest
				.addRecommendRequest(ids.get(pos),MyApplication.uid), new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				Log.d("RES","����="+response.toString());
				int state = 0;
				try {
					state = response.getInt("state");
					switch (state) {
					case 1:
						Toast.makeText(ImagePagerActivity.this, "推荐成功", Toast.LENGTH_SHORT)
						.show();
						break;
					default:
						response.getString("message");
						break;
					}
				} catch (JSONException e) {
					e.printStackTrace();
					e.getMessage();
				}			
			}
		}, null){
			@Override
		    public Map<String, String> getHeaders() throws AuthFailureError
		    {
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Charset", "UTF-8");
			headers.put("Content-Type", "application/json");
			return headers;
		    }
		});
	}
	//***************************
	private void delete(int pos) {
		// TODO Auto-generated method stub
		String id=ids.get(pos);
		urls.remove(pos);		
		ids.remove(pos);
		infos.remove(pos);
		typs.remove(pos);
		deleteState=1;
		if(typs.size()==0){
			finish();
		}else if(pos>0){
			deletePosition=pos-1;
			mAdapter.notifyDataSetChanged();
			mPager.setCurrentItem(deletePosition);
			
		}else{
			mAdapter.notifyDataSetChanged();
			mPager.setCurrentItem(deletePosition);
			
		}
		mQueue.add(new JsonObjectRequest(Constants.HOST+"deletePic", JsonRequest
				.deletedPicRequest(MyApplication.uid,id), new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				Log.d("Debug",response.toString());
				int state = 0;
				try {
					state = response.getInt("state");
					switch (state) {
					case 1:					
						Toast.makeText(ImagePagerActivity.this, "照片删除成功", Toast.LENGTH_SHORT)
						.show();
						break;
					default:
						response.getString("message");
						break;
					}
				} catch (JSONException e) {
					e.printStackTrace();
					e.getMessage();
				}
				
			}
		}, null){
			@Override
		    public Map<String, String> getHeaders() throws AuthFailureError
		    {
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Charset", "UTF-8");
			headers.put("Content-Type", "application/json");
			return headers;
		    }
		});
	}
	//**************************
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
	@SuppressLint("SimpleDateFormat") 
	public static String TimeStamp2Date(String timestampString){    
		String re_StrTime = null;  	  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
		long lcc_time = Long.valueOf(timestampString);  
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));  		  
		return re_StrTime;    		   
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
