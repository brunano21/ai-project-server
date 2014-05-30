package ai.server.controller;

import hibernate.Inserzione;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import dati.Dati;

@Controller
public class AndroidValutazioneController {

	@Autowired
	private Dati dati;
	
	public void setDati(Dati dati) {
		this.dati = dati;
	}
	
	@RequestMapping(value="/android/valutazione/getIdInserzioni", method = RequestMethod.GET)
	@ResponseBody
	public JSONArray getIdInserzioni(HttpServletRequest request, Principal principal) {
		float lat = Float.valueOf(request.getParameter("lat"));
		float lng = Float.valueOf(request.getParameter("lng"));
		System.out.println("Called: /android/valutazione/getIdInserzioni - LAT: " + request.getParameter("lat") + " LNG: " + request.getParameter("lng"));
		
		JSONArray response = new JSONArray();
		List idInserzioni = dati.getInserzioniDaValutare(principal.getName(), request.getParameter("lat"), request.getParameter("lng"));
		
		response.addAll(idInserzioni);
		System.out.println("JSONARRAY " + response.size());
		return response;
	}
	
	@RequestMapping(value="/android/valutazione/getInserzioneById", method = RequestMethod.GET)
	@ResponseBody
	public JSONArray getIdInserzioneById(HttpServletRequest request, Principal principal) {
		String idInserzioneListString = request.getParameter("idInserzioneList");
		System.out.println("Called: /android/valutazione/getIdInserzioneById - " + idInserzioneListString);
		JSONArray response = new JSONArray();
		
		for (String id : idInserzioneListString.split(",")) {
			JSONObject jsonObj = new JSONObject();
			Inserzione inserzione = dati.getInserzioni().get(Integer.valueOf(id));
			jsonObj.put("id", inserzione.getIdInserzione());
			jsonObj.put("categoria", inserzione.getProdotto().getSottocategoria().getCategoria().getNome());
			jsonObj.put("sottocategoria", inserzione.getProdotto().getSottocategoria().getNome());
			jsonObj.put("data_inizio", inserzione.getDataInizio().toString());
			jsonObj.put("data_fine", inserzione.getDataFine().toString());
			jsonObj.put("descrizione", inserzione.getProdotto().getDescrizione());
			jsonObj.put("prezzo", inserzione.getPrezzo());
			response.add(jsonObj);
		}
		System.out.println("JSONARRAY " + response.size());
		return response;
	}
	
}
