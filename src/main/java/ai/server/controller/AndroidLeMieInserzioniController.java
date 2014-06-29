package ai.server.controller;

import hibernate.Inserzione;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Iterator;

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
public class AndroidLeMieInserzioniController {
	
	@Autowired
	private Dati dati;
	
	public void setDati(Dati dati){
		this.dati=dati;
	}
	
	@RequestMapping(value="/android/lemieinserzioni/getIdInserzioni", method = RequestMethod.GET)
	@ResponseBody
	public JSONArray getIdInserzioni(HttpServletRequest request, Principal principal) {
		System.out.println("Called: /android/lemieinserzioni/getIdInserzioni");
		JSONArray response = new JSONArray();

		for(Iterator<Inserzione> iter = dati.getUtenti().get(principal.getName()).getInserziones().iterator(); iter.hasNext(); ) {
			Inserzione inserzione = iter.next();
			response.add(inserzione.getIdInserzione());
		}
		return response;
	}
	
	@RequestMapping(value="/android/lemieinserzioni/getInserzioneById", method = RequestMethod.GET)
	@ResponseBody
	public JSONArray getInserzioneById(HttpServletRequest request) {
		String idInserzioneListString = request.getParameter("idInserzioneList");
		System.out.println("Called: /android/lemieinserzioni/getInserzioneById - " + idInserzioneListString);
		JSONArray response = new JSONArray();

		for (String id : idInserzioneListString.split(",")) {
			JSONObject jsonObj = new JSONObject();
			Inserzione inserzione = dati.getInserzioni().get(Integer.valueOf(id));
			jsonObj.put("id", inserzione.getIdInserzione());
			jsonObj.put("categoria", inserzione.getProdotto().getSottocategoria().getCategoria().getNome());
			jsonObj.put("sottocategoria", inserzione.getProdotto().getSottocategoria().getNome());
			jsonObj.put("prezzo", inserzione.getPrezzo().toString());
			jsonObj.put("data_inizio", (new SimpleDateFormat("yyyy-MM-dd")).format(inserzione.getDataInizio()));
			jsonObj.put("data_fine", (new SimpleDateFormat("yyyy-MM-dd")).format(inserzione.getDataFine()));
			jsonObj.put("descrizione", inserzione.getDescrizione());
			
			String imageDataString = null; 
			try {
				BufferedImage originalImage = ImageIO.read(new File(inserzione.getFoto()));
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(originalImage, "jpg", baos);
				baos.flush();
				byte[] imageInByte = baos.toByteArray();
				imageDataString = new String(Base64.encodeBase64(imageInByte));
				baos.close();
				
				jsonObj.put("foto", imageDataString);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println(inserzione.getIdInserzione()+ " -- " + inserzione.getFoto());
				e.printStackTrace();
			}

			Integer valutazioni[] = dati.getNumeroValutazioniByIdInserzione(inserzione.getIdInserzione(), inserzione.getUtente().getIdUtente());
			
			jsonObj.put("valutazioni_positive", valutazioni[0]);
			jsonObj.put("valutazioni_negative", valutazioni[1]);
			response.add(jsonObj);
		}
		
		return response;
	}
	
}	
