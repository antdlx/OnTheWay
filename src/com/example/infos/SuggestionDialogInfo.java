package com.example.infos;

public class SuggestionDialogInfo {
	public String key;
	public String cityAndDistric;
	
	public String getKey() {
		return key;
	}
	public String getCityAndDistric() {
		return cityAndDistric;
	}
	
	public SuggestionDialogInfo(String key, String cityAndDistric) {
		this.key = key;
		this.cityAndDistric = cityAndDistric;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public void setCityAndDistric(String cityAndDistric) {
		this.cityAndDistric = cityAndDistric;
	}
	
}
