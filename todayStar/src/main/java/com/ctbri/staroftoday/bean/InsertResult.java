package com.ctbri.staroftoday.bean;

public class InsertResult {
	public String parentname;
	public String inserttime;
	public Insertinfo insertinfo;
	public InsertResult(String parentname,String inserttime,Insertinfo insertinfo){
		super();
		this.parentname=parentname;
		this.inserttime=inserttime;
		this.insertinfo=insertinfo;
	}
}
