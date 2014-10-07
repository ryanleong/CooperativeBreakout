package com.unimelb.breakout.object;

import java.util.ArrayList;

public class MapList {

	private String name;
	ArrayList<MapInfo> maps;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<MapInfo> getMaps() {
		return maps;
	}
	public void setMaps(ArrayList<MapInfo> maps) {
		this.maps = maps;
	}
	
	class MapInfo{
		private String name;
		private String filename;
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getFilename() {
			return filename;
		}
		
		public void setFilename(String filename) {
			this.filename = filename;
		}
		
	}
}
