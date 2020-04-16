window.onload = function() {
	prikaziRecenzije();
}
function pretvoriSliku(element, slika) {
	var file = element.files[0];
	var reader = new FileReader();
	reader.onloadend = function() {
		var res = reader.result;
		$.ajax({
			type : "POST",
			url : "/ProjekatWEB/rest/server/postaviSliku",
			data : res,
			success : function(result) {
				$(slika).attr("src", result);
			},
			error : function(result) {
				console.log(result);
			}
		});
	}
	reader.readAsDataURL(file);
}

function prikaziRecenzije() {
	$.ajax({
		type : "GET",
		url : "/ProjekatWEB/rest/server/recenzija_izmena_prikaz",
		success : function(result) {
			console.log(result);

			result.forEach(function(Recenzija) {
				$("#id_hide").val(Recenzija.id);
				$("#naslov_txt_izmena").val(Recenzija.title);
				$("#sadrzaj_txt_izmena").val(Recenzija.content);
				$("#opis_cb_izmena")[0].checked = Recenzija.correct;
				$("#dogovor_cb_izmena")[0].checked = Recenzija.agreed;
				$("#image_izmena").attr("src", Recenzija.image);
				$("#id_oglas_hide").val(Recenzija.name);

			})

		}
	}

	);

}
function dodajRecenziju() {
	

	var naslov = $("#naslov_txt_izmena").val();
	var sadrzaj = $("#sadrzaj_txt_izmena").val();
	var slika = $("#image_izmena").attr("src");
	var opis = $("#opis_cb_izmena:checked").length > 0;
	var dogovor = $("#dogovor_cb_izmena:checked").length > 0;
	var id = $("#id_hide").val();
	var oglas = $("#id_oglas_hide").val();

	$.ajax({
		type : "PUT",
		url : "/ProjekatWEB/rest/server/dodaj_izmenjenu_recenziju",
		contentType : "application/json",
		data : JSON.stringify({
			"naslov" : naslov,
			"sadrzaj" : sadrzaj,
			"slika" : slika,
			"opis" : opis,
			"dogovor" : dogovor,
			"id":id,
			"oglas":oglas
		}),
		success : function(result) {
			location.href = "kupac.html";
		}

	}

	);

}
