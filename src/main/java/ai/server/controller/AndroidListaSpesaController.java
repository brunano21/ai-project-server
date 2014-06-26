package ai.server.controller;

import hibernate.Inserzione;
import hibernate.ListaDesideri;
import hibernate.ListaDesideriProdotti;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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
			
			Set<ListaDesideriProdotti> SetListaDesideriProdotti = dati.getListaDesideri(Integer.parseInt(request.getParameter("id_lista_desideri"))).getListaDesideriProdottis();
			for(ListaDesideriProdotti listaDesideriProdotto : SetListaDesideriProdotti) {
				ldpJsonObj = new JSONObject();
				ldpJsonObj.put("id_elemento", listaDesideriProdotto.getId().getIdElemento());
				ldpJsonObj.put("descrizione", listaDesideriProdotto.getDescrizione());
				ldpJsonObj.put("quantita", listaDesideriProdotto.getQuantità());
				ldpJsonObj.put("acquistato", false);
				
				if(listaDesideriProdotto.getInserzione() != null) {
					ldpInserzioneJsonObj = new JSONObject();
					ldpInserzioneJsonObj.put("id_inserzione", listaDesideriProdotto.getInserzione().getIdInserzione());
					ldpInserzioneJsonObj.put("descrizione", listaDesideriProdotto.getInserzione().getProdotto().getDescrizione());
					ldpInserzioneJsonObj.put("data_fine", listaDesideriProdotto.getInserzione().getDataFine().toString());
					ldpInserzioneJsonObj.put("supermercato", listaDesideriProdotto.getInserzione().getSupermercato().getNome() + ", " + listaDesideriProdotto.getInserzione().getSupermercato().getIndirizzo() + ", " + listaDesideriProdotto.getInserzione().getSupermercato().getComune());
					ldpInserzioneJsonObj.put("prezzo", listaDesideriProdotto.getInserzione().getPrezzo());
					ldpInserzioneJsonObj.put("foto", listaDesideriProdotto.getInserzione().getFoto());
					ldpJsonObj.put("inserzione", ldpInserzioneJsonObj);
				}
				
				response.add(ldpJsonObj);
			}
			System.out.println("JSONARRAY " + response.size());
			break;
			
		case "nuovo_elemento":
			System.out.println(request.getParameter("id_lista_desideri"));
			response = new JSONArray();
			JSONObject suggerimentoJsonObj;
			ArrayList<Integer> inserzioniDaSuggerireList;
			Map<Integer, Inserzione> inserzioniMapTmp;

			//TODO aggiungere elemento nella struttura, controllare i parametri inviati
			dati.inserisciElementoListaDesideri(Integer.parseInt(request.getParameter("id_lista_desideri")), Integer.parseInt(request.getParameter("id_elemento")), request.getParameter("descrizione"), Integer.parseInt(request.getParameter("quantita")), Dati.getInstance().getUtenti().get(principal.getName()), request.getParameter("id_inserzione") != null ? Integer.parseInt(request.getParameter("idInserzione")) : -1);
			
			inserzioniDaSuggerireList = dati.getSuggerimentiProdotto(principal.getName(), request.getParameter("latitudine"), request.getParameter("longitudine"), request.getParameter("descrizione"));
			if(!inserzioniDaSuggerireList.isEmpty()) {
				inserzioniMapTmp = dati.getInserzioni();
				for(Integer idInserzione : inserzioniDaSuggerireList) {
					suggerimentoJsonObj = new JSONObject();
					suggerimentoJsonObj.put("id_inserzione", inserzioniMapTmp.get(idInserzione).getIdInserzione());
					suggerimentoJsonObj.put("data_fine", inserzioniMapTmp.get(idInserzione).getDataFine().toString());
					suggerimentoJsonObj.put("descrizione", inserzioniMapTmp.get(idInserzione).getProdotto().getDescrizione());
					suggerimentoJsonObj.put("supermercato", inserzioniMapTmp.get(idInserzione).getSupermercato().getNome() + ", " + inserzioniMapTmp.get(idInserzione).getSupermercato().getIndirizzo() + ", " + inserzioniMapTmp.get(idInserzione).getSupermercato().getComune());
					suggerimentoJsonObj.put("prezzo", inserzioniMapTmp.get(idInserzione).getPrezzo());
					suggerimentoJsonObj.put("foto", inserzioniMapTmp.get(idInserzione).getFoto());
					
					response.add(suggerimentoJsonObj);
				}
			}
			System.out.println("JSONARRAY " + response.size());
			break;
			
		case "modificaDescrizioneElemento":
			System.out.println(request.getParameter("id_lista_desideri"));
			System.out.println(request.getParameter("id_elemento"));
			System.out.println(request.getParameter("descrizione"));
			dati.modificaDescrizioneElementoListaDesideri(Integer.parseInt(request.getParameter("idListaDesideri")), Integer.parseInt(request.getParameter("idElemento")), request.getParameter("descrizione"), Dati.getInstance().getUtenti().get(principal.getName()));
			break;
			
		case "modificaFlagAcquistatoElemento":
			System.out.println(request.getParameter("id_lista_desideri"));
			System.out.println(request.getParameter("id_elemento"));
			System.out.println(request.getParameter("acquistato"));
			dati.modificaAcquistatoElementoListaDesideri(Integer.parseInt(request.getParameter("id_lista_desideri")), Integer.parseInt(request.getParameter("id_elemento")), Boolean.parseBoolean(request.getParameter("acquistato")), Dati.getInstance().getUtenti().get(principal.getName()));
			break;
			
		case "nuovaListaDesideri":
			System.out.println(request.getParameter("nome"));
			System.out.println(request.getParameter("id_lista_desideri"));
			
			//TODO aggiungere elemento nella struttura, controllare i parametri inviati
			dati.inserisciListaDesideri(Integer.parseInt(request.getParameter("id_lista_desideri")), Dati.getInstance().getUtenti().get(principal.getName()), request.getParameter("nome"));
			break;
			
		case "modificaNomeListaDesideri":
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
			System.out.println(request.getParameter("id_lista_desideri"));
			System.out.println(request.getParameter("id_elemento"));
			System.out.println(request.getParameter("id_inserzione"));
			dati.modificaIDInserzioneElementoListaDesideri(Integer.parseInt(request.getParameter("id_lista_desideri")), Integer.parseInt(request.getParameter("id_elemento")), Integer.parseInt(request.getParameter("id_inserzione")), Dati.getInstance().getUtenti().get(principal.getName()));
			break;
			
		case "ottieni_suggerimenti":
			System.out.println(request.getParameter("latitudine"));
			System.out.println(request.getParameter("longitudine"));
			System.out.println(request.getParameter("descrizione"));
			response = new JSONArray();
			JSONObject suggerimentoJsonObj1;
			ArrayList<Integer> inserzioniDaSuggerireList1;
			Map<Integer, Inserzione> inserzioniMapTmp1;

			inserzioniDaSuggerireList1 = dati.getSuggerimentiProdotto(principal.getName(), request.getParameter("latitudine"), request.getParameter("longitudine"), request.getParameter("descrizione"));
			if(!inserzioniDaSuggerireList1.isEmpty()) {
				inserzioniMapTmp1 = dati.getInserzioni();
				for(Integer idInserzione : inserzioniDaSuggerireList1) {
					suggerimentoJsonObj1 = new JSONObject();
					suggerimentoJsonObj1.put("id_inserzione", inserzioniMapTmp1.get(idInserzione).getIdInserzione());
					suggerimentoJsonObj1.put("data_fine", inserzioniMapTmp1.get(idInserzione).getDataFine().toString());
					suggerimentoJsonObj1.put("descrizione", inserzioniMapTmp1.get(idInserzione).getProdotto().getDescrizione());
					suggerimentoJsonObj1.put("supermercato", inserzioniMapTmp1.get(idInserzione).getSupermercato().getNome() + ", " + inserzioniMapTmp1.get(idInserzione).getSupermercato().getIndirizzo() + ", " + inserzioniMapTmp1.get(idInserzione).getSupermercato().getComune());
					suggerimentoJsonObj1.put("prezzo", inserzioniMapTmp1.get(idInserzione).getPrezzo());
					suggerimentoJsonObj1.put("foto", inserzioniMapTmp1.get(idInserzione).getFoto());
					
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
