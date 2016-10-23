package com.ctbri.staroftoday;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ctbri.staroftoday.adapter.TeacherRecordAdapter;
import com.ctbri.staroftoday.bean.InsertResult;
import com.ctbri.staroftoday.net.JsonParser;
import com.ctbri.staroftoday.net.JsonRequest;
import com.ctbri.staroftoday.utils.Constants;
import com.ctbri.staroftoday.utils.SPUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.umeng.analytics.MobclickAgent;
import de.greenrobot.event.EventBus;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class TeacherRecordActivity extends Activity  implements
OnClickListener,OnRefreshListener2<ListView>{
	
	private static final String IMAGE_FILE_NAME = "temp.jpg";	
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	public static final int ADD_REQUEST_CODE = 3;
	public static final int EDIT_REQUEST_CODE = 4;
	private String imgUri;
	private TeacherRecordAdapter ra;
	private PullToRefreshListView mPullRefreshListView;
	private RequestQueue mQueue;
	private LinkedList<InsertResult> rdlist;
	public  int getrecordstart =1;
	public PopupWindow  popupWindow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teacher_record);
		setOverflowShowingAlways();
		
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.my_actionbar);//自定义ActionBar布局
        TextView tittle=(TextView)actionBar.getCustomView().findViewById(R.id.tittle);
        Typeface typeFace =Typeface.createFromAsset(getAssets(),"fonts/Roboto-Black.ttf");
        tittle.setTypeface(typeFace);
        ImageButton bt_back=(ImageButton)actionBar.getCustomView().findViewById(R.id.back);
        bt_back.setOnClickListener(new OnClickListener() {//监听事件
            @Override
            public void onClick(View arg0) {         
            finish(); 
            }
        });		
		mPullRefreshListView = (PullToRefreshListView)findViewById(R.id.ptrlv_record);
		mPullRefreshListView.setMode(Mode.BOTH);
		mQueue = Volley.newRequestQueue(TeacherRecordActivity.this);
		rdlist=new LinkedList<InsertResult>();
		ra =new TeacherRecordAdapter(TeacherRecordActivity.this,rdlist);
		mPullRefreshListView.setOnRefreshListener(this);
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		
		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				//Toast.makeText(getActivity(), "End of List!", Toast.LENGTH_SHORT).show();
			}
		});
		
		actualListView.setAdapter(ra); 
		try {
			getRecordList(1,MyApplication.count);
			} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ra.notifyDataSetChanged();
	}
//**********
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		
		String label = DateUtils.formatDateTime(this, System.currentTimeMillis(),  
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);  

        // Update the LastUpdatedLabel  
        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);  

        // Do work to refresh the list here.  
        new GetDownDataTask().execute();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		
		String label = DateUtils.formatDateTime(this, System.currentTimeMillis(),  
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);  

        // Update the LastUpdatedLabel  
        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);  

        // Do work to refresh the list here.  
        new GetUpDataTask().execute();
	}
	
	private class GetUpDataTask extends AsyncTask<Void, Void, Void> {  
		  
        @Override  
        protected Void doInBackground(Void... params) {  
            try {  
                Thread.sleep(1000);  
            } catch (InterruptedException e) {  
            }   
            try {  
            	getRecordList(getrecordstart,MyApplication.count); 
            } catch (Exception e) {  
                // TODO: handle exception   
                return null;  
            }                
            return null;  
        }  
  
        @Override  
        protected void onPostExecute(Void result) {  
            try {               
            	ra.notifyDataSetChanged();  
                // Call onRefreshComplete when the list has been refreshed.    
            } catch (Exception e) {  
                // TODO: handle exception  
                Log.d("Debug", e.getMessage());  
            }             
            super.onPostExecute(result);  
        }  
    }  
      
	private class GetDownDataTask extends AsyncTask<Void, Void, Void> {  
		  
        @Override  
        protected Void doInBackground(Void... params) {  
            try {  
                Thread.sleep(1000);  
            } catch (InterruptedException e) {  
            }   
            try {  
            	rdlist.clear();
            	getrecordstart=1;
            	getRecordList(1,MyApplication.count); 
            } catch (Exception e) {  
                // TODO: handle exception   
                return null;  
            }                
            return null;  
        }  
  
        @Override  
        protected void onPostExecute(Void result) {  
            try {               
            	ra.notifyDataSetChanged();  
                // Call onRefreshComplete when the list has been refreshed.    
            } catch (Exception e) {  
                // TODO: handle exception  
                Log.d("Debug", e.getMessage());  
            }             
            super.onPostExecute(result);  
        }  
    } 
	private void getRecordList(int startId, int count) throws JSONException {
		Listener<JSONObject> listener = new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				int state = 0;
				try {
					state = response.getInt("state");
					switch (state) {
					case 1:
						getrecordstart=getrecordstart+response.getJSONArray("data").length();
							JsonParser.parseRecordResultList(response.getJSONArray("data"),
								rdlist);							
							ra.notifyDataSetChanged();	
						break;
					case 0:
						Toast.makeText(TeacherRecordActivity.this, response.getString("message"),
			              	     Toast.LENGTH_SHORT).show();
						break;
					default:
						break;
					}
						
				} catch (JSONException e) {
					e.printStackTrace();
					e.getMessage();
				}
				mPullRefreshListView.onRefreshComplete();
			}		
		};
		ErrorListener errorListener =new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Toast.makeText(TeacherRecordActivity.this, error.toString(), Toast.LENGTH_LONG).show();
			}
		};
		JsonObjectRequest json=new JsonObjectRequest(com.android.volley.Request.Method.POST, Constants.HOST+"getRequests", 
				JsonRequest.getMyRequest(MyApplication.uid, startId, count),
				listener, errorListener)
				{
			 	@Override
			 	public Map<String, String> getHeaders() throws AuthFailureError {
			    HashMap<String, String> headers = new HashMap<String, String>();
			    headers.put("Content-Type", "application/json");
			    headers.put("Charset", "UTF-8 ");
			    return headers;
			   }
		};
		mQueue.add(json);
	}
 //*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.record, menu);
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
		case android.R.id.home:
			finish();
			break;
		case R.id.action_album:
			Intent intentFromGallery = new Intent();
			intentFromGallery.setType("image/*");
			intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
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
					Intent i = new Intent(TeacherRecordActivity.this, EditPicActivity.class);
					i.putExtra("uri", imgUri);
					i.putExtra("type", EditPicActivity.ADD);
					startActivityForResult(i, requestCode);
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
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		default:
			break;
		}
	}
	//友盟*****************
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
	//*****************
	
    
}
