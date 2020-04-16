$(document).ready(function(){
	prikaziURealizaciji();
	prikaziDostavljeni();
	prikaziOmiljeni();
	prikaziRecenzije();
});

function prikaziURealizaciji(){
	$.ajax(
			{
				type:"GET",
				url:"/ProjekatWEB/rest/server/realizacija",
				success: function(result){
					console.log(result);
					
					result.forEach(function(Oglas) {
						
						$("#u_realizaciji tbody").append("<tr>");
						
						$("#u_realizaciji tbody").append("<td>"+Oglas.name+"</td>");
						$("#u_realizaciji tbody").append("<td>"+Oglas.price+"</td>");
						$("#u_realizaciji tbody").append("<td>"+Oglas.describe+"</td>");
						$("#u_realizaciji tbody").append("<td>"+Oglas.like+"</td>");
						$("#u_realizaciji tbody").append("<td>"+Oglas.dislike+"</td>");
						$("#u_realizaciji tbody").append("<td><img src='"+Oglas.image+"' style=\"max-width:200px\"</img></td></tr>");
						$("#u_realizaciji tbody").append("<td>"+Oglas.dateDelivered+"</td>");
						$("#u_realizaciji tbody").append("<td>"+Oglas.dateTop+"</td>");
						$("#u_realizaciji tbody").append("<td>"+Oglas.active+"</td>");
						$("#u_realizaciji tbody").append("<td>"+Oglas.city+"</td></tr>");
						$("#u_realizaciji tbody").append("<td><a href='javascript:dostavljeno(\""+Oglas.id+"\");'>Dostavljen</a></td></tr>");
						
						
						
					})
					
						
				}
			}	
		
		);
	
	
}

function prikaziDostavljeni(){
	$.ajax(
			{
				type:"GET",
				url:"/ProjekatWEB/rest/server/dostavljeni",
				success: function(result){
					console.log(result);
					
					result.forEach(function(Oglas) {
						
						$("#dostavljeni tbody").append("<tr>");
						
						$("#dostavljeni tbody").append("<td>"+Oglas.name+"</td>");
						$("#dostavljeni tbody").append("<td>"+Oglas.price+"</td>");
						$("#dostavljeni tbody").append("<td>"+Oglas.describe+"</td>");
						$("#dostavljeni tbody").append("<td>"+Oglas.like+"</td>");
						$("#dostavljeni tbody").append("<td>"+Oglas.dislike+"</td>");
						$("#dostavljeni tbody").append("<td><img src='"+Oglas.image+"' style=\"max-width:200px\"</img></td></tr>");
						$("#dostavljeni tbody").append("<td>"+Oglas.dateDelivered+"</td>");
						$("#dostavljeni tbody").append("<td>"+Oglas.dateTop+"</td>");
						$("#dostavljeni tbody").append("<td>"+Oglas.active+"</td>");
						$("#dostavljeni tbody").append("<td>"+Oglas.city+"</td></tr>");
						$("#dostavljeni tbody").append("<td><a href='javascript:recenzija(\""+Oglas.id+"\");'>Recenzija</a></td>");
						$("#dostavljeni tbody").append("<td><a href='javascript:prijava(\""+Oglas.id+"\");'>Prijavi korisnika</a></td>")
						
						
					})
					
						
				}
			}	
		
		);
	
	
}

function prikaziOmiljeni(){
	$.ajax(
			{
				type:"GET",
				url:"/ProjekatWEB/rest/server/omiljeni",
				success: function(result){
					console.log(result);
					
					result.forEach(function(Oglas) {
						
						$("#omiljeni tbody").append("<tr>");
						
						$("#omiljeni tbody").append("<td>"+Oglas.name+"</td>");
						$("#omiljeni tbody").append("<td>"+Oglas.price+"</td>");
						$("#omiljeni tbody").append("<td>"+Oglas.describe+"</td>");
						$("#omiljeni tbody").append("<td>"+Oglas.city+"</td></tr>");
						
						
						
					})
					
						
				}
			}	
		
		);
	
	
}

function prikaziRecenzije(){
	$.ajax(
			{	
				type:"GET",
				url:"/ProjekatWEB/rest/server/kupac_recenzije",
				success: function(result){
					console.log(result);
					
					result.forEach(function(Recenzija) {
						
						$("#recenzije tbody").append("<tr>");
						$("#recenzije tbody").append("<td>"+Recenzija.title+"</td>");
						$("#recenzije tbody").append("<td>"+Recenzija.content+"</td>");	
						$("#recenzije tbody").append("<td><img src='"+Recenzija.image+"' style=\"max-width:200px\"</img></td></tr>");	
						$("#recenzije tbody").append("<td><a href='javascript:obrisi_recenziju(\""+Recenzija.id+"\");'>Obriši</a></tr>");
						$("#recenzije tbody").append("<td><a href='javascript:izmeni_recenziju(\""+Recenzija.id+"\");'>Izmeni</a></tr>");
						
					})
					
					
						
				}
			}	
		
		);
	
	
}

function dostavljeno(broj) {
	$.ajax({
		type: "POST",
		url: "/ProjekatWEB/rest/server/dostavljeno",
		contentType: "application/json",
		data: broj,
		success: function(result) {
			location.href = "kupac.html";
		}
	});
}



function recenzija(broj) {
	$.ajax({
		type: "POST",
		url: "/ProjekatWEB/rest/server/recenzija",
		contentType: "application/json",
		data: broj,
		success: function(result) {
			location.href = "recenzija.html";
		}
	});
}

function prijava(broj) {
	$.ajax({
		type: "POST",
		url: "/ProjekatWEB/rest/server/prijava",
		contentType: "application/json",
		data: broj,
		success: function(result) {
				confirm("Da li ste sigurni da zelite da prijavite korisnika");
				location.href = "prijava.html";
			
			
			
		}
	});
}

function obrisi_recenziju(broj) {
	$.ajax({
		type: "DELETE",
		url: "/ProjekatWEB/rest/server/obrisi_recenziju",
		contentType: "application/json",
		data: broj,
		success: function(result) {
			location.href = "kupac.html";
		}
	});
}

function izmeni_recenziju(broj) {
	$.ajax({
		type: "POST",
		url: "/ProjekatWEB/rest/server/izmeni_recenziju",
		contentType: "application/json",
		data: broj,
		success: function(result) {
			location.href = "izmena_recenzije.html";
		}
	});
}


