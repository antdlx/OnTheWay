package com.example.infos;

import java.io.Serializable;

import com.baidu.mapapi.model.LatLng;

public class DialogListviewInfo implements  Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4014903829153864177L;
	private String spName,spAddress;
	private double spLatitude,spLongitude;
	
	public DialogListviewInfo(String spName,String spAddress,double spLatitude,double spLongitude) {
		this.spName = spName;
		this.spAddress = spAddress;
		this.spLatitude = spLatitude;
		this.spLongitude= spLongitude;
	}

	public String getSpName() {
		return spName;
	}

	public double getSpLatitude() {
		return spLatitude;
	}

	public double getSpLongitude() {
		return spLongitude;
	}

	public String getSpAddress() {
		return spAddress;
	}



}
