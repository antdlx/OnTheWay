package com.example.infos;

public class MyBaseInformation {
	private String nickname,img_url,sex,age,city;
	private int i=0;
	
	public String getnickname(){
		return nickname;
	}
	
	public void setnickname(String nickname){
		this.nickname=nickname;
	}
	
	public String getimg_url(){
		return img_url;
	}
	
	public void setimg_url(String img_url){
		this.img_url=img_url;
	}
	

	public int getSex(){
		setsex(sex);
		return i;	
	}
	
	public void setsex(String sex){
		this.sex=sex;
		if(sex==("男")){
			i = 0;
		}else{
			i = 1;
		}
	}
	
	public String getage(){
		return age;
	}
	
	public void setage(String age){
		this.age=age;
	}
	
	public String getcity(){
		return city;
	}
	
	public void setcity(String city){
		this.city=city;
	}
}
