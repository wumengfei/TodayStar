package com.ctbri.staroftoday.bean;

public class Pics{
	public String ctime;
	public String id;
	public String purl;
	public String pname;
	public String uid;
	public Integer ishown;
	public Pics(String ctime,String id,String purl,String pname,String uid,Integer ishown){
		super();
		this.ctime=ctime;
		this.id=id;
		this.uid=uid;
		this.purl=purl;
		this.pname=pname;
		this.ishown=ishown;	
	}
}