package services;

import java.util.ArrayList;

public class Omiljen {
	private String id;
	private String idOglasa;
	private ArrayList<String> listaKorisnika;
	
	
	public Omiljen() {
		super();
		this.listaKorisnika=new ArrayList<String>();
		// TODO Auto-generated constructor stub
	}
	
	
	public Omiljen(String id, String idOglasa, ArrayList<String> listaKorisnika) {
		super();
		this.id = id;
		this.idOglasa = idOglasa;
		this.listaKorisnika = listaKorisnika;
	}
	

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getIdOglasa() {
		return idOglasa;
	}
	public void setIdOglasa(String idOglasa) {
		this.idOglasa = idOglasa;
	}
	public ArrayList<String> getListaKorisnika() {
		return listaKorisnika;
	}
	public void setListaKorisnika(ArrayList<String> listaKorisnika) {
		this.listaKorisnika = listaKorisnika;
	}
	


}
