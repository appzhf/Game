package com.example.zhf.game.entity;

public class UserInfo {
	private String name;
	private String pwd;
	private String sex="男";
	private int icon;
	private String address;
	
	public UserInfo() {
		super();
	}

	public UserInfo(String name, String pwd, String sex, int icon,
			String address) {
		super();
		this.name = name;
		this.pwd = pwd;
		this.sex = sex;
		this.icon = icon;
		this.address = address;
	}


	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
}
