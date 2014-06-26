package ai.server.controller;

import hibernate.Inserzione;
import hibernate.ListaDesideri;
import hibernate.ListaDesideriProdotti;
import hibernate.Utente;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
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
public class AndroidInScadenzaController {

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

	@RequestMapping(value="/android/inscadenza/getIdInserzioni", method = RequestMethod.GET)
	@ResponseBody
	public JSONArray getIdInserzioni(HttpServletRequest request, Principal principal) {
		float lat = Float.valueOf(request.getParameter("lat"));
		float lng = Float.valueOf(request.getParameter("lng"));
		System.out.println("Called: /android/inscadenza/getIdInserzioni - LAT: " + request.getParameter("lat") + " LNG: " + request.getParameter("lng"));

		JSONArray response = new JSONArray();
		List idInserzioniInScadenza = dati.getInserzioniInScadenza(principal.getName(), request.getParameter("lat"), request.getParameter("lng"));
		response.addAll(idInserzioniInScadenza);
		System.out.println(response.toString());
		return response;
	}

	@RequestMapping(value="/android/inscadenza/getInserzioneById", method = RequestMethod.GET)
	@ResponseBody
	public JSONArray getIdInserzioneById(HttpServletRequest request, Principal principal) {
		String idInserzioneListString = request.getParameter("idInserzioneList");
		System.out.println("Called: /android/inscadenza/getInserzioneById - " + idInserzioneListString);
		JSONArray response = new JSONArray();

		//		try {
		//			Thread.sleep(1500);
		//		} catch (InterruptedException e1) {
		//			e1.printStackTrace();
		//		}

		for (String id : idInserzioneListString.split(",")) {
			JSONObject jsonObj = new JSONObject();
			Inserzione inserzione = dati.getInserzioni().get(Integer.valueOf(id));

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

			jsonObj.put("id", inserzione.getIdInserzione());
			jsonObj.put("prezzo", inserzione.getPrezzo());
			jsonObj.put("data_fine", inserzione.getDataFine().toString());
			jsonObj.put("descrizione", inserzione.getProdotto().getDescrizione());
			jsonObj.put("supermercato", inserzione.getSupermercato().getNome());
			jsonObj.put("supermercato_indirizzo", inserzione.getSupermercato().getIndirizzo() + ", " + inserzione.getSupermercato().getComune() + " (" + inserzione.getSupermercato().getProvincia() + ")");
			jsonObj.put("foto", imageDataString);
			System.out.println("Searching for idInserzione = " + inserzione.getIdInserzione());

			outerloop:
				for(Iterator<ListaDesideri> iter = dati.getUtenti().get(principal.getName()).getListaDesideris().iterator(); iter.hasNext(); ) {
					ListaDesideri ld = iter.next();
					Boolean trovato = false;
					System.out.println("Searching in lista desideri con nome = " + ld.getNomeListaDesideri() );
					for(Iterator<ListaDesideriProdotti> iter1 = ld.getListaDesideriProdottis().iterator(); iter1.hasNext(); ) {
						ListaDesideriProdotti ldp = iter1.next();
						if(ldp.getInserzione().getIdInserzione() != null && ldp.getInserzione().getIdInserzione() == inserzione.getIdInserzione()) {
							System.out.println("trovato " + ldp.getId().getIdElemento() + " in riferimento all'idInserzione = " + ldp.getInserzione().getIdInserzione());
							jsonObj.put("nome_todolist", ld.getNomeListaDesideri());
							break outerloop;
						}
					}

				}
			response.add(jsonObj);
		}
		System.out.println("JSONARRAY " + response.size());
		return response;
	}

	@RequestMapping(value="/android/inscadenza/getTodoLists", method = RequestMethod.GET)
	@ResponseBody
	public JSONArray getTodoLists(HttpServletRequest request, Principal principal) {
		System.out.println("Called: /android/inscadenza/getTodolists");
		JSONArray response = new JSONArray();

		for(Iterator<ListaDesideri> iter = dati.getUtenti().get(principal.getName()).getListaDesideris().iterator(); iter.hasNext(); ) {
			ListaDesideri ld = iter.next();
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("nomeLista", ld.getNomeListaDesideri());
			jsonObj.put("idLista", ld.getIdListaDesideri());
			response.add(jsonObj);
		}
		return response;
	}

	@RequestMapping(value="/android/inscadenza/aggiungiElemento", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray setElementoAllaTodoList(HttpServletRequest request, Principal principal) {
		System.out.println("Called: /android/inscadenza/aggiungiElemento");
		Integer idInserzione = Integer.valueOf(request.getParameter("idInserzione"));
		Integer idListaDesideri = Integer.valueOf(request.getParameter("idListaDesideri"));
		JSONArray response = new JSONArray();
		int idElemento = new Date().hashCode();
		dati.inserisciElementoListaDesideri(idListaDesideri, idElemento, dati.getInserzioni().get(idInserzione).getDescrizione() , 1, dati.getUtenti().get(principal.getName()), idInserzione);

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("posizione", request.getParameter("posizione"));
		response.add(jsonObj);
		// fare un controllo se non si è verificata alcuna eccezione.

		return response;
	}
}
