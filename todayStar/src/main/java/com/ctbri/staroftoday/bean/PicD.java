package com.ctbri.staroftoday.bean;

import java.util.ArrayList;

public class PicD{
	public String pictime;
	public ArrayList<Pics> pics;
	public PicD() {
		super();
	}
	public PicD(String pictime,ArrayList<Pics> pics){
		super();
		this.pictime=pictime;
		this.pics=pics;
	}
	public String getTime() {
		return pictime;
	}

	public void setTime(String id) {
		this.pictime = id;
	}
	
	public ArrayList<Pics> getInfo() {
		return pics;
	}

	public void setInfo(ArrayList<Pics> info) {
		this.pics = info;
	}

}
