window.onload = function() {
	prikaziRecenzije();
	prikaziOglase();
	postFilter();
	filterOglas();
	
}

function prikaziOglase(){
	var broj
	
	   
	   
	
	$.ajax(
		{	
			type:"GET",
			url:"/ProjekatWEB/rest/server/prodavcevi_oglasi",
			success: function(result){
				console.log(result);
				var like=0;
				var dislike=0;
				
				result.forEach(function(Oglas) {
					broj=Oglas.id;
					
					
					
					
						$("#svi_oglasi tbody").append("<tr>");
						$("#svi_oglasi tbody").append("<td>"+Oglas.name+"</td>");
						$("#svi_oglasi tbody").append("<td>"+Oglas.price+"</td>");
						$("#svi_oglasi tbody").append("<td>"+Oglas.describe+"</td>");
						$("#svi_oglasi tbody").append("<td>"+Oglas.city+"</td>");
						$("#svi_oglasi tbody").append("<td><img src='"+Oglas.image+"' style=\"max-width:200px\"</img></td></tr>");
						$("#svi_oglasi tbody").append("<td><a href='javascript:obrisi(\""+broj+"\");'>Obrisi</a></td>");
						$("#svi_oglasi tbody").append("<td><a href='javascript:izmeni_oglas(\""+broj+"\");'>Izmeni</a></td>");
						$("#lajkoviidislajkovi tbody").append("<tr>");
				
					
					
					like += Oglas.like;
					dislike += Oglas.dislike;					
				})
				$("#lajkoviidislajkovi tbody").append("<td>"+like+"</td>");
				$("#lajkoviidislajkovi tbody").append("<td>"+dislike+"</td></tr>");
				
					
			}
		}	
	
	);
	

}
function prikaziRecenzije(){
	
	$.ajax(
			{	
				type:"GET",
				url:"/ProjekatWEB/rest/server/prodavac_recenzije",
				
				success: function(result){
					console.log(result);
					
					result.forEach(function(Recenzija) {
						
						$("#recenzije tbody").append("<tr>");
						$("#recenzije tbody").append("<td>"+Recenzija.recesent+"</td>");
						$("#recenzije tbody").append("<td>"+Recenzija.title+"</td>");
						$("#recenzije tbody").append("<td>"+Recenzija.content+"</td>");	
						$("#recenzije tbody").append("<td><img src='"+Recenzija.image+"' style=\"max-width:200px\"</img></td></tr>");	
						
						
					})
					
					
						
				}
			}	
		
		);
	
	
}
function filterOglas(){
	
	    
		
		$.ajax(
				{	
					type:"GET",
					url:"/ProjekatWEB/rest/server/filterOglasa",
					contentType : "application/json",
					
					success: function(result){
						console.log(result);
						
						
						result.forEach(function(Oglas) {
							
							
							
							
							
								$("#filtriraniOglasi tbody").append("<tr>");
								$("#filtriraniOglasi tbody").append("<td>"+Oglas.name+"</td>");
								$("#filtriraniOglasi tbody").append("<td>"+Oglas.price+"</td>");
								$("#filtriraniOglasi tbody").append("<td>"+Oglas.describe+"</td>");
								$("#filtriraniOglasi tbody").append("<td>"+Oglas.city+"</td>");
								$("#filtriraniOglasi tbody").append("<td><img src='"+Oglas.image+"' style=\"max-width:200px\"</img></td></tr>");
								
								$("#filtriraniOglasi tbody").append("<tr>");
						
							
							
											
						})
						
						
							
					}
				}	
			
			);
		
		
		
	   

}

function postFilter(){
	$('#filterProdavac').on('change', function (e) {
		var valueSelected = $("#filterProdavac").find(":selected").text(); 
		$.ajax(
				{	
					type:"POST",
					url:"/ProjekatWEB/rest/server/setFilterOglasa",
					contentType : "application/json",
					data : JSON.stringify({
						
						"status":valueSelected
					}),
					success: function(result){
						location.href = "prodavac.html";
						
						
						
					}
				
				}	
				
		);
				
				
		
	});
}

function obrisi(naziv){
	$.ajax({
		type: "DELETE",
		url: "/ProjekatWEB/rest/server/obrisi",
		contentType: "application/json",
		data: naziv,
		success: function(result) {
			if(result==="realizacija"){
				alert("Oglas koji je u realizaciji ili dostavljen ne moze biti obrisan");
			}
			location.href = "prodavac.html";
		
		
		}
	});
}

function obrisi_recenziju(naziv){
	$.ajax({
		type: "DELETE",
		url: "/ProjekatWEB/rest/server/obrisi_recenziju",
		contentType: "application/json",
		data: naziv,
		success: function(result) {
			location.href = "prodavac.html";
		
		
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