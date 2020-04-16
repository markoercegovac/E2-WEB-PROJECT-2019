package dao;


import java.util.HashMap;

import services.Korisnik;
import services.Kupac;

public class Kupci extends Korisnik {


	
	
	private HashMap<String, Kupac>kupci;

	public Kupci() {
		super();
		this.kupci=new HashMap<String, Kupac>();
		// TODO Auto-generated constructor stub
	}

	public HashMap<String, Kupac> getKupci() {
		return kupci;
	}

	public void setKupci(HashMap<String, Kupac> kupci) {
		this.kupci = kupci;
	}
	
	
	
	
}
