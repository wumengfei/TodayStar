package com.ctbri.staroftoday.bean;


public class Insertinfo {
	public Long id;
	public Long pid;
	public Long tid;
	public Long gid;
	public String itime;
	public String remark;
	public Integer ishown;
	public Integer dbstate;
	public Insertinfo(Long id,Long pid,Long tid,Long gid,String itime,String remark,Integer ishown){
		super();
		this.id=id;
		this.pid=pid;
		this.tid=tid;
		this.gid=gid;
		this.itime=itime;
		this.remark=remark;
		this.ishown=ishown;
	}
	
}
