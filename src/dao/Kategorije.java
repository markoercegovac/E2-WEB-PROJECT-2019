package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import services.Kategorija;
import services.Oglas;

public class Kategorije {

	private HashMap<String, Kategorija>kategorije;

	public Kategorije() {
		super();
		kategorije=new HashMap<String, Kategorija>();
		// TODO Auto-generated constructor stub
	}

	public void save(String path,boolean overwrite) {
		try {
			
			FileWriter fw=new FileWriter(path,overwrite);
			
			BufferedWriter bw=new BufferedWriter(fw);
			PrintWriter pw=new PrintWriter(bw);
			for(Kategorija k:this.kategorije.values()) {
				pw.println(k.getId()+"|"+k.getName()+"|"+k.getDescribe());
			}
			
			pw.flush();
			pw.close();
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	public void load(String path) {
		BufferedReader in=null;
		
		try {
			File file=new File(path);
			in=new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while((line=in.readLine())!=null) {
				line=line.trim();
				if(line.equals("")||line.indexOf('#')==0)
					continue;
				st=new StringTokenizer(line,"|");
				while(st.hasMoreTokens()) {
					String id=st.nextToken().trim();
					String nazi=st.nextToken().trim();
					String opis=st.nextToken().trim();
					if(opis==null) {
						opis="";
					}
					
					Kategorija kategorija=new Kategorija(id,nazi,opis);
					this.kategorije.put(id, kategorija);
					
				}
			}
			
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}finally {
			if(in!=null) {
				try {
					in.close();
				}
				catch (Exception e) { }
			}
		}
	}
	
	public HashMap<String, Kategorija> getKategorije() {
		return kategorije;
	}

	public void setKategorije(HashMap<String, Kategorija> kategorije) {
		this.kategorije = kategorije;
	}
	public ArrayList<Kategorija>vratiSveKategorije(){
		ArrayList<Kategorija> kat = new ArrayList<Kategorija>();
		for(Kategorija k:this.kategorije.values()) {
			kat.add(k);
		}
		return kat;
	}
	public void brisanjeKategorije(String id) {
		this.kategorije.remove(id);
	}
	
	
	//dodavanje oglasa za odredjenu kategoriju
	public String dodajOglasUKategoriju(Oglas o,Oglasi oglasi) {
		if(!proveriNazivOglasa(o.getName())) {
			return "naziv";
		}
		if(!proveriCenu(o.getPrice())) {
			return "cena";
		}
		if(!proveriGrad(o.getCity())) {
			return "grad";
		}
		if(o.getImage().equals("")) {
			return "slika";
		}
		
		
		
		
		oglasi.dodajOglas(o);
		
		return "ok";
	}
	
	
	private boolean proveriNazivOglasa(String naz) {
		if ((naz.length() < 4 ) || (naz.length() > 10)) {
			return false;
		}
		else {
			return true;
		}
	}
	
	private boolean proveriCenu(String cena) {
		String validniKarakteri = "\\d+";
		if (!(cena.matches(validniKarakteri))) {
			return false;
		}
		else {
			return true;
		}
	}
	
	
	private boolean proveriGrad(String grad) {
		String validniKarakteri = "[a-zA-Z]+";
		if (!(grad.matches(validniKarakteri))) {
			return false;
		}
		else {
			return true;
		}
	}
	
	
	
	
}
