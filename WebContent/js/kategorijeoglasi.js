$(document).ready(function() {
	prikaziOglase();
	
});

function prikaziOglase(){
	$.ajax(
		{
			type:"GET",
			url:"/ProjekatWEB/rest/server/oglasprik",
			success: function(result){
				console.log(result);
				
				result.forEach(function(Oglas) {
					
					$("#oglasiKat tbody").append("<tr>");
					
					$("#oglasiKat tbody").append("<td>"+Oglas.name+"</td>");
					$("#oglasiKat tbody").append("<td>"+Oglas.price+"</td>");
					$("#oglasiKat tbody").append("<td>"+Oglas.describe+"</td>");
					$("#oglasiKat tbody").append("<td>"+Oglas.like+"</td>");
					$("#oglasiKat tbody").append("<td>"+Oglas.dislike+"</td>");
					$("#oglasiKat tbody").append("<td><img src='"+Oglas.image+"' style=\"max-width:200px\"</img></td></tr>");
					$("#oglasiKat tbody").append("<td>"+Oglas.dateDelivered+"</td>");
					$("#oglasiKat tbody").append("<td>"+Oglas.dateTop+"</td>");
					$("#oglasiKat tbody").append("<td>"+Oglas.active+"</td>");
					$("#oglasiKat tbody").append("<td>"+Oglas.city+"</td></tr>");
					$("#h1").append("<td><a href='javascript:poruci(\""+Oglas.id+"\");'><font color:\"red\">Poruci</font></a></td></tr>");
					
					
				})
				
					
			}
		}	
	
	);
	
	
}

function poruci(broj){
	$.ajax({
		type: "POST",
		url: "/ProjekatWEB/rest/server/poruci",
		contentType: "application/json",
		data: broj,
		success: function(result) {
			

			if(result==="ok"){
				location.href = "kupac.html";
			}
			if(result==="no"){
				alert("Prodavac ne moze da porucuje oglase");
			}
		}
	});
}