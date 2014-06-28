package ai.server.controller;

import hibernate.Inserzione;
import hibernate.Profilo;
import hibernate.Utente;

import java.security.Principal;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import dati.Dati;

@Controller
public class AndroidLoginController {

	@Autowired
	private Dati dati;

	public void setDati(Dati dati) {
		this.dati = dati;
	}


	@RequestMapping(value="/android/login", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray androidLoginSuccess(HttpServletRequest request, Principal principal) {
		System.out.println("Called: /android/login");
		JSONArray response = new JSONArray();
		JSONObject jsonObj = new JSONObject();
		Utente utente = dati.getUtenti().get(principal.getName());
		Profilo profilo = (Profilo) utente.getProfilos().iterator().next();
		jsonObj.put("username", utente.getNickname());
		jsonObj.put("reputazione", profilo.getReputazione());
		jsonObj.put("crediti_pendenti", profilo.getCreditiPendenti());
		jsonObj.put("crediti_acquisiti", profilo.getCreditiAcquisiti());
		jsonObj.put("numero_infrazioni", profilo.getContatoreInfrazioni());
		jsonObj.put("numero_inserzioni_totali", profilo.getNumeroInserzioniTotali());
		jsonObj.put("numero_inserzioni_positive", profilo.getNumeroInserzioniPositive());
		jsonObj.put("numero_valutazioni_totali", profilo.getNumeroValutazioniTotali());
		jsonObj.put("numero_valutazioni_positive", profilo.getNumeroValutazioniPositive());

		int inserzioniPendentiCounter = 0;
		// ricerca delle inserzioni ancora in corso
		for(Iterator<Inserzione> iter =  utente.getInserziones().iterator(); iter.hasNext(); ) {
			Inserzione ins = iter.next();
			DateTime now = new DateTime();
			DateTime fine = new DateTime(ins.getDataFine());
			DateTime inizio = new DateTime(ins.getDataInizio());
			if((now.toDateMidnight().isAfter(inizio.toDateMidnight()) || now.toDateMidnight().isEqual(inizio.toDateMidnight())) && (now.toDateMidnight().isBefore(fine.toDateMidnight())) || now.toDateMidnight().isEqual(fine.toDateMidnight()))
				inserzioniPendentiCounter++;

		}

		jsonObj.put("numero_inserzioni_correnti", inserzioniPendentiCounter);
		
		response.add(jsonObj);

		return response;
	}
}
