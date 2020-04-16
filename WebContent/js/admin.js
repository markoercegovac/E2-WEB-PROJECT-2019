$(document).ready(function(){
	prikaziKorisnike();
	
	prikaziOglase();
	prikaziKategorije();
});

function prikaziKorisnike(){
	$.ajax(
			{	
				type:"GET",
				url:"/ProjekatWEB/rest/server/korisnici",
				success: function(result){
					console.log(result);
					result.forEach(function (Korisnik){
						$("#korisnici").append("<option>"+Korisnik.username+"</option></select>");
					})
					
						
				}
			}	
		
		);
}

function promeniTip(){
	var korIme = $("#korisnici").find(":selected").text();
	var uloga = $("#uloga").find(":selected").text();
	$.ajax(
			{
				type:"POST",
				url:"/ProjekatWEB/rest/server/promeniulogu",
				contentType:"application/json",
				data: JSON.stringify({"korIme":korIme,"uloga":uloga}),
				success: function(result){
					console.log(result);
					$("#uspesno").append("Uspesna promena");
				}
				
				
				
			}	
		
		
		
		);
}

function prikaziKategorije(){
	$.ajax(
			{
				type:"GET",
				url:"/ProjekatWEB/rest/server/prikazi_kategorije",
				success: function(result){
					console.log(result);
					
					result.forEach(function(Kategorija) {
						broj=Kategorija.id;
						$("#kategorije tbody").append("<tr>");
						$("#kategorije tbody").append("<td>"+Kategorija.name+"</td>");
						$("#kategorije tbody").append("<td>"+Kategorija.describe+"</td></tr>");
						$("#kategorije tbody").append("<td><a href='javascript:obrisikat(\""+broj+"\");'>Obrisi</a></td>");
						$("#kategorije tbody").append("<td><a href='javascript:izmeni_kategoriju(\""+broj+"\");'>Izmeni</a></td>");
						
						
					})
					
						
				}
			}	
		
		);
}

function izmeni_kategoriju(id){
	$.ajax({
		type: "POST",
		url: "/ProjekatWEB/rest/server/kat_id",
		contentType: "application/json",
		data: id,
		success:function(result){
			location.href="izmena_kategorije.html";
		}
		
	});
	
}

function prikaziOglase() {
	$
			.ajax({
				type : "GET",
				url : "/ProjekatWEB/rest/server/svi_oglasi_admin",
				success : function(result) {
					console.log(result);

					result
							.forEach(function(Oglas) {

								$("#svi tbody").append("<tr>");

								$("#svi tbody").append(
										"<td>" + Oglas.name + "</td>");
								$("#svi tbody").append(
										"<td>" + Oglas.price + "</td>");
								$("#svi tbody").append(
										"<td>" + Oglas.describe + "</td>");
								$("#svi tbody").append(
										"<td>" + Oglas.like + "</td>");
								$("#svi tbody").append(
										"<td>" + Oglas.dislike + "</td>");
								$("#svi tbody")
										.append(
												"<td><img src='"
														+ Oglas.image
														+ "' style=\"max-width:200px\"</img></td></tr>");
								$("#svi tbody").append(
										"<td>" + Oglas.dateDelivered + "</td>");
								$("#svi tbody").append(
										"<td>" + Oglas.dateTop + "</td>");
								if(Oglas.aktivan==true){
									$("#svi tbody").append(
											"<td>" +"Aktivan"+ "</td>");
								}
								else{
									$("#svi tbody").append(
											"<td>" +"Neaktivan"+ "</td>");
								}
								
								$("#svi tbody").append(
										"<td>" + Oglas.city + "</td></tr>");
								$("#svi tbody").append("<td><a href='javascript:obrisi_admin(\""+Oglas.id+"\");'>Obrisi</a></td>");
								$("#svi tbody").append("<td><a href='javascript:izmeni_oglas(\""+Oglas.id+"\");'>Izmeni</a></td>");
							})

				}
			}

			);

}

function obrisi_admin(naziv){
	$.ajax({
		type: "DELETE",
		url: "/ProjekatWEB/rest/server/obrisi_admin",
		contentType: "application/json",
		data: naziv,
		success: function(result) {
			location.href = "administrator.html";
		
		
		}
	});
}
function izmeni_oglas(id){
	$.ajax({
		type: "POST",
		url: "/ProjekatWEB/rest/server/izmeni_oglas",
		contentType: "application/json",
		data: id,
		success:function(result){
			location.href="izmena_oglasa.html";
		}
		
	});
	
}

function obrisikat(broj) {
	$.ajax({
		type : "DELETE",
		url : "/ProjekatWEB/rest/server/obrisikat",
		contentType : "application/json",
		data : broj,
		success : function(result) {
			console.log(broj);
			location.href = "administrator.html";
		}
	});
}
