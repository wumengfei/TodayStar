package com.ctbri.staroftoday.net;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;



import com.ctbri.staroftoday.bean.GroupData;
import com.ctbri.staroftoday.bean.InsertResult;
import com.ctbri.staroftoday.bean.PicD;
import com.ctbri.staroftoday.bean.PicData;
import com.ctbri.staroftoday.bean.Pics;
import com.ctbri.staroftoday.bean.RecommendData;
import com.ctbri.staroftoday.bean.Insertinfo;
import com.ctbri.staroftoday.bean.UserData;
import com.ctbri.staroftoday.utils.Constants;

public class JsonParser {

	public static String parse(JSONObject data) {
		int state = 0;
		String message = null;
		try {
			state = data.getInt("state");
			switch (state) {
			case 0:

				break;
			default:
				message = data.getString("message");
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			message = e.getMessage();
		}
		return message;
	}
	public static void parseGroupList(JSONArray array,LinkedList<GroupData> list)
			throws JSONException {		
			for (int i = 0; i < array.length(); i++) {
				GroupData pd=parseGroupData(array.getJSONObject(i));
				list.add(pd);
				}
		}

	public static GroupData parseGroupData(JSONObject data) {
		GroupData gd = new GroupData();
		try {
			JSONObject group=data.getJSONObject("group");
			String gid=group.getString("gid");
			String ctime=group.getString("createdate"); 
			String day=ctime.substring(8, 10);
			String month=ctime.substring(5, 7);
			
			JSONObject userData=data.getJSONObject("userData");
			
			UserData ud=new UserData();
			ud.setId(userData.getString("uid"));
			ud.setname(userData.getString("realName"));

			
			JSONArray picData=data.getJSONArray("picData");
			ArrayList<PicData> pic= new ArrayList<PicData>();
			for (int i = 0; i < picData.length(); i++) {
				PicData pd=parsePicData(picData.getJSONObject(i));
				pic.add(pd);
			}
			
			gd = new GroupData( gid, day,  month, ud.getId(),
		    		ud.getname(), ud.getHeadpic(), pic);
		    
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("Json parse error"); 
			e.printStackTrace();
		}
		return gd;
	}
	public static PicData parsePicData(JSONObject data) {
		PicData pd = null;
		try {
			String id=data.getString("id");
			String ctime=data.getString("ctime"); 
			String info=data.getString("pname");   
			String purl=data.getString("purl");
			purl=Stringinsert(purl,"/",11);
		    String url=Constants.POST+purl;  
		    int ishown=data.getInt("ishown");
		    pd = new PicData(id,info,url,ctime,ishown);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("Json parse error"); 
			e.printStackTrace();
		}
		return pd;
		
	}
	public static void parsePicList(JSONArray array,ArrayList<PicD> alist)
			throws JSONException {		
			for (int i = 0; i < array.length(); i++) {
				PicD pd=parsePicD(array.getJSONObject(i));
				alist.add(pd);
			}
			
		}
		

	
	private static PicD parsePicD(JSONObject jsonObject) throws JSONException  {
		// TODO Auto-generated method stub
		PicD pd = null;
		try {
			String pictime=jsonObject.getString("pictime");
			JSONArray array1=jsonObject.getJSONArray("pics");
			ArrayList<Pics> pics=new ArrayList<Pics>();
			for (int i = 0; i < array1.length(); i++) {
				Pics ps=parsePics(array1.getJSONObject(i));
				pics.add(ps);
			}
		    pd = new PicD(pictime,pics);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("Json parse error"); 
			e.printStackTrace();
		}
		return pd;
	}

	private static Pics parsePics(JSONObject jsonObject) throws JSONException{
		Pics pd = null;
		try {
			String ctime=jsonObject.getString("ctime");
			String id=jsonObject.getString("id");
			String purl=jsonObject.getString("purl");
			purl=Stringinsert(purl,"/",11);
		    purl=Constants.POST+purl;  
			String pname=jsonObject.getString("pname");
			String uid=jsonObject.getString("uid");
			Integer ishown=jsonObject.getInt("ishown");
		    pd = new Pics(ctime,id,purl,pname,uid,ishown);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("Json parse error"); 
			e.printStackTrace();
		}
		return pd;
	}

	public static void parseRecList(JSONArray array,LinkedList<RecommendData> list)
			throws JSONException {
		
		for (int i = 0; i < array.length(); i++) {
			RecommendData rd=parseRecommendData(array.getJSONObject(i));
			list.addFirst(rd);
		}
		
	}

	public static RecommendData parseRecommendData(JSONObject data) {
		RecommendData rd = null;
		try {
			String id=data.getString("id");
			String ctime=data.getString("ctime"); 
			String info=data.getString("pname");   
			String purl=data.getString("purl");
			purl=Stringinsert(purl,"/",11);
		    String url=Constants.POST+purl;   
		    rd = new RecommendData(id,ctime,info,url);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("Json parse error"); 
			e.printStackTrace();
		}
		return rd;
		
	}
	
	@SuppressLint("SimpleDateFormat") 
	public static String TimeStamp2Date(String timestampString){    
		String re_StrTime = null;  	  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
		long lcc_time = Long.valueOf(timestampString);  
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));  		  
		return re_StrTime;    		   
	}  
	@SuppressLint("SimpleDateFormat") 
	public static String TimeStamp2Date1(String timestampString){    
		String re_StrTime = null;  	  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
		long lcc_time = Long.valueOf(timestampString);  
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));  		  
		return re_StrTime;    		   
	}  
