package dao;

import java.util.HashMap;

import services.Prodavac;

public class Prodavci {

	
	private HashMap<String, Prodavac>sellers;

	public Prodavci() {
		super();
		this.sellers=new HashMap<String, Prodavac>();
		// TODO Auto-generated constructor stub
	}

	public HashMap<String, Prodavac> getSellers() {
		return sellers;
	}

	public void setSellers(HashMap<String, Prodavac> sellers) {
		this.sellers = sellers;
	}
	//brisanje iz mape
	public void deleteSeller(String korIme,String id) {
		this.sellers.get(korIme).getBroadcastedPosts().remove(id);
	}
	
	
	
	
}
