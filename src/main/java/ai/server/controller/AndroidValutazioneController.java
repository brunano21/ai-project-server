
package ai.server.controller;

import hibernate.Inserzione;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
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
		System.out.println("JSONARRAY: " + response.size());
		return response;
	}

	@RequestMapping(value="/android/valutazione/getInserzioneById", method = RequestMethod.GET)
	@ResponseBody
	public JSONArray getIdInserzioneById(HttpServletRequest request, Principal principal) {
		String idInserzioneListString = request.getParameter("idInserzioneList");
		System.out.println("Called: /android/valutazione/getInserzioneById - " + idInserzioneListString);
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
			jsonObj.put("codiceBarre", inserzione.getProdotto().getCodiceBarre());
			jsonObj.put("supermercato", inserzione.getSupermercato().getNome());
			jsonObj.put("supermercato_indirizzo", inserzione.getSupermercato().getNome());

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
				e.printStackTrace();
			}
			jsonObj.put("foto", imageDataString);
			response.add(jsonObj);
		}
		System.out.println("JSONARRAY " + response.size());
		return response;
	}

	@RequestMapping(value="/android/valutazione/aggiungiValutazione", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray aggiungiValutazione(HttpServletRequest request, Principal principal) {
		//System.out.println(request.getParameter("idInserzione"));
		System.out.println(request.getParameter("risultato"));
		Integer idInserzione = Integer.valueOf(request.getParameter("idInserzione"));
		String risultato = request.getParameter("risultato");


		System.out.println("Called: /android/valutazione/aggiungiValutazione - ID_INSERZIONE: " + idInserzione + " - RISULTATO: " + risultato);
		JSONArray response = new JSONArray();

		dati.inserimentoValutazioneInserzione(
				dati.getInserzioni().get(idInserzione),
				dati.getUtenti().get(principal.getName()),
				dati.getInserzioni().get(idInserzione).getUtente(),
				Integer.valueOf(risultato),
				new Date());

		response.add(idInserzione);
		response.add(request.getParameter("posizione"));
		System.out.println(response.toString());
		return response;
	}
}
