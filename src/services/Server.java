package services;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;

import dao.Kategorije;
import dao.Korisnici;
import dao.Kupci;
import dao.Oglasi;
import dao.Omiljeni;
import dao.Prodavci;
import data.KorisnikDto;
import data.NovaKategorija;
import data.NovaPoruka;
import data.NovaRecenzija;
import data.NovaUloga;
import data.NoviKorisnik;
import data.NoviLogin;
import data.NoviOglas;
import data.OglasPosrednikZaPretragu;

@Path("/server")
public class Server {

	private static final String COMMA_DELIMITER = ",";
	private ArrayList<Kupci> kupacLista;
	private ArrayList<Prodavci> prodavacLista;
	private ArrayList<Oglas> oglasiLista = new ArrayList<Oglas>();

	@Context
	ServletContext ctx;
	@Context
	HttpServletRequest request;

	// REGISTRACIJA
	@POST
	@Path("/registracija")
	@Produces(MediaType.TEXT_PLAIN)
	public String registracija(NoviKorisnik nk) {
		Korisnici korisnici = getKorisnici();

		Korisnik korisnik = new Korisnik(nk.korisnickoIme, nk.lozinka, nk.ime, nk.prezime, nk.kontakt, nk.grad,
				nk.email, "", nk.uloga);
		String response = korisnici.dodajNovogKorisnika(nk.korisnickoIme, korisnik);// dodavanje novog korisnika
		korisnici.save(putanjaKorisnici(), false);

		return response;

	}

	// LOGIN
	@POST
	@Path("/login")
	@Produces(MediaType.TEXT_PLAIN)
	public String registracija(NoviLogin nl) {
		Korisnici korisnici = getKorisnici();
		if (!korisnici.getKorisnici().containsKey(nl.korIme)) {
			return "no";
		}
		if (korisnici.getKorisnici().get(nl.korIme).getPassword().equals(nl.lozinka)) {
			request.getSession().setAttribute("korIme", nl.korIme);
			if (korisnici.getKorisnici().get(nl.korIme).getTipKorisnika().equals("Admin")) {

				return "admin";
			} else if (korisnici.getKorisnici().get(nl.korIme).getTipKorisnika().equals("Kupac")) {
				Kupci kupci = getKupci();
				if (!kupci.getKupci().containsKey(nl.korIme)) {
					Kupac kupac = new Kupac();
					kupci.getKupci().put(nl.korIme, kupac);

				}

				return "kupac";
			}

			else if (korisnici.getKorisnici().get(nl.korIme).getTipKorisnika().equalsIgnoreCase("Prodavac")) {
				Prodavci prodavci = getProdavci();

				if (!prodavci.getSellers().containsKey(nl.korIme)) {
					Prodavac prodavac = new Prodavac();
					prodavci.getSellers().put(nl.korIme, prodavac);

				}

				return "prodavac";
			}
		}

		return "no";
	}

	/* OGLASI */
	// cuvanje oglasa
	@POST
	@Path("/napravi_oglas")
	@Produces(MediaType.TEXT_PLAIN)
	public String napravi_oglas(NoviOglas og) {
		String korIme = (String) request.getSession().getAttribute("korIme");
		LocalDate date = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");// trenutni datum
		String uuid = UUID.randomUUID().toString();// random id za oglas

		Oglas oglas = new Oglas(uuid, og.ime, og.cena, og.opis, 0, 0, og.slika, false, date.format(formatter), og.top,
				0, og.aktivan, true, og.grad, og.kat, "erceg", "erceg", korIme);
		Oglasi oglasi = getOglasi();
		Kategorije kategorije = getKategorije();

		// Prodavci prodavci = getProdavci();
		String response = kategorije.dodajOglasUKategoriju(oglas, oglasi);

		oglasi.save(putanja(), false);
		return response;

	}

	// PRETRAGA
	@POST
	@Path("/set_pretraga")
	@Produces(MediaType.TEXT_PLAIN)

	public String set_pretraga(OglasPosrednikZaPretragu op) {
		request.getSession().setAttribute("oglasPretraga", op);
		return "ok";
	}

	@POST
	@Path("/set_pretraga_korisnika")
	@Produces(MediaType.TEXT_PLAIN)
	public String set_pretraga_korisnika(KorisnikDto kDto) {
		request.getSession().setAttribute("korisnikPretraga", kDto);
		return "ok";
	}

	@GET
	@Path("/pretraga_korisnika")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Korisnik> pretraga_korisnika() {
		Korisnici korisnici = getKorisnici();
		KorisnikDto korisnikDto = (KorisnikDto) request.getSession().getAttribute("korisnikPretraga");
		ArrayList<Korisnik> trazeniKorisnik = new ArrayList<Korisnik>();
		for (Korisnik k : korisnici.getKorisnici().values()) {
			trazeniKorisnik.add(k);
		}
		ArrayList<Korisnik> korisnikZaBrisanje = new ArrayList<Korisnik>();
		ArrayList<String> aktivniFilteri = getAktivniFilteri(korisnikDto);

		for (int i = 0; i < aktivniFilteri.size(); i++) {
			switch (aktivniFilteri.get(i)) {
			case "naziv":
				korisnikZaBrisanje = new ArrayList<Korisnik>();
				String nazivParam = korisnikDto.naziv.toLowerCase();
				for (Korisnik korisnik : trazeniKorisnik) {
					String nazivKorisnika = korisnik.getUsername().toLowerCase();
					if (!nazivKorisnika.contains(nazivParam)) {
						korisnikZaBrisanje.add(korisnik);
					}
				}
				trazeniKorisnik.removeAll(korisnikZaBrisanje);
				break;
			case "gradOglasa":
				korisnikZaBrisanje = new ArrayList<Korisnik>();
				for (Korisnik korisnik : trazeniKorisnik) {
					if (!korisnik.getCity().equals(korisnikDto.grad)) {
						korisnikZaBrisanje.add(korisnik);
					}
				}
				trazeniKorisnik.removeAll(korisnikZaBrisanje);
				break;
			/*
			 * case "status": oglasiZaBrisanje = new ArrayList<Oglas>(); for (Oglas oglas :
			 * trazeniOglasi) { if (oglas.getPorucen() != oglasPosZaPretragu.status) {
			 * oglasiZaBrisanje.add(oglas); } } trazeniOglasi.removeAll(oglasiZaBrisanje);
			 * break;
			 */
			}
		}

		request.getSession().setAttribute("korisnikPretraga", null);
		return trazeniKorisnik;
	}

	@POST
	@Path("/setFilterOglasa")
	@Produces(MediaType.TEXT_PLAIN)
	public String setFilterOglasa(OglasPosrednikZaPretragu opz) {
		request.getSession().setAttribute("posrednikFilter", opz);
		return "ok";
	}

