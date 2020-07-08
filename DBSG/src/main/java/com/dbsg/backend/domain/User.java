package com.dbsg.backend.domain;

import java.util.Date;

public class User {
	
	private Integer user_no;
	private String user_email;
	private String user_nickname;
	private String user_age;
	private int user_gender;		//1 남자 2 여자
	private String user_birthday;
	private String user_image;
	private String user_type;
	private int user_level;
	private Date user_delete;
	private String etc;
	
	public int getUser_no() {
		return user_no;
	}
	public void setUser_no(int user_no) {
		this.user_no = user_no;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	public String getUser_nickname() {
		return user_nickname;
	}
	public void setUser_nickname(String user_nickname) {
		this.user_nickname = user_nickname;
	}
	public String getUser_age() {
		return user_age;
	}
	public void setUser_age(String user_age) {
		this.user_age = user_age;
	}
	public int getUser_gender() {
		return user_gender;
	}
	public void setUser_gender(int user_gender) {
		this.user_gender = user_gender;
	}
	public String getUser_birthday() {
		return user_birthday;
	}
	public void setUser_birthday(String user_birthday) {
		this.user_birthday = user_birthday;
	}
	public String getUser_image() {
		return user_image;
	}
	public void setUser_image(String user_image) {
		this.user_image = user_image;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public int getUser_level() {
		return user_level;
	}
	public void setUser_level(int user_level) {
		this.user_level = user_level;
	}
	public Date getUser_delete() {
		return user_delete;
	}
	public void setUser_delete(Date user_delete) {
		this.user_delete = user_delete;
	}
	public String getEtc() {
		return etc;
	}
	public void setEtc(String etc) {
		this.etc = etc;
	}
	
	@Override
	public String toString() {
		return "User [user_no=" + user_no + ", user_email=" + user_email + ", user_nickname=" + user_nickname
				+ ", user_age=" + user_age + ", user_gender=" + user_gender + ", user_birthday=" + user_birthday
				+ ", user_image=" + user_image + ", user_type=" + user_type + ", user_level=" + user_level
				+ ", user_delete=" + user_delete + ", etc=" + etc + "]";
	}
	
}
