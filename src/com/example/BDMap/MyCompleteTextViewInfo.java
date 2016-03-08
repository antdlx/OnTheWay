package com.example.BDMap;

public class MyCompleteTextViewInfo {
	public String title,des;
	public double x,y;
	public MyCompleteTextViewInfo(String title,String des,double x,double y){
		this.title = title;
		this.des = des;
		this.x = x;
		this.y = y;
	}
	public String getTitle() {
		return title;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
}
