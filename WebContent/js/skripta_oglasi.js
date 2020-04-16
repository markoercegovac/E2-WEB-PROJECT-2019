window.onload = function() {
	prikaziOglase();	
	prikaziKategorije();
	prikaziKategorije1();
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

function prikaziKategorije() {
	$.ajax({
		type : "GET",
		url : "/ProjekatWEB/rest/server/prikazi_kategorije",
		success : function(result) {
			console.log(result);

			result.forEach(function(Kategorija) {

				$("#kat").append(
						"<option>" + Kategorija.name + "</option></select>");
				

			})
			
			

		}
	}

	);

}

function prikaziKategorije1() {
	$.ajax({
		type : "GET",
		url : "/ProjekatWEB/rest/server/prikazi_kategorije",
		success : function(result) {
			console.log(result);

			
			result.forEach(function(Kategorija) {

				$("#kat_izmena").append(
						"<option>" + Kategorija.name + "</option></select>");
				

			})
			

		}
	}

	);

}

function dodajOglas() {
	var ime = $('#naziv_text').val();
	var cena = $('#cena_text').val();
	var opis = $('#opis_text').val();
	var grad = $('#grad_text').val();
	var slika = $("#image").attr("src");
	var kat = $("#kat").find(":selected").text();
	var top=$("#datum_isticanja").val();
	var aktivan= $("#cb_aktivan:checked").length > 0;

	$.ajax({
		type : "POST",
		url : "/ProjekatWEB/rest/server/napravi_oglas",
		contentType : "application/json",
		data : JSON.stringify({
			"ime" : ime,
			"cena" : cena,
			"opis" : opis,
			"grad" : grad,
			"kat" : kat,
			"slika" : slika,
			"top":top,
			"aktivan":aktivan
		}),
		success : function(result) {

			switch (result) {
				case "naziv":
					alert("Naziv oglasa mora sadržati između 4 i 10 karaktera!");
					break;
				case "cena":
					alert("Nevalidna cena! ");
					break;
				case "grad":
					alert("Nevalidno ime grada! ");
					break;
				case "slika":
					alert("Slika oglasa je obavezna! ");
					break;
				
				default: {
					location.href = "prodavac.html";
					console.log(result);
				}
			}
			
		}

	}

	);

}
function prikaziOglase(){
	$.ajax({
		type : "GET",
		url : "/ProjekatWEB/rest/server/izmena_oglasa",
		success : function(result) {
			console.log(result);
			
			
			result.forEach(function(Oglas) {
				if(Oglas.id!=""||Oglas.id!=null){
					$("#hidden_id").val(Oglas.id);
					$("#naziv_izmena").val(Oglas.name);
					$("#cena_izmena").val(Oglas.price);
					$("#opis_izmena").val(Oglas.describe);
					$("#grad_izmena").val(Oglas.city);
					$("#image_izmena").attr("src",Oglas.image);
					$("#kat_izmena").find(":selected").text(Oglas.kategorija);
					$("#datum_isticanja_izmena").val(Oglas.dateTop)
					$("#cb_aktivan_izmena")[0].checked = Oglas.aktivan;
				}
				
				
				

			})

		}
	}

	);
}
function izmeniOglas() {
	var id=$('#hidden_id').val();
	var ime = $('#naziv_izmena').val();
	var cena = $('#cena_izmena').val();
	var opis = $('#opis_izmena').val();
	var grad = $('#grad_izmena').val();
	var slika = $("#image_izmena").attr("src");
	var kat = $("#kat_izmena").find(":selected").text();
	var top=$("#datum_isticanja_izmena").val();
	var aktivan=$("#cb_aktivan_izmena")[0].checked;
	$.ajax({
		type : "POST",
		url : "/ProjekatWEB/rest/server/napravi_izmenjen_oglas",
		contentType : "application/json",
		data : JSON.stringify({
			"id":id,
			"ime" : ime,
			"cena" : cena,
			"opis" : opis,
			"grad" : grad,
			"kat" : kat,
			"slika" : slika,
			"top":top,
			"aktivan":aktivan
		}),
		success : function(result) {

			switch (result) {
				case "naziv":
					alert("Naziv oglasa mora sadržati između 4 i 10 karaktera!");
					break;
				case "cena":
					alert("Nevalidna cena! ");
					break;
				case "grad":
					alert("Nevalidno ime grada! ");
					break;
				
				
				default: {
					location.href = "prodavac.html";
					console.log(result);
				}
			}
			
		}

	}

	);

}