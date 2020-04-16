package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

import services.Korisnik;
import services.Poruka;

public class Korisnici {
	private HashMap<String, Korisnik> korisnici;

	public Korisnici() {
		super();
		this.korisnici = new HashMap<String, Korisnik>();
		// TODO Auto-generated constructor stub
	}

	public void save(String path, boolean overwrite) {
		try {
			FileWriter fw = new FileWriter(path, overwrite);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			for (Korisnik k : this.korisnici.values()) {
				pw.println(k.getUsername() + "|" + k.getPassword() + "|" + k.getName() + "|" + k.getSurname() + "|"
						+ k.getPhone() + "|" + k.getCity() + "|" + k.getEmail() + "|" + k.getTipKorisnika());
			}
			pw.flush();
			pw.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
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
					String username = st.nextToken().trim();
					String password = st.nextToken().trim();
					String name = st.nextToken().trim();
					String surname = st.nextToken().trim();
					String phone = st.nextToken().trim();
					String city = st.nextToken().trim();
					String email = st.nextToken().trim();

					String tip = st.nextToken().trim();
					Korisnik korisnik = new Korisnik(username, password, name, surname, phone, city, email, "", tip);
					this.korisnici.put(username, korisnik);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public void savePrimljenePoruke(String path, boolean overwrite, String korIme) {
		try {
			FileWriter fw = new FileWriter(path, overwrite);

			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			for (Poruka p : this.korisnici.get(korIme).getPrimljene_poruke().values()) {
				pw.println(p.getId() + "|" + p.getNamePoster() + "|" + p.getSender() + "|" + p.getTitle() + "|"
						+ p.getContent() + "|" + p.getDateAndTime() + "|" + p.getPrimaoc());
			}
			pw.flush();
			pw.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void loadPrimljenePoruke(String path, String korIme) {
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
					String nazi = st.nextToken().trim();
					String sender = st.nextToken().trim();
					String title = st.nextToken().trim();
					String content = st.nextToken().trim();
					String datum = st.nextToken().trim();
					String primaoc = st.nextToken().trim();
					Poruka poruka = new Poruka(id, nazi, sender, title, content, datum, primaoc);
					
					this.korisnici.get(primaoc).getPrimljene_poruke().put(id, poruka);

				}
			}
			

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}
	
	public void savePoslatePoruke(String path, boolean overwrite, String korIme) {
		try {
			FileWriter fw = new FileWriter(path, overwrite);

			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			for (Poruka p : this.korisnici.get(korIme).getPoslate_poruke().values()) {
				pw.println(p.getId() + "|" + p.getNamePoster() + "|" + p.getSender() + "|" + p.getTitle() + "|"
						+ p.getContent() + "|" + p.getDateAndTime() + "|" + p.getPrimaoc());
			}
			pw.flush();
			pw.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void loadPoslatePoruke(String path, String korIme) {
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
					String nazi = st.nextToken().trim();
					String sender = st.nextToken().trim();
					String title = st.nextToken().trim();
					String content = st.nextToken().trim();
					String datum = st.nextToken().trim();
					String primaoc = st.nextToken().trim();
					Poruka poruka = new Poruka(id, nazi, sender, title, content, datum, primaoc);
					
					this.korisnici.get(sender).getPoslate_poruke().put(id, poruka);

				}
			}
			

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}
	
	
	
	
	public HashMap<String, Korisnik> getKorisnici() {
		return korisnici;
	}

	public void setKorisnici(HashMap<String, Korisnik> korisnici) {
		this.korisnici = korisnici;
	}
	
	//dodavanje novog korisnika
	public String dodajNovogKorisnika(String id,Korisnik k) {
		if (!proveriKorIme(k.getUsername())) {
			return "korIme";
		}
		else if (!proveriLozinku(k.getPassword())) {
			return "loz";
		}
		else if (!proveriIme(k.getName())) {
			return "ime";
		}
		else if (!proveriIme(k.getSurname())) {
			return "prezime";
		}
		else if (!proveriTelefon(k.getPhone())) {
			return "telefon";
		}
		else if (!proveriMejl(k.getEmail())) {
			return "mejl";
		}
		else if(!proveriGrad(k.getCity())) {
			return "grad";
		}
		
		if(this.korisnici.containsKey(id)) {
			return "postoji";
		}
		
		this.korisnici.put(id, k);
		return "ok";
	}
	
	
	private boolean proveriKorIme(String korIme) {
		String validniKarakteri = "^[a-zA-Z0-9]+$";
		if ((korIme.length() < 4 ) || (korIme.length() > 10)) {
			return false;
		}
		else if (!(korIme.matches(validniKarakteri))) {
			return false;
		}
		else {
			return true;
		}
		
	}
	private boolean proveriLozinku(String loz) {
		if ((loz.length() < 4 ) || (loz.length() > 10)) {
			return false;
		}
		else {
			return true;
		}
	}
	
	private boolean proveriIme(String ime) {
		String validniKarakteri = "[a-zA-Z]+";
		if (!(ime.matches(validniKarakteri))) {
			return false;
		}
		else {
			return true;
		}
	}
	
	private boolean proveriTelefon(String tel) {
		String validniKarakteri = "\\d+";
		if (!(tel.matches(validniKarakteri))) {
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
	
	private boolean proveriMejl(String mejl) {
		String validniKarakteri = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		if (!(mejl.matches(validniKarakteri))) {
			return false;
		}
		else {
			return true;
		}
	}
	
	
}
