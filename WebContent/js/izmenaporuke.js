$(document).ready(function() {

	prikaziIzmenu();
});

function prikaziIzmenu() {
	$.ajax({
		type : "GET",
		url : "/ProjekatWEB/rest/server/izmeniporuku",
		success : function(result) {
			console.log(result);
			result.forEach(function(Poruka) {
				$("#id_txt_izmena").val(Poruka.id);
				$("#naz_txt_izmena").val(Poruka.namePoster);
				$("#naslov_txt_izmena").val(Poruka.title);
				$("#sadrzaj_txt_izmena").val(Poruka.content);
				$("#id_txt_primaoc").val(Poruka.primaoc);
			})

		}
	}

	);

}
function izmeniPoruku() {
	var id = $("#id_txt_izmena").val();
	var naziv = $("#naz_txt_izmena").val();
	var primaoc=$("#id_txt_primaoc").val();
	var naslov = $("#naslov_txt_izmena").val();
	var sadrzaj = $("#sadrzaj_txt_izmena").val();
	$.ajax({
		type : "POST",
		url : "/ProjekatWEB/rest/server/izporuku",
		contentType : "application/json",
		data : JSON.stringify({
			"id" : id,
			"naziv" : naziv,
			"naslov" : naslov,
			"primalac":primaoc,
			"sadrzaj" : sadrzaj
		}),
		success : function(result) {
			location.href = "poruke.html";
		}

	}

	);

}
