window.onload = function() {
	pretraziOglase();	
	prikaziGradove();
	pretraziKorisnikee();
}
function pretraziOglase(){
	var naziv=$("#naziv_text").val();
	var minCena=$("#min_text_cena").val();
	var maxCena=$("#max_text_cena").val();
	var minOcena=$("#min_text_ocena").val();
	var maxOcena=$("#max_text_ocena").val();
	var minDatum=$("#min_text_datum").val();
	var maxDatum=$("#max_text_datum").val();
	var grad=$("#grad_sel").find(":selected").text();
	var status= $("#status_sel").find(":selected").text();
	$.ajax({
		type : "GET",
		url : "/ProjekatWEB/rest/server/pretraga_oglasa",
		
		success : function(result) {
			console.log(result);
			data:JSON.stringify({"naziv":naziv,"minCena":minCena,"maxCena":maxCena,"minOcena":minOcena
				,"maxOcena":maxOcena,"minDatum":minDatum,"maxDatum":maxDatum,"grad":grad,"status":status}),
			
			result.forEach(function(Oglas) {
				
				$("#rezultat_pretrage_oglasi tbody").append("<tr>");
				$("#rezultat_pretrage_oglasi tbody").append("<td>"+Oglas.name+"</td>");
				$("#rezultat_pretrage_oglasi tbody").append("<td>"+Oglas.price+"</td>");
				$("#rezultat_pretrage_oglasi tbody").append("<td>"+Oglas.describe+"</td>");
				$("#rezultat_pretrage_oglasi tbody").append("<td>"+Oglas.like+"</td>");
				$("#rezultat_pretrage_oglasi tbody").append("<td>"+Oglas.dislike+"</td>");
				$("#rezultat_pretrage_oglasi tbody").append("<td><img src='"+Oglas.image+"' style=\"max-width:200px\"</img></td></tr>");
				$("#rezultat_pretrage_oglasi tbody").append("<td>"+Oglas.dateDelivered+"</td>");
				$("#rezultat_pretrage_oglasi tbody").append("<td>"+Oglas.dateTop+"</td>");
				
				if(Oglas.aktivan==true){
					$("#rezultat_pretrage_oglasi tbody").append("<td>"+"Aktivan"+"</td>");
				}
				else{
					$("#rezultat_pretrage_oglasi tbody").append("<td>"+"Neaktivan"+"</td>");
				}
				$("#rezultat_pretrage_oglasi tbody").append("<td>"+Oglas.city+"</td></tr>");
				
				
				

			})

		}
	}

	);
}

function pretraziKorisnikee(){
	
	$.ajax({
		type : "GET",
		url : "/ProjekatWEB/rest/server/pretraga_korisnika",
		
		success : function(result) {
			console.log(result);
			
			
			result.forEach(function(Korisnik) {
				$("#rezultat_pretrage_korisnici tbody").append("<tr>");
				$("#rezultat_pretrage_korisnici tbody").append("<td>"+Korisnik.username+"</td>");
				$("#rezultat_pretrage_korisnici tbody").append("<td>"+Korisnik.tipKorisnika+"</td></tr>");
				
				
				

			})

		}
	}

	);
}
function pretraziKorisnike(){
	var naziv=$("#ime_text").val();
	var grad=$("#grad_text").val();
	
	$.ajax({
		type : "POST",
		url : "/ProjekatWEB/rest/server/set_pretraga_korisnika",
		contentType : "application/json",
		data:JSON.stringify({"naziv":naziv,"grad":grad}),
		
		success : function(result) {
			console.log(result);
			location.href="pretraga.html";
			
			

		}
	}

	);
}

function pretraziOglas(){
	var naziv=$("#naziv_text").val();
	var minCena=$("#min_text_cena").val();
	var maxCena=$("#max_text_cena").val();
	var minOcena=$("#min_text_ocena").val();
	var maxOcena=$("#max_text_ocena").val();
	var minDatum=$("#min_text_datum").val();
	var maxDatum=$("#max_text_datum").val();
	var grad=$("#grad_sel").find(":selected").text();
	var status= $("#status_sel").find(":selected").text();
	$.ajax({
		type : "POST",
		url : "/ProjekatWEB/rest/server/set_pretraga",
		contentType : "application/json",
		data:JSON.stringify({"naziv":naziv,"minCena":minCena,"maxCena":maxCena,"minOcena":minOcena
			,"maxOcena":maxOcena,"minDatum":minDatum,"maxDatum":maxDatum,"grad":grad,"status":status}),
		
		success : function(result) {
			location.href="pretraga.html";
			
				
		}
			
		

	

	});
}

function prikaziGradove() {
	$.ajax({
		type : "GET",
		url : "/ProjekatWEB/rest/server/gradovi_oglasa",
		success : function(result) {
			console.log(result);

			for(var i=0;i<result.length;i++){

				$("#grad_sel").append(
						"<option>" + result[i] + "</option></select>");
				

			}

		}
	}

	);

}

