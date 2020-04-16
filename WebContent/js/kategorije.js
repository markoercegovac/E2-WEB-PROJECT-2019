window.onload = function() {
	izmenaKategorije();
}


function napraviKategoriju(){

	var naziv=$('#txt_naziv').val();
	var opis=$('#txt_opis').val();
	if(naziv==""){
		alert("Niste uneli naziv");
		return;
	}
	if(opis==""){
		alert("Opis definise kategoriju i ne sme biti prazan");
		return;
	}
	$.ajax(
		{
			type:"POST",
			url:"/ProjekatWEB/rest/server/napravi_kategoriju",
			contentType:"aplication/json",
			data: JSON.stringify({"naziv":naziv,"opis":opis}),
			success:function(result){
				location.href="pocetna.html";
			}
		}	
	
	
	
	
	);
	
	
}

function izmenaKategorije(){
	$.ajax(
			{
				type:"GET",
				url:"/ProjekatWEB/rest/server/izmeni_kategoriju",
				success: function(result){
					console.log(result);
					
					result.forEach(function(Kategorija) {
					
						$("#txt_naziv_izmena").val(Kategorija.name);
						$("#txt_opis_izmena").val(Kategorija.describe);
						$("#hidden_kat").val(Kategorija.id);
						
					})
					
						
				}
			}	
		
		);
}
function napraviIzmenjenuKategoriju(){

	var naziv=$('#txt_naziv_izmena').val();
	var opis=$('#txt_opis_izmena').val();
	var id=$("#hidden_kat").val();
	if(naziv==""){
		alert("Niste uneli naziv");
		return;
	}
	$.ajax(
		{
			type:"PUT",
			url:"/ProjekatWEB/rest/server/izmenjena_kategorija",
			contentType:"aplication/json",
			data: JSON.stringify({"naziv":naziv,"opis":opis,"id":id}),
			success:function(result){
				location.href="pocetna.html";
			}
		}	
	
	
	
	
	);
	
	
}