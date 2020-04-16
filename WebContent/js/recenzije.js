

function dodajRecenziju(){
	
	
	
	var naslov=$("#nasolv_txt").val();
	var sadrzaj=$("#sadrzaj_txt").val();
	var slika = $("#image").attr("src");
	var opis= $("#opis_cb:checked").length > 0;
	var dogovor= $("#dogovor_cb:checked").length > 0;
	
	
	$.ajax(
			{
				type:"POST",
				url:"/ProjekatWEB/rest/server/napravi_recenziju",
				contentType:"application/json",
				data: JSON.stringify({"naslov":naslov,"sadrzaj":sadrzaj,"slika":slika,"opis":opis,"dogovor":dogovor}),
				success: function(result){
					location.href="kupac.html";
				}
				
				
				
			}	
		
		
		
	);
	
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