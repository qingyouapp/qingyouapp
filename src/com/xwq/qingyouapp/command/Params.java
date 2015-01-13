package com.xwq.qingyouapp.command;

import java.util.HashMap;
import java.util.Map;

public class Params {

	private Map<String,String> map;
	
	public Params(){
		this.map = new HashMap<String, String>();
	}
	
	public void put(String key, String value){
		map.put(key, value);
	}
	
	public String get(String key){
		return this.map.get(key);
	}
	
	public int size(){
		return map.size();
	}
	
	public Map<String,String> getMap(){
		return this.map;
	}
	
	
}
