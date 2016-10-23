package com.ctbri.staroftoday.fragment;


import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ctbri.staroftoday.MyApplication;
import com.ctbri.staroftoday.adapter.FrontPageAdapter;
import com.ctbri.staroftoday.bean.GroupData;
import com.ctbri.staroftoday.net.JsonParser;
import com.ctbri.staroftoday.net.JsonRequest;
import com.ctbri.staroftoday.utils.Constants;
import com.ctbri.staroftoday.volley.cache.CombineCache;
import com.ctbri.staroftoday.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;


public class FrontPageFragment extends Fragment implements
		OnRefreshListener2<ListView> {
	private PullToRefreshListView mPullRefreshListView;
	public LinkedList<GroupData> flist ;
	private FrontPageAdapter fpa;
	private RequestQueue mQueue;
	private ImageLoader mImageLoader;
	public CombineCache mCombineCache;
	private int getgroupstart =1;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_frontpage, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mQueue = Volley.newRequestQueue(getActivity());
		mCombineCache=new CombineCache(getActivity());
		mImageLoader = new ImageLoader(mQueue, mCombineCache);  
		mPullRefreshListView = (PullToRefreshListView) getView().findViewById(
				R.id.ptrlv_frontpage);
		mPullRefreshListView.setMode(Mode.BOTH);
		flist=new LinkedList<GroupData>();	
		fpa = new FrontPageAdapter(getActivity(),mQueue, mImageLoader,flist);
		
		mPullRefreshListView.setOnRefreshListener(this);
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		
		actualListView.setAdapter(fpa); 
		/*
		try {
			getGroupList(1,MyApplication.count);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fpa.notifyDataSetChanged();
		*/
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
            	getGroupList(getgroupstart,MyApplication.count); 
            } catch (Exception e) {  
                // TODO: handle exception   
                return null;  
            }                
            return null;  
        }  
  
        @Override  
        protected void onPostExecute(Void result) {  
            //在头部增加新添内容  
              
            try {                             
                fpa.notifyDataSetChanged();  
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
            	flist.clear();
            	getgroupstart=1;
            	getGroupList(1,MyApplication.count); 
            } catch (Exception e) {  
                // TODO: handle exception   
                return null;  
            }                
            return null;  
        }  
  
        @Override  
        protected void onPostExecute(Void result) {  
            //在头部增加新添内容  
              
            try {                             
                fpa.notifyDataSetChanged();  
            } catch (Exception e) {  
                // TODO: handle exception  
                Log.d("Debug", e.getMessage());  
            }  
              
  
            super.onPostExecute(result);  
        }  
    }  
	private void getGroupList( int start,  int count  ) throws JSONException {
		mQueue.add(new JsonObjectRequest(Constants.HOST+"getRecommendList", JsonRequest
				.getGroupRequest(MyApplication.uid,start,count), new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				int state = 0;
				Log.d("RES", response.toString());
				try {
					state = response.getInt("state");
					switch (state) {
					case 1:
						getgroupstart=getgroupstart+response.getJSONArray("groupData").length();
						JsonParser.parseGroupList(response.getJSONArray("groupData"),
								flist);
						fpa.notifyDataSetChanged();	
					break;
					case 0:
						Toast.makeText(getActivity(), response.getString("msg"),
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
		}, null));
	}

	@Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("FrontPageScreen"); //统计页面，"AlbumScreen"为页面名称，可自定义
        try {
        	flist.clear();
			getGroupList(1,MyApplication.count);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fpa.notifyDataSetChanged();
    }
	@Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("FrontPageScreen"); 
    }

}
