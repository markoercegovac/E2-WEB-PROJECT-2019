$(document).ready(function() {
	prikaziOglase();
	prikaziKategorije();
	prikaziOglaseIKat();
	proveriRole();
});

function prikaziOglase() {
	$
			.ajax({
				type : "GET",
				url : "/ProjekatWEB/rest/server/svi_oglasi",
				success : function(result) {
					console.log(result);

					result
							.forEach(function(Oglas) {

								$("#top tbody").append("<tr>");

								$("#top tbody").append(
										"<td>" + Oglas.name + "</td>");
								$("#top tbody").append(
										"<td>" + Oglas.price + "</td>");
								$("#top tbody").append(
										"<td>" + Oglas.describe + "</td>");
								$("#top tbody").append(
										"<td>" + Oglas.like + "</td>");
								$("#top tbody").append(
										"<td>" + Oglas.dislike + "</td>");
								$("#top tbody")
										.append(
												"<td><img src='"
														+ Oglas.image
														+ "' style=\"max-width:200px\"</img></td></tr>");
								$("#top tbody").append(
										"<td>" + Oglas.dateDelivered + "</td>");
								$("#top tbody").append(
										"<td>" + Oglas.dateTop + "</td>");
								if(Oglas.aktivan==true){
									$("#top tbody").append(
											"<td>" +"Aktivan"+ "</td>");
								}
								else{
									$("#top tbody").append(
											"<td>" +"Neaktivan"+ "</td>");
								}
								
								$("#top tbody").append(
										"<td>" + Oglas.city + "</td></tr>");

							})

				}
			}

			);

}

function proveriRole() {
	$
			.ajax({
				type : "GET",
				url : "/ProjekatWEB/rest/server/proveri_role",
				success : function(result) {
					console.log(result);
					if (result === "admin") {
						$("#cont1")
								.append(
										"<div> <a href=\"http://localhost:8080/ProjekatWEB/administrator.html\">Administratorska stranica</a> </div>");
						$("#cont2").append(
						"<td><a href='javascript:odjava();'>Odjava</a></td>");
					} else if (result === "kupac") {
						$("#cont1")
								.append(
										"<div> <a href=\"http://localhost:8080/ProjekatWEB/kupac.html\">Kupceva stranica</a> </div>");
						$("#cont2").append(
						"<td><a href='javascript:odjava();'>Odjava</a></td>");
					} else if (result === "prodavac") {
						$("#cont1")
								.append(
										"<div> <a href=\"http://localhost:8080/ProjekatWEB/prodavac.html\">Prodavceva stranica</a> </div>");
						$("#cont2").append(
						"<td><a href='javascript:odjava();'>Odjava</a></td>");
					} else if (result === "login") {
						$("#cont1").append("<div> <a href=\"http://localhost:8080/ProjekatWEB/login.html\">Login</a> </div>");
						
					}

				}
			}

			);
}

function prikaziKategorije() {
	$.ajax({
		type : "GET",
		url : "/ProjekatWEB/rest/server/prikazi_kategorije",
		success : function(result) {
			console.log(result);

			result.forEach(function(Kategorija) {
				broj = Kategorija.id;
				$("#svi_oglasi tbody").append("<tr>");
				$("#svi_oglasi tbody").append(
						"<td>" + Kategorija.name + "</td>");
				$("#svi_oglasi tbody").append(
						"<td>" + Kategorija.describe + "</td></tr>");
				
				$("#svi_oglasi tbody").append(
						"<td><a href='javascript:oglasikat(\"" + broj
								+ "\");'>Prikazi</a></td>");

			})

		}
	}

	);
}

