package com.ctbri.staroftoday.bean;

import java.util.ArrayList;


/**
 * @author liucong
 * @ 2015-12-30 下午8:54:47
 * @ description:群组实体
 */
public class GroupData {
	private String id;
	private String day = "9";
	private String month = "9";
	private String userId;
	private String username = "习大大";
	private String userHeadPic = "";
	private ArrayList<PicData> pics;
	
	public GroupData() {
		super();
	}
    
	public GroupData(String id,String day, String month,String userId,
			String username,String userHeadPic,ArrayList<PicData> pics) {
		super();
		this.id = id;
		this.day = day;
		this.month = month;
		this.userId = userId;
		this.username = username;
		this.userHeadPic = userHeadPic;
		this.pics = pics;
	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUserHeadPic() {
		return userHeadPic;
	}

	public void setUserHeadPic(String userHeadPic) {
		this.userHeadPic = userHeadPic;
	}
	
	
	public ArrayList<PicData> getPic() {
		return pics;
	}

	public void setPic(ArrayList<PicData> pics) {
		this.pics = pics;
	}
}
