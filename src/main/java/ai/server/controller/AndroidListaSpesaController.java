package ai.server.controller;

import hibernate.Inserzione;
import hibernate.ListaDesideri;
import hibernate.ListaDesideriProdotti;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import dati.Dati;

@Controller
public class AndroidListaSpesaController {
	@Autowired
	private Dati dati;

	public void setDati(Dati dati){
		this.dati=dati;
	}
	
	@RequestMapping(value="/android/todolist/getTodoListIDs", method = RequestMethod.GET)
	@ResponseBody
	public JSONArray getTodoListIDs(Principal principal) {
		JSONArray response = new JSONArray();
		
		Set<ListaDesideri> SetListaDesideri = dati.getUtenti().get(principal.getName()).getListaDesideris();
		for (ListaDesideri listaDesideri : SetListaDesideri) {
			JSONObject ldJsonObj = new JSONObject();
			System.out.println("ANDROID(getTodoListIDs): Lista Desideri ID: " + listaDesideri.getIdListaDesideri());
			System.out.println("\tNome: " + listaDesideri.getNomeListaDesideri());
			
			ldJsonObj.put("id", listaDesideri.getIdListaDesideri());
			ldJsonObj.put("nome", listaDesideri.getNomeListaDesideri());
			
			response.add(ldJsonObj);
		}
		System.out.println("JSONARRAY " + response.size());
		return response;
	}
	
