package com.unimelb.breakout.object;

/**
 * JSON Object
 * 
 * @author Siyuan Zhang
 *
 */
public 	class MapMeta{
	private String name;
	private String type;
	private String next;
	
	public MapMeta(String name, String type, String next){
		this.name = name;
		this.type = type;
		this.next = next;
	}
	
	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
