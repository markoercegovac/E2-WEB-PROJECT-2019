package services;

import java.util.HashMap;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import data.TipKorisnika;

public class Korisnik {
	
	private String username;
	private String password;
	private String name;
	private String surname;
	private String phone;
	private String city;
	private String email;
	private String date;
	private String tipKorisnika;
	private HashMap<String, Poruka>primljene_poruke;
	private HashMap<String, Poruka>poslate_poruke;
	
	
	public Korisnik() {
		super();
		
		// TODO Auto-generated constructor stub
	}

	public Korisnik(String username, String password, String name, String surname, String phone, String city,
			String email, String date,String tipKorisnika) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.phone = phone;
		this.city = city;
		this.email = email;
		this.date = date;
		this.tipKorisnika=tipKorisnika;
		this.poslate_poruke=new HashMap<String, Poruka>();
		this.primljene_poruke=new HashMap<String, Poruka>();
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTipKorisnika() {
		return tipKorisnika;
	}

	public void setTipKorisnika(String tipKorisnika) {
		this.tipKorisnika = tipKorisnika;
	}

	public HashMap<String, Poruka> getPrimljene_poruke() {
		return primljene_poruke;
	}

	public void setPrimljene_poruke(HashMap<String, Poruka> primljene_poruke) {
		this.primljene_poruke = primljene_poruke;
	}

	public HashMap<String, Poruka> getPoslate_poruke() {
		return poslate_poruke;
	}

	public void setPoslate_poruke(HashMap<String, Poruka> poslate_poruke) {
		this.poslate_poruke = poslate_poruke;
	}
	
	
	
	
	
	
}