function prikaziOglaseIKat() {
	$
			.ajax({
				type : "GET",
				url : "/ProjekatWEB/rest/server/prikazizakat",
				success : function(result) {
					console.log(result);

					result
							.forEach(function(Oglas) {

								$("#oglasiKat tbody").append("<tr>");

								$("#oglasiKat tbody").append(
										"<td>" + Oglas.name + "</td>");
								$("#oglasiKat tbody").append(
										"<td>" + Oglas.price + "</td>");
								$("#oglasiKat tbody").append(
										"<td>" + Oglas.describe + "</td>");
								$("#oglasiKat tbody").append(
										"<td>" + Oglas.like + "</td>");
								$("#oglasiKat tbody").append(
										"<td>" + Oglas.dislike + "</td>");
								$("#oglasiKat tbody")
										.append(
												"<td><img src='"
														+ Oglas.image
														+ "' style=\"max-width:200px\"</img></td></tr>");
								$("#oglasiKat tbody").append(
										"<td>" + Oglas.dateTop + "</td>");
								$("#oglasiKat tbody").append(
										"<td>" + Oglas.active + "</td>");
								$("#oglasiKat tbody").append(
										"<td>" + Oglas.city + "</td>");
								$("#oglasiKat tbody").append(
										"<td><a class=\"btn btn-success\" href='javascript:oglas(\""
												+ Oglas.id + "\");'>Prikazi "
												+ Oglas.name + "</a></td>");
								$("#oglasiKat tbody").append(
										"<td><a class=\"btn btn-success\" href='javascript:like(\""
												+ Oglas.id
												+ "\");'><span class=\"glyphicon glyphicon-thumbs-up\">Like</a></td>");
								$("#oglasiKat tbody").append(
										"<td><a class=\"btn btn-danger\" href='javascript:dislike(\""
												+ Oglas.id
												+ "\");'><span class=\"glyphicon glyphicon-star\"></span>Dislike</a></td>");

								$("#oglasiKat tbody")
										.append(
												"<td><a class=\"btn btn-default btn-sm\" href='javascript:omiljen(\""
														+ Oglas.id
														+ "\");'><span class=\"glyphicon glyphicon-star\"></span>Omiljen</a></td></tr>");

							})

				}
			}

			);

}



function oglasikat(broj) {
	$.ajax({
		type : "POST",
		url : "/ProjekatWEB/rest/server/oglasikat",
		contentType : "application/json",
		data : broj,
		success : function(result) {
			console.log(broj);
			location.href = "pocetna.html";
		}
	});
}



function oglas(broj) {
	$.ajax({
		type : "POST",
		url : "/ProjekatWEB/rest/server/oglas",
		contentType : "application/json",
		data : broj,
		success : function(result) {

			location.href = "oglasizakategoriju.html";
		}
	});
}

function odjava() {
	$.ajax({
		type : "POST",
		url : "/ProjekatWEB/rest/server/odjava",
		contentType : "application/json",

		success : function(result) {

			location.href = "login.html";
		}
	});
}
function like(broj) {
	$.ajax({
		type : "POST",
		url : "/ProjekatWEB/rest/server/like",
		contentType : "application/json",
		data : broj,
		success : function(result) {

			if (result === "ok") {
				location.href = "pocetna.html";
			}
			if (result === "no") {
				alert("Prodavac ili admin ne moze da lajkuje oglase");
			}
			if (result === "moras") {

				alert("Morate biti prijavljeni");
			}
		}
	});
}

function dislike(broj) {
	$
			.ajax({
				type : "POST",
				url : "/ProjekatWEB/rest/server/dislike",
				contentType : "application/json",
				data : broj,
				success : function(result) {
					if (result === "ok") {
						location.href = "pocetna.html";
					}
					if (result === "no") {
						alert("Prodavac ne moze da dislajkuje oglase ili niste prijavljeni");
					}
					if (result === "moras") {
						alert("morate biti prijavljeni");

					}
				}
			});
}
function omiljen(broj) {
	$
			.ajax({
				type : "POST",
				url : "/ProjekatWEB/rest/server/omiljen",
				contentType : "application/json",
				data : broj,
				success : function(result) {
					if (result === "ok") {
						location.href = "kupac.html";
					}
					if (result === "no") {
						alert("Prodavac ne moze da ima omiljene oglase! Molim Vas da se prebacite u kupca");
					}
					if (result === "moras") {
						alert("morate biti prijavljeni");
					}
					if(result==="vec"){
						alert("vec imate ovaj oglas u omiljenim");
					}

				}
			});
}
