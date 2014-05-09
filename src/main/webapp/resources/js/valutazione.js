ids = [];

function getIds(){
	if(localStorage.getItem("lat")){
		$.ajax({type:"GET",
			url: window.location.pathname+"/getSupermercati",
			contentType:"application/json",
			data:{"lat":localStorage.getItem("lat"),"lng":localStorage.getItem("lng")},
			success:function(data){
				$.each(data,function(index,value){
					ids.push(value);
				});	
			}
		});
	}else{
		if(navigator.geolocation){
			
			navigator.geolocation.getCurrentPosition(function(position){
				localStorage.setItem("lat", position.coords.latitude);
				localStorage.setItem("lng", position.coords.longitude);
			}, function(){
				alert('consenti di sapere la tua posizione se vuoi essere localizzato error: ');
				$.ajax({type:"GET",
					url: window.location.pathname+"/getSupermercati",
					contentType:"application/json",
					data:{"lat":localStorage.getItem("lat"),"lng":localStorage.getItem("lng")},
					success:function(data){
						$.each(data,function(index,value){
							ids.push(value);
						});	
					}
				});
			},null);
			
		}
	}
}

function initialize(){
	$("#pic").parent().before('<td><div id="progressBar"></div></td>');
	$("#progressBar").progressbar({
		value:false
	});
	getIds();
	alert(window.location.pathname+"/"+ids.pop());
	$("#picture").attr("src",window.location.pathname+"/"+ids.pop());
}

initialize();