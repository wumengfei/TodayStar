package com.ctbri.staroftoday;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;

import com.ctbri.staroftoday.EditPicActivity;
import com.ctbri.staroftoday.net.JsonRequest;
import com.ctbri.staroftoday.utils.Constants;
import com.ctbri.staroftoday.volley.cache.CombineCache;
import com.ctbri.staroftoday.volley.upload.MultipartRequest;
import com.ctbri.staroftoday.volley.upload.MultipartRequestParams;
import com.ctbri.staroftoday.R;
import com.umeng.analytics.MobclickAgent;


public class EditPicActivity extends Activity{
	private EditText et;
	private ImageView iv;
	private RequestQueue mQueue;
	private ImageLoader mImageLoader;
	public CombineCache mCombineCache;
	public ProgressDialog progressDialog = null;
	public static final int ADD = 0;
	public static final int EDIT = 1;
	private Bundle data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.myactionbar1);//�Զ���ActionBar����
        ImageButton bt_back=(ImageButton)actionBar.getCustomView().findViewById(R.id.back);
        TextView tittle=(TextView)actionBar.getCustomView().findViewById(R.id.tittle);
        Typeface typeFace =Typeface.createFromAsset(getAssets(),"fonts/Roboto-Black.ttf");
        tittle.setTypeface(typeFace);
        final ImageButton bt_send=(ImageButton)actionBar.getCustomView().findViewById(R.id.send);
		mQueue = Volley.newRequestQueue(this);
		mCombineCache=new CombineCache(this);
		mImageLoader = new ImageLoader(mQueue, mCombineCache);  
		setContentView(R.layout.activity_edit);
		et = (EditText) findViewById(R.id.et_activity_edit);	
		iv = (ImageView) findViewById(R.id.iv_activity_edit);
		bt_back.setOnClickListener(new OnClickListener() {//�����¼�
            @Override
            public void onClick(View arg0) {           
            	if(data.getInt("page")==1){
					Intent i = new Intent(EditPicActivity.this, ImagePagerActivity.class);
					i.putExtra("page", 2);
					startActivity(i);
					finish();
					}
				else{
					finish();
				}
            }
        });
		//b = (Button) findViewById(R.id.b_activity_edit);	
		data = getIntent().getExtras();
		String url=null;
		switch (data.getInt("type")) {
		case ADD:
			bt_send.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String info=et.getText().toString();
					bt_send.setEnabled(false);
					addPic(info);
				}
			});
			break;
		case EDIT:
			bt_send.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String id=data.getString("id");
					String info=et.getText().toString();
					bt_send.setEnabled(false);
					editPic(info,id);
				}
			});
			break;
		default:
			break;
		}
		
		if(data.getString("uri").startsWith("http"))
			url=data.getString("uri");
		else 
			url="file://"+data.getString("uri");
			
		mImageLoader.get(url, new ImageListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onResponse(ImageContainer response, boolean isImmediate) {
				iv.setImageBitmap(response.getBitmap());
			}
		});
		iv.setImageURI(Uri.parse(data.getString("uri")));
		super.onCreate(savedInstanceState);
		/*
		switch (data.getInt("type")) {
		case ADD:
			b.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String info=et.getText().toString();
					b.setEnabled(false);
					addPic(info);
				}
			});
			break;
		case EDIT:
			b.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String id=data.getString("id");
					String info=et.getText().toString();
					editPic(info,id);
				}
			});
			break;
		default:
			break;
		}
		*/
	}
	private void editPic(String info, String id ) {
		final String msg=info;
		Listener<JSONObject> listener = new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub	
				Toast.makeText(EditPicActivity.this, "修改描述成功",
                     Toast.LENGTH_SHORT).show();
				//startActivity(new Intent(EditPicActivity.this, MainActivity.class));
				goThrough(msg);
			}		
		};
		ErrorListener errorListener =new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Toast.makeText(EditPicActivity.this, "错误",
	              	     Toast.LENGTH_SHORT).show();
			}
		};
		JsonObjectRequest json=new JsonObjectRequest(Method.POST, Constants.HOST+"updatePic", JsonRequest.updatePicRequest(MyApplication.uid,id, info),
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
	protected void goThrough(String msg) {
		if(data.getInt("page")==1){
			Intent i = new Intent(EditPicActivity.this, ImagePagerActivity.class);
			ArrayList<String> A = new ArrayList<String>();
					A=data.getStringArrayList(ImagePagerActivity.EXTRA_IMAGE_INFOS);
			A.set(data.getInt(ImagePagerActivity.EXTRA_IMAGE_INDEX),msg);
			i.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, data.getStringArrayList(ImagePagerActivity.EXTRA_IMAGE_URLS));
			i.putExtra(ImagePagerActivity.EXTRA_IMAGE_IDS, data.getStringArrayList(ImagePagerActivity.EXTRA_IMAGE_IDS));
			i.putExtra(ImagePagerActivity.EXTRA_IMAGE_INFOS, A);
			i.putExtra(ImagePagerActivity.EXTRA_IMAGE_TYPS, data.getIntegerArrayList(ImagePagerActivity.EXTRA_IMAGE_TYPS));
			i.putExtra(ImagePagerActivity.EXTRA_IMAGE_DATES, data.getStringArrayList(ImagePagerActivity.EXTRA_IMAGE_DATES));
			i.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, data.getInt(ImagePagerActivity.EXTRA_IMAGE_INDEX));
			startActivity(i);
			finish();
			}
		else{
			finish();
		}
		
	}
	private void addPic(String info) {
		if (null != et.getText() && TextUtils.isEmpty(et.getText().toString()))
			et.getText().toString();	
		
		File file = new File(data.getString("uri"));
		String uri = Constants.HOST+"upload";	
		MultipartRequestParams params = new MultipartRequestParams();
		String model = Build.MODEL;
		params.put("uid", MyApplication.uid);
		params.put("info", info);
		params.put("file", file);
		params.put("psourcetype", model);
		
		mQueue.add(new MultipartRequest(Method.POST, params, uri, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Toast.makeText(EditPicActivity.this, "上传成功",
              	     Toast.LENGTH_SHORT).show();
				//startActivity(new Intent(EditPicActivity.this, MainActivity.class));
					finish();
			}
		}, new ErrorListener() {@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
			Log.d("test", "### error : " + error.toString()); 
			Toast.makeText(EditPicActivity.this, "错误",
             	     Toast.LENGTH_SHORT).show();
			}
		}));               
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
