ids = [];

function getIds(){
	if(localStorage.getItem("lat")){
		$.ajax({type:"GET",
			url: window.location.pathname+"/getIds",
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
				$.ajax({type:"GET",
					url: window.location.pathname+"/getIds",
					contentType:"application/json",
					data:{"lat":localStorage.getItem("lat"),"lng":localStorage.getItem("lng")},
					success:function(data){
						$.each(data,function(index,value){
							ids.push(value);
						});	
					}
				});
			}, function(){
				alert('consenti di sapere la tua posizione se vuoi essere localizzato error: ');
				$.ajax({type:"GET",
					url: window.location.pathname+"/getIds",
					contentType:"application/json",
					data:{"lat":0,"lng":0},
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
	$("#progressBar").height("20").width("200").progressbar({
		value:false
	});
	getIds();
	$("#progressBar").parent().remove();
	$("#picture").attr("src",window.location.pathname+"/pictures/"+ids.pop());
}

initialize();