package com.ctbri.staroftoday.net;

import java.io.File;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonRequest {
	public static JSONObject getAdRequest() {
		JSONObject data = new JSONObject();
		return data;
	}

	public static JSONObject getGroupRequest() {
		JSONObject data = new JSONObject();
		return data;
	}
	public static JSONObject denyPlayRequest(String gid) throws JSONException {
		JSONObject data = new JSONObject();
		data.put("gid", gid);
		return data;
	}
	
	public static JSONObject insertPlayRequest(String gid,String insertdate) throws JSONException {
		JSONObject data = new JSONObject();
		data.put("gid", gid);
		data.put("insertdate", insertdate);
		return data;
	}
	public static JSONObject insertRecordRequest(String uid,String cid,String insertdate,String remark) throws JSONException {
		JSONObject data = new JSONObject();
		data.put("cid", cid);
		data.put("uid", uid);
		data.put("remark", remark);
		data.put("insertdate", insertdate);
		return data;
	}
	public static JSONObject getGroupRequest(String userId,int start, int count) throws JSONException {
		JSONObject data = new JSONObject();
		data.put("userId",userId);
		data.put("start", start);
		data.put("count", count);
		return data;
	}
	public static JSONObject getPicRequest(String userId,int start, int count) throws JSONException {
		JSONObject data = new JSONObject();
		data.put("userId",userId);
		data.put("start", start);
		data.put("count", count);
		return data;
	}

	public static JSONObject getRecommendRequest(String userId,int start, int count) throws JSONException {
		JSONObject data = new JSONObject();
		data.put("userId",userId);
		data.put("start", start);
		data.put("count", count);
		return data;
	}
	
	public static JSONObject addRecommendRequest(String picId,String userId) throws JSONException {
		JSONObject data = new JSONObject();
		data.put("userId",userId);
		data.put("picId",picId);
		
		return data;
	}
	
	public static JSONObject cancelRecommendRequest(String picId,String userId) throws JSONException {
		JSONObject data = new JSONObject();
		data.put("userId",userId);
		data.put("picId",picId);
		return data;
	}
	
	public static JSONObject deletedPicRequest(String userId,String picId) {
		HashMap<String, String>map=new HashMap<String, String>();
		map.put("userId", userId);
		map.put("picId", picId);
		JSONObject data = new JSONObject(map);
		return data;
	}

	public static JSONObject uploadPicRequest(String userId,File file,String info) throws JSONException {
		JSONObject data = new JSONObject();
		data.put("uid",userId);
		data.put("data", file);
		data.put("info", info);
		return data;
	}
	
	public static JSONObject updatePicRequest(String userId,String picid,String info) {
		HashMap<String, String>map=new HashMap<String, String>();
		map.put("userId", userId);
		map.put("picId", picid);
		map.put("info", info);
		JSONObject data = new JSONObject(map);
		return data;
	}

	
	public static JSONObject loginRequest(String userName, String password)
			throws JSONException {
		JSONObject data = new JSONObject();
		data.put("username", userName);
		data.put("password", password);
		return data;
	}

	public static JSONObject editPicRequest(String info) {
		JSONObject data = new JSONObject();
		//data.put(name, value)
		return data;
	}

	public static JSONObject updateVersion() throws JSONException {
		JSONObject data = new JSONObject();
		return data;
	}

	public static JSONObject getMyRequest(String uid, int start, int count) throws JSONException{
		JSONObject data = new JSONObject();
		data.put("userId", uid);
		data.put("start", start);
		data.put("count", count);
		return data;
	}
	//验证码
	public static JSONObject validateCode(String vCode, String accountNum, String registerType) throws JSONException{
		JSONObject data = new JSONObject();
		data.put("validateCode", vCode);
		data.put("accountNumber", accountNum);
		data.put("registerType", registerType);
		return data;
	}

	public static JSONObject insertPlayRequest1(Long gid, String itime)throws JSONException {
		JSONObject data = new JSONObject();
		data.put("gid", gid);
		data.put("insertdate", itime);
		return data;
	}
}
