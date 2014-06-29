package ai.server.controller;

import hibernate.Inserzione;
import hibernate.ListaDesideri;
import hibernate.ListaDesideriProdotti;
import hibernate.Prodotto;
import hibernate.Supermercato;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import java.security.Principal;

import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import dati.Dati;

@Controller
public class ListaSpesaController {

	@Autowired
	private Dati dati;

	public void setDati(Dati dati){
		this.dati=dati;
	}
	
	@Autowired
	private ServletContext context;

	public void setServletContext(ServletContext context){
		this.context = context;
	}

	@RequestMapping(value="/todolist", method = RequestMethod.GET)
	public String showForm(Map model) {
		return "todolist";
	}

	@RequestMapping(value="/todolist/getTodoList",method=RequestMethod.GET)
	public @ResponseBody ObjectNode getTodoList(Principal principal) {
		JsonNodeFactory factory = JsonNodeFactory.instance;
		ObjectNode result = factory.objectNode(); 

		Set<ListaDesideri> SetListaDesideri = dati.getUtenti().get(principal.getName()).getListaDesideris();
		for (ListaDesideri listaDesideri : SetListaDesideri) {
			ObjectNode objTodoList = factory.objectNode();

			ObjectNode objProdotti = factory.objectNode();
			Set<ListaDesideriProdotti> setProdotto = listaDesideri.getListaDesideriProdottis();
			for(ListaDesideriProdotti prodotto : setProdotto ) {
				ObjectNode objProdotto = factory.objectNode();
				objProdotto.put("ID_Elemento", prodotto.getId().getIdElemento());
				objProdotto.put("testo", prodotto.getDescrizione());
				objProdotto.put("quantita", prodotto.getQuantità());
				objProdotto.put("acquistato", false);
				objProdotto.put("ID_Inserzione", (prodotto.getInserzione() != null) ? prodotto.getInserzione().getIdInserzione() : null);

				objProdotti.put("item_"+prodotto.getId().getIdElemento(), objProdotto);
			}
			objTodoList.put("Nome", listaDesideri.getNomeListaDesideri());
			objTodoList.put("ID_ListaDB", listaDesideri.getIdListaDesideri());
			objTodoList.put("Elementi", objProdotti);
			result.put("list_"+listaDesideri.getIdListaDesideri() ,objTodoList);
		}

		return result;
	}

	/*
	@RequestMapping(value="/todolist/userGeoloc",method=RequestMethod.POST)
	public @ResponseBody ObjectNode getTodoListSuggestion(HttpServletRequest request, Principal principal) {
		System.out.println(request.getParameter("latitudine"));
		System.out.println(request.getParameter("longitudine"));
		return null;
	}
	*/

