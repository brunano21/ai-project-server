package ai.server.controller;

import hibernate.Argomenti;
import hibernate.Categoria;
import hibernate.Prodotto;
import hibernate.Sottocategoria;
import hibernate.Supermercato;
import hibernate.Utente;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dati.Dati;
import dati.InserzioneForm;
import dati.InserzioneValidation;

@Controller
public class InserzioneController {
	
	@Autowired
	private ServletContext context;
	
	public void setServletContext(ServletContext context){
		this.context=context;
	}
	
	@Autowired
	private Dati dati;
	
	public void setDati(Dati dati){
		
		this.dati=dati;
		
	}
	
	@Autowired
	private InserzioneValidation inserzioneValidator;
	
	public void setInserzioneValidator(InserzioneValidation inserzioneValidator){
		this.inserzioneValidator=inserzioneValidator;
	}
	
	@RequestMapping(value="/inserzione", method=RequestMethod.GET)
	public ModelAndView showForm(Map<String, Object> model){
		/*
		InserzioneForm inserzioneForm = new InserzioneForm();
		model.put("inserzioneForm", inserzioneForm);
		Set<String> categorie = new HashSet<String>();
		
		for(Map.Entry<Integer,Categoria> c : dati.getCategorie().entrySet()){
			categorie.add(c.getValue().getNome());
		}
		
		model.put("categorie", categorie);
		*/
		Set<String> argomenti = new HashSet<String>();
		
		for(Map.Entry<String, Argomenti> a : dati.getArgomenti().entrySet()){
			
			argomenti.add(a.getValue().getArgomento());
			
		}
		model.put("argomenti", argomenti);
	//	return "inserzione";
		
		Set<String> categorie = new HashSet<String>();
		
		for(Map.Entry<Integer,Categoria> c : dati.getCategorie().entrySet()){
			categorie.add(c.getValue().getNome());
		}
		model.put("categoria", categorie);
		
		return new ModelAndView("inserzioneForm", model);
	}
	
	@RequestMapping(value="/inserzione/sottocategorie/{name}",method = RequestMethod.GET, consumes="application/json")
	public @ResponseBody Set<String> getSottoCategorie(@PathVariable String name){
		
		Set<String> sottocategorie = new HashSet<String>();
		for(Map.Entry<Integer, Categoria> c : dati.getCategorie().entrySet()){
			if(c.getValue().getNome().equals(name)){
				for(Sottocategoria s : (Set<Sottocategoria>) c.getValue().getSottocategorias()){
					sottocategorie.add(s.getNome());
				}
				break;
			}
		}
		
		return sottocategorie;
		
	}
	
	@RequestMapping(value="/inserzione/getSupermercati",method=RequestMethod.GET)
	public @ResponseBody ArrayNode getSupermercati(String lat,String lng){
		JsonNodeFactory factory = JsonNodeFactory.instance;
		ArrayNode results = factory.arrayNode();
		ObjectNode obj;
		
		for(Map.Entry<Integer, Supermercato> s : dati.getSupermercati().entrySet()){
			System.out.println(s.getValue().getNome()+" "+distFrom(Float.parseFloat(lat), Float.parseFloat(lng),(float) s.getValue().getLatitudine(),(float) s.getValue().getLongitudine()));
			if(distFrom(Float.parseFloat(lat), Float.parseFloat(lng),(float) s.getValue().getLatitudine(),(float) s.getValue().getLongitudine()) < 3){
				obj=factory.objectNode();
				obj.put("nome", s.getValue().getNome());
				obj.put("indirizzo", s.getValue().getIndirizzo());
				obj.put("comune", s.getValue().getComune());
				obj.put("provincia", s.getValue().getProvincia());
				obj.put("lat", s.getValue().getLatitudine());
				obj.put("lng", s.getValue().getLongitudine());
				results.add(obj);
			}
		}
		
		return results;
	}
	
