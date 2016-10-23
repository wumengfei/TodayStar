package com.ctbri.staroftoday.fragment;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ctbri.staroftoday.MyApplication;
import com.ctbri.staroftoday.adapter.RecommendAdapter;
import com.ctbri.staroftoday.bean.RecommendData;
import com.ctbri.staroftoday.net.JsonParser;
import com.ctbri.staroftoday.net.JsonRequest;
import com.ctbri.staroftoday.utils.Constants;
import com.ctbri.staroftoday.volley.cache.CombineCache;
import com.ctbri.staroftoday.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.umeng.analytics.MobclickAgent;



/**
 */
@SuppressLint("InflateParams")
public class RecommendFragment extends Fragment implements
		OnRefreshListener2<ListView> {


	private PullToRefreshListView mPullRefreshListView;
	public LinkedList<RecommendData> rlist;
	private RecommendAdapter mAdapter;
	public CombineCache mCombineCache;
	
	private RequestQueue mQueue;
	private ImageLoader mImageLoader;
	private View rootView;
	private TextView nomore;
	private Handler mHandler;
	
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
        if(rootView==null){  
            rootView=inflater.inflate(R.layout.fragment_recommend,null);  
        }  
      ViewGroup parent = (ViewGroup) rootView.getParent();  
        if (parent != null) {  
            parent.removeView(rootView);  
        }   
        return rootView;  
    } 

	
	@SuppressLint("HandlerLeak")
	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mQueue = Volley.newRequestQueue(getActivity());
		mCombineCache=new CombineCache(getActivity());
		mImageLoader = new ImageLoader(mQueue, mCombineCache); 
		
		mPullRefreshListView = (PullToRefreshListView) getView().findViewById(R.id.ptrlv_recommend);
		
		nomore=(TextView) getView().findViewById(
				R.id.nomore);
		mPullRefreshListView.setMode(Mode.PULL_DOWN_TO_REFRESH);
		rlist=new LinkedList<RecommendData>();
		mAdapter=new RecommendAdapter(getActivity(),mQueue, mImageLoader, rlist);
		mPullRefreshListView.setAdapter(mAdapter);
		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView.setOnRefreshListener(this);

		// You can also just use mPullRefreshListFragment.getListView()
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		actualListView.setAdapter(mAdapter);
		/*
		try {
			getRecommendList(1,10);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mAdapter.notifyDataSetChanged();
		*/
		mHandler = new Handler()  
        {  
            public void handleMessage(Message msg)  
            {  
            	switch(msg.what){
            		case 1:
            			nomore.setVisibility(View.VISIBLE);
            			break;
            		case 2:
            			nomore.setVisibility(View.GONE);
            			break;
            		default:
            			break;
            	}
            }  
        };  
		}		
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		
		String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),  
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);  

        // Update the LastUpdatedLabel  
        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);  

        // Do work to refresh the list here.  
        new GetDataTask().execute(); 
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		
		String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),  
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);  

        // Update the LastUpdatedLabel  
        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);  

        // Do work to refresh the list here.  

	}
	
	private class GetDataTask extends AsyncTask<Void, Void, Void> {  
		  
        @Override  
        protected Void doInBackground(Void... params) {  
            // Simulates a background job.  
            try {  
                Thread.sleep(1000);  
            } catch (InterruptedException e) {  
            }   
            try {  
            	rlist.clear();
                getRecommendList(1,10);            
            } catch (Exception e) {  
                // TODO: handle exception   
                return null;  
            }                
            return null;  
        }  
  
        @Override  
        protected void onPostExecute(Void result) {  
  
            try {               
                mAdapter.notifyDataSetChanged();             
            } catch (Exception e) {  
                // TODO: handle exception  
                Log.d("Debug", e.getMessage());  
            }  
              
  
            super.onPostExecute(result);  
        }  
    }  
      
	private void getRecommendList(int startId, int count) throws JSONException {
		mQueue.add(new JsonObjectRequest(Constants.HOST+"getMyRecList", JsonRequest
				.getRecommendRequest(MyApplication.uid,startId,count), new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				int state = 0;
				 try {
					state = response.getInt("state");
					switch (state) {
					case 1:
						JsonParser.parseRecList(response.getJSONArray("PicData"),
								rlist);
						mAdapter.notifyDataSetChanged();
						if(rlist.size()==0){
							 Message message=new Message();  
			                 message.what=1;  
			                 mHandler.sendMessage(message);  
						}else{
							 Message message=new Message();  
			                 message.what=2;  
			                 mHandler.sendMessage(message); 
						}
						break;
					case 0:
						if(rlist.size()==0){
							 Message message=new Message();  
			                 message.what=1;  
			                 mHandler.sendMessage(message);  
						}
						break;
					default:
						Toast.makeText(getActivity(), response.getString("msg"),
			              	     Toast.LENGTH_SHORT).show();
						break;
					}
				} catch (JSONException e) {
					e.printStackTrace();
					e.getMessage();
				}
				mPullRefreshListView.onRefreshComplete();
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
	@Override
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("RecommendScreen"); //统计页面，"RecommendScreen"为页面名称，可自定义
	    try {
	    	rlist.clear();
			getRecommendList(1,10);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
		mAdapter.notifyDataSetChanged();
	}
	@Override
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("RecommendScreen"); 
	}
}
