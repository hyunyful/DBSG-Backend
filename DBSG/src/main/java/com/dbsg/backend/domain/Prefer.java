package com.dbsg.backend.domain;

public class Prefer {

	private int user_no;
	private int menu_no;

	public int getUser_no() {
		return user_no;
	}
	public void setUser_no(int user_no) {
		this.user_no = user_no;
	}
	public int getMenu_no() {
		return menu_no;
	}
	public void setMenu_no(int menu_no) {
		this.menu_no = menu_no;
	}
	
	@Override
	public String toString() {
		return "Prefer [user_no=" + user_no + ", menu_no=" + menu_no + "]";
	}
	
}
