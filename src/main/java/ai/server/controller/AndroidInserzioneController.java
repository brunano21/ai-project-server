package ai.server.controller;

import hibernate.Argomenti;
import hibernate.Categoria;
import hibernate.Sottocategoria;
import hibernate.Supermercato;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;


import org.hibernate.Hibernate;
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
	
	@RequestMapping(value="/android/inserzione/aggiungi", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray elaboraInserzione(HttpServletRequest request, Principal principal) {
		System.out.println("Called: /android/inserzione");
		System.out.println(request.getParameter("descrizione"));
		System.out.println(request.getParameter("codiceBarre"));
		System.out.println(request.getParameter("categoria"));
		System.out.println(request.getParameter("sottocategoria"));
		System.out.println(request.getParameter("data_inizio"));
		System.out.println(request.getParameter("data_fine"));
		System.out.println(request.getParameter("supermercato"));
		System.out.println(request.getParameter("prezzo"));
		System.out.println(request.getParameter("argomento"));
		System.out.println(request.getParameter("valore_argomento"));
		System.out.println("\n");

		int hashcode = new Long(request.getParameter("codiceBarre")).hashCode()*principal.getName().hashCode()*request.getParameter("data_inizio").hashCode()*request.getParameter("data_fine").hashCode();
		String percorsoFoto = context.getRealPath("/")+"resources\\images"+File.separator+Integer.toString(hashcode)+".png";
		System.out.println(percorsoFoto);
		// eventuale inserimento del prodotto, se non presente nel sistema.
		if(! dati.getProdotti().containsKey(Long.valueOf(request.getParameter("codiceBarre")))) {
			// prodotto non presente nel sistema
			dati.inserisciProdotto(
					dati.getSottocategorie().get(request.getParameter("sottocategoria")), 
					Long.valueOf(request.getParameter("codiceBarre")),
					request.getParameter("descrizione"));
		}
		
		
		// inserimento dell'inserzione
		try {
			dati.inserisciInserzione(dati.getUtenti().get(principal.getName()),
					dati.getSupermercati().get(Integer.valueOf(request.getParameter("supermercato"))), 
					dati.getProdotti().get(Long.valueOf(request.getParameter("codiceBarre"))),
					Float.parseFloat(request.getParameter("prezzo")), 
					new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("data_inizio")), 
					new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("data_fine")), 
					request.getParameter("descrizione"), 
					percorsoFoto, 
					(request.getParameter("argomento") != null) ? new ArrayList<Argomenti>(Arrays.asList(new Argomenti(request.getParameter("argomento")))) : null,
					(request.getParameter("valore_argomento") != null) ? new ArrayList<String>(Arrays.asList(request.getParameter("valore_argomento"))) : null);
		} catch (NumberFormatException | ParseException e1) {
			e1.printStackTrace();
		}
		
		// salvataggio dell'immagine.
		byte[] imageByteArray = Base64.decodeBase64(request.getParameter("foto").getBytes());
		FileOutputStream imageOutFile;
		try {
			imageOutFile = new FileOutputStream(percorsoFoto);
			imageOutFile.write(imageByteArray);
			imageOutFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONArray a = new JSONArray();
		a.add(new Boolean(true));
		return a;
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
			jsonObj.put("categoria", dati.getProdotti().get(barcode).getSottocategoria().getCategoria().getNome());
			jsonObj.put("sottocategoria", dati.getProdotti().get(barcode).getSottocategoria().getNome());
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
	
	@RequestMapping(value="/android/inserzione/getSupermercati", method = RequestMethod.GET)
	@ResponseBody 
	public JSONArray getSupermercatiAndroid(float lat, float lng){
		System.out.println("Called: /android/inserzione/getSupermercati " + lat + " - " + lng);
		JSONArray response = new JSONArray();
		List<JSONObject> jsonObjList = new ArrayList<JSONObject>();
		float massimaDistanza = 50000; // distanza = 3 km!
		for(Map.Entry<Integer, Supermercato> s : dati.getSupermercati().entrySet()) {
			float distanza = distFrom(lat, lng,(int) s.getValue().getLatitudine(),(int) s.getValue().getLongitudine());
			if( distanza <= massimaDistanza) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("id", s.getValue().getIdSupermercato());
				jsonObj.put("nome", s.getValue().getNome());
				jsonObj.put("distanza", distanza);
				jsonObj.put("indirizzo", s.getValue().getIndirizzo() + ", " + s.getValue().getComune() +  "(" + s.getValue().getProvincia() + ")");
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
	
	
	@RequestMapping(value="/android/inserzione/getArgomenti", method = RequestMethod.GET)
	@ResponseBody 
	public JSONArray getArgomentiAndroid(){
		System.out.println("Called: /android/inserzione/getArgomenti");
		JSONArray response = new JSONArray();
		response.addAll(dati.getArgomenti().keySet());
		return response;
	}
	
	private static float distFrom(float lat1, float lng1, float lat2, float lng2) {
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
