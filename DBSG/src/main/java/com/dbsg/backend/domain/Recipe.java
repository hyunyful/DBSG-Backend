package com.dbsg.backend.domain;

public class Recipe {
 
	 private Integer recipe_no;
	 private Integer menu_no;
	 private Integer recipe_processNo;
	 private String recipe_action;
	 private Integer recipe_fire;
	 private Integer recipe_reqTime;
	 private String recipe_image;
	 private String etc;
	 
	public int getRecipe_no() {
		return recipe_no;
	}
	public void setRecipe_no(int recipe_no) {
		this.recipe_no = recipe_no;
	}
	public int getMenu_no() {
		return menu_no;
	}
	public void setMenu_no(int menu_no) {
		this.menu_no = menu_no;
	}
	public int getRecipe_processNo() {
		return recipe_processNo;
	}
	public void setRecipe_processNo(int recipe_processNo) {
		this.recipe_processNo = recipe_processNo;
	}
	public String getRecipe_action() {
		return recipe_action;
	}
	public void setRecipe_action(String recipe_action) {
		this.recipe_action = recipe_action;
	}
	public int getRecipe_fire() {
		return recipe_fire;
	}
	public void setRecipe_fire(int recipe_fire) {
		this.recipe_fire = recipe_fire;
	}
	public int getRecipe_reqTime() {
		return recipe_reqTime;
	}
	public void setRecipe_reqTime(int recipe_reqTime) {
		this.recipe_reqTime = recipe_reqTime;
	}
	public String getRecipe_image() {
		return recipe_image;
	}
	public void setRecipe_image(String recipe_image) {
		this.recipe_image = recipe_image;
	}
	public String getEtc() {
		return etc;
	}
	public void setEtc(String etc) {
		this.etc = etc;
	}
	 
}
