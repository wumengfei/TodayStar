package com.ctbri.staroftoday.bean;

public class PicData {
	public String id;
	private String info; // Í¼Æ¬ÄÚÈÝ  
    private String url; // Í¼Æ¬url  
    private String ctime; // Í¼Æ¬Ê±¼ä  
    private int ishown ;
    public PicData() {
		super();
	}
    
	
	public PicData(String id,String info,
			String url,String time,int ishown) {
		super();
		this.id = id;
		this.info = info;
		this.url = url;
		this.ctime = time;
		this.ishown=ishown;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public int getIshown() {
		return ishown;
	}

	public void setIshown(int ishown) {
		this.ishown = ishown;
	}
	
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getTime() {
		return ctime;
	}

	public void setTime(String time) {
		this.ctime = time;
	}

}