	@GET
	@Path("/filterOglasa")
	@Produces(MediaType.APPLICATION_JSON)

	public ArrayList<Oglas> filterOglasa() {
		Oglasi oglasi = getOglasi();
		OglasPosrednikZaPretragu opzk = (OglasPosrednikZaPretragu) request.getSession().getAttribute("posrednikFilter");
		ArrayList<Oglas> ogl = new ArrayList<Oglas>();
		for (Oglas o : oglasi.getOglasi().values()) {
			if (o.isAktivan() == true && opzk.status.equals("Aktivan")) {
				ogl.add(o);
			} else if (o.isAktivan() == false && opzk.status.equals("Neaktivan")) {
				ogl.add(o);
			} else if (opzk.status.equals("Svi")) {
				ogl.add(o);
			}
		}
		return ogl;

	}

	@GET
	@Path("/pretraga_oglasa")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Oglas> pretraga_oglasa() throws ParseException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		Oglasi oglasi = getOglasi();
		OglasPosrednikZaPretragu oglasPosZaPretragu = (OglasPosrednikZaPretragu) request.getSession()
				.getAttribute("oglasPretraga");
		ArrayList<Oglas> trazeniOglasi = new ArrayList<Oglas>();
		for (Oglas o : oglasi.getOglasi().values()) {
			if (o.getuRealizaciji().equals("erceg")) {
				trazeniOglasi.add(o);
			}

		}
		ArrayList<Oglas> oglasiZaBrisanje = new ArrayList<Oglas>();
		ArrayList<String> aktivniFilteri = getAktivniFilteri(oglasPosZaPretragu);

		for (int i = 0; i < aktivniFilteri.size(); i++) {
			switch (aktivniFilteri.get(i)) {
			case "naziv":
				oglasiZaBrisanje = new ArrayList<Oglas>();
				String nazivParam = oglasPosZaPretragu.naziv.toLowerCase();
				for (Oglas oglas : trazeniOglasi) {
					String nazivOglasa = oglas.getName().toLowerCase();
					if (!nazivOglasa.contains(nazivParam)) {
						oglasiZaBrisanje.add(oglas);
					}
				}
				trazeniOglasi.removeAll(oglasiZaBrisanje);
				break;
			case "minCena":
				oglasiZaBrisanje = new ArrayList<Oglas>();
				for (Oglas oglas : trazeniOglasi) {
					if (Integer.parseInt(oglas.getPrice()) < oglasPosZaPretragu.minCena) {
						oglasiZaBrisanje.add(oglas);
					}
				}
				trazeniOglasi.removeAll(oglasiZaBrisanje);
				break;
			case "maxCena":
				oglasiZaBrisanje = new ArrayList<Oglas>();
				for (Oglas oglas : trazeniOglasi) {
					if (Integer.parseInt(oglas.getPrice()) > oglasPosZaPretragu.maxCena) {
						oglasiZaBrisanje.add(oglas);
					}
				}
				trazeniOglasi.removeAll(oglasiZaBrisanje);
				break;
			case "minOcena":
				oglasiZaBrisanje = new ArrayList<Oglas>();
				for (Oglas oglas : trazeniOglasi) {
					if (oglas.getLike() < oglasPosZaPretragu.minOcena) {
						oglasiZaBrisanje.add(oglas);
					}
				}
				trazeniOglasi.removeAll(oglasiZaBrisanje);
				break;
			case "maxOcena":
				oglasiZaBrisanje = new ArrayList<Oglas>();
				for (Oglas oglas : trazeniOglasi) {
					if (oglas.getLike() > oglasPosZaPretragu.maxOcena) {
						oglasiZaBrisanje.add(oglas);
					}
				}
				trazeniOglasi.removeAll(oglasiZaBrisanje);
				break;
			case "minDatum":
				oglasiZaBrisanje = new ArrayList<Oglas>();
				for (Oglas oglas : trazeniOglasi) {
					String dateString1 = oglas.getDateTop();
					String dateString2 = oglasPosZaPretragu.minDatum;
					LocalDate date = LocalDate.parse(dateString1);
					LocalDate date1 = LocalDate.parse(dateString2);

					if (date.isBefore(date1)) {
						oglasiZaBrisanje.add(oglas);
					}
				}
				trazeniOglasi.removeAll(oglasiZaBrisanje);
				break;
			case "maxDatum":
				oglasiZaBrisanje = new ArrayList<Oglas>();
				for (Oglas oglas : trazeniOglasi) {

					String dateString1 = oglas.getDateTop();
					String dateString2 = oglasPosZaPretragu.maxDatum;
					LocalDate date = LocalDate.parse(dateString1);
					LocalDate date1 = LocalDate.parse(dateString2);

					if (date.isAfter(date1)) {
						oglasiZaBrisanje.add(oglas);
					}
				}
				trazeniOglasi.removeAll(oglasiZaBrisanje);
				break;
			case "gradOglasa":
				oglasiZaBrisanje = new ArrayList<Oglas>();
				for (Oglas oglas : trazeniOglasi) {
					if (!oglas.getCity().equals(oglasPosZaPretragu.grad)) {
						oglasiZaBrisanje.add(oglas);
					}
				}
				trazeniOglasi.removeAll(oglasiZaBrisanje);
				break;

			case "status":
				oglasiZaBrisanje = new ArrayList<Oglas>();
				for (Oglas oglas : trazeniOglasi) {
					if (oglas.isAktivan() == true && !oglasPosZaPretragu.status.equals("Aktivan")) {
						oglasiZaBrisanje.add(oglas);
					}

					if (oglas.isAktivan() == false && !oglasPosZaPretragu.status.equals("Nije aktivan")) {
						oglasiZaBrisanje.add(oglas);
					}
				}
				trazeniOglasi.removeAll(oglasiZaBrisanje);
				break;

			}
		}

