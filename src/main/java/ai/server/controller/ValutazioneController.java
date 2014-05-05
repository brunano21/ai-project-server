package ai.server.controller;

import hibernate.Inserzione;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import dati.Dati;

@Controller
public class ValutazioneController {
	
	@Autowired
	private Dati dati;
	
	public void setDati(Dati dati){
		this.dati=dati;
	}
	
	public ModelAndView showValutazione(){

		Set<Inserzione> inserzioni = new HashSet<Inserzione>();	
		
		for(Inserzione i : (Set<Integer,Inserzione>)dati.getInserzioni().entrySet().)
		
		return new ModelAndView();
	}
	

}
