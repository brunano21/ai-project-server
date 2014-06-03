package ai.server.controller;

import hibernate.ListaDesideri;
import hibernate.ListaDesideriProdotti;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import java.security.Principal;

import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dati.Dati;
import dati.Registration;

@Controller
public class ListaSpesaController {
	
	
	
	@Autowired
	private Dati dati;
	
	public void setDati(Dati dati){
		this.dati=dati;
	}
	
	@RequestMapping(value="/todolist", method = RequestMethod.GET)
	public String showForm(Map model) {
		
		return "todolist";
	}
	
	@RequestMapping(value="/todolist/getTodoList",method=RequestMethod.GET)
	public @ResponseBody ObjectNode getTodoList(Principal principal){
		JsonNodeFactory factory = JsonNodeFactory.instance;
		ObjectNode result = factory.objectNode(); 
		
		Set<ListaDesideri> SetListaDesideri = dati.getUtenti().get(principal.getName()).getListaDesideris();
		for (ListaDesideri listaDesideri : SetListaDesideri) {
			ObjectNode objTodoList = factory.objectNode();
			
			ObjectNode objProdotti = factory.objectNode();;
			Set<ListaDesideriProdotti> setProdotto = listaDesideri.getListaDesideriProdottis();
			for(ListaDesideriProdotti prodotto : setProdotto ) {
				ObjectNode objProdotto = factory.objectNode();;
				objProdotto.put("ID_Elemento", prodotto.getId().getIdElemento());
				objProdotto.put("testo", prodotto.getDescrizione());
				objProdotto.put("quantita", prodotto.getQuantità());
				objProdotto.put("acquistato", false);
				
				objProdotti.put("item_"+prodotto.getId().getIdElemento(), objProdotto);
				
			}
			objTodoList.put("Nome", listaDesideri.getNomeListaDesideri());
			objTodoList.put("ID_ListaDB", listaDesideri.getIdListaDesideri());
			objTodoList.put("Elementi", objProdotti);
			result.put("list_"+listaDesideri.getIdListaDesideri() ,objTodoList);
		}
		
		return result;
			
	}
	
	@RequestMapping(value="/todolist", method = RequestMethod.POST)
	public String processTodoList(HttpServletRequest request, Principal principal){
		
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
		
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
				dati.inserisciElementoListaDesideri(Integer.parseInt(request.getParameter("idListaDesideri")), Integer.parseInt(request.getParameter("idElemento")), request.getParameter("descrizione"), Integer.parseInt(request.getParameter("quantita")), Dati.getInstance().getUtenti().get(principal.getName()), request.getParameter("idInserzione") != null ? Integer.parseInt(request.getParameter("idInserzione")) : -1);
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
				dati.modificaDescrizioneElementoListaDesideri(Integer.parseInt(request.getParameter("idListaDesideri")), Integer.parseInt(request.getParameter("idElemento")), request.getParameter("quantita"), Dati.getInstance().getUtenti().get(principal.getName()));
				break;
			
			case "modificaFlagAcquistatoElemento":
				System.out.println(request.getParameter("idListaDesideri"));
				System.out.println(request.getParameter("idElemento"));
				System.out.println(request.getParameter("acquistato"));
				dati.modificaAcquistatoElementoListaDesideri(Integer.parseInt(request.getParameter("idListaDesideri")), Integer.parseInt(request.getParameter("idElemento")), Boolean.parseBoolean(request.getParameter("acquistato")), Dati.getInstance().getUtenti().get(principal.getName()));
				break;
			
			case "eliminaElemento":
				System.out.println(request.getParameter("idListaDesideri"));
				System.out.println(request.getParameter("idElemento"));
				dati.eliminaElementoListaDesideri(Integer.parseInt(request.getParameter("idListaDesideri")), Integer.parseInt(request.getParameter("idElemento")), Dati.getInstance().getUtenti().get(principal.getName()));
				break;
			
			default:
				System.out.println("OPS!! - uknown command " +  request.getParameter("cmd"));
		
		}
		
		return "todolist";
	}
}
