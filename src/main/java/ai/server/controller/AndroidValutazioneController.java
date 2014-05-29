package ai.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import dati.Dati;

@Controller
public class AndroidValutazioneController {

	@Autowired
	private Dati dati;
	
	public void setDati(Dati dati) {
		this.dati = dati;
	}
	
	
}
