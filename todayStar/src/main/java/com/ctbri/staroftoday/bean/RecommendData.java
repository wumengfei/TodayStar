package com.ctbri.staroftoday.bean;

/**
 * @author liucong
 * @ 2015-12-30 ����8:55:14
 * @ description:�Ƽ�ʵ��
 */
public class RecommendData {
	public String id;
	private String ctime; // ����ʱ��
	private String info; // ͼƬ����  
    private String url; // ͼƬurl  

    public RecommendData() {
		super();
	}
    
	public RecommendData(String id,String ctime ,String info,
			String url) {
		super();
		this.id=id;
		this.url = url;
		this.info = info;
		this.ctime = ctime;
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public void setTime(String ctime) {
		this.ctime = ctime;
	}


}
