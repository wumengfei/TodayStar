package com.ctbri.staroftoday.fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ctbri.staroftoday.EditPicActivity;
import com.ctbri.staroftoday.MyApplication;
import com.ctbri.staroftoday.adapter.AlbumAdapter;
import com.ctbri.staroftoday.bean.PicD;
import com.ctbri.staroftoday.net.JsonParser;
import com.ctbri.staroftoday.net.JsonRequest;
import com.ctbri.staroftoday.utils.Constants;
import com.ctbri.staroftoday.view.PopupWindow1;
import com.ctbri.staroftoday.volley.cache.CombineCache;
import com.ctbri.staroftoday.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

public class AlbumFragment extends Fragment implements OnClickListener,
		OnRefreshListener2<ListView> {
	
	private static final String IMAGE_FILE_NAME = "temp.jpg";
	
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	public static final int ADD_REQUEST_CODE = 3;
	public static final int EDIT_REQUEST_CODE = 4;
	private AlbumAdapter aa;
	private PullToRefreshListView mPullRefreshListView;
	public  ArrayList<PicD> alist;
	private RequestQueue mQueue;
	private ImageLoader mImageLoader;
	public CombineCache mCombineCache;
	private String imgUri;
	public  int getpicstart =1;
	public PopupWindow1 puw;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_album, container, false); 
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mQueue = Volley.newRequestQueue(getActivity());
		mCombineCache=new CombineCache(getActivity());
		mImageLoader = new ImageLoader(mQueue, mCombineCache);  
		
		FloatingActionButton button = (FloatingActionButton)getView().findViewById(R.id.pink_icon);
		button.setOnClickListener(this);
		
		mPullRefreshListView = (PullToRefreshListView) getView().findViewById(
				R.id.ptrlv_album);
		mPullRefreshListView.setMode(Mode.BOTH);
		alist=new ArrayList<PicD>();		
		aa =new AlbumAdapter(getActivity(),mQueue, mImageLoader,alist);		
		mPullRefreshListView.setOnRefreshListener(this);
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		
		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				//Toast.makeText(getActivity(), "End of List!", Toast.LENGTH_SHORT).show();
			}
		});
		
		actualListView.setAdapter(aa); 
		/*
		try {
			getPicList(1,MyApplication.count);
			} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		aa.notifyDataSetChanged();
		*/
	}
	
	@SuppressLint("InflateParams")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pink_icon:
			puw = new PopupWindow1(getActivity(), itemsOnClick);
			//��ʾ����
			puw.showAtLocation(getActivity().findViewById(R.id.main), Gravity.CENTER, 0, 0); //����layout��PopupWindow����ʾ��λ��
			break;
		default:
			break;
		}
	}
	 
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		
		String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),  
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);  

        // Update the LastUpdatedLabel  
        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);  

        // Do work to refresh the list here.  
        new GetDownDataTask().execute();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		
		String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),  
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
            	getPicList(getpicstart,MyApplication.count); 
            } catch (Exception e) {  
                // TODO: handle exception   
                return null;  
            }                
            return null;  
        }  
  
        @Override  
        protected void onPostExecute(Void result) {  
            try {               
            	aa.notifyDataSetChanged();  
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
            	alist.clear();
            	getPicList(1,MyApplication.count); 
            } catch (Exception e) {  
                // TODO: handle exception   
                return null;  
            }                
            return null;  
        }  
  
        @Override  
        protected void onPostExecute(Void result) {  
            try {               
            	aa.notifyDataSetChanged();  
                // Call onRefreshComplete when the list has been refreshed.    
            } catch (Exception e) {  
                // TODO: handle exception  
                Log.d("Debug", e.getMessage());  
            }             
            super.onPostExecute(result);  
        }  
    } 
	
	private void getPicList(int startId, int count) throws JSONException {
		Listener<JSONObject> listener = new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				int state = 0;
				try {
					state = response.getInt("state");
					switch (state) {
					case 1:
							getpicstart=response.getInt("nextstart");
							JsonParser.parsePicList(response.getJSONArray("PicData"),
								alist);
							aa.notifyDataSetChanged();	
						break;
					case 0:
						Toast.makeText(getActivity(), "没有更多图片",
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
				Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_LONG).show();
			}
		};
		JsonObjectRequest json=new JsonObjectRequest(Method.POST, Constants.HOST+"getMyAblum", 
				JsonRequest.getPicRequest(MyApplication.uid, startId, count),
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
					Intent i = new Intent(getActivity(), EditPicActivity.class);
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
				"����֮��");
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
	//Ϊ��������ʵ�ּ�����
    private OnClickListener  itemsOnClick = new OnClickListener(){

		public void onClick(View v) {
			puw.dismiss();
			switch (v.getId()) {
			case R.id.btn_pick_photo:
				Intent intentFromGallery = new Intent();
				if(getSDKVersionNumber()<19){
					intentFromGallery.setType("image/*");
					intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
				}else{
					intentFromGallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				}			
				getActivity().startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
				break;
			case R.id.btn_take_photo:	
				Intent intentFromCapture = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
						.fromFile(new File(Environment
								.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
				getActivity().startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
				break;
			default:
				break;
			}
			
				
		}
    	
    };
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
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AlbumScreen"); //ͳ��ҳ�棬"AlbumScreen"Ϊҳ����ƣ����Զ���
        try {
        	alist.clear();
			getPicList(1,MyApplication.count);
			} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		aa.notifyDataSetChanged();
    }
	@Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AlbumScreen"); 
    }
}
