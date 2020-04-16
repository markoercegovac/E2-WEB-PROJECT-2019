$(document).ready(function(){
	prikaziKorisnike();
	prikaziPoslate();
	prikaziPrimeljene();

});

function posaljiPoruku(){
	
	var naziv=$("#naz_txt").val();
	
	var naslov=$("#naslov_txt").val();
	var sadrzaj=$("#sadrzaj_txt").val();
	
	var korIme = $("#sel_kor").find(":selected").text();
	
	$.ajax(
			{
				type:"PUT",
				url:"/ProjekatWEB/rest/server/posalji_poruku",
				contentType:"application/json",
				data: JSON.stringify({"naziv":naziv,"naslov":naslov,"sadrzaj":sadrzaj,"primalac":korIme}),
				success: function(result){
					location.href="poruke.html";
				}
				
				
				
			}	
		
		
		
		);
}

function prikaziPoslate(){
	$.ajax(
			{	
				type:"GET",
				url:"/ProjekatWEB/rest/server/prikazi_poslate",
				success: function(result){
					console.log(result);
					result.forEach(function (Poruka){
						$("#poslate tbody").append("<tr>");
						
						$("#poslate tbody").append("<td>"+Poruka.namePoster+"</td>");
						$("#poslate tbody").append("<td>"+Poruka.primaoc+"</td>");
						$("#poslate tbody").append("<td>"+Poruka.title+"</td>");
						$("#poslate tbody").append("<td>"+Poruka.content+"</td>");
						$("#poslate tbody").append("<td>"+Poruka.dateAndTime+"</td>");
						$("#poslate tbody").append("<td><a href='javascript:obrisiposlatu(\""+Poruka.id+"\");'>Obriši</a></td>");
						$("#poslate tbody").append("<td><a href='javascript:izmeniposlatu(\""+Poruka.id+"\");'>Izmeni</a></td>");
					})
					
						
				}
			}	
		
		);
	
	
}

function prikaziPrimeljene(){
	$.ajax(
			{	
				type:"GET",
				url:"/ProjekatWEB/rest/server/prikazi_primljene",
				success: function(result){
					console.log(result);
					result.forEach(function (Poruka){
						$("#primljene tbody").append("<tr>");
						
						$("#primljene tbody").append("<td>"+Poruka.namePoster+"</td>");
						$("#primljene tbody").append("<td>"+Poruka.sender+"</td>");
						$("#primljene tbody").append("<td>"+Poruka.title+"</td>");
						$("#primljene tbody").append("<td>"+Poruka.content+"</td>");
						$("#primljene tbody").append("<td>"+Poruka.dateAndTime+"</td>");
						$("#primljene tbody").append("<td><a href='javascript:obrisiprimljenu(\""+Poruka.id+"\");'>Obriši</a></td>");
					})
					
						
				}
			}	
		
		);
	
}

function prikaziKorisnike(){
	$.ajax(
			{	
				type:"GET",
				url:"/ProjekatWEB/rest/server/primaoci",
				success: function(result){
					console.log(result);
					result.forEach(function (Korisnik){
						$("#sel_kor").append("<option>"+Korisnik.username+"</option></select>");
					})
					
						
				}
			}	
		
		);
}



function obrisiposlatu(broj) {
	$.ajax({
		type: "POST",
		url: "/ProjekatWEB/rest/server/obrisiposlatu",
		contentType: "application/json",
		data: broj,
		success: function(result) {
			location.href = "poruke.html";
		}
	});
}

function obrisiprimljenu(broj) {
	$.ajax({
		type: "POST",
		url: "/ProjekatWEB/rest/server/obrisiprimljenu",
		contentType: "application/json",
		data: broj,
		success: function(result) {
			location.href = "poruke.html";
		}
	});
}
function izmeniposlatu(broj) {
	$.ajax({
		type: "POST",
		url: "/ProjekatWEB/rest/server/izmeniposlatu",
		contentType: "application/json",
		data: broj,
		success: function(result) {
			location.href = "izmena_poruke.html";
		}
	});
}