	@RequestMapping(value="/android/todolist", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray processTodoList(HttpServletRequest request, Principal principal){
		JSONArray response = null;
		ObjectNode result = null;
		
		switch(request.getParameter("cmd")){
		case "todoListItems":
			System.out.println("todoListItems " + request.getParameter("id_lista_desideri"));
			response = new JSONArray();
			JSONObject ldpJsonObj = null;
			JSONObject ldpInserzioneJsonObj = null;
			String imageDataString = null;
			
			Set<ListaDesideriProdotti> SetListaDesideriProdotti = dati.getListaDesideri(Integer.parseInt(request.getParameter("id_lista_desideri"))).getListaDesideriProdottis();
			for(ListaDesideriProdotti listaDesideriProdotto : SetListaDesideriProdotti) {
				ldpJsonObj = new JSONObject();
				ldpJsonObj.put("id_elemento", listaDesideriProdotto.getId().getIdElemento());
				ldpJsonObj.put("descrizione", listaDesideriProdotto.getDescrizione());
				ldpJsonObj.put("quantita", listaDesideriProdotto.getQuantità());
				ldpJsonObj.put("acquistato", false);
				
				if(listaDesideriProdotto.getInserzione() != null) {
					imageDataString = null;
					try {
						
						BufferedImage originalImage = ImageIO.read(new File(listaDesideriProdotto.getInserzione().getFoto()));
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						ImageIO.write( originalImage, "jpg", baos);
						baos.flush();
						byte[] imageInByte = baos.toByteArray();
						imageDataString = new String(Base64.encodeBase64(imageInByte));
						baos.close();
						
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						System.out.println(listaDesideriProdotto.getInserzione().getIdInserzione()+ " -- " + listaDesideriProdotto.getInserzione().getFoto());
						e.printStackTrace();
					}
					
					ldpInserzioneJsonObj = new JSONObject();
					ldpInserzioneJsonObj.put("id_inserzione", listaDesideriProdotto.getInserzione().getIdInserzione());
					ldpInserzioneJsonObj.put("descrizione", listaDesideriProdotto.getInserzione().getProdotto().getDescrizione());
					ldpInserzioneJsonObj.put("data_fine", listaDesideriProdotto.getInserzione().getDataFine().toString());
					ldpInserzioneJsonObj.put("supermercato", listaDesideriProdotto.getInserzione().getSupermercato().getNome() + ", " + listaDesideriProdotto.getInserzione().getSupermercato().getIndirizzo() + ", " + listaDesideriProdotto.getInserzione().getSupermercato().getComune());
					ldpInserzioneJsonObj.put("prezzo", listaDesideriProdotto.getInserzione().getPrezzo());
					ldpInserzioneJsonObj.put("foto", imageDataString);
					ldpJsonObj.put("inserzione", ldpInserzioneJsonObj);
				}
				
				response.add(ldpJsonObj);
			}
			System.out.println("JSONARRAY " + response.size());
			break;
			
		case "nuovo_elemento":
			System.out.println("nuovo_elemento");
			System.out.println(request.getParameter("id_lista_desideri"));
			response = new JSONArray();
			JSONObject suggerimentoJsonObj;
			ArrayList<Integer> inserzioniDaSuggerireList;
			Map<Integer, Inserzione> inserzioniMapTmp;

			dati.inserisciElementoListaDesideri(Integer.parseInt(request.getParameter("id_lista_desideri")), Integer.parseInt(request.getParameter("id_elemento")), request.getParameter("descrizione"), Integer.parseInt(request.getParameter("quantita")), Dati.getInstance().getUtenti().get(principal.getName()), request.getParameter("id_inserzione") != null ? Integer.parseInt(request.getParameter("idInserzione")) : -1);
			System.out.println("JSONARRAY " + response.size());
			break;
			
		case "modificaDescrizioneElemento":
			System.out.println("modificaDescrizioneElemento");
			System.out.println(request.getParameter("id_lista_desideri"));
			System.out.println(request.getParameter("id_elemento"));
			System.out.println(request.getParameter("descrizione"));
			dati.modificaDescrizioneElementoListaDesideri(Integer.parseInt(request.getParameter("id_lista_desideri")), Integer.parseInt(request.getParameter("id_elemento")), request.getParameter("descrizione"), Dati.getInstance().getUtenti().get(principal.getName()));
			break;
			
		case "modificaFlagAcquistatoElemento":
			System.out.println("modificaFlagAcquistatoElemento");
			System.out.println(request.getParameter("id_lista_desideri"));
			System.out.println(request.getParameter("id_elemento"));
			System.out.println(request.getParameter("acquistato"));
			dati.modificaAcquistatoElementoListaDesideri(Integer.parseInt(request.getParameter("id_lista_desideri")), Integer.parseInt(request.getParameter("id_elemento")), Boolean.parseBoolean(request.getParameter("acquistato")), Dati.getInstance().getUtenti().get(principal.getName()));
			break;
			
		case "nuovaListaDesideri":
			System.out.println("nuovaListaDesideri");
			System.out.println(request.getParameter("nome"));
			System.out.println(request.getParameter("id_lista_desideri"));
			dati.inserisciListaDesideri(Integer.parseInt(request.getParameter("id_lista_desideri")), Dati.getInstance().getUtenti().get(principal.getName()), request.getParameter("nome"));
			break;
			
		case "modificaNomeListaDesideri":
			System.out.println("modificaNomeListaDesideri");
			System.out.println(request.getParameter("nuovo_nome_lista_desideri"));
			System.out.println(request.getParameter("id_lista_desideri"));
			dati.modificaNomeListaDesideri(Integer.parseInt(request.getParameter("id_lista_desideri")), Dati.getInstance().getUtenti().get(principal.getName()), request.getParameter("nuovo_nome_lista_desideri"));
			break;

		case "eliminaListaDesideri":
			System.out.println(request.getParameter("id_lista_desideri"));
			dati.eliminaListaDesideri(Integer.parseInt(request.getParameter("id_lista_desideri")), Dati.getInstance().getUtenti().get(principal.getName()));
			break;
			
		case "eliminaElemento":
			System.out.println(request.getParameter("id_lista_desideri"));
			System.out.println(request.getParameter("id_elemento"));
			dati.eliminaElementoListaDesideri(Integer.parseInt(request.getParameter("id_lista_desideri")), Integer.parseInt(request.getParameter("id_elemento")), Dati.getInstance().getUtenti().get(principal.getName()));
			break;
			
		case "aggiungiIDInserzione":
			System.out.println("aggiungiIDInserzione");
			System.out.println(request.getParameter("id_lista_desideri"));
			System.out.println(request.getParameter("id_elemento"));
			System.out.println(request.getParameter("id_inserzione"));
			dati.modificaIDInserzioneElementoListaDesideri(Integer.parseInt(request.getParameter("id_lista_desideri")), Integer.parseInt(request.getParameter("id_elemento")), Integer.parseInt(request.getParameter("id_inserzione")), Dati.getInstance().getUtenti().get(principal.getName()));
			break;
			
		case "ottieni_suggerimenti":
			System.out.println("ottieni_suggerimenti");
			System.out.println(request.getParameter("latitudine"));
			System.out.println(request.getParameter("longitudine"));
			System.out.println(request.getParameter("descrizione"));
			response = new JSONArray();
			JSONObject suggerimentoJsonObj1, idElemento;
			ArrayList<Integer> inserzioniDaSuggerireList1;
			Map<Integer, Inserzione> inserzioniMapTmp1;
			String imageDataString1 = null;

			inserzioniDaSuggerireList1 = dati.getSuggerimentiProdotto(principal.getName(), request.getParameter("latitudine"), request.getParameter("longitudine"), request.getParameter("descrizione"));
			if(!inserzioniDaSuggerireList1.isEmpty()) {
				if(request.getParameter("id_elemento") != null) {
					idElemento = new JSONObject();
					idElemento.put("id_elemento", request.getParameter("id_elemento"));
					response.add(0, idElemento);
				}
				inserzioniMapTmp1 = dati.getInserzioni();
				for(Integer idInserzione : inserzioniDaSuggerireList1) {
					imageDataString1 = null;
					try {
						
						BufferedImage originalImage = ImageIO.read(new File(inserzioniMapTmp1.get(idInserzione).getFoto()));
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						ImageIO.write( originalImage, "jpg", baos);
						baos.flush();
						byte[] imageInByte = baos.toByteArray();
						imageDataString1 = new String(Base64.encodeBase64(imageInByte));
						baos.close();
						
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						System.out.println(inserzioniMapTmp1.get(idInserzione).getIdInserzione()+ " -- " + inserzioniMapTmp1.get(idInserzione).getFoto());
						e.printStackTrace();
					}
					suggerimentoJsonObj1 = new JSONObject();
					suggerimentoJsonObj1.put("id_inserzione", inserzioniMapTmp1.get(idInserzione).getIdInserzione());
					suggerimentoJsonObj1.put("data_fine", inserzioniMapTmp1.get(idInserzione).getDataFine().toString());
					suggerimentoJsonObj1.put("descrizione", inserzioniMapTmp1.get(idInserzione).getProdotto().getDescrizione());
					suggerimentoJsonObj1.put("supermercato", inserzioniMapTmp1.get(idInserzione).getSupermercato().getNome() + ", " + inserzioniMapTmp1.get(idInserzione).getSupermercato().getIndirizzo() + ", " + inserzioniMapTmp1.get(idInserzione).getSupermercato().getComune());
					suggerimentoJsonObj1.put("prezzo", inserzioniMapTmp1.get(idInserzione).getPrezzo());
					suggerimentoJsonObj1.put("foto", imageDataString1);
					
					response.add(suggerimentoJsonObj1);
				}
			}
			System.out.println("JSONARRAY " + response.size());
			break;
			
		default:
			System.out.println("OPS!! - uknown command " +  request.getParameter("cmd"));
			break;
		}
		
		return response;
	}
	
}