/*
	public static void parseAdList(JSONArray array,
			List<AdRecommandResourcePackage> list) throws JSONException {
		for (int i = 0; i < array.length(); i++) {
			AdRecommandResourcePackage arrp = new AdRecommandResourcePackage();
			parseAdData(array.getJSONObject(i), arrp);
			list.add(arrp);
		}
	}

	public static void parseAdData(JSONObject data,
			AdRecommandResourcePackage arrp) {

	}
	*/
	public static String Stringinsert(String a,char b,int t){ 
		return a.substring(0,t)+b+a.substring(t,a.length());
		}
	public static String Stringinsert(String a,String b,int t){ 
		return a.substring(0,t)+b+a.substring(t,a.length());
		}
	public static void parseRecordList(JSONArray array,
			LinkedList<Insertinfo> rdlist) throws JSONException{
		for (int i = 0; i < array.length(); i++) {
			Insertinfo pd=parseRecord(array.getJSONObject(i));
			rdlist.add(pd);
		}		
	} 
	private static Insertinfo parseRecord(JSONObject data) {
		Insertinfo pd = null;
		try {
			String ctime=data.getString("itime"); 
			ctime = ctime.substring(0,ctime.length()-3);
			String dtime=TimeStamp2Date1(ctime);	
			dtime=dtime.substring(0, 4)+"-"+dtime.substring(5,7)+"-"+dtime.substring(8,10);
			String remark=data.getString("remark");
			Long id=data.getLong("id");
			Long pid=data.getLong("pid");
			Long tid=data.getLong("tid");
			Long gid=data.getLong("gid");
			Integer ishown=data.getInt("ishown");
		    pd = new Insertinfo(id,pid,tid,gid,dtime,remark,ishown);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("Json parse error"); 
			e.printStackTrace();
		}
		return pd;
	}
	public static void parseRecordResultList(JSONArray array,
			LinkedList<InsertResult> rdlist)  throws JSONException{
			for (int i = 0; i < array.length(); i++) {
				InsertResult pd=parseRecordResult(array.getJSONObject(i));
				rdlist.add(pd);
			}		
		
	}
	private static InsertResult parseRecordResult(JSONObject data) {
		// TODO Auto-generated method stub
		InsertResult pd = null;
		try {
			String parentname=data.getString("parentname");
			String inserttime=data.getString("inserttime");
			Insertinfo insertinfo=parseRecord(data.getJSONObject("insertinfo"));
		    pd = new InsertResult(parentname,inserttime,insertinfo);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("Json parse error"); 
			e.printStackTrace();
		}
		return pd;
	}
}