	@RequestMapping(value="/todolist", method = RequestMethod.POST)
	public @ResponseBody ObjectNode processTodoList(HttpServletRequest request, Principal principal){
		JsonNodeFactory factory;
		ObjectNode result = null;

		switch(request.getParameter("cmd")){
		case "nuovaListaDesideri":
			System.out.println(request.getParameter("nomeListaDesideri"));
			System.out.println(request.getParameter("idListaDesideri"));
			dati.inserisciListaDesideri(Integer.parseInt(request.getParameter("idListaDesideri")), Dati.getInstance().getUtenti().get(principal.getName()), request.getParameter("nomeListaDesideri"));
			break;

		case "modificaNomeListaDesideri":
			System.out.println(request.getParameter("nuovoNomeListaDesideri"));
			System.out.println(request.getParameter("idListaDesideri"));
			dati.modificaNomeListaDesideri(Integer.parseInt(request.getParameter("idListaDesideri")), Dati.getInstance().getUtenti().get(principal.getName()), request.getParameter("nuovoNomeListaDesideri"));
			break;

		case "eliminaListaDesideri":
			System.out.println(request.getParameter("idListaDesideri"));
			dati.eliminaListaDesideri(Integer.parseInt(request.getParameter("idListaDesideri")), Dati.getInstance().getUtenti().get(principal.getName()));
			break;

		case "nuovoElemento":
			System.out.println(request.getParameter("idListaDesideri"));
			System.out.println(request.getParameter("idElemento"));
			System.out.println(request.getParameter("descrizione"));
			System.out.println(request.getParameter("quantita"));
			System.out.println(request.getParameter("acquistato"));
			System.out.println(request.getParameter("latitudine"));
			System.out.println(request.getParameter("longitudine"));

			ArrayList<Integer> inserzioniDaSuggerireList;
			ObjectNode objSuggerimenti;
			factory = JsonNodeFactory.instance;
			result = factory.objectNode();
			ObjectNode objInfo = factory.objectNode();

			dati.inserisciElementoListaDesideri(Integer.parseInt(request.getParameter("idListaDesideri")), Integer.parseInt(request.getParameter("idElemento")), request.getParameter("descrizione"), Integer.parseInt(request.getParameter("quantita")), Dati.getInstance().getUtenti().get(principal.getName()), request.getParameter("idInserzione") != null ? Integer.parseInt(request.getParameter("idInserzione")) : -1);
			
			result.put("cmd", request.getParameter("cmd")+"_Response");

			objInfo.put("ID_ListaDesideri", request.getParameter("idListaDesideri"));
			objInfo.put("ID_Elemento", request.getParameter("idElemento"));
			result.put("infoElemento", objInfo);
			
			if(request.getParameter("latitudine") == "" || request.getParameter("longitudine") == "")
				break;
			
			inserzioniDaSuggerireList = dati.getSuggerimentiProdotto(principal.getName(), request.getParameter("latitudine"), request.getParameter("longitudine"), request.getParameter("descrizione"));
			Map<Integer, Inserzione> inserzioniMap = dati.getInserzioni();

			if (!inserzioniDaSuggerireList.isEmpty()) {
				objSuggerimenti = factory.objectNode();

				for (Integer idInserzione : inserzioniDaSuggerireList) {
					ObjectNode objInserzione = factory.objectNode();
					objInserzione.put("ID_Inserzione", inserzioniMap.get(idInserzione).getIdInserzione());
					objInserzione.put("dataFine", inserzioniMap.get(idInserzione).getDataFine().toString());
					objInserzione.put("descrizione", inserzioniMap.get(idInserzione).getProdotto().getDescrizione());
					objInserzione.put("supermercato", inserzioniMap.get(idInserzione).getSupermercato().getNome() + ", " + inserzioniMap.get(idInserzione).getSupermercato().getIndirizzo() + ", " + inserzioniMap.get(idInserzione).getSupermercato().getComune());
					objInserzione.put("prezzo", inserzioniMap.get(idInserzione).getPrezzo());
					objInserzione.put("foto", inserzioniMap.get(idInserzione).getFoto());

					objSuggerimenti.put("suggerimento_" + request.getParameter("idListaDesideri") + "_" + inserzioniMap.get(idInserzione).getIdInserzione(), objInserzione);
				}
				result.put("suggerimenti", objSuggerimenti);
			}
			System.out.println(result);
			break;

		case "modificaDescrizioneElemento":
			System.out.println(request.getParameter("idListaDesideri"));
			System.out.println(request.getParameter("idElemento"));
			System.out.println(request.getParameter("descrizione"));
			dati.modificaDescrizioneElementoListaDesideri(Integer.parseInt(request.getParameter("idListaDesideri")), Integer.parseInt(request.getParameter("idElemento")), request.getParameter("descrizione"), Dati.getInstance().getUtenti().get(principal.getName()));
			break;

		case "modificaQuantitaElemento":
			System.out.println(request.getParameter("idListaDesideri"));
			System.out.println(request.getParameter("idElemento"));
			System.out.println(request.getParameter("quantita"));
			dati.modificaQuantitaElementoListaDesideri(Integer.parseInt(request.getParameter("idListaDesideri")), Integer.parseInt(request.getParameter("idElemento")), Integer.parseInt(request.getParameter("quantita")), Dati.getInstance().getUtenti().get(principal.getName()));
			break;

		case "modificaFlagAcquistatoElemento":
			System.out.println(request.getParameter("idListaDesideri"));
			System.out.println(request.getParameter("idElemento"));
			System.out.println(request.getParameter("acquistato"));
			dati.modificaAcquistatoElementoListaDesideri(Integer.parseInt(request.getParameter("idListaDesideri")), Integer.parseInt(request.getParameter("idElemento")), Boolean.parseBoolean(request.getParameter("acquistato")), Dati.getInstance().getUtenti().get(principal.getName()));
			break;

		case "aggiungiIDInserzione":
			System.out.println(request.getParameter("idListaDesideri"));
			System.out.println(request.getParameter("idElemento"));
			System.out.println(request.getParameter("idInserzione"));
			dati.modificaIDInserzioneElementoListaDesideri(Integer.parseInt(request.getParameter("idListaDesideri")), Integer.parseInt(request.getParameter("idElemento")), Integer.parseInt(request.getParameter("idInserzione")), Dati.getInstance().getUtenti().get(principal.getName()));

			factory = JsonNodeFactory.instance;
			result = factory.objectNode();
			result.put("cmd", request.getParameter("cmd")+"_Response");
			result.put("idListaDesideri", request.getParameter("idListaDesideri"));
			result.put("idElemento", request.getParameter("idElemento"));
			result.put("idInserzione", request.getParameter("idInserzione"));
			break;

		case "eliminaElemento":
			System.out.println(request.getParameter("idListaDesideri"));
			System.out.println(request.getParameter("idElemento"));
			dati.eliminaElementoListaDesideri(Integer.parseInt(request.getParameter("idListaDesideri")), Integer.parseInt(request.getParameter("idElemento")), Dati.getInstance().getUtenti().get(principal.getName()));
			break;

		case "recuperaInserzione":
			System.out.println(request.getParameter("idListaDesideri"));
			System.out.println(request.getParameter("idElemento"));
			System.out.println(request.getParameter("idInserzione"));
			Inserzione inserzione = dati.getInserzioni().get(Integer.parseInt(request.getParameter("idInserzione")));

			factory = JsonNodeFactory.instance;
			result = factory.objectNode();
			result.put("cmd", request.getParameter("cmd")+"_Response");
			result.put("idListaDesideri", request.getParameter("idListaDesideri"));
			result.put("idElemento", request.getParameter("idElemento"));
			result.put("idInserzione", request.getParameter("idInserzione"));
			result.put("descrizione", inserzione.getProdotto().getDescrizione());
			result.put("dataFine", inserzione.getDataFine().toString());
			result.put("supermercato", inserzione.getSupermercato().getNome() + ", " + inserzione.getSupermercato().getIndirizzo() + ", " + inserzione.getSupermercato().getComune());
			result.put("prezzo", inserzione.getPrezzo());
			result.put("foto", inserzione.getFoto());
			break;

		default:
			System.out.println("OPS!! - uknown command " +  request.getParameter("cmd"));
			break;
		}
		return result;
	}

	/*
	public static float getGeoDistance(float lat1, float lng1, float lat2, float lng2) {
		double earthRadius = 6378.137;
		double lat1_rad = Math.toRadians(lat1);
		double lng1_rad = Math.toRadians(lng1);
		double lat2_rad = Math.toRadians(lat2);
		double lng2_rad = Math.toRadians(lng2);

		double dist_km = Math.acos( (Math.sin(lat1_rad)*Math.sin(lat2_rad)) + 
				(Math.cos(lat1_rad)*Math.cos(lat2_rad)*Math.cos(lng1_rad-lng2_rad)) ) * earthRadius;
		return (float) dist_km;
	}
	*/
}