		request.getSession().setAttribute("oglasPretraga", null);
		return trazeniOglasi;
	}

	@POST
	@Path("/odjava")
	@Produces(MediaType.TEXT_PLAIN)
	public String odjava() {
		request.getSession().setAttribute("korIme", null);
		return "ok";
	}

	@GET
	@Path("/svi_oglasi_admin")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> svi_oglasi_admin() {
		ArrayList<Oglas> ogl = new ArrayList<Oglas>();
		Oglasi oglasi = getOglasi();
		for (Oglas o : oglasi.getOglasi().values()) {
			ogl.add(o);
		}
		return ogl;
	}

	@GET
	@Path("/gradovi_oglasa")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<String> gradovi_oglasa() {
		ArrayList<String> ogl = new ArrayList<String>();
		Oglasi oglasi = getOglasi();
		for (Oglas o : oglasi.getOglasi().values()) {
			if (!ogl.contains(o.getCity())) {
				ogl.add(o.getCity());
			}

		}

		return ogl;

	}

	// IZMENA OGLASA
	@POST
	@Path("/napravi_izmenjen_oglas")
	@Produces(MediaType.TEXT_PLAIN)
	public String napravi_izmenjen_oglas(NoviOglas og) {
		String korIme = (String) request.getSession().getAttribute("korIme");

		Oglasi oglasi = getOglasi();
		Kategorije kategorije = getKategorije();

		// Prodavci prodavci = getProdavci();
		oglasi.getOglasi().get(og.id).setName(og.ime);
		oglasi.getOglasi().get(og.id).setDescribe(og.opis);
		oglasi.getOglasi().get(og.id).setCity(og.grad);
		oglasi.getOglasi().get(og.id).setPrice(og.cena);
		oglasi.getOglasi().get(og.id).setKategorija(og.kat);
		oglasi.getOglasi().get(og.id).setImage(og.slika);
		oglasi.getOglasi().get(og.id).setAktivan(og.aktivan);
		oglasi.getOglasi().get(og.id).setDateTop(og.top);

		oglasi.save(putanja(), false);
		return "ok";

	}

	// TOP10 oglasa
	@GET
	@Path("/svi_oglasi")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> svi_oglasi() {
		ArrayList<Oglas> ogl = new ArrayList<Oglas>();

		Oglasi oglasi = getOglasi();
		int br = 0;
		oglasi.loadOmiljeni(putanjaOmiljeni());
		ArrayList<Oglas> oglasiPoOmiljenim = new ArrayList<Oglas>();
		for (Oglas o : oglasi.getOglasi().values()) {
			if (o.isActive() == true) {
				oglasiPoOmiljenim.add(o);
			}

		}
		Collections.sort(oglasiPoOmiljenim);
		Collections.reverse(oglasiPoOmiljenim);
		for (Oglas o : oglasiPoOmiljenim) {

			ogl.add(o);
			br++;
			if (br == 10) {
				break;
			}

		}

		return ogl;
	}

	// brisanje oglasa
	@DELETE
	@Path("/obrisi")
	@Produces(MediaType.TEXT_PLAIN)
	public String obrisi(String naziv) throws IOException {
		Prodavci prodavci = getProdavci();
		Korisnici korisnici = getKorisnici();
		LocalDate date = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String uuid = UUID.randomUUID().toString();
		String korIme = (String) request.getSession().getAttribute("korIme");
		Oglasi oglasi = getOglasi();
		if (!oglasi.getOglasi().get(naziv).getuRealizaciji().equals("erceg")) {
			return "realizacija";
		}
		for (Korisnik k : korisnici.getKorisnici().values()) {
			if (k.getTipKorisnika().equals("Admin")) {
				Poruka poruka = new Poruka(uuid, "OBRISAN oglas " + oglasi.getOglasi().get(naziv).getName(), korIme,
						"PORUDZBINA", "Postovani ADMINE " + korIme + " je izbrisao oglas", date.format(formatter),
						oglasi.getOglasi().get(naziv).getProdavac());
				k.getPrimljene_poruke().put(poruka.getId(), poruka);
				korisnici.savePrimljenePoruke(putanjaPrimljenePoruke(), false, k.getName());

			}
		}

		oglasi.ukloniOglas(naziv);

		prodavci.deleteSeller(korIme, naziv);
		oglasi.save(putanja(), false);

		return "no";
	}

	@DELETE
	@Path("/obrisi_admin")
	@Produces(MediaType.TEXT_PLAIN)
	public String obrisi_admin(String naziv) throws IOException {

		Oglasi oglasi = getOglasi();
		String korIme = (String) request.getSession().getAttribute("korIme");
		Korisnici korisnici = getKorisnici();
		LocalDate date = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String uuid = UUID.randomUUID().toString();
		Poruka poruka = new Poruka(uuid, "OBRISAN oglas " + oglasi.getOglasi().get(naziv).getName(), korIme,
				"PORUDZBINA", "Postovani prodavce ADMIN " + korIme + " je izbrisao vas oglas", date.format(formatter),
				oglasi.getOglasi().get(naziv).getProdavac());
		korisnici.getKorisnici().get(oglasi.getOglasi().get(naziv).getProdavac()).getPrimljene_poruke()
				.put(poruka.getId(), poruka);

		System.out.println(oglasi.getOglasi().get(naziv).isActive());
		System.out.println(oglasi.getOglasi().get(naziv).getProdavac());
		if (oglasi.getOglasi().get(naziv).isActive() == false) {
			korisnici.savePrimljenePoruke(putanjaPrimljenePoruke(), false,
					oglasi.getOglasi().get(naziv).getuRealizaciji());
		}

		korisnici.savePrimljenePoruke(putanjaPrimljenePoruke(), false, oglasi.getOglasi().get(naziv).getProdavac());
		oglasi.ukloniOglas(naziv);
		oglasi.save(putanja(), false);
		return "no";
	}

	// izmena oglasa
	@POST
	@Path("/izmeni_oglas")
	@Produces(MediaType.TEXT_PLAIN)
	public String izmeni_oglas(String id) {
		request.getSession().setAttribute("IdIzmenaOglasa", id);
		return "ok";
	}

	@GET
	@Path("/izmena_oglasa")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> izmena_oglasa() {

		Oglasi oglasi = getOglasi();
		String id = (String) request.getSession().getAttribute("IdIzmenaOglasa");
		ArrayList<Oglas> ogl = oglasi.izmenaOglasa(id);

		return ogl;
	}

	@POST
	@Path("/prijava")
	@Produces(MediaType.TEXT_PLAIN)
	public String prijava(String id) {
		Oglasi oglasi = getOglasi();
		String result = "";
		Korisnici korisnici = getKorisnici();
		LocalDate date = LocalDate.now();
		String korIme = (String) request.getSession().getAttribute("korIme");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String uuid = UUID.randomUUID().toString();
		Poruka poruka = new Poruka(uuid, "PRIJAVLJEN oglas " + oglasi.getOglasi().get(id).getName(), korIme, "PRIJAVA",
				"Postovani prodavce  vas oglas je preksio pravila koriscenja i dobili ste jedno od 5 mogucih upozorenja nakon 5 upozorenja vasem nalogu ce biti onemoguceno dodavanje oglasa",
				date.format(formatter), oglasi.getOglasi().get(id).getProdavac());
		
		korisnici.getKorisnici().get(oglasi.getOglasi().get(id).getProdavac()).getPrimljene_poruke().put(poruka.getId(),
				poruka);
		korisnici.savePrimljenePoruke(putanjaPrimljenePoruke(), false, oglasi.getOglasi().get(id).getProdavac());
		oglasi.save(putanja(), false);
		return "ok";
	}

	/* KATEGORIJE */
	@POST
	@Path("/napravi_kategoriju")
	@Produces(MediaType.TEXT_PLAIN)
	public String napravi_kategoriju(NovaKategorija nk) {
		String uuid = UUID.randomUUID().toString();// random id za kategoriju
		Kategorije kategorije = getKategorije();
		Kategorija kategorija = new Kategorija(uuid, nk.naziv, nk.opis);
		kategorije.getKategorije().put(uuid, kategorija);
		kategorije.save(putanja1(), false);
		return "ok";

	}

	// prikaz kategorije
	@GET
	@Path("/prikazi_kategorije")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Kategorija> prikazi_kategorije() {

		Kategorije kategorije = getKategorije();
		ArrayList<Kategorija> kat = new ArrayList<Kategorija>();
		for (Kategorija k : kategorije.getKategorije().values()) {
			kat.add(k);
		}
		return kat;

	}

	// id za izmenu kategorije
	@POST
	@Path("/kat_id")
	@Produces(MediaType.TEXT_PLAIN)
	public String kat_id(String id) {
		request.getSession().setAttribute("katIdIzmena", id);
		return "ok";
	}

	// izmena kategorije
	@GET
	@Path("/izmeni_kategoriju")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Kategorija> izmeni_kategoriju() {
		ArrayList<Kategorija> kat = new ArrayList<Kategorija>();
		String id = (String) request.getSession().getAttribute("katIdIzmena");

		Kategorije kategorije = getKategorije();
		kat.add(kategorije.getKategorije().get(id));
		return kat;
	}

	@PUT
	@Path("/izmenjena_kategorija")
	@Produces(MediaType.TEXT_PLAIN)
	public String izmenjena_kategorija(NovaKategorija nk) {
		Kategorije kategorije = getKategorije();
		Oglasi oglasi = getOglasi();

		for (Oglas o : oglasi.getOglasi().values()) {
			if (o.getKategorija().equals(kategorije.getKategorije().get(nk.id).getName())) {
				o.setKategorija(nk.naziv);
			}
		}
		kategorije.getKategorije().get(nk.id).setName(nk.naziv);
		kategorije.getKategorije().get(nk.id).setDescribe(nk.opis);
		kategorije.save(putanja1(), false);
		oglasi.save(putanja(), false);

		return "ok";
	}

	// brisanje kategorie
	@DELETE
	@Path("/obrisikat")
	@Produces(MediaType.TEXT_PLAIN)
	public String obrisikat(String id) throws IOException {
		Kategorije kategorije = getKategorije();
		kategorije.brisanjeKategorije(id);
		kategorije.save(putanja1(), false);
		return "ok";
	}

	@POST
	@Path("/oglasikat")
	@Produces(MediaType.TEXT_PLAIN)
	public String oglasikat(String id) throws NullPointerException {
		request.getSession().setAttribute("Id", id);
		return "ok";
	}

	@GET
	@Path("/prikazizakat")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> prikazizakat() throws NullPointerException {
		ArrayList<Oglas> ogl = new ArrayList<Oglas>();
		Kategorije kategorije = getKategorije();
		Oglasi oglasi = getOglasi();

		String id = (String) request.getSession().getAttribute("Id");
		if (id == null) {
			return ogl;
		}

		for (Oglas o : oglasi.getOglasi().values()) {
			if (o.getKategorija().equals(kategorije.getKategorije().get(id).getName()) && o.isActive() == true) {
				ogl.add(o);
			}
		}

		return ogl;
	}

	// id za Oglas
	@POST
	@Path("/oglas")
	@Produces(MediaType.TEXT_PLAIN)
	public String oglas(String id) {
		request.getSession().setAttribute("IdOglas", id);
		return "ok";
	}

	// Prikaz oglasa
	@GET
	@Path("/oglasprik")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> oglasprik() {
		ArrayList<Oglas> ogl = new ArrayList<Oglas>();
		String id = (String) request.getSession().getAttribute("IdOglas");
		Oglasi oglasi = getOglasi();
		ogl.add(oglasi.getOglasi().get(id));
		return ogl;
	}

	// LIKE
	@POST
	@Path("/like")
	@Produces(MediaType.TEXT_PLAIN)
	public String like(String id) {
		Oglasi oglasi = getOglasi();
		Korisnici korisnici = getKorisnici();
		String korIme = (String) request.getSession().getAttribute("korIme");
		if (korIme == null) {
			return "moras";
		}

		else if (korisnici.getKorisnici().get(korIme).getTipKorisnika().equals("Prodavac")
				|| korisnici.getKorisnici().get(korIme).getTipKorisnika().equals("Admin")) {
			return "no";
		}

		oglasi.getOglasi().get(id).setLike(oglasi.getOglasi().get(id).getLike() + 1);
		oglasi.save(putanja(), false);
		return "ok";
	}

	@POST
	@Path("/dislike")
	@Produces(MediaType.TEXT_PLAIN)
	public String dislike(String id) {
		Oglasi oglasi = getOglasi();
		Korisnici korisnici = getKorisnici();
		String korIme = (String) request.getSession().getAttribute("korIme");
		if (korIme == null) {
			return "moras";
		}

		else if (korisnici.getKorisnici().get(korIme).getTipKorisnika().equals("Prodavac")
				|| korisnici.getKorisnici().get(korIme).getTipKorisnika().equals("Admin")) {
			return "no";
		}

		oglasi.getOglasi().get(id).setDislike(oglasi.getOglasi().get(id).getDislike() + 1);
		oglasi.save(putanja(), false);
		return "ok";
	}

	/* KUPAC */

	@POST
	@Path("/poruci")
	@Produces(MediaType.TEXT_PLAIN)
	public String poruci(String id) {
		request.getSession().setAttribute("idPorucbina", id);
		Korisnici korisnici = getKorisnici();
		Oglasi oglasi = getOglasi();
		String korIme = (String) request.getSession().getAttribute("korIme");

		oglasi.getOglasi().get(id).setuRealizaciji(korIme);
		Kupci kupci = getKupci();

		if (korIme == null) {
			return "moras";
		} else if (korisnici.getKorisnici().get(korIme).getTipKorisnika().equals("Prodavac")
				|| korisnici.getKorisnici().get(korIme).getTipKorisnika().equals("Admin")) {
			return "no";
		}

		kupci.getKupci().get(korIme).getOrdered().put(id, oglasi.getOglasi().get(id));
		oglasi.getOglasi().get(id).setActive(false);
		LocalDate date = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		Poruka poruka = new Poruka(id, "PORUCEN oglas " + oglasi.getOglasi().get(id).getName(), korIme, "PORUDZBINA",
				"Postovani prodavce korisnik " + korIme + " je porucio vas oglas", date.format(formatter),
				oglasi.getOglasi().get(id).getProdavac());
		korisnici.getKorisnici().get(oglasi.getOglasi().get(id).getProdavac()).getPrimljene_poruke().put(poruka.getId(),
				poruka);
		oglasi.save(putanja(), false);
		korisnici.savePrimljenePoruke(putanjaPrimljenePoruke(), false, oglasi.getOglasi().get(id).getProdavac());

		return "ok";
	}

	@GET
	@Path("/realizacija")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> realizacija() {
		ArrayList<Oglas> ogl = new ArrayList<Oglas>();

		String korIme = (String) request.getSession().getAttribute("korIme");
		Kupci kupci = getKupci();
		Oglasi oglasi = getOglasi();
		for (Oglas o : oglasi.getOglasi().values()) {
			if (o.getuRealizaciji().equals(korIme)) {
				if (o.getuRealizaciji().equals("")) {
					return ogl;
				}
				kupci.getKupci().get(korIme).getOrdered().put(o.getId(), o);
			}
		}
		for (Oglas o : kupci.getKupci().get(korIme).getOrdered().values()) {
			if (o.isDostavljen() == false) {
				ogl.add(o);
			}

		}

		return ogl;
	}

	@POST
	@Path("/dostavljeno")
	@Produces(MediaType.TEXT_PLAIN)
	public String dostavljeno(String id) {
		Kupci kupci = getKupci();
		String korIme = (String) request.getSession().getAttribute("korIme");
		Oglasi oglasi = getOglasi();
		Korisnici korisnici = getKorisnici();
		LocalDate date = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		Poruka poruka = new Poruka(id, "DOSTAVLJEN oglas" + oglasi.getOglasi().get(id).getName(), korIme,
				"USPESNO DOSTAVLJEN", "Postovani prodavce korisniku " + korIme + " je uspesno dostavljen vas oglas",
				date.format(formatter), oglasi.getOglasi().get(id).getProdavac());
		oglasi.getOglasi().get(id).setPorucen(korIme);
		kupci.getKupci().get(korIme).getDelivered().put(id, oglasi.getOglasi().get(id));
		kupci.getKupci().get(korIme).getOrdered().remove(id);
		korisnici.getKorisnici().get(oglasi.getOglasi().get(id).getProdavac()).getPrimljene_poruke().put(poruka.getId(),
				poruka);
		oglasi.getOglasi().get(id).setDostavljen(true);
		oglasi.save(putanja(), false);
		korisnici.savePrimljenePoruke(putanjaPrimljenePoruke(), false, oglasi.getOglasi().get(id).getProdavac());
		oglasi.save(putanja(), false);
		return "ok";
	}

	@GET
	@Path("/dostavljeni")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> dostavljeni() {
		ArrayList<Oglas> ogl = new ArrayList<Oglas>();
		Kupci kupci = getKupci();
		String korIme = (String) request.getSession().getAttribute("korIme");
		Oglasi oglasi = getOglasi();

		for (Oglas o : oglasi.getOglasi().values()) {
			if (o.getPorucen().equals(korIme)) {
				kupci.getKupci().get(korIme).getDelivered().put(o.getId(), o);
			}
		}

		for (Oglas o : kupci.getKupci().get(korIme).getDelivered().values()) {
			ogl.add(o);
		}
		return ogl;

	}

	@POST
	@Path("/omiljen")
	@Produces(MediaType.TEXT_PLAIN)
	public String omiljen(String id) {
		Oglasi oglasi = getOglasi();
		String korIme = (String) request.getSession().getAttribute("korIme");
		oglasi.loadOmiljeni(putanjaOmiljeni());
		String uuid = UUID.randomUUID().toString();// random id za omiljen
		Korisnici korisnici = getKorisnici();
		if (oglasi.getOglasi().get(id).getOmiljen().containsValue(korIme)) {
			return "vec";
		}
		if (korIme == null) {
			return "moras";
		}

		if (korisnici.getKorisnici().get(korIme).getTipKorisnika().equals("Prodavac")
				|| korisnici.getKorisnici().get(korIme).getTipKorisnika().equals("Admin")) {
			return "no";
		}

		oglasi.getOglasi().get(id).getOmiljen().put(uuid, korIme);
		oglasi.saveOmiljeni(putanjaOmiljeni(), true, id, uuid, korIme);
		return "ok";

	}

	@GET
	@Path("/omiljeni")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> omiljeni() {
		ArrayList<Oglas> ogl = new ArrayList<Oglas>();
		Kupci kupci = (Kupci) request.getSession().getAttribute("kupci");
		String korIme = (String) request.getSession().getAttribute("korIme");
		Oglasi oglasi = getOglasi();
		oglasi.loadOmiljeni(putanjaOmiljeni());

		for (Oglas ogla : oglasi.getOglasi().values()) {
			for (String str : ogla.getOmiljen().values()) {
				kupci.getKupci().get(str).getFavorite().put(ogla.getId(), ogla);
			}
		}

		for (Oglas o : kupci.getKupci().get(korIme).getFavorite().values()) {
			ogl.add(o);
		}
		return ogl;

	}

	/* PRODAVAC */
	@GET
	@Path("/prodavcevi_oglasi")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> prodavcevi_oglasi() {
		ArrayList<Oglas> ogl = new ArrayList<Oglas>();
		String korIme = (String) request.getSession().getAttribute("korIme");
		Prodavci prodavci = (Prodavci) request.getSession().getAttribute("prodavci");

		Oglasi oglasi = getOglasi();

		for (Oglas o : oglasi.getOglasi().values()) {
			if (o.getProdavac().equals(korIme)) {
				prodavci.getSellers().get(korIme).getBroadcastedPosts().put(o.getId(), o);
			}
		}

		for (Oglas o : prodavci.getSellers().get(korIme).getBroadcastedPosts().values()) {
			ogl.add(o);
		}

		return ogl;

	}

	@GET
	@Path("/prodavac_recenzije")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Recenzija> prodavac_recenzije() {
		ArrayList<Recenzija> rec = new ArrayList<Recenzija>();
		String korIme = (String) request.getSession().getAttribute("korIme");
		Prodavci prodavci = (Prodavci) request.getSession().getAttribute("prodavci");
		Oglasi oglasi = getOglasi();

		for (Oglas o : oglasi.getOglasi().values()) {
			if (prodavci.getSellers().get(korIme).getBroadcastedPosts().containsKey(o.getId())) {
				for (Recenzija r : o.getRecension().values()) {
					rec.add(r);
				}
			}
		}
		return rec;

	}

	@DELETE
	@Path("/obrisi_recenziju")
	@Produces(MediaType.TEXT_PLAIN)
	public String obrisi_recenziju(String id) {
		Oglasi oglasi = getOglasi();
		oglasi.ukloniRecenziju(id);
		return "ok";
	}

	/* ADMIN */
	// prikaz svih korisnika
	@GET
	@Path("/korisnici")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Korisnik> korisnici() {
		ArrayList<Korisnik> kor = new ArrayList<Korisnik>();
		Korisnici korisnici = getKorisnici();

		for (Korisnik korisnik : korisnici.getKorisnici().values()) {
			kor.add(korisnik);
		}

		return kor;
	}

	// menjanje uloge
	@POST
	@Path("/promeniulogu")
	@Produces(MediaType.TEXT_PLAIN)
	public String promeniulogu(NovaUloga nu) {
		Korisnici korisnici = getKorisnici();

		korisnici.getKorisnici().get(nu.korIme).setTipKorisnika(nu.uloga);
		korisnici.save(putanjaKorisnici(), false);
		return "ok";
	}

	/* PORUKE */
	@PUT
	@Path("/posalji_poruku")
	@Produces(MediaType.TEXT_PLAIN)
	public String posalji_poruku(NovaPoruka np) {
		Korisnici korisnici = getKorisnici();
		LocalDate date = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String uuid = UUID.randomUUID().toString();
		String korIme = (String) request.getSession().getAttribute("korIme");
		Poruka poruka = new Poruka(uuid, np.naziv, korIme, np.naslov, np.sadrzaj, formatter.format(date), np.primalac);
		korisnici.getKorisnici().get(np.primalac).getPrimljene_poruke().put(poruka.getId(), poruka);
		korisnici.getKorisnici().get(korIme).getPoslate_poruke().put(poruka.getId(), poruka);
		korisnici.savePoslatePoruke(putanjaPoslatePoruke(), false, korIme);
		korisnici.savePrimljenePoruke(putanjaPrimljenePoruke(), false, np.primalac);
		return "ok";
	}

	// prikazi primaoce koji se prikazuju u zavisnosti koju ulogu ima korisnik koji
	// salje poruku
	// ako je korisnik on moze da vidi samo prodavce i njima salje, admin moze
	// svima, a prodavac moze samo korisnicima.
	@GET
	@Path("/primaoci")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Korisnik> primaoci() {
		ArrayList<Korisnik> kor = new ArrayList<Korisnik>();
		HashMap<String, Korisnik> mapa = new HashMap<String, Korisnik>();
		String korIme = (String) request.getSession().getAttribute("korIme");
		Korisnici korisnici = getKorisnici();

		for (Korisnik k : korisnici.getKorisnici().values()) {
			if (korisnici.getKorisnici().get(korIme).getTipKorisnika().equals("Kupac")) {
				if (k.getTipKorisnika().equals("Prodavac")) {
					kor.add(k);
				}
			} else if (korisnici.getKorisnici().get(korIme).getTipKorisnika().equals("Prodavac")) {
				for (Poruka p : korisnici.getKorisnici().get(korIme).getPrimljene_poruke().values()) {
					if (k.getUsername().equals(p.getSender())) {
						mapa.put(k.getUsername(), k);
					}
				}

			} else if (korisnici.getKorisnici().get(korIme).getTipKorisnika().equals("Admin")) {
				kor.add(k);
			}

		}
		for (Korisnik korMap : mapa.values()) {
			kor.add(korMap);
		}

		return kor;

	}

	// prikazi poslate poruke
	@GET
	@Path("/prikazi_poslate")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Poruka> prikazi_poslate() {
		ArrayList<Poruka> por = new ArrayList<Poruka>();
		String korIme = (String) request.getSession().getAttribute("korIme");
		Korisnici korisnici = getKorisnici();
		for (Poruka p : korisnici.getKorisnici().get(korIme).getPoslate_poruke().values()) {
			por.add(p);
		}

		return por;

	}

	@GET
	@Path("/prikazi_primljene")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Poruka> prikazi_primljene() {
		ArrayList<Poruka> por = new ArrayList<Poruka>();
		String korIme = (String) request.getSession().getAttribute("korIme");
		Korisnici korisnici = getKorisnici();

		for (Poruka p : korisnici.getKorisnici().get(korIme).getPrimljene_poruke().values()) {
			por.add(p);
		}

		return por;

	}

	@POST
	@Path("/obrisiposlatu")
	@Produces(MediaType.TEXT_PLAIN)
	public String obrisiposlatu(String id) {
		Korisnici korisnici = getKorisnici();
		String korIme = (String) request.getSession().getAttribute("korIme");

		korisnici.getKorisnici().get(korIme).getPoslate_poruke().remove(id);
		korisnici.savePoslatePoruke(putanjaPoslatePoruke(), false, korIme);
		return "ok";
	}

	@POST
	@Path("/obrisiprimljenu")
	@Produces(MediaType.TEXT_PLAIN)
	public String obrisiprimljenu(String id) {
		Korisnici korisnici = getKorisnici();
		String korIme = (String) request.getSession().getAttribute("korIme");
		korisnici.getKorisnici().get(korIme).getPrimljene_poruke().remove(id);
		korisnici.savePrimljenePoruke(putanjaPrimljenePoruke(), false, korIme);
		return "ok";
	}
	// izmena poruka

	@POST
	@Path("izmeniposlatu")
	@Produces(MediaType.TEXT_PLAIN)
	public String izmeniposlatu(String id) {
		request.getSession().setAttribute("idPorukaZaIzmenu", id);
		return "ok";
	}

	@GET
	@Path("/izmeniporuku")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Poruka> izmeniporuku() {
		ArrayList<Poruka> por = new ArrayList<Poruka>();
		String id = (String) request.getSession().getAttribute("idPorukaZaIzmenu");
		Korisnici korisnici = getKorisnici();
		String korIme = (String) request.getSession().getAttribute("korIme");
		Poruka p = korisnici.getKorisnici().get(korIme).getPoslate_poruke().get(id);
		por.add(p);
		return por;
	}

	@POST
	@Path("/izporuku")
	@Produces(MediaType.TEXT_PLAIN)
	public String izporuku(NovaPoruka np) {
		Korisnici korisnici = getKorisnici();
		String korIme = (String) request.getSession().getAttribute("korIme");
		korisnici.getKorisnici().get(korIme).getPoslate_poruke().get(np.id).setContent(np.sadrzaj);
		korisnici.getKorisnici().get(korIme).getPoslate_poruke().get(np.id).setTitle(np.naslov);
		korisnici.getKorisnici().get(korIme).getPoslate_poruke().get(np.id).setNamePoster(np.naziv);

		korisnici.getKorisnici().get(np.primalac).getPrimljene_poruke().get(np.id).setContent(np.sadrzaj);
		korisnici.getKorisnici().get(np.primalac).getPrimljene_poruke().get(np.id).setTitle(np.naslov);
		korisnici.getKorisnici().get(np.primalac).getPrimljene_poruke().get(np.id).setNamePoster(np.naziv);

		korisnici.savePoslatePoruke(putanjaPoslatePoruke(), false, korIme);
		korisnici.savePrimljenePoruke(putanjaPrimljenePoruke(), false, np.primalac);

		return "ok";
	}

	/* RECENZIJE */
	@POST
	@Path("/recenzija")
	@Produces(MediaType.TEXT_PLAIN)
	public String recenzija(String id) {
		request.getSession().setAttribute("idOglasRec", id);
		return "ok";
	}

	@POST
	@Path("/napravi_recenziju")
	@Produces(MediaType.TEXT_PLAIN)
	public String napravi_recenziju(NovaRecenzija nr) {
		Oglasi oglasi = getOglasi();
		Korisnici korisnici = getKorisnici();
		String korIme = (String) request.getSession().getAttribute("korIme");
		String idOglasRec = (String) request.getSession().getAttribute("idOglasRec");
		String uuid = UUID.randomUUID().toString();
		String uuid2 = UUID.randomUUID().toString();
		LocalDate date = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		Recenzija recenzija = new Recenzija(uuid, idOglasRec, korIme, nr.naslov, nr.sadrzaj, nr.slika, nr.opis,
				nr.dogovor);
		Poruka poruka = new Poruka(uuid2, "RECENZIJA " + oglasi.getOglasi().get(idOglasRec).getName(), korIme,
				"PORUDZBINA", "Postovani prodavce Kupac " + korIme + " je poslao recenziju", date.format(formatter),
				oglasi.getOglasi().get(idOglasRec).getProdavac());
		korisnici.getKorisnici().get(oglasi.getOglasi().get(idOglasRec).getProdavac()).getPrimljene_poruke()
				.put(poruka.getId(), poruka);
		korisnici.savePrimljenePoruke(putanjaPrimljenePoruke(), false,
				oglasi.getOglasi().get(idOglasRec).getProdavac());
		oglasi.getOglasi().get(idOglasRec).getRecension().put(uuid, recenzija);
		oglasi.saveRecenzije(putanjaRecenzije(), false, idOglasRec);
		return "ok";
	}

	@GET
	@Path("/kupac_recenzije")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Recenzija> kupac_recenzije() {
		ArrayList<Recenzija> rec = new ArrayList<Recenzija>();
		Oglasi oglasi = getOglasi();
		String korIme = (String) request.getSession().getAttribute("korIme");
		for (Oglas o : oglasi.getOglasi().values()) {
			for (Recenzija r : o.getRecension().values()) {
				if (r.getRecesent().equals(korIme)) {
					rec.add(r);
				}
			}
		}

		return rec;
	}

	@POST
	@Path("/izmeni_recenziju")
	@Produces(MediaType.TEXT_PLAIN)
	public String izmeni_recenziju(String id) {
		request.getSession().setAttribute("idRecZaIzmenu", id);
		return "ok";
	}

	@GET
	@Path("/recenzija_izmena_prikaz")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Recenzija> recenzija_izmena_prikaz() {
		String id = (String) request.getSession().getAttribute("idRecZaIzmenu");
		ArrayList<Recenzija> rec = new ArrayList<Recenzija>();
		Oglasi oglasi = getOglasi();
		for (Oglas o : oglasi.getOglasi().values()) {
			if (o.getRecension().get(id) != null) {
				rec.add(o.getRecension().get(id));
			}
		}

		return rec;
	}

	@PUT
	@Path("/dodaj_izmenjenu_recenziju")
	@Produces(MediaType.TEXT_PLAIN)
	public String dodaj_izmenjenu_recenziju(NovaRecenzija nr) {
		Oglasi oglasi = getOglasi();
		Korisnici korisnici = getKorisnici();
		String korIme = (String) request.getSession().getAttribute("korIme");
		String idOglasRec = (String) request.getSession().getAttribute("idOglasRec");
		String uuid = UUID.randomUUID().toString();
		String uuid2 = UUID.randomUUID().toString();
		LocalDate date = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		oglasi.getOglasi().get(nr.oglas).getRecension().get(nr.id).setContent(nr.sadrzaj);
		oglasi.getOglasi().get(nr.oglas).getRecension().get(nr.id).setImage(nr.slika);
		oglasi.getOglasi().get(nr.oglas).getRecension().get(nr.id).setTitle(nr.naslov);
		oglasi.getOglasi().get(nr.oglas).getRecension().get(nr.id).setCorrect(nr.opis);
		oglasi.getOglasi().get(nr.oglas).getRecension().get(nr.id).setAgreed(nr.dogovor);
		Poruka poruka = new Poruka(uuid2, "RECENZIJA " + oglasi.getOglasi().get(nr.oglas).getName(), korIme,
				"PORUDZBINA", "Postovani prodavce Kupac " + korIme + " je izmenio recenziju", date.format(formatter),
				oglasi.getOglasi().get(nr.oglas).getProdavac());
		korisnici.getKorisnici().get(oglasi.getOglasi().get(nr.oglas).getProdavac()).getPrimljene_poruke()
				.put(poruka.getId(), poruka);
		korisnici.savePrimljenePoruke(putanjaPrimljenePoruke(), false, oglasi.getOglasi().get(nr.oglas).getProdavac());

		oglasi.saveRecenzije(putanjaRecenzije(), false, nr.oglas);
		return "ok";
	}
	/* Postavljanje konteksta */

	private Kategorije getKategorije() {
		Kategorije kategorije = (Kategorije) ctx.getAttribute("kategorije");
		if (kategorije == null) {
			kategorije = new Kategorije();
			kategorije.load(putanja1());
			ctx.setAttribute("kategorije", kategorije);
		}

		return kategorije;
	}

	private Oglasi getOglasi() {
		Oglasi oglasi = (Oglasi) ctx.getAttribute("oglasi");
		if (oglasi == null) {
			oglasi = new Oglasi();
			oglasi.load(putanja());
			oglasi.loadRecenzije(putanjaRecenzije());
			ctx.setAttribute("oglasi", oglasi);
		}

		return oglasi;
	}

	private Korisnici getKorisnici() {
		String korIme = (String) request.getSession().getAttribute("korIme");
		Korisnici korisnici = (Korisnici) ctx.getAttribute("korisnici");
		if (korisnici == null) {
			korisnici = new Korisnici();
			korisnici.load(putanjaKorisnici());
			korisnici.loadPrimljenePoruke(putanjaPrimljenePoruke(), korIme);
			korisnici.loadPoslatePoruke(putanjaPoslatePoruke(), korIme);
			ctx.setAttribute("korisnici", korisnici);

		}
		return korisnici;
	}

	private Kupci getKupci() {
		Kupci kupci = (Kupci) request.getSession().getAttribute("kupci");
		if (kupci == null) {
			kupci = new Kupci();
			request.getSession().setAttribute("kupci", kupci);
		}
		return kupci;
	}

	private Prodavci getProdavci() {
		Prodavci prodavci = (Prodavci) request.getSession().getAttribute("prodavci");
		if (prodavci == null) {
			prodavci = new Prodavci();
			request.getSession().setAttribute("prodavci", prodavci);
		}
		return prodavci;
	}

	private Omiljeni getOmiljeni() {
		Omiljeni omiljeni = (Omiljeni) ctx.getAttribute("omiljeni");
		if (omiljeni == null) {
			omiljeni = new Omiljeni();
			ctx.setAttribute("omiljeni", omiljeni);
		}
		return omiljeni;
	}

	@POST
	@Path("/postaviSliku")
	public String postaviSliku(String slika) {
		try {
			byte[] imageByte = DatatypeConverter.parseBase64Binary(slika);
			String b = DatatypeConverter.printBase64Binary(imageByte);
			return "data:image/jpeg;base64," + b.substring(20);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// ROLE
	@GET
	@Path("/proveri_role")
	public String proveri_role() {
		Korisnici korisnici = getKorisnici();
		String korIme = (String) request.getSession().getAttribute("korIme");
		if (korIme == null) {
			return "login";
		}

		else if (korisnici.getKorisnici().get(korIme).getTipKorisnika().equals("Admin")) {
			return "admin";
		} else if (korisnici.getKorisnici().get(korIme).getTipKorisnika().equals("Kupac")) {
			return "kupac";
		} else if (korisnici.getKorisnici().get(korIme).getTipKorisnika().equals("Prodavac")) {
			return "prodavac";
		}

		else {
			return "ok";
		}

	}

	/* PUTANJE */
	// putanja do ogalasa
	public String putanja() {
		File podaciDir = new File(System.getProperty("catalina.base") + File.separator + "podaci");
		if (!podaciDir.exists()) {
			podaciDir.mkdir();
		}
		return System.getProperty("catalina.base") + File.separator + "podaci" + File.separator + "oglasi.txt";
	}

	public String putanja1() {
		File podaciDir = new File(System.getProperty("catalina.base") + File.separator + "podaci");
		if (!podaciDir.exists()) {
			podaciDir.mkdir();
		}
		return System.getProperty("catalina.base") + File.separator + "podaci" + File.separator + "kategorije.txt";
	}

	public String putanjaKorisnici() {
		File podaciDir = new File(System.getProperty("catalina.base") + File.separator + "podaci");
		if (!podaciDir.exists()) {
			podaciDir.mkdir();
		}
		return System.getProperty("catalina.base") + File.separator + "podaci" + File.separator + "korisnici.txt";
	}

	public String putanjaPrimljenePoruke() {
		File podaciDir = new File(System.getProperty("catalina.base") + File.separator + "podaci");
		if (!podaciDir.exists()) {
			podaciDir.mkdir();
		}
		return System.getProperty("catalina.base") + File.separator + "podaci" + File.separator
				+ "primljene_poruke.txt";
	}

	public String putanjaPoslatePoruke() {
		File podaciDir = new File(System.getProperty("catalina.base") + File.separator + "podaci");
		if (!podaciDir.exists()) {
			podaciDir.mkdir();
		}
		return System.getProperty("catalina.base") + File.separator + "podaci" + File.separator + "poslate_poruke.txt";
	}

	public String putanjaRecenzije() {
		File podaciDir = new File(System.getProperty("catalina.base") + File.separator + "podaci");
		if (!podaciDir.exists()) {
			podaciDir.mkdir();
		}
		return System.getProperty("catalina.base") + File.separator + "podaci" + File.separator + "recenzije.txt";
	}

	public String putanjaOmiljeni() {
		File podaciDir = new File(System.getProperty("catalina.base") + File.separator + "podaci");
		if (!podaciDir.exists()) {
			podaciDir.mkdir();
		}
		return System.getProperty("catalina.base") + File.separator + "podaci" + File.separator + "omiljeni.txt";
	}

	private ArrayList<String> getAktivniFilteri(OglasPosrednikZaPretragu oglasPosZaPretragu) {
		ArrayList<String> aktivniFilteri = new ArrayList<String>();

		if (oglasPosZaPretragu == null) {
			return aktivniFilteri;
		}

		if (oglasPosZaPretragu.naziv != null && !oglasPosZaPretragu.naziv.isEmpty()) {
			aktivniFilteri.add("naziv");
		}
		if (oglasPosZaPretragu.minCena != -1 && oglasPosZaPretragu.minCena != 0) {
			aktivniFilteri.add("minCena");
		}
		if (oglasPosZaPretragu.maxCena != -1 && oglasPosZaPretragu.maxCena != 0) {
			aktivniFilteri.add("maxCena");
		}
		if (oglasPosZaPretragu.minOcena != -1 && oglasPosZaPretragu.minOcena != 0) {
			aktivniFilteri.add("minOcena");
		}
		if (oglasPosZaPretragu.maxOcena != -1 && oglasPosZaPretragu.maxOcena != 0) {
			aktivniFilteri.add("maxOcena");
		}
		if (oglasPosZaPretragu.minDatum != null && !oglasPosZaPretragu.minDatum.isEmpty()) {
			aktivniFilteri.add("minDatum");
		}
		if (oglasPosZaPretragu.maxDatum != null && !oglasPosZaPretragu.maxDatum.isEmpty()) {
			aktivniFilteri.add("maxDatum");
		}
		if (oglasPosZaPretragu.grad != null && !oglasPosZaPretragu.grad.isEmpty()
				&& !oglasPosZaPretragu.grad.equals("Svi")) {
			aktivniFilteri.add("gradOglasa");
		}
		if (oglasPosZaPretragu.status != null && !oglasPosZaPretragu.status.equals("*")) {
			aktivniFilteri.add("status");
		}

		return aktivniFilteri;
	}

	private ArrayList<String> getAktivniFilteri(KorisnikDto oglasPosZaPretragu) {
		ArrayList<String> aktivniFilteri = new ArrayList<String>();

		if (oglasPosZaPretragu == null) {
			return aktivniFilteri;
		}

		if (oglasPosZaPretragu.naziv != null && !oglasPosZaPretragu.naziv.isEmpty()) {
			aktivniFilteri.add("naziv");

		}
		if (oglasPosZaPretragu.grad != null && !oglasPosZaPretragu.grad.isEmpty()) {
			aktivniFilteri.add("gradOglasa");
		}

		return aktivniFilteri;
	}

}
