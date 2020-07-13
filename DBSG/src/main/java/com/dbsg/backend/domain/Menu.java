package com.dbsg.backend.domain;

public class Menu {
 
	 private int menu_no;
	 private String menu_name;
	 private String menu_writer;
	 private int menu_category;
	 private String menu_tag;
	 private String menu_reqMaterial;
	 private String menu_needlessMaterial;
	 private String menu_description;
	 private String menu_image;
	 private String menu_kids;
	 private int menu_totalTime;
	 private int menu_delete;
	 private float menu_star;
	 private int menu_preferCnt;
	 private int menu_readCnt;
	 private String etc;
	 
	
	public int getMenu_no() {
		return menu_no;
	}
	public void setMenu_no(int menu_no) {
		this.menu_no = menu_no;
	}
	public String getMenu_name() {
		return menu_name;
	}
	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}
	public String getMenu_writer() {
		return menu_writer;
	}
	public void setMenu_writer(String menu_writer) {
		this.menu_writer = menu_writer;
	}
	public String getMenu_tag() {
		return menu_tag;
	}
	public void setMenu_tag(String menu_tag) {
		this.menu_tag = menu_tag;
	}
	public String getMenu_reqMaterial() {
		return menu_reqMaterial;
	}
	public void setMenu_reqMaterial(String menu_reqMaterial) {
		this.menu_reqMaterial = menu_reqMaterial;
	}
	public String getMenu_needlessMaterial() {
		return menu_needlessMaterial;
	}
	public void setMenu_needlessMaterial(String menu_needlessMaterial) {
		this.menu_needlessMaterial = menu_needlessMaterial;
	}
	public String getMenu_description() {
		return menu_description;
	}
	public void setMenu_description(String menu_description) {
		this.menu_description = menu_description;
	}
	public String getMenu_image() {
		return menu_image;
	}
	public void setMenu_image(String menu_image) {
		this.menu_image = menu_image;
	}
	public String getMenu_kids() {
		return menu_kids;
	}
	public void setMenu_kids(String menu_kids) {
		this.menu_kids = menu_kids;
	}
	public int getMenu_totalTime() {
		return menu_totalTime;
	}
	public void setMenu_totalTime(int menu_totalTime) {
		this.menu_totalTime = menu_totalTime;
	}
	public int getMenu_delete() {
		return menu_delete;
	}
	public void setMenu_delete(int menu_delete) {
		this.menu_delete = menu_delete;
	}
	public String getEtc() {
		return etc;
	}
	public void setEtc(String etc) {
		this.etc = etc;
	}
	public float getMenu_star() {
		return menu_star;
	}
	public void setMenu_star(float menu_star) {
		this.menu_star = menu_star;
	}
	public int getMenu_preferCnt() {
		return menu_preferCnt;
	}
	public void setMenu_preferCnt(int menu_preferCnt) {
		this.menu_preferCnt = menu_preferCnt;
	}
	public int getMenu_readCnt() {
		return menu_readCnt;
	}
	public void setMenu_readCnt(int menu_readCnt) {
		this.menu_readCnt = menu_readCnt;
	}
	public void setMenu_no(Integer menu_no) {
		this.menu_no = menu_no;
	}
	public int getMenu_category() {
		return menu_category;
	}
	public void setMenu_category(int menu_category) {
		this.menu_category = menu_category;
	}
	
	@Override
	public String toString() {
		return "Menu [menu_no=" + menu_no + ", menu_name=" + menu_name + ", menu_writer=" + menu_writer
				+ ", menu_category=" + menu_category + ", menu_tag=" + menu_tag + ", menu_reqMaterial="
				+ menu_reqMaterial + ", menu_needlessMaterial=" + menu_needlessMaterial + ", menu_description="
				+ menu_description + ", menu_image=" + menu_image + ", menu_kids=" + menu_kids + ", menu_totalTime="
				+ menu_totalTime + ", menu_delete=" + menu_delete + ", menu_star=" + menu_star + ", menu_preferCnt="
				+ menu_preferCnt + ", menu_readCnt=" + menu_readCnt + ", etc=" + etc + "]";
	}
	
}
