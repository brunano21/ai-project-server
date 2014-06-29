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
				return "Error-gia-valutato";
		
		if("+1".equals(valutazione))
			dati.inserimentoValutazioneInserzione(inserzione, inserzione.getUtente(), dati.getUtenti().get(principal.getName()), 1, new Date());
		else
			dati.inserimentoValutazioneInserzione(inserzione, inserzione.getUtente(), dati.getUtenti().get(principal.getName()), -1, new Date());
		
		return idInserzione + "_@_" + valutazione;
		
		/*
		boolean trovato = false;
		if(inserzione != null) {			
			for(ValutazioneInserzione vi : (Set<ValutazioneInserzione>) inserzione.getValutazioneInserziones()) {				
				if(vi.getUtenteByIdUtenteValutatore().getMail().equals(principal.getName())) {
					trovato = true;
					break;
				}				
			}		
			if(!trovato) {
				if("corretta".equals(valutazione)) {
					dati.inserimentoValutazioneInserzione(inserzione, inserzione.getUtente(), dati.getUtenti().get(principal.getName()), 1, new Date());
					return "ok";
				}
				if("errata".equals(valutazione)) {
					dati.inserimentoValutazioneInserzione(inserzione,inserzione.getUtente(), dati.getUtenti().get(principal.getName()),-1, new Date());
					return "ok";
				}
			}
		}
		return "errore";
		*/
	}
	/*
	
	@RequestMapping(value="/valutazione/getInserzioni",method = RequestMethod.GET,consumes="application/json")
	public @ResponseBody ArrayNode getInserzioni(String lat,String lng,Principal principal){
		
		JsonNodeFactory factory = JsonNodeFactory.instance;
		ArrayNode results = factory.arrayNode();
		ObjectNode obj;
		ArrayNode argomenti = factory.arrayNode();
		ObjectNode argomento = factory.objectNode();
		boolean trovato = false;
		if(!lat.equals("null") && !lng.equals("null")){
			inserzioni = new HashMap<Integer,Inserzione>();	
			
			for(Map.Entry<Integer, Inserzione> ii : dati.getInserzioni().entrySet()){
				for(ValutazioneInserzione vi : (Set<ValutazioneInserzione>)ii.getValue().getValutazioneInserziones()){				
					if(vi.getUtenteByIdUtenteValutatore().getMail().equals(principal.getName())){
						trovato = true;
						break;
					}				
				}					
				if((int)((ii.getValue().getDataFine().getTime() - new Date().getTime()) / 86400000) > 1 &&
						distFrom(Float.parseFloat(lat), Float.parseFloat(lng), ii.getValue().getSupermercato().getLatitudine(), ii.getValue().getSupermercato().getLongitudine()) < 20 &&
						!trovato){
					argomenti = factory.arrayNode();
					inserzioni.put(ii.getKey(), ii.getValue());	
					obj=factory.objectNode();
					obj.put("id", ii.getKey());
					obj.put("descrizione", ii.getValue().getDescrizione());
					obj.put("numerovalutazioni", ii.getValue().getNumeroValutazioni());
					obj.put("prezzo", ii.getValue().getPrezzo());
					obj.put("totalevoti", ii.getValue().getTotaleVoti());
					obj.put("supermercato", ii.getValue().getSupermercato().getNome());
					for(ArgomentiInserzione ai : (Set<ArgomentiInserzione>) ii.getValue().getArgomentiInserziones()){
						argomento = factory.objectNode();
						argomento.put(ai.getArgomenti().getArgomento(), ai.getArgVal());
						argomenti.add(argomento);
					}
					obj.put("argomenti", argomenti);
					if(ii.getValue().getFoto() != null)
						obj.put("foto", "true");
					else
						obj.put("foto", "false");
					results.add(obj);				
				}
			}
						
		}else{
			
			inserzioni = new HashMap<Integer,Inserzione>();	
			for(Map.Entry<Integer, Inserzione> ii : dati.getInserzioni().entrySet()){
				for(ValutazioneInserzione vi : (Set<ValutazioneInserzione>)ii.getValue().getValutazioneInserziones()){				
					if(vi.getUtenteByIdUtenteValutatore().getMail().equals(principal.getName())){
						trovato = true;
						break;
					}				
				}	
				if((int)((ii.getValue().getDataFine().getTime() - new Date().getTime()) / 86400000) > 1 &&
						!trovato){
					inserzioni.put(ii.getKey(), ii.getValue());	
					obj=factory.objectNode();
					obj.put("id", ii.getKey());
					obj.put("descrizione", ii.getValue().getDescrizione());
					obj.put("numerovalutazioni", ii.getValue().getNumeroValutazioni());
					obj.put("prezzo", ii.getValue().getPrezzo());
					obj.put("totalevoti", ii.getValue().getTotaleVoti());
					obj.put("supermercato", ii.getValue().getSupermercato().getNome());
					for(ArgomentiInserzione ai : (Set<ArgomentiInserzione>) ii.getValue().getArgomentiInserziones()){
						argomento = factory.objectNode();
						argomento.put(ai.getArgomenti().getArgomento(), ai.getArgVal());
						argomenti.add(argomento);
					}
					obj.put("argomenti", argomenti);
					if(ii.getValue().getFoto() != null)
						obj.put("foto", "true");
					else
						obj.put("foto", "false");
					results.add(obj);
				}
			}
		}
		return results;
		
	}
	
	
	
	@RequestMapping(value = "/valutazione/pictures/{idInserzione}")
	@ResponseBody
	public byte[] getImage(@PathVariable Integer idInserzione)  {
		String path = context.getRealPath("/")+"resources\\images"+File.separator+inserzioni.get(idInserzione).getFoto();
		File image = new File(path);
		
		try {
			return FileUtils.readFileToByteArray(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
	    double earthRadius = 6371;
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;


	    return new Float(dist).floatValue();
	 }
*/
}
