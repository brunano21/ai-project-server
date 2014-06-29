package ai.server.controller;

import hibernate.Inserzione;
import hibernate.ValutazioneInserzione;

import java.security.Principal;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dati.Dati;

@Controller
public class ValutazioneController {
	private Map<Integer,Inserzione> inserzioni;
	
	@Autowired
	private Dati dati;
	
	public void setDati(Dati dati) {
		this.dati=dati;
	}
	
	@Autowired
	private ServletContext context;
	
	public void setServletContext(ServletContext context) {
		this.context=context;
	}
	
	@RequestMapping(value = "/valutazione", method = RequestMethod.GET)
	public ModelAndView showValutazione(HttpServletRequest request) {
		return new ModelAndView("valutazione");
	}
	
	@RequestMapping(value = "/valutazione/riceviValutazione", method = RequestMethod.POST)
	public @ResponseBody String riceviValutazione(String idInserzione, String valutazione, Principal principal) {
		System.out.println("VALUTAZIONE "+valutazione);
		if(!dati.getInserzioni().containsKey(Integer.parseInt(idInserzione)))
			throw new RuntimeException("ID Inserzione non valido: " + idInserzione);
		
		Inserzione inserzione = dati.getInserzioni().get(Integer.parseInt(idInserzione));
		
		for(ValutazioneInserzione vi : (Set<ValutazioneInserzione>) inserzione.getValutazioneInserziones())		
			if(vi.getUtenteByIdUtenteValutatore().getMail().equals(principal.getName()))
				return "Error-gia-valutato" + "_@_" + idInserzione;
		
		if("+1".equals(valutazione))
			dati.inserimentoValutazioneInserzione(inserzione, inserzione.getUtente(), dati.getUtenti().get(principal.getName()), 1, new Date());
		else
			dati.inserimentoValutazioneInserzione(inserzione, inserzione.getUtente(), dati.getUtenti().get(principal.getName()), -1, new Date());
		
		return idInserzione + "_@_" + valutazione;
	}
	
}
