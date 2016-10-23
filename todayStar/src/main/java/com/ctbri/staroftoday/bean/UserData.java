package com.ctbri.staroftoday.bean;

public class UserData {
	public String id;
	public String name;
	public String headpic="";

	public UserData() {
	}
	
	public UserData(String id,String name
			) {
		super();
		this.id = id;
		this.name = name;
		
	}
	
	public UserData(String id,String name,String headpic
			) {
		super();
		this.id = id;
		this.name = name;
		this.headpic = headpic;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getname() {
		return name;
	}

	public void setname(String name) {
		this.name = name;
	}

	public String getHeadpic() {
		return headpic;
	}

	public void setHeadpic(String headpic) {
		this.headpic = headpic;
	}
	@Override
	public String toString() {
		return "UserDataEntity [id="+id +"name=" + name + ",headpic=" + headpic  +  "]";
	}
}
