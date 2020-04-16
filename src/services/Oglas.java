package services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Oglas implements Comparable<Oglas> {

	private String id;
	private String name;
	private String price;
	private String describe;
	private int like;
	private int dislike;
	private String image;
	private String dateDelivered;
	private String dateTop;
	private boolean active;
	private boolean dostavljen;
	private HashMap<String, Recenzija> recension;
	private String city;
	private String kategorija;
	private String uRealizaciji;
	private String porucen;
	private	HashMap<String, String>omiljeni;
	private String prodavac;
	private int penal;
	private boolean aktivan;
	
	public Oglas() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Oglas(String id,String name, String price, String describe, int like, int dislike, String image,Boolean dostavljen,
			String dateDelivered, String dateTop,int penal,boolean aktivan, boolean active, String city,String kat,String uRealizaciji,String poru,String prodavac) {
		super();
		this.id=id;
		this.name = name;
		this.price = price;
		this.describe = describe;
		this.like = like;
		this.dislike = dislike;
		this.image = image;
		this.dateDelivered = dateDelivered;
		this.dateTop = dateTop;
		this.active = active;
		this.dostavljen=dostavljen;
		this.city = city;
		this.kategorija=kat;
		this.recension=new HashMap<String, Recenzija>();
		this.uRealizaciji=uRealizaciji;
		this.porucen=poru;
		this.omiljeni=new HashMap<String, String>();
		this.prodavac=prodavac;
		this.aktivan=aktivan;
		this.penal=penal;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDostavljen() {
		return dostavljen;
	}

	public void setDostavljen(boolean dostavljen) {
		this.dostavljen = dostavljen;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public int getLike() {
		return like;
	}

	public void setLike(int like) {
		this.like = like;
	}

	public int getDislike() {
		return dislike;
	}

	public void setDislike(int dislike) {
		this.dislike = dislike;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDateDelivered() {
		return dateDelivered;
	}

	public void setDateDelivered(String dateDelivered) {
		this.dateDelivered = dateDelivered;
	}

	public String getDateTop() {
		return dateTop;
	}

	public void setDateTop(String dateTop) {
		this.dateTop = dateTop;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public HashMap<String, Recenzija> getRecension() {
		return recension;
	}

	public void setRecension(HashMap<String, Recenzija> recension) {
		this.recension = recension;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKategorija() {
		return kategorija;
	}

	public void setKategorija(String kategorija) {
		this.kategorija = kategorija;
	}

	
	public String getuRealizaciji() {
		return uRealizaciji;
	}

	public void setuRealizaciji(String uRealizaciji) {
		this.uRealizaciji = uRealizaciji;
	}

	public String getPorucen() {
		return porucen;
	}

	public void setPorucen(String porucen) {
		this.porucen = porucen;
	}

	public HashMap<String, String> getOmiljen() {
		return omiljeni;
	}

	public void setOmiljen(HashMap<String, String>omiljen) {
		this.omiljeni = omiljen;
	}

	public String getProdavac() {
		return prodavac;
	}

	public void setProdavac(String prodavac) {
		this.prodavac = prodavac;
	}
	
	public int getPenal() {
		return penal;
	}

	public void setPenal(int penal) {
		this.penal = penal;
	}

	public boolean isAktivan() {
		return aktivan;
	}

	public void setAktivan(boolean aktivan) {
		this.aktivan = aktivan;
	}

	public int compareTo(Oglas o) {
		// TODO Auto-generated method stub
		if(this.omiljeni.size()>o.getOmiljen().size()) {
			return 1;
		}
		else if(this.omiljeni.size()<o.getOmiljen().size()) {
			return -1;
		}
		return 0;
	}
	
}
