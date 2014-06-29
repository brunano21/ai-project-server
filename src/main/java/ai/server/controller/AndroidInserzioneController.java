package ai.server.controller;

import hibernate.Argomenti;
import hibernate.Categoria;
import hibernate.Inserzione;
import hibernate.Sottocategoria;
import hibernate.Supermercato;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
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
		boolean modificaInserzione = false;
		int hashcode = new Long(request.getParameter("codiceBarre")).hashCode()*principal.getName().hashCode()*request.getParameter("data_inizio").hashCode()*request.getParameter("data_fine").hashCode();
		String percorsoFoto = context.getRealPath("/")+"resources\\images"+File.separator+Integer.toString(hashcode)+".png";

		if(request.getParameter("modificaInserzione") != null && Boolean.valueOf(request.getParameter("modificaInserzione")) == true)
			modificaInserzione = true;
		
		if(!modificaInserzione) { 

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
						new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("data_inizio")), 
						new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("data_fine")), 
						request.getParameter("descrizione"), 
						percorsoFoto, 
						((request.getParameter("argomento") != null) ? new ArrayList<Argomenti>(Arrays.asList(new Argomenti(request.getParameter("argomento")))) : null),
						((request.getParameter("valore_argomento") != null) ? new ArrayList<String>(Arrays.asList(request.getParameter("valore_argomento"))) : null));
			} catch (NumberFormatException | ParseException e1) {
				e1.printStackTrace();
			}

		}
		else {
			// modifica inserzione
			try {
				dati.modificaInserzione(Integer.valueOf(request.getParameter("idInserzione")), 
						dati.getUtenti().get(principal.getName()), 
						dati.getSupermercati().get(Integer.valueOf(request.getParameter("supermercato"))), 
						dati.getProdotti().get(Long.valueOf(request.getParameter("codiceBarre"))),
						Float.parseFloat(request.getParameter("prezzo")), 
						new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("data_inizio")),
						new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("data_fine")),
						request.getParameter("descrizione"),
						percorsoFoto, 
						((request.getParameter("argomento") != null) ? new HashSet<Argomenti>(Arrays.asList(new Argomenti(request.getParameter("argomento")))) : null),
						((request.getParameter("valore_argomento") != null) ? new ArrayList<String>(Arrays.asList(request.getParameter("valore_argomento"))) : null));
			} catch (NumberFormatException | ParseException e) {
				e.printStackTrace();
			}

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
		
		JSONArray response = new JSONArray();

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("result", Boolean.valueOf(true));
		jsonObj.put("modificaInserzione", Boolean.valueOf(modificaInserzione));
		response.add(jsonObj);
		
		return response;
	}


	@RequestMapping(value="/android/inserzione/getCategorieSottocategorie", method = RequestMethod.GET)
	@ResponseBody
	public JSONArray getCategorieSottocategorie() {
		System.out.println("Called: /android/inserzione/getCategorieSottocategorie");
		JSONArray response = new JSONArray();

		JSONObject jsonObject = new JSONObject();

		HashMap<String, List<String>> categorieSottocategorieMap = new HashMap<String, List<String>>();
		
		for(Map.Entry<Integer,Categoria> cat : dati.getCategorie().entrySet()){
			
			String categoriaString = cat.getValue().getNome();
			categorieSottocategorieMap.put(categoriaString, new ArrayList<String>());
			for(Iterator<Sottocategoria> iter = cat.getValue().getSottocategorias().iterator(); iter.hasNext(); ) {
				Sottocategoria sottocat = iter.next();
				categorieSottocategorieMap.get(categoriaString).add(sottocat.getNome());
			}
		}
		jsonObject.putAll(categorieSottocategorieMap);
		response.add(jsonObject);
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

	@RequestMapping(value="/android/inserzione/modifica/getInserzioneById", method = RequestMethod.GET)
	@ResponseBody
	public JSONArray getInserzioneDaModificare(HttpServletRequest request) { 
		Integer idInserzione =  Integer.valueOf(request.getParameter("idInserzione"));
		System.out.println("Called: /android/inserzione/modifica/getInserzioneById - id=" + idInserzione );
		JSONArray response = new JSONArray();
		Inserzione inserzione = dati.getInserzioni().get(idInserzione);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("descrizione", inserzione.getDescrizione());
		jsonObj.put("categoria", inserzione.getProdotto().getSottocategoria().getCategoria().getNome());
		jsonObj.put("sottocategoria", inserzione.getProdotto().getSottocategoria().getNome());
		jsonObj.put("codiceBarre", String.valueOf(inserzione.getProdotto().getCodiceBarre())); 
		jsonObj.put("prezzo", String.valueOf(inserzione.getPrezzo()));

		String imageDataString = null; 
		try {

			BufferedImage originalImage = ImageIO.read(new File(inserzione.getFoto()));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write( originalImage, "jpg", baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			imageDataString = new String(Base64.encodeBase64(imageInByte));
			baos.close();


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(inserzione.getIdInserzione()+ " -- " + inserzione.getFoto());
			e.printStackTrace();
		}

		jsonObj.put("foto", imageDataString);
		jsonObj.put("supermercato", String.valueOf(inserzione.getSupermercato().getIdSupermercato()));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		jsonObj.put("data_inizio", sdf.format(inserzione.getDataInizio()));
		jsonObj.put("data_fine", sdf.format(inserzione.getDataFine()));

		response.add(jsonObj);
		return response;

	}
}