	@RequestMapping(value="/inserzione/getSuggerimenti/{name}",method= RequestMethod.GET)
	public @ResponseBody ArrayNode getSuggerimenti(@PathVariable String name,String term){
		JsonNodeFactory factory = JsonNodeFactory.instance;
		
		ArrayNode results = factory.arrayNode();
		ObjectNode obj;
		if(name.equals("prodotti")){
			for(Map.Entry<Long, Prodotto> prodotto : dati.getProdotti().entrySet()){
				if(prodotto.getValue().getDescrizione().toLowerCase().contains(term.toLowerCase())){
					obj=factory.objectNode();
					obj.put(Long.toString(prodotto.getValue().getCodiceBarre()),prodotto.getValue().getDescrizione());
					results.add(obj);
				}
			}
		}
		if(name.equals("supermercati")){
			for(Map.Entry<Integer, Supermercato> s : dati.getSupermercati().entrySet()){
				if(s.getValue().getNome().toLowerCase().contains(term.toLowerCase())){	
					results.add(s.getValue().getNome());
				}
			}
		}
		return results;
		
	}
	
	@RequestMapping(value="/inserzione",method= RequestMethod.POST)
	public @ResponseBody Object processInserzione(InserzioneForm inserzioneForm, BindingResult result,Principal principal, HttpServletResponse response){
		boolean inserimentoSupermercato=false;
		boolean inserimentoInserzione=false;
		boolean inserimentoProdotto=false;
		Utente utente=null;
		Supermercato supermercato = null;
		int idInsererzione=-1;
		int idProdotto = -1;
		int idSupermercato = -1;
		try{	
			if(result.hasErrors())
				System.out.println(result.getAllErrors().get(0).toString());
			inserzioneValidator.validate(inserzioneForm, result,principal);
			if(result.hasErrors()){
				
				response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
				JsonNodeFactory factory = JsonNodeFactory.instance;				
				ArrayNode errors = factory.arrayNode();
				ObjectNode obj;
				for(FieldError fieldError : result.getFieldErrors()){
					obj=factory.objectNode();
					obj.put(fieldError.getField(), fieldError.getDefaultMessage());
					errors.add(obj);
				}
				return errors;
			}
			String path = "";
			int hashcode = 0;
			utente = dati.getUtenti().get(principal.getName());
			if(!inserzioneForm.getFoto().equals("")){
				hashcode = new Long(inserzioneForm.getCodiceBarre()).hashCode()*utente.getMail().hashCode()*inserzioneForm.getDataInizio().hashCode()*inserzioneForm.getDataFine().hashCode();
				URL url = new URL(inserzioneForm.getFoto());
				BufferedImage image = ImageIO.read(url);
				path = context.getRealPath("/")+"resources\\images"+File.separator+Integer.toString(hashcode)+".png";
				File file = new File(path);
			    ImageIO.write(image, "png", file);			    
			}
			if(inserzioneForm.getFile()!=null){
				hashcode = new Long(inserzioneForm.getCodiceBarre()).hashCode()*utente.getMail().hashCode()*inserzioneForm.getDataInizio().hashCode()*inserzioneForm.getDataFine().hashCode();
				path = context.getRealPath("/")+"resources\\images"+File.separator+Integer.toString(hashcode)+".png";
				File file = new File(path);
				FileUtils.writeByteArrayToFile(file, inserzioneForm.getFile().getBytes());
				System.out.println("file salvato in : "+path);
			}
			//TODO bisogna modificare il criterio di uguaglianza, ed effettuare una query
			Session session = null;
			Transaction tx = null;
			session = Dati.factory.openSession();
			Integer id = -1;
			try {
				tx=session.beginTransaction();			
				Query q = session.createSQLQuery("select s.ID_Supermercato "+
				"from supermercato s"+
						"where s.Nome = :nome and s.Indirizzo = :indirizzo and s.Comune = :comune and s.Provincia = :provincia");
				q.setParameter("nome", inserzioneForm.getSupermercato());
				q.setParameter("indirizzo", inserzioneForm.getIndirizzo());
				q.setParameter("comune", inserzioneForm.getComune());
				q.setParameter("provincia", inserzioneForm.getProvincia());
				List idS = q.list();
				Object ident = idS.get(0);
				System.out.println("id = "+ident);
				id = (Integer)ident;
				tx.commit();
			} catch(RuntimeException e) {
				if(tx!=null)
					tx.rollback();
				throw e;
			} finally {
				if(session!=null && session.isOpen())
					session.close();
			}
			
			supermercato = dati.getSupermercati().get(id);
			
			if(supermercato == null){
				idSupermercato = dati.inserisciSupermercato(inserzioneForm.getSupermercato(), inserzioneForm.getIndirizzo(), inserzioneForm.getComune(), inserzioneForm.getProvincia(), inserzioneForm.getLat(), inserzioneForm.getLng());
				inserimentoSupermercato = true;
				supermercato = dati.getSupermercati().get(idSupermercato);				
			}
			
			
			
			boolean trovato = false;
			Prodotto prodotto = dati.getProdotti().get(inserzioneForm.getCodiceBarre());
			List<Argomenti> argomenti = new LinkedList<Argomenti>();
			Argomenti argomento = null;
			for(String nomeArgomento : inserzioneForm.getArgomento()){
				argomento = dati.getArgomento(nomeArgomento);
				if(argomento != null)
					argomenti.add(argomento);
			}
			List<String> valori = new LinkedList<String>();
			if(inserzioneForm.getArg_corpo() != null)
				valori.addAll(inserzioneForm.getArg_corpo());
			
			if(prodotto != null){
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				trovato = true;
				if(inserzioneForm.getDataFine()!=null&&!(inserzioneForm.getDataFine().equals(""))){				
					idInsererzione=dati.inserisciInserzione(utente, supermercato, prodotto, inserzioneForm.getPrezzo(), sdf.parse(inserzioneForm.getDataInizio()), sdf.parse(inserzioneForm.getDataFine()), inserzioneForm.getDescrizione(),Integer.toString(hashcode)+".png",argomenti,valori);
					inserimentoInserzione=true;
				}else{
					Calendar c = Calendar.getInstance();
					c.setTime(sdf.parse(inserzioneForm.getDataInizio()));
					c.add(Calendar.DATE, 14);
					idInsererzione=dati.inserisciInserzione(utente, supermercato, prodotto, inserzioneForm.getPrezzo(), sdf.parse(inserzioneForm.getDataInizio()),c.getTime() , inserzioneForm.getDescrizione(), Integer.toString(hashcode)+".png",argomenti,valori);
					inserimentoInserzione=true;
				}
			}
			
		
			if(!trovato){
				Sottocategoria sottocategoria=null;
				sottocategoria = dati.getSottocategorie().get(inserzioneForm.getSottoCategoria());
				idProdotto=dati.inserisciProdotto(sottocategoria, inserzioneForm.getCodiceBarre(), inserzioneForm.getDescrizione());
				inserimentoProdotto = true;
				Prodotto p = dati.getProdotti().get(inserzioneForm.getCodiceBarre());
				//Si devono inserire gli argomenti usati
				if(p.getIdProdotto().equals(idProdotto)){
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					if(inserzioneForm.getDataFine()!=null&&!(inserzioneForm.getDataFine().equals(""))){						
						idInsererzione=dati.inserisciInserzione(utente, supermercato, p, inserzioneForm.getPrezzo(), sdf.parse(inserzioneForm.getDataInizio()), sdf.parse(inserzioneForm.getDataFine()), inserzioneForm.getDescrizione(),Integer.toString(hashcode)+".png",argomenti,valori);
						inserimentoInserzione=true;
					}else{
						idInsererzione=dati.inserisciInserzione(utente, supermercato, p, inserzioneForm.getPrezzo(), sdf.parse(inserzioneForm.getDataInizio()), null, inserzioneForm.getDescrizione(), Integer.toString(hashcode)+".png",argomenti,valori);
						inserimentoInserzione=true;
					}
				}
				
			}
		}catch(Exception e){
			System.out.println(inserimentoInserzione+" "+inserimentoProdotto+" "+inserimentoSupermercato);
			
			if(inserimentoInserzione){
				dati.eliminaInserzione(idInsererzione);
			}
			if(inserimentoProdotto){
				dati.eliminaProdotto(inserzioneForm.getCodiceBarre());
			}
			if(inserimentoSupermercato){
				dati.eliminaSupermercato(idSupermercato);
			}
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
			JsonNodeFactory factory = JsonNodeFactory.instance;				
			ArrayNode errors = factory.arrayNode();
			ObjectNode obj;
			obj=factory.objectNode();				
			obj.put("exception", "errore nell'immissione del form");
			errors.add(obj);
			return errors;
		}
		Map<String,Object> model = new HashMap<String, Object>();
		model.put("inserzione", inserzioneForm);
		model.put("idInserzione", new Integer(idInsererzione));
		model.put("dati",dati);
		response.setStatus(HttpServletResponse.SC_OK);
		return new ModelAndView("inserzionesuccess",model);
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
	
	
}
