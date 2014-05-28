package ai.server.controller;

import hibernate.Categoria;
import hibernate.Sottocategoria;
import hibernate.Supermercato;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import dati.Dati;

@Controller
public class AndroidInserzioneController {
	
	@Autowired
	private ServletContext context;
	
	public void setServletContext(ServletContext context){
		this.context = context;
	}
	
	@Autowired
	private Dati dati;
	
	public void setDati(Dati dati){
		this.dati = dati;
	}
	
	@RequestMapping(value="/android/inserzione", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray elaboraInserzione(HttpServletRequest request, Principal principal) {
		System.out.println("Called: /android/inserzione");
		
		System.out.println(principal.getName());
		return null;
	}
	
	
	@RequestMapping(value="/android/inserzione/getCategorie", method = RequestMethod.GET)
	@ResponseBody
	public JSONArray getCategorie() {
		System.out.println("Called: /android/inserzione/getCategorie");
		JSONArray response = new JSONArray();
		
		ArrayList<String> categorieList = new ArrayList<String>();
		for(Map.Entry<Integer,Categoria> cat : dati.getCategorie().entrySet()){
			response.add(cat.getValue().getNome());
		}
		return response;
	}
	
	@RequestMapping(value="/android/inserzione/checkbarcode/{barcode}", method= RequestMethod.GET)
	@ResponseBody
	public JSONArray checkbarcode(@PathVariable Long barcode) {
		System.out.println("Called: /android/inserzione/checkbarcode " + barcode);
		JSONArray response = new JSONArray();
		JSONObject jsonObj = new JSONObject();
		
		if(dati.getProdotti().containsKey(barcode)) {
			jsonObj.put("descrizione", dati.getProdotti().get(barcode).getDescrizione());
			jsonObj.put("trovato", true);
		}
		else
			jsonObj.put("trovato", false);
		
		response.add(jsonObj);
		System.out.println("Elemento trovato: " + jsonObj.get("trovato"));
		return response;
	}
	
	@RequestMapping(value="/android/inserzione/getSottoCategorie/{categoria}", method = RequestMethod.GET)
	@ResponseBody
	public JSONArray getSottoCategorieAndroid(@PathVariable String categoria) {
		System.out.println("Called: /android/inserzione/getSottoCategorie " + categoria);
		JSONArray response = new JSONArray();
		for(Map.Entry<Integer, Categoria> c : dati.getCategorie().entrySet())
			if(c.getValue().getNome().equals(categoria))
				for(Sottocategoria s : (Set<Sottocategoria>) c.getValue().getSottocategorias())
					response.add(s.getNome());
		return response;
	}
	
	@RequestMapping(value="/android/inserzione/getSupermercati")
	@ResponseBody 
	public JSONArray getSupermercatiAndroid(float lat, float lng){
		System.out.println("Called: /android/inserzione/getSupermercati " + lat + " - " + lng);
		JSONArray response = new JSONArray();
		List<JSONObject> jsonObjList = new ArrayList<JSONObject>();
		float massimaDistanza = 50000; // distanza = 3 km!
		for(Map.Entry<String, Supermercato> s : dati.getSupermercati().entrySet()) {
			float distanza = distFrom(lat, lng, s.getValue().getLatitudine().floatValue(), s.getValue().getLongitudine().floatValue());
			if( distanza <= massimaDistanza) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("id", s.getKey());
				jsonObj.put("nome", s.getValue().getNome());
				jsonObj.put("distanza", distanza);
				jsonObj.put("provincia", "prov");
				jsonObj.put("comune", "com");
				jsonObj.put("via", "via");
				jsonObjList.add(jsonObj);
			}
		}
		
		Collections.sort(jsonObjList, new Comparator<JSONObject>(){
			@Override
			public int compare(JSONObject arg0, JSONObject arg1) {
				return (int) (((float) arg0.get("distanza")) - ((float) arg1.get("distanza")));
			}});
		
		response.addAll(jsonObjList);
		return response;
	}
	
	public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
	    double earthRadius = 6378.137;
	    double lat1_rad = Math.toRadians(lat1);
	    double lng1_rad = Math.toRadians(lng1);
	    double lat2_rad = Math.toRadians(lat2);
	    double lng2_rad = Math.toRadians(lng2);
	    
	    double dist_km = Math.acos( (Math.sin(lat1_rad)*Math.sin(lat2_rad)) + 
	    							(Math.cos(lat1_rad)*Math.cos(lat2_rad)*Math.cos(lng1_rad-lng2_rad)) ) * earthRadius;
	    return (float) dist_km;
	}

}
