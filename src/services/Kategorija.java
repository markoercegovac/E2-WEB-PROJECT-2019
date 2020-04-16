package services;

import java.util.ArrayList;
import java.util.HashMap;

public class Kategorija {
	
	private String id;
	private String name;
	private String describe;
	private HashMap<String, Oglas>oglasi=new HashMap<String, Oglas>();
	
	public Kategorija() {
		super();
		
		// TODO Auto-generated constructor stub
	}

	public Kategorija(String id,String name, String describe) {
		super();
		this.id=id;
		this.name = name;
		this.describe = describe;
		
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	

	public HashMap<String, Oglas> getOglasi() {
		return oglasi;
	}

	public void setOglasi(HashMap<String, Oglas> oglasi) {
		this.oglasi = oglasi;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	
	
}
