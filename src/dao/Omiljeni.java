package dao;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import services.Oglas;
import services.Omiljen;
import services.Recenzija;

public class Omiljeni {

	private HashMap<String, String>omiljeni;

	
	
	public Omiljeni() {
		super();
		this.omiljeni=new HashMap<String, String>();
		// TODO Auto-generated constructor stub
	}

	public HashMap<String, String> getOmiljeni() {
		return omiljeni;
	}

	public void setOmiljeni(HashMap<String, String> omiljeni) {
		this.omiljeni = omiljeni;
	}
	
	public void saveOmiljeni(String path, boolean overwrite,String idOglasa) {

		try {

			FileWriter fw = new FileWriter(path, overwrite);

			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);

			for (String str:this.omiljeni.values()) {
				pw.println(str+"|");

			}

			pw.flush();
			pw.close();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}

		

	}
	
}
