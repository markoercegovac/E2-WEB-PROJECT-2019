function registracijaKorisnika() {
	var korisnickoIme = $('#korisnik_text').val();
	var lozinka = $('#lozinka_text').val();
	var ime = $('#ime_text').val();
	var prezime = $('#prezime_text').val();
	var uloga = $('#uloga_sel').find(":selected").text();
	var kontakt = $('#telefon_text').val();
	var grad = $('#grad_text').val();
	var email = $('#email_text').val();
	if (korisnickoIme == "") {
		alert("Niste uneli korisnicko ime");
		return;
	}
	$.ajax({
		type : "POST",
		url : "/ProjekatWEB/rest/server/registracija",
		contentType : "application/json",
		data : JSON.stringify({
			"korisnickoIme" : korisnickoIme,
			"lozinka" : lozinka,
			"ime" : ime,
			"prezime" : prezime,
			"uloga" : uloga,
			"kontakt" : kontakt,
			"grad" : grad,
			"email" : email
		}),
		success : function(result) {
			switch (result) {
				case "korIme":
					alert("Korisničko ime mora sadržati između 4 i 10 karaktera i"
						+ " sme sadržati samo slova i brojeve! ");
					break;
				case "loz":
					alert("Lozinka mora sadržati između 4 i 10 karaktera! ");
					break;
				case "ime":
					alert("Nevalidno ime!");
					break;
				case "prezime":
					alert("Nevalidno prezime!");
					break;
				case "telefon":
					alert("Broj telefona može sadržati samo cifre!");
					break;
				case "mejl":
					alert("Nevalidna mejl adresa!");
					break;
				case "postoji":
					alert("Korisnik sa tim ussername vec postoji");
					break;
				case "grad":
					alert("Nevalidno ime grada");
					break;
					
				default: {
					location.href = "login.html";
					console.log(result);
				}
			}
			
		}

	}

	);

}

function prijavaKlik() {
	var korIme = $('#ussername_text').val();
	var lozinka = $('#password_text').val();

	if (korIme == "") {
		alert("Niste uneli korisnicko ime");
		return;
	}
	$.ajax({
		type : "POST",
		url : "/ProjekatWEB/rest/server/login",
		contentType : "application/json",
		data : JSON.stringify({
			"korIme" : korIme,
			"lozinka" : lozinka
		}),
		success : function(result) {
			if (result === "kupac") {
				location.href = "kupac.html";

			} else if (result === "admin") {
				location.href = "administrator.html";
			} else if (result === "prodavac") {
				location.href = "prodavac.html";
			} else {
				alert("Uneli ste pogresno korisnicko ime ili lozinku");
			}

		}

	});
}

function validacija(name, val) {
	switch (name) {
	case "korIme":
		if (!val.match(/^[a-zA-Z0-9]{4,10}$/))
			return "Korisničko ime mora sadržati između 4 i 10 karaktera i"
					+ " sme sadržati samo slova i brojeve!";
		break;
	case "lozinka":
		if (val.length < 4 || val.length > 10)
			return "Lozinka mora sadržati između 4 i 10 karaktera!";
		break;
	case "ime":
		if (!val.match(/[a-zA-Z]+/))
			return "Nevalidno ime!";
		break;
	case "prezime":
		if (!val.match(/[a-zA-Z]+/))
			return "Nevalidno prezime!";
		break;
	case "telefon":
		if (!val.match(/\d+/))
			return "Broj telefona može sadržati samo cifre!";
		break;
	case "mejl":
		if (!val.match(/^[a-zA-Z0-9_!#$%&â€™*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$/))
			return "Nevalidna mejl adresa!";
		break;
	default:
		return "";
	}
	return "";
}