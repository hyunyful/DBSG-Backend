package com.dbsg.backend.domain;

//Frontend에 보내줄 정보만 담을 DTO
public class MenuDisplay {

	//메뉴 번호, 메뉴 이름, 메뉴 이미지, 메뉴 설명, 필요 재료, 메뉴 평점, 메뉴 태그, 예상 조리시간, 작성자, 즐찾수, 조회수
	private int menu_no;
	private String menu_name;
	private String menu_image;
	private String menu_description;
	private String menu_reqMaterial;
	private float menu_star;
	private String menu_tag;
	private int menu_totalTime;
	private String menu_writer;
	private int menu_preferCnt;
	private int menu_readCnt;
	
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
	public String getMenu_image() {
		return menu_image;
	}
	public void setMenu_image(String menu_image) {
		this.menu_image = menu_image;
	}
	public String getMenu_description() {
		return menu_description;
	}
	public void setMenu_description(String menu_description) {
		this.menu_description = menu_description;
	}
	public String getMenu_reqMaterial() {
		return menu_reqMaterial;
	}
	public void setMenu_reqMaterial(String menu_reqMaterial) {
		this.menu_reqMaterial = menu_reqMaterial;
	}
	public float getMenu_star() {
		return menu_star;
	}
	public void setMenu_star(float menu_star) {
		this.menu_star = menu_star;
	}
	public String getMenu_tag() {
		return menu_tag;
	}
	public void setMenu_tag(String menu_tag) {
		this.menu_tag = menu_tag;
	}
	public int getMenu_totalTime() {
		return menu_totalTime;
	}
	public void setMenu_totalTime(int menu_totalTime) {
		this.menu_totalTime = menu_totalTime;
	}
	public String getMenu_writer() {
		return menu_writer;
	}
	public void setMenu_writer(String menu_writer) {
		this.menu_writer = menu_writer;
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
}
