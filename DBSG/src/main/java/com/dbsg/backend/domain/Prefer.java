package com.dbsg.backend.domain;

public class Prefer {

	private int prefer_no;
	private String user_nickname;
	private int menu_no;
	public int getPrefer_no() {
		return prefer_no;
	}
	public void setPrefer_no(int prefer_no) {
		this.prefer_no = prefer_no;
	}
	public String getUser_nickname() {
		return user_nickname;
	}
	public void setUser_nickname(String user_nickname) {
		this.user_nickname = user_nickname;
	}
	public int getMenu_no() {
		return menu_no;
	}
	public void setMenu_no(int menu_no) {
		this.menu_no = menu_no;
	}
	
	@Override
	public String toString() {
		return "Prefer [prefer_no=" + prefer_no + ", user_nickname=" + user_nickname + ", menu_no=" + menu_no + "]";
	}
}
