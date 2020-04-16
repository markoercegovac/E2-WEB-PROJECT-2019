package services;

import java.util.HashMap;

public class Kupac extends Korisnik {
	/*par Ussername:Oglas*/
	
	private HashMap<String,Oglas> ordered;//mapa porucenih
	private HashMap<String,Oglas> delivered;//mapa dostavljenih
	private HashMap<String,Oglas> favorite;//mapa omiljenih

	
	
	public Kupac() {
		super();
		this.ordered=new HashMap<String, Oglas>();
		this.delivered=new HashMap<String, Oglas>();
		this.favorite=new HashMap<String, Oglas>();
		
		// TODO Auto-generated constructor stub
	}

	public Kupac(HashMap<String, Oglas> ordered, HashMap<String, Oglas> delivered, HashMap<String, Oglas> favorite) {
		super();
		this.ordered = ordered;
		this.delivered = delivered;
		this.favorite = favorite;
	}

	public HashMap<String, Oglas> getOrdered() {
		return ordered;
	}

	public void setOrdered(HashMap<String, Oglas> ordered) {
		this.ordered = ordered;
	}

	public HashMap<String, Oglas> getDelivered() {
		return delivered;
	}

	public void setDelivered(HashMap<String, Oglas> delivered) {
		this.delivered = delivered;
	}

	public HashMap<String, Oglas> getFavorite() {
		return favorite;
	}

	public void setFavorite(HashMap<String, Oglas> favorite) {
		this.favorite = favorite;
	}

	
	
}
