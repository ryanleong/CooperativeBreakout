package com.unimelb.breakout.object;

import java.util.ArrayList;

public class MapList {

	private String name;
	ArrayList<MapMeta> maps;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<MapMeta> getMaps() {
		return maps;
	}
	public void setMaps(ArrayList<MapMeta> maps) {
		this.maps = maps;
	}
	

}
