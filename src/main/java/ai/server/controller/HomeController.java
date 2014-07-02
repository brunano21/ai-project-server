package ai.server.controller;

import hibernate.Profilo;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import dati.Dati;

@Controller
public class HomeController {
	
	@Autowired
	private Dati dati;
	
	public void setDati(Dati dati) {
		this.dati=dati;
	}

	@RequestMapping(value="/")
	public ModelAndView home(HttpServletRequest request, @ModelAttribute("error")String error, Principal principal) throws IOException {
		if(error != null) {
			Map <String,Object> map = new HashMap<String, Object>();
			map.put("dati",dati);
			map.put("error", error);
			map.put("principal", principal);
			System.out.println("home");
			return new ModelAndView("index",map);
		} else {
			System.out.println("fatto2");
			return new ModelAndView("index","dati",dati);
		}
	}
	
	@RequestMapping(value="/home")
	public ModelAndView homePage() throws IOException {
		return new ModelAndView("home");
	}
	
	@RequestMapping(value="/suggerimentoSingolo", method=RequestMethod.POST)
	public ModelAndView getSuggerimentoSingolo(HttpServletRequest request, Principal principal) {
		System.out.println("getSuggerimentoSingolo: " + request.getParameter("idInserzione"));
		System.out.println("getSuggerimentoSingolo: " + request.getParameter("tipoSuggerimento"));
		
		return new ModelAndView("suggerimentoSingolo");
	}
	
	@RequestMapping(value="/about", method=RequestMethod.GET)
	public ModelAndView getAboutPage(HttpServletRequest request, Principal principal) {
		return new ModelAndView("about");
	}

	@RequestMapping(value="/convertiCrediti", method=RequestMethod.GET)
	public ModelAndView getConvertiCrediti(HttpServletRequest request, Principal principal) {
		return new ModelAndView("convertiCrediti");
	}
	
	@RequestMapping(value="/richiediBuono", method=RequestMethod.POST)
	public void richiediBuono(HttpServletRequest request, Principal principal) {
		System.out.println("richiediBuono: " + request.getParameter("id_buono"));
		System.out.println("richiediBuono: " + request.getParameter("valore_sconto"));
		System.out.println("richiediBuono: " + request.getParameter("descrizione"));
		System.out.println("richiediBuono: " + request.getParameter("costo"));
		
		// TODO Se si riesce, inviare una mail al tizio con il buono

		Profilo p = (Profilo) (dati.getUtenti().get(principal.getName()).getProfilos().iterator().next());
		dati.modificaProfilo(p.getIdProfilo(), p.getCreditiAcquisiti() - Integer.parseInt(request.getParameter("costo")), p.getCreditiPendenti(), p.getReputazione(), p.getPremium(), p.getContatoreInfrazioni(), p.getNumeroInserzioniPositive(), p.getNumeroInserzioniTotali(), p.getNumeroValutazioniPositive(), p.getNumeroValutazioniTotali());
	}
	
}
