package com.example.infos;

public class ExpandableviewGInfo {

	private String BusNames;
	private int duration,distance,num;
	
	public ExpandableviewGInfo(String BusNames,int duration,int distance,int num){
		this.BusNames = BusNames;
		this.distance = distance;
		this.duration = duration;
		this.num = num;
	}

	public String getBusNames() {
		return BusNames;
	}

	public String getNum() {
		return "0"+num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public void setBusNames(String busNames) {
		BusNames = busNames;
	}

	//保留1位小数
	public String getDuration() {
		float f = ((float)duration)/1000;
		float fx = (float)(Math.round(f*10))/10;
		return fx+"公里";
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getDistance() {
		return distance/60+"分钟";
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}
	
}
