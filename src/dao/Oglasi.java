package dao;

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
import java.util.Optional;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.xml.bind.ParseConversionEvent;

import services.Oglas;
import services.Recenzija;

public class Oglasi {

	private HashMap<String, Oglas> oglasi;

	private Optional<Integer> tryParseInteger(String string) {
		try {
			return Optional.of(Integer.valueOf(string));
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}

	@Context
	ServletContext ctx;

	public Oglasi() {
		super();
		this.oglasi = new HashMap<String, Oglas>();
		// TODO Auto-generated constructor stub
	}

	public Oglasi(HashMap<String, Oglas> oglasi) {
		super();
		this.oglasi = oglasi;
	}

	public HashMap<String, Oglas> getOglasi() {
		return oglasi;
	}

	public void setOglasi(HashMap<String, Oglas> oglasi) {
		this.oglasi = oglasi;
	}

	// cuvanje oglasa
	public void save(String path, boolean overwrite) {

		try {

			FileWriter fw = new FileWriter(path, overwrite);

			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);

			for (Oglas og : this.oglasi.values()) {
				pw.println(og.getId() + "|" + og.getName() + "|" + og.getPrice() + "|" + og.getDescribe() + "|"
						+ og.getLike() + "|" + og.getDislike() + "|" + og.getCity() + "|" + og.getKategorija() + "|"
						+ og.getuRealizaciji() + "|" + og.getPorucen() + "|" + "|" + og.getProdavac() + "|"
						+ og.getDateDelivered() + "|" + og.getDateTop() +"|"+ og.getPenal()+"|"+og.isAktivan()+"|" + og.isActive() + "|" + og.isDostavljen()
						+ "|" + og.getImage());

			}

			pw.flush();
			pw.close();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

		ArrayList<Recenzija> rec = new ArrayList<Recenzija>();
		// Oglas oglas=new
		// Oglas(og.ime,og.cena,og.opis,0,0,og.slika,timeStamp,"none",true,rec,og.grad);

	}

	public void load(String path) {

		BufferedReader in = null;

		try {
			File file = new File(path);
			in = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, "|");
				while (st.hasMoreTokens()) {
					String id = st.nextToken().trim();
					String ime = st.nextToken().trim();
					String cena = st.nextToken().trim();
					String opis = st.nextToken().trim();
					String like = st.nextToken().trim();
					String dislike = st.nextToken().trim();
					String grad = st.nextToken().trim();
					String kat = st.nextToken().trim();
					String real = st.nextToken().trim();
					String porucen = st.nextToken().trim();

					String prodavac = st.nextToken().trim();
					String postavljen = st.nextToken().trim();
					String top = st.nextToken().trim();
					String penal=st.nextToken().trim();
					String aktivann=st.nextToken().trim();
					String aktivan = st.nextToken().trim();
					String dostavljen = st.nextToken().trim();
					String slika = st.nextToken().trim();
					Oglas oglas = new Oglas(id, ime, cena, opis, tryParseInteger(like).orElse(0),
							tryParseInteger(dislike).orElse(0), slika, Boolean.parseBoolean(dostavljen), postavljen,
							top,tryParseInteger(penal).orElse(0),Boolean.parseBoolean(aktivann),Boolean.parseBoolean(aktivan), grad, kat, real, porucen, prodavac);
					this.oglasi.put(id, oglas);
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public void saveRecenzije(String path, boolean overwrite, String idOglas) {

		try {

			FileWriter fw = new FileWriter(path, overwrite);

			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);

			for (Recenzija r : this.oglasi.get(idOglas).getRecension().values()) {
				pw.println(r.getId() + "|" + r.getName() + "|" + r.getRecesent() + "|" + r.getTitle() + "|"
						+ r.getContent() + "|" + r.getImage() + "|" + r.isCorrect() + "|" + r.isAgreed());

			}

			pw.flush();
			pw.close();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}

	}

	public void loadRecenzije(String path) {

		BufferedReader in = null;

		try {
			File file = new File(path);
			in = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, "|");
				while (st.hasMoreTokens()) {
					String id = st.nextToken().trim();
					String ime = st.nextToken().trim();
					String recezent = st.nextToken().trim();
					String title = st.nextToken().trim();
					String content = st.nextToken().trim();
					String image = st.nextToken().trim();
					String correct = st.nextToken().trim();
					String agreed = st.nextToken().trim();

					Recenzija recenzija = new Recenzija(id, ime, recezent, title, content, image,
							Boolean.parseBoolean(correct), Boolean.parseBoolean(agreed));
					this.oglasi.get(ime).getRecension().put(id, recenzija);
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public void saveOmiljeni(String path, boolean overwrite, String idOglasa, String uuid, String korIme) {

		try {

			FileWriter fw = new FileWriter(path, overwrite);

			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			pw.println(uuid + "|" + korIme + "|" + idOglasa);

			pw.flush();
			pw.close();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}

	}

	public void loadOmiljeni(String path) {

		BufferedReader in = null;

		try {
			File file = new File(path);
			in = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, "|");
				while (st.hasMoreTokens()) {
					String uuid = st.nextToken().trim();
					String ussername = st.nextToken().trim();
					String id = st.nextToken().trim();
					this.oglasi.get(id).getOmiljen().put(uuid, ussername);

				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public void dodajOglas(Oglas o) {
		this.oglasi.put(o.getId(), o);
	}

	public void ukloniOglas(String id) {
		this.oglasi.remove(id);
	}
	
	public void ukloniRecenziju(String id) {
		for(Oglas o:this.oglasi.values()) {
			o.getRecension().remove(id);
		}
	}

	public ArrayList<Oglas> izmenaOglasa(String id) {
		ArrayList<Oglas> ogl=new ArrayList<Oglas>();
		Oglas oglas = new Oglas(this.oglasi.get(id).getId(), this.oglasi.get(id).getName(), this.oglasi.get(id).getPrice(),this.oglasi.get(id).getDescribe(), this.oglasi.get(id).getLike(), this.oglasi.get(id).getDislike(), this.oglasi.get(id).getImage(), this.oglasi.get(id).isActive(), this.oglasi.get(id).getDateDelivered(),this.oglasi.get(id).getDateTop(),this.oglasi.get(id).getPenal(),this.oglasi.get(id).isAktivan(),
				this.oglasi.get(id).isDostavljen(), this.oglasi.get(id).getCity(), this.oglasi.get(id).getKategorija(), this.oglasi.get(id).getuRealizaciji(), this.oglasi.get(id).getPorucen(), this.oglasi.get(id).getProdavac());
		ogl.add(oglas);
		return ogl;
	
	}

}
