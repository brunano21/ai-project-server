package dati;

import hibernate.Argomenti;
import hibernate.ArgomentiInserzione;
import hibernate.ArgomentiInserzioneId;
import hibernate.Categoria;
import hibernate.Inserzione;
import hibernate.ListaDesideri;
import hibernate.ListaDesideriProdotti;
import hibernate.ListaDesideriProdottiId;
import hibernate.ListaSpesa;
import hibernate.ListaSpesaProdotti;
import hibernate.ListaSpesaProdottiId;
import hibernate.Prodotto;
import hibernate.Profilo;
import hibernate.Sottocategoria;
import hibernate.Supermercato;
import hibernate.Utente;
import hibernate.ValutazioneInserzione;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
/**
 * @author ciakky
 *
 */

/**
 * @author gnz.chp
 */

@Service("dati")
@Scope("singleton")
public class Dati {

	private static volatile Dati istanza;
	
	private volatile Map<String,Utente> mappaUtente = new ConcurrentHashMap<String, Utente>();
	private volatile Map<Integer,ArgomentiInserzione> mappaArgomentiInserzione = new ConcurrentHashMap<Integer, ArgomentiInserzione>();
	private volatile Map<Integer,Categoria> mappaCategorie = new ConcurrentHashMap<Integer, Categoria>();
	private volatile Map<ListaSpesaProdottiId,ListaSpesaProdotti> mappaListaSpesaProdotti = new ConcurrentHashMap<ListaSpesaProdottiId, ListaSpesaProdotti>();
	private volatile Map<ListaDesideriProdottiId,ListaDesideriProdotti> mappaListaDesideriProdotti = new ConcurrentHashMap<ListaDesideriProdottiId, ListaDesideriProdotti>();
	private volatile Map<String,Argomenti> mappaArgomenti = new ConcurrentHashMap<String, Argomenti>();
	private volatile Map<Integer,Inserzione> mappaInserzioni = new ConcurrentHashMap<Integer, Inserzione>();
	private volatile Map<Integer,ListaDesideri> mappaListaDesideri = new ConcurrentHashMap<Integer, ListaDesideri>();
	private volatile Map<Integer,ListaSpesa> mappaListaSpesa = new ConcurrentHashMap<Integer, ListaSpesa>();
	private volatile Map<Long,Prodotto> mappaProdotti = new ConcurrentHashMap<Long, Prodotto>();
	private volatile Map<Integer,Profilo> mappaProfili = new ConcurrentHashMap<Integer, Profilo>();
	private volatile Map<String,Sottocategoria> mappaSottocategorie = new ConcurrentHashMap<String, Sottocategoria>();
	private volatile Map<String,Supermercato> mappaSupermercati = new ConcurrentHashMap<String, Supermercato>();
	private volatile Map<Integer,ValutazioneInserzione> mappaValutazioneInserzione = new ConcurrentHashMap<Integer, ValutazioneInserzione>();

	/***
	 * Questa è session factory
	 */
	public static SessionFactory factory = null;
	public int a;
	private TimerSistemaCrediti timerSC;
	private Timer timer;
	
	public static SessionFactory buildSessionFactory(){
		try{
			Configuration configuration = new Configuration().configure("hibernate.cfg.xml");		
			ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
			return configuration.buildSessionFactory(serviceRegistry);
		}catch(Throwable ex){
			System.err.println("Errore nell'inizializzazione della sessionfactory"+ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	private Dati(){

		factory = buildSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;

		session = factory.openSession();
		try{
			tx=session.beginTransaction();

			List<Utente> result = session.createQuery("from Utente").list();
			for(Utente u : result){
				mappaUtente.put(u.getMail(), u);
			}
			for(ArgomentiInserzione ai : (List<ArgomentiInserzione>)session.createQuery("from ArgomentiInserzione").list()){
				mappaArgomentiInserzione.put(ai.getId().hashCode(), ai);
			}
			for(Categoria c : (List<Categoria>)session.createQuery("from Categoria").list())
			{
				mappaCategorie.put(c.getIdCategoria(), c);
			}
			
			for(ListaSpesaProdotti lsp : (List<ListaSpesaProdotti>)session.createQuery("from ListaSpesaProdotti").list())
			{
				mappaListaSpesaProdotti.put(lsp.getId(), lsp);
			}
			for(ListaDesideriProdotti ldp :(List<ListaDesideriProdotti>)session.createQuery("from ListaDesideriProdotti").list())
			{
				mappaListaDesideriProdotti.put(ldp.getId(), ldp);
			}
			for(Argomenti a : (List<Argomenti>)session.createQuery("from Argomenti").list())
			{
				mappaArgomenti.put(a.getArgomento(), a);
			}
			
			for(Inserzione i : (List<Inserzione>)session.createQuery("from Inserzione").list())
			{
				mappaInserzioni.put(i.getIdInserzione(), i);
			}
			
			for(ListaDesideri ld :(List<ListaDesideri>)session.createQuery("from ListaDesideri").list())
			{
				mappaListaDesideri.put(ld.getIdListaDesideri(), ld);
			}
			
			for(ListaSpesa ls : (List<ListaSpesa>)session.createQuery("from ListaSpesa").list())
			{
				mappaListaSpesa.put(ls.getIdSpesa(), ls);
			}
			for(Prodotto p : (List<Prodotto>)session.createQuery("from Prodotto").list())
			{
				mappaProdotti.put(p.getCodiceBarre(), p);
			}
			for(Profilo p : (List<Profilo>)session.createQuery("from Profilo").list())
			{
				mappaProfili.put(p.getIdProfilo(), p);
			}
			for(Sottocategoria s : (List<Sottocategoria>)session.createQuery("from Sottocategoria").list())
			{
				mappaSottocategorie.put(s.getNome(), s);
			}
			
			for(Supermercato s : (List<Supermercato>)session.createQuery("from Supermercato").list())
			{
				mappaSupermercati.put(s.getNome(), s);
			}
			for(ValutazioneInserzione vi : (List<ValutazioneInserzione>)session.createQuery("from ValutazioneInserzione").list())
			{
				mappaValutazioneInserzione.put(vi.getIdValutazioneInserzione(), vi);
			}
			
			tx.commit();
		}catch(RuntimeException e){
			if(tx!=null)
				tx.rollback();
			throw e;
		}finally{
			if(session!=null && session.isOpen())
				session.close();
		}
		
		// Inizializzazione Timer per Sistema a Crediti
		setTimerSistemaCrediti();
	}
	
	/***
	 * Questo metodo inizializza il timer che scatterà per la prima volta alla mezzanotte del giorno in cui viene lanciato
	 * e successivamente ogni 24 ore. Inoltre inizializza un oggetto della classe TimerSistemaCrediti che contiene il metodo (run) che
	 * verrà eseguito quando il timer scatterà.
	 */
	public void setTimerSistemaCrediti() {
		timerSC = new TimerSistemaCrediti();
		timer = new Timer();
		
		Calendar cal = new GregorianCalendar();
		System.out.println("#1: " + cal.getTime());
		
		cal.set(Calendar.HOUR, 11);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		
		System.out.println("#2: " + cal.getTime());
		System.out.println("#3: " + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
		
		timer.schedule(timerSC, cal.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
		
		// SOLO TEST
		//cal.add(Calendar.MINUTE, +1);
		//timer.schedule(timerSC, cal.getTime(), 100*1000);
	}

	public static Dati getInstance()
	{
		if (istanza == null)
			istanza = new Dati();
		return istanza; 
	}

	
	/**metodo get per ottenere tutti i vari argomenti delle inserzioni 
	 * Es : litri, grammi etc etc
	 * @return
	 */
	public Map<String,Argomenti> getArgomenti(){
		
		HashMap<String,Argomenti> argomenti = new HashMap<String, Argomenti>();
		
		argomenti.putAll(mappaArgomenti);
		
		return argomenti;
	}

	
	
	/**Inserimento di un argomento
	 * @param arg1 nome dell'argomento
	 */
	public void inserisciArgomento (String arg1) {
		if(arg1 == null)
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");
		
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		try {
			tx=session.beginTransaction();			
			Argomenti arg = new Argomenti(arg1,new HashSet<ArgomentiInserzione>());
			
			mappaArgomenti.put(arg1, arg);

			tx.commit();
		}
		catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}

	/**metodo get di un argomento
	 * @param IdArgomento
	 * @return
	 */
	public Argomenti getArgomento(String nomeArgomento){


		Argomenti argomento = null;

		argomento = mappaArgomenti.get(nomeArgomento);
		
		if(argomento == null)
			throw new RuntimeException("Elemento non trovato");
		
		return argomento;
	}
	
	
	/**modifica dell'argomento
	 * @param idArgomento
	 * @param nomeArgomentoNuovo
	 * @param argomentiInserzione Set delle inserzioni che usano tale argomento
	 */
	public void modificaArgomento(String nomeArgomento,String nomeArgomentoNuovo){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		if(nomeArgomento != null || nomeArgomentoNuovo == null)
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");
		
		Argomenti argomentoVecchio = null;
		argomentoVecchio = mappaArgomenti.get(nomeArgomento);		
		
		if(argomentoVecchio==null)
			throw new RuntimeException("elemento non trovato");
		
		Argomenti argomento = new Argomenti(nomeArgomentoNuovo,argomentoVecchio.getArgomentiInserziones());
		
		try{
			tx=session.beginTransaction();			
			session.update(argomento);
			argomentoVecchio.setArgomento(nomeArgomentoNuovo);
			tx.commit();
		}catch(Throwable ex){			
			if(tx!=null)
				tx.rollback();
			
			throw new RuntimeException(ex);			
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}


	/**Inserimento di una categoria
	 * @param nome
	 * @param sottocategorie 
	 * Set delle SottoCategorie di questa Categoria
	 * @param prodotti 
	 * Set dei prodotti di questa categoria
	 */
	public void inserisciCategoria(String nome,Set<Sottocategoria> sottocategorie,Set<Prodotto> prodotti){
		if(nome == null || sottocategorie == null || prodotti == null)
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");
		
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		try{
			tx=session.beginTransaction();
			
			Categoria categoria = new Categoria(nome, sottocategorie);

			Integer idCategoria= (Integer)session.save(categoria);


			mappaCategorie.put(idCategoria, categoria);
			
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}


	}

	/**metodo get della mappa categorie
	 * @return
	 */
	public Map<Integer,Categoria> getCategorie(){
		
		HashMap<Integer, Categoria> categorie = new HashMap<Integer, Categoria>();
		
		categorie.putAll(mappaCategorie);

		return categorie;

	}

	/**Inserimento di un'inserzione
	 * @param utente
	 * @param supermercato
	 * @param prodotto
	 * @param prezzo
	 * @param dataInizio
	 * @param dataFine
	 * @param descrizione
	 * @param foto uri della foto all'interno del server
	 * @return
	 */
	public int inserisciInserzione(Utente utente,Supermercato supermercato,Prodotto prodotto,float prezzo,Date dataInizio,Date dataFine,String descrizione,String foto,List<Argomenti> argomenti,List<String> valori) {
		if(utente == null || supermercato == null || prodotto == null || prezzo <= 0 || dataInizio == null )
			throw new RuntimeException("errore nell'immissione dei parametri");
		
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		int idInserzione = -1;
		boolean salvataggioArgomentiInserzione = false;
		Set<ArgomentiInserzione> argomentiInserzioneSalvati = new HashSet<ArgomentiInserzione>();
		try {
			
			tx=session.beginTransaction();	
			
			Inserzione inserzione = new Inserzione(utente, supermercato, prodotto, prezzo, dataInizio, dataFine, descrizione, foto,0,(float)0.0,new HashSet<ValutazioneInserzione>(),new HashSet<ArgomentiInserzione>());
			idInserzione=(Integer)session.save(inserzione);
			Iterator<Argomenti> itArgomenti = argomenti.iterator();
			Iterator<String> itValori = valori.iterator();
			Argomenti a = null;
			String valore = null;
			while(itArgomenti.hasNext() && itValori.hasNext()){
				a = itArgomenti.next();
				valore = itValori.next();
				ArgomentiInserzioneId id = new ArgomentiInserzioneId(idInserzione, a.getArgomento());
				ArgomentiInserzione ai = new ArgomentiInserzione(id, inserzione, a,new Float(valore));
				session.save(ai);
				mappaArgomentiInserzione.put(id.hashCode(), ai);
				mappaArgomenti.get(ai.getArgomenti().getArgomento()).getArgomentiInserziones().add(a);
				argomentiInserzioneSalvati.add(ai);
				if(!salvataggioArgomentiInserzione)
					salvataggioArgomentiInserzione = true;
			}
			inserzione.setArgomentiInserziones(argomentiInserzioneSalvati);
			session.update(inserzione);
			mappaInserzioni.put(idInserzione,inserzione);			
			mappaUtente.get(utente.getMail()).getInserziones().add(inserzione);
			mappaSupermercati.get(supermercato.getNome()).getInserziones().add(inserzione);
			mappaProdotti.get(prodotto.getCodiceBarre()).getInserziones().add(inserzione);
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			if(salvataggioArgomentiInserzione){
				for(ArgomentiInserzione ai : argomentiInserzioneSalvati){
					mappaArgomentiInserzione.remove(ai.getId().hashCode());
					mappaArgomenti.get(ai.getArgomenti().getArgomento()).getArgomentiInserziones().remove(ai);
				}
			}
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
		return idInserzione;
	}
	
	/**modifica di un'inserzione
	 * @param idInserzione
	 * @param utente
	 * @param supermercato
	 * @param prodotto
	 * @param prezzo
	 * @param dataInizio
	 * @param dataFine
	 * @param descrizione
	 * @param foto
	 * @param valutazioni
	 * @param argomenti gli argomenti usati per quella inserzione
	 */
	public void modificaInserzione(int idInserzione,Utente utente,Supermercato supermercato,Prodotto prodotto,float prezzo,Date dataInizio,Date dataFine,String descrizione,String foto,Set<Argomenti> argomenti,List<String> valori){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		boolean eliminazioneArgomentiInserzione = false;
		boolean inserimentoArgomentiInserzione = false;
		
		if(idInserzione <= 0 || utente == null || supermercato == null || prodotto == null || prezzo <= 0 || dataInizio == null || dataFine == null || descrizione == null || foto == null || argomenti == null)
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");
		
		Inserzione inserzioneVecchia = null;
		inserzioneVecchia=mappaInserzioni.get(idInserzione);
		if(inserzioneVecchia == null)
			throw new RuntimeException("elemento non trovato");
		Set<ArgomentiInserzione> argomentiInserzione = new HashSet<ArgomentiInserzione>();
		Set<ArgomentiInserzione> argomentiInserzioneEliminati = new HashSet<ArgomentiInserzione>();
		Set<ArgomentiInserzione> argomentiInserzioneInseriti = new HashSet<ArgomentiInserzione>();
		
		Iterator<Argomenti> itArgomenti = argomenti.iterator();
		Iterator<String> itValori = valori.iterator();
		Argomenti a = null;
		String valore = null;
		
		while(itArgomenti.hasNext() && itValori.hasNext()){
			a = itArgomenti.next();
			valore = itValori.next();
			ArgomentiInserzioneId id = new ArgomentiInserzioneId(idInserzione, a.getArgomento());
			ArgomentiInserzione ai = new ArgomentiInserzione(id, inserzioneVecchia, a,new Float(valore));			
		}		
		
		Inserzione inserzione = new Inserzione(utente, supermercato, prodotto, prezzo, dataInizio, dataFine, descrizione, foto,0,(float)0.0,inserzioneVecchia.getValutazioneInserziones(),argomenti);
		inserzione.setIdInserzione(idInserzione);
		
		try{
			tx=session.beginTransaction();
			session.update(inserzione);			
			
			for(ArgomentiInserzione ai : (Set<ArgomentiInserzione>)inserzioneVecchia.getArgomentiInserziones()){
				if(!argomentiInserzione.contains(ai)){
					session.delete(ai);
					mappaArgomentiInserzione.remove(ai.getId().hashCode());
					mappaArgomenti.get(ai.getArgomenti().getArgomento()).getArgomentiInserziones().remove(ai);
					argomentiInserzioneEliminati.add(ai);
					if(!eliminazioneArgomentiInserzione)
						eliminazioneArgomentiInserzione = true;
				}
			}
			
			for(ArgomentiInserzione ai : argomentiInserzione){				
				ai.setInserzione(inserzioneVecchia);
				if(!inserzioneVecchia.getArgomentiInserziones().contains(ai)){
					session.save(ai);
					mappaArgomentiInserzione.put(ai.getId().hashCode(),ai);
					mappaArgomenti.get(ai.getArgomenti().getArgomento()).getArgomentiInserziones().add(ai);
					argomentiInserzioneInseriti.add(ai);
					if(!inserimentoArgomentiInserzione)
						inserimentoArgomentiInserzione = true;
					
				}
			}
			if(!utente.equals(inserzioneVecchia.getUtente())){
				inserzioneVecchia.setUtente(utente);
				mappaUtente.get(inserzioneVecchia.getUtente().getMail()).getInserziones().remove(inserzioneVecchia);
				mappaUtente.get(utente.getMail()).getInserziones().add(inserzioneVecchia);
			}
			if(!supermercato.equals(inserzioneVecchia.getSupermercato())){				
				inserzioneVecchia.setSupermercato(supermercato);
				mappaSupermercati.get(inserzioneVecchia.getSupermercato().getNome()).getInserziones().remove(inserzioneVecchia);
				mappaSupermercati.get(supermercato.getNome()).getInserziones().add(inserzioneVecchia);
			}
			if(!prodotto.equals(inserzioneVecchia.getProdotto())){
				inserzioneVecchia.setProdotto(prodotto);
				mappaProdotti.get(inserzioneVecchia.getProdotto().getCodiceBarre()).getInserziones().remove(inserzioneVecchia);
				mappaProdotti.get(prodotto.getCodiceBarre()).getInserziones().add(inserzioneVecchia);		
			}
			

			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			if(eliminazioneArgomentiInserzione && !inserimentoArgomentiInserzione){
				for(ArgomentiInserzione ai : argomentiInserzioneEliminati){
					mappaArgomentiInserzione.put(ai.getId().hashCode(), ai);
					mappaArgomenti.get(ai.getArgomenti().getArgomento()).getArgomentiInserziones().add(ai);
				}
			}
			if(inserimentoArgomentiInserzione){
				for(ArgomentiInserzione ai : argomentiInserzioneEliminati){
					mappaArgomentiInserzione.put(ai.getId().hashCode(), ai);
					mappaArgomenti.get(ai.getArgomenti().getArgomento()).getArgomentiInserziones().add(ai);
				}
				for(ArgomentiInserzione ai : argomentiInserzioneInseriti){
					mappaArgomentiInserzione.remove(ai.getId().hashCode());
					mappaArgomenti.get(ai.getArgomenti().getArgomento()).getArgomentiInserziones().remove(ai);
				}
			}
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}

	/**Eliminazione di un'inserzione
	 * @param IdInserzione
	 */
	public void eliminaInserzione(int IdInserzione){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		Inserzione inserzioneDaEliminare = mappaInserzioni.get(IdInserzione);
		boolean eliminazioneValutazioneInserzione = false;
		boolean eliminazioneArgomentiInserzione = false;
		if(inserzioneDaEliminare == null)
			throw new RuntimeException("elemento non trovato nelle inserzioni");
		Set<ValutazioneInserzione> valutazioniEliminate = new HashSet<ValutazioneInserzione>();
		Set<ArgomentiInserzione> argomentiInserzioneEliminati = new HashSet<ArgomentiInserzione>();
		
		try{
			tx=session.beginTransaction();
			session.delete(inserzioneDaEliminare);
			
			for(ValutazioneInserzione v : (Set<ValutazioneInserzione>)inserzioneDaEliminare.getValutazioneInserziones()){
				session.delete(v);
				mappaValutazioneInserzione.remove(v.getIdValutazioneInserzione());
				valutazioniEliminate.add(v);
				if(!eliminazioneValutazioneInserzione)
					eliminazioneValutazioneInserzione = true;
			}
			
			for(ArgomentiInserzione ai : (Set<ArgomentiInserzione>)inserzioneDaEliminare.getArgomentiInserziones()){
				
				session.delete(ai);
				mappaArgomentiInserzione.remove(ai.getId().hashCode());
				mappaArgomenti.get(ai.getArgomenti().getArgomento()).getArgomentiInserziones().remove(ai);
				argomentiInserzioneEliminati.add(ai);
				if(!eliminazioneArgomentiInserzione)
					eliminazioneArgomentiInserzione = true;
				
			}
			
			mappaUtente.get(inserzioneDaEliminare.getUtente().getMail()).getInserziones().remove(inserzioneDaEliminare);
			mappaSupermercati.get(inserzioneDaEliminare.getSupermercato().getNome()).getInserziones().remove(inserzioneDaEliminare);
			mappaProdotti.get(inserzioneDaEliminare.getProdotto().getCodiceBarre()).getInserziones().remove(inserzioneDaEliminare);
			mappaInserzioni.remove(IdInserzione);
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			if(eliminazioneValutazioneInserzione && !eliminazioneArgomentiInserzione){
				for(ValutazioneInserzione vi : valutazioniEliminate){
					mappaValutazioneInserzione.put(vi.getIdValutazioneInserzione(), vi);
				}
			}
			if(eliminazioneArgomentiInserzione){
				for(ValutazioneInserzione vi : valutazioniEliminate){
					mappaValutazioneInserzione.put(vi.getIdValutazioneInserzione(), vi);
				}
				for(ArgomentiInserzione ai : argomentiInserzioneEliminati){
					mappaArgomentiInserzione.put(ai.getId().hashCode(), ai);
					mappaArgomenti.get(ai.getArgomenti().getArgomento()).getArgomentiInserziones().add(ai);
				}				
			}
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}

	
	/**Inserimento di una lista desideri
	 * @param utente
	 * @param listaElementi 
	 * Set di ListaDesideriProdotti dove ogni suo elemento ha come id composto, l'id elemento e l'id listadesideri (Hashcodes) da voi creati
	 * @param nomeListaDesideri
	 * @param descrizione
	 */
	public void inserisciListaDesideri(int idListaDesideri,Utente utente,Set<ListaDesideriProdotti> listaElementi,String nomeListaDesideri,String descrizione){
		if(utente == null || listaElementi == null || nomeListaDesideri == null || descrizione == null)
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		boolean salvataggioListaDesideriProdotti = false;
		Set<ListaDesideriProdotti> listaDesideriProdottiAggiunti = new HashSet<ListaDesideriProdotti>();
		try {
			tx=session.beginTransaction();		
			
			ListaDesideri listaDesideri = new ListaDesideri(idListaDesideri,utente, nomeListaDesideri, new HashSet<ListaDesideriProdotti>());
			session.save(listaDesideri);
			
			for(ListaDesideriProdotti listaDesideriProdotto : listaElementi){
				listaDesideri.getListaDesideriProdottis().add(listaDesideriProdotto);
				session.save(listaDesideriProdotto);
				mappaListaDesideriProdotti.put(listaDesideriProdotto.getId(), listaDesideriProdotto);
				listaDesideriProdottiAggiunti.add(listaDesideriProdotto);
				
				if(!salvataggioListaDesideriProdotti)
					salvataggioListaDesideriProdotti=true;
			}
					
			mappaListaDesideri.put(idListaDesideri,listaDesideri);
			mappaUtente.get(utente.getMail()).getListaDesideris().add(listaDesideri);
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			if(salvataggioListaDesideriProdotti == true){
				for(ListaDesideriProdotti ldp : listaDesideriProdottiAggiunti){
					mappaListaDesideriProdotti.remove(ldp.getId());
				}
			}
				
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}

	/**modifica di una lista desideri
	 * @param idListaDesideri 
	 * id della listadesideri vecchia da modificare
	 * @param utente
	 * @param nomeListaDesideri
	 * @param listaDesideriElementi 
	 * Set di ListaDesideriProdotti nuova, dove ogni suo elemento ha come id composto, l'id elemento e l'id listadesideri (Hashcodes) da voi creati
	 * 
	 */
	public void modificaListaDesideri(int idListaDesideri,Utente utente,String nomeListaDesideri,Set<ListaDesideriProdotti> listaDesideriElementi){
		Session session = factory.getCurrentSession();

		Transaction tx = null;
		if(idListaDesideri <= 0 || utente == null || nomeListaDesideri == null || listaDesideriElementi == null)
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");
		
		ListaDesideri listaDesideri = new ListaDesideri(idListaDesideri,utente, nomeListaDesideri,new HashSet<ListaDesideriProdotti>());
		boolean salvataggioListaDesideriProdotti = false;
		boolean eliminazioneListaDesideriProdotti = false;
		ListaDesideri listaDesideriVecchia = mappaListaDesideri.get(idListaDesideri);
		if(listaDesideriVecchia == null) 
			throw new RuntimeException("elemento non trovato");
		Set<ListaDesideriProdotti> listaDesideriProdottiVecchia = new HashSet<ListaDesideriProdotti>();
		listaDesideriProdottiVecchia.addAll(listaDesideriVecchia.getListaDesideriProdottis());
		Set<ListaDesideriProdotti> listaDesideriProdottiAggiunti = new HashSet<ListaDesideriProdotti>();
		Set<ListaDesideriProdotti> listaDesideriProdottiRimossi = new HashSet<ListaDesideriProdotti>();
		try {
			tx=session.beginTransaction();
			session.update(listaDesideri);		
			
			for(ListaDesideriProdotti ldp : (Set<ListaDesideriProdotti>) listaDesideriVecchia.getListaDesideriProdottis()){
				if(!listaDesideriElementi.contains(ldp)){
					session.delete(ldp);
					mappaListaDesideriProdotti.remove(ldp.getId());
					listaDesideriProdottiRimossi.add(ldp);					
					if(!eliminazioneListaDesideriProdotti)
						eliminazioneListaDesideriProdotti = true;
				}
			}				
			for(ListaDesideriProdotti listaDesideriProdotto : listaDesideriElementi) {
				if(!listaDesideriProdottiVecchia.contains(listaDesideriProdotto)){
					session.save(listaDesideriProdotto);
					mappaListaDesideriProdotti.put(listaDesideriProdotto.getId(), listaDesideriProdotto);
					listaDesideriProdottiAggiunti.add(listaDesideriProdotto);					
					if(!salvataggioListaDesideriProdotti)
						salvataggioListaDesideriProdotti = true;
				}
				listaDesideri.getListaDesideriProdottis().add(listaDesideriProdotto);
			}
			mappaListaDesideri.remove(idListaDesideri);
			mappaListaDesideri.put(idListaDesideri, listaDesideri);
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();			
			
			if(eliminazioneListaDesideriProdotti && !salvataggioListaDesideriProdotti) {
								
				for(ListaDesideriProdotti ldp : listaDesideriProdottiRimossi){					
					mappaListaDesideriProdotti.put(ldp.getId(),ldp);
				}
			}
			
			if(salvataggioListaDesideriProdotti && !eliminazioneListaDesideriProdotti){
				
				
				for(ListaDesideriProdotti ldp : listaDesideriProdottiAggiunti){
					mappaListaDesideriProdotti.remove(ldp.getId());
				}
				
			}
			
			if(salvataggioListaDesideriProdotti && eliminazioneListaDesideriProdotti){
				
				
				for(ListaDesideriProdotti ldp : listaDesideriProdottiAggiunti){
					mappaListaDesideriProdotti.remove(ldp.getId());
				}
				
				for(ListaDesideriProdotti ldp : listaDesideriProdottiRimossi){					
					mappaListaDesideriProdotti.put(ldp.getId(),ldp);
				}				
			}
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}

	/**Eliminazione di una lista desideri
	 * @param idListaDesideri
	 */
	public void eliminaListaDesideri(int idListaDesideri) {
		if(idListaDesideri <= 0)
			throw new RuntimeException("id non valido");	
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		boolean eliminazioneListaDesideriProdotti = false;
		ListaDesideri listaDesideri = null;
		Set<ListaDesideriProdotti> listaDesideriProdotti = new HashSet<ListaDesideriProdotti>();
		listaDesideri=mappaListaDesideri.get(idListaDesideri);
		if(listaDesideri == null)
			throw new RuntimeException("elemento non trovato");
		try {
			tx=session.beginTransaction();			
				
			for(ListaDesideriProdotti ldp : (Set<ListaDesideriProdotti>)listaDesideri.getListaDesideriProdottis()) {					
				session.delete(ldp);
				mappaListaDesideriProdotti.remove(ldp.getId());
				listaDesideriProdotti.add(ldp);
				
				if(!eliminazioneListaDesideriProdotti)
					eliminazioneListaDesideriProdotti = true;
			}
			
			session.delete(listaDesideri);
			mappaListaDesideri.remove(listaDesideri.getIdListaDesideri());
			mappaUtente.get(listaDesideri.getUtente().getMail()).getListaDesideris().remove(listaDesideri);			
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			if(eliminazioneListaDesideriProdotti){
				for(ListaDesideriProdotti ldp : listaDesideriProdotti) {					
					mappaListaDesideriProdotti.put(ldp.getId(),ldp);
				}
			}
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}

	/**metodo get di una ListaDesideri
	 * @param idListaDesideri
	 * @return
	 */
	public ListaDesideri getListaDesideri(int idListaDesideri){
	
		ListaDesideri ld = mappaListaDesideri.get(idListaDesideri);
		if(ld==null)
			throw new RuntimeException("elemento non trovato");
		return ld;
	}


	/**Inserimento di una inserzione
	 * @param utente
	 * @param prodottiQuantita
	 * Set di ListaSpesaProdotti nuova, dove ogni suo elemento ha come id composto, l'id elemento e l'id listaspesa (Hashcodes) da voi creati
	 * @param nomeListaSpesa
	 */
	public void inserimentoListaSpesa(int idSpesa,Utente utente,Set<ListaSpesaProdotti> prodottiQuantita,String nomeListaSpesa){
		if(utente == null || prodottiQuantita == null || nomeListaSpesa == null )
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		Set<ListaSpesaProdotti> listaSpesaProdotti = new HashSet<ListaSpesaProdotti>();
		boolean salvataggioListaSpesaProdotti = false;
		try{
			tx=session.beginTransaction();			
			
			ListaSpesa listaspesa = new ListaSpesa(idSpesa,utente, nomeListaSpesa);
			session.save(listaspesa);
			
			for(ListaSpesaProdotti listaSpesaProdotto : prodottiQuantita){
				session.save(listaSpesaProdotto);
				mappaListaSpesaProdotti.put(listaSpesaProdotto.getId(), listaSpesaProdotto);
				listaSpesaProdotti.add(listaSpesaProdotto);
				if(!salvataggioListaSpesaProdotti)
					salvataggioListaSpesaProdotti=true;
			}
		
			mappaListaSpesa.put(idSpesa,listaspesa);
			mappaUtente.get(utente.getMail()).getListaSpesas().add(listaspesa);
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			if(salvataggioListaSpesaProdotti){
				for(ListaSpesaProdotti lsp : listaSpesaProdotti){
					mappaListaSpesaProdotti.remove(lsp.getId());
				}
			}
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}

	/**Modifica di una ListaSpesa
	 * @param idSpesa
	 * @param utente
	 * @param nomeListaSpesa
	 * @param prodottiQuantita
	 * Set di ListaSpesaProdotti nuova, dove ogni suo elemento ha come id composto, l'id elemento e l'id listaspesa (Hashcodes) da voi creati
	 */
	public void modificaListaSpesa(int idSpesa,Utente utente,String nomeListaSpesa,Set<ListaSpesaProdotti> prodottiQuantita){
		if(utente == null || prodottiQuantita == null || nomeListaSpesa == null )
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");
		
		Session session = factory.getCurrentSession();
		Transaction tx = null;
				
		Set<ListaSpesaProdotti> listaSpesaProdottiVecchia = new HashSet<ListaSpesaProdotti>();
		Set<ListaSpesaProdotti> listaSpesaProdottiInseriti = new HashSet<ListaSpesaProdotti>();
		Set<ListaSpesaProdotti> listaSpesaProdottiRimossi = new HashSet<ListaSpesaProdotti>();
		boolean salvataggioListaSpesaProdotti = false;
		boolean eliminazioneListaSpesaProdottiVecchia = false;
		ListaSpesa listaSpesa = new ListaSpesa(idSpesa,utente, nomeListaSpesa, new HashSet<ListaSpesaProdotti>());
		ListaSpesa listaSpesaVecchia = mappaListaSpesa.get(idSpesa);		
		
		if(listaSpesaVecchia == null)
			throw new RuntimeException("elemento non trovato");
		
		listaSpesaProdottiVecchia.addAll(listaSpesaVecchia.getListaSpesaProdottis());

		try{
			tx=session.beginTransaction();
			session.update(listaSpesa);					
			for(ListaSpesaProdotti listaSpesaProdotto : prodottiQuantita){
				if(!listaSpesaProdottiVecchia.contains(listaSpesaProdotto)){
				
					session.save(listaSpesaProdotto);
					listaSpesaProdottiInseriti.add(listaSpesaProdotto);
					mappaListaSpesaProdotti.put(listaSpesaProdotto.getId(), listaSpesaProdotto);
					if(!salvataggioListaSpesaProdotti)
						salvataggioListaSpesaProdotti=true;				
				}
				listaSpesa.getListaSpesaProdottis().add(listaSpesaProdotto);	
			}
			
			for(ListaSpesaProdotti lsp :(Set<ListaSpesaProdotti>) listaSpesaVecchia.getListaSpesaProdottis()){
				if(!prodottiQuantita.contains(lsp)){
					session.delete(lsp);
					listaSpesaProdottiRimossi.add(lsp);
					mappaListaSpesaProdotti.remove(lsp.getId());
					if(!eliminazioneListaSpesaProdottiVecchia)
						eliminazioneListaSpesaProdottiVecchia = true;
				}
			}
			mappaListaSpesa.remove(idSpesa);
			mappaListaSpesa.put(listaSpesa.getIdSpesa(), listaSpesa);
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			if(eliminazioneListaSpesaProdottiVecchia && !salvataggioListaSpesaProdotti){
				
				for(ListaSpesaProdotti lsp : listaSpesaProdottiRimossi){
					mappaListaSpesaProdotti.put(lsp.getId(), lsp);
				}
			}
			if(salvataggioListaSpesaProdotti && !eliminazioneListaSpesaProdottiVecchia){
				

				for(ListaSpesaProdotti lsp : listaSpesaProdottiInseriti){
					mappaListaSpesaProdotti.remove(lsp.getId());
				}
				
				for(ListaSpesaProdotti lsp : listaSpesaProdottiRimossi){
					mappaListaSpesaProdotti.put(lsp.getId(), lsp);
				}
				
			}
			if(salvataggioListaSpesaProdotti && eliminazioneListaSpesaProdottiVecchia){
				

				for(ListaSpesaProdotti lsp : listaSpesaProdottiInseriti){
					mappaListaSpesaProdotti.remove(lsp.getId());
				}
				
				for(ListaSpesaProdotti lsp : listaSpesaProdottiRimossi){
					mappaListaSpesaProdotti.put(lsp.getId(), lsp);
				}
				
			}
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}

	/**eliminazione di una ListaSpesa
	 * @param idSpesa
	 */
	public void eliminaListaSpesa(int idSpesa){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		
		if(idSpesa<=0)
			throw new RuntimeException("id non valido");
		
		ListaSpesa listaSpesaDaEliminare = mappaListaSpesa.get(idSpesa);
		
		if(listaSpesaDaEliminare == null)
			throw new RuntimeException("elemento non trovato");
		
		Set<ListaSpesaProdotti> listaSpesaProdotti = new HashSet<ListaSpesaProdotti>();
		boolean eliminazioneListaSpesaProdotti = false;
		try{
			tx=session.beginTransaction();
			
			session.delete(listaSpesaDaEliminare);
			mappaListaSpesa.remove(listaSpesaDaEliminare);

			for(ListaSpesaProdotti lsp : (Set<ListaSpesaProdotti>)listaSpesaDaEliminare.getListaSpesaProdottis()){
				session.delete(lsp);
				mappaListaSpesaProdotti.remove(lsp.getId());
				listaSpesaProdotti.add(lsp);
				if(!eliminazioneListaSpesaProdotti)
					eliminazioneListaSpesaProdotti = true;
			}

			mappaUtente.get(listaSpesaDaEliminare.getUtente().getMail()).getListaSpesas().remove(listaSpesaDaEliminare);
			
			tx.commit();
		}catch(Throwable ex){
			
			if(tx!=null)
				tx.rollback();
			
			if(eliminazioneListaSpesaProdotti){
				mappaListaSpesa.put(listaSpesaDaEliminare.getIdSpesa(), listaSpesaDaEliminare);
				for(ListaSpesaProdotti lsp : listaSpesaProdotti){
					mappaListaSpesaProdotti.put(lsp.getId(), lsp);
				}
			}
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}

	/**metodo get per una listaSpesa
	 * @param idSpesa
	 * @return
	 */
	public ListaSpesa getListaSpesa(int idSpesa){
		ListaSpesa listaSpesa = mappaListaSpesa.get(idSpesa);
		
		if(listaSpesa==null)
			throw new RuntimeException("elemento non trovato");
		
		return listaSpesa;
	}

	/**Inserimento di un prodotto
	 * @param sottoCategoria
	 * @param codiceBarre
	 * @param descrizione
	 * @return
	 */
	public int inserisciProdotto(Sottocategoria sottoCategoria,long codiceBarre,String descrizione){
		
		if(codiceBarre <=0 || descrizione == null )
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");

		Session session = factory.getCurrentSession();
		Transaction tx = null;
		int idProdotto=-1;
		try{
			tx=session.beginTransaction();
			Prodotto prodotto = new Prodotto(sottoCategoria,codiceBarre, descrizione, new HashSet<Inserzione>());			
			idProdotto = (Integer)session.save(prodotto);
			prodotto.setIdProdotto(idProdotto);
			mappaProdotti.put(codiceBarre,prodotto);
			mappaSottocategorie.get(sottoCategoria.getNome()).getProdottos().add(prodotto);
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
		return idProdotto;
	}

	/**modifica di un Prodotto
	 * @param sottoCategoria
	 * @param idProdotto
	 * @param codiceBarre
	 * @param descrizione
	 */
	public void modificaProdotto(Sottocategoria sottoCategoria,int idProdotto,long codiceBarre,String descrizione){
		Prodotto prodottoVecchio = mappaProdotti.get(codiceBarre);
		
		if(prodottoVecchio == null)
			throw new RuntimeException("elemento non trovato");
		
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		
		if(idProdotto <= 0 || codiceBarre<=0 || descrizione == null)
			throw new RuntimeException("tutti gli argomenti devono essere immessi");
		
		long codiceBarreVecchio = prodottoVecchio.getCodiceBarre();
		Sottocategoria sottoCategoriaVecchia = prodottoVecchio.getSottocategoria();
		String descrizioneVecchia = prodottoVecchio.getDescrizione();
		Prodotto prodotto = new Prodotto(sottoCategoria, codiceBarre, descrizione, prodottoVecchio.getInserziones());
		prodotto.setIdProdotto(idProdotto);
		
		try{
			tx=session.beginTransaction();
			session.update(prodotto);	
			prodottoVecchio.setCodiceBarre(codiceBarre);
			prodottoVecchio.setDescrizione(descrizione);
			prodottoVecchio.setSottocategoria(sottoCategoria);		
			tx.commit();

		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			prodottoVecchio.setCodiceBarre(codiceBarreVecchio);
			prodottoVecchio.setDescrizione(descrizioneVecchia);
			prodottoVecchio.setSottocategoria(sottoCategoriaVecchia);
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}


	}

	/**Eliminazione di un Prodotto
	 * @param codiceBarre
	 */
	public void eliminaProdotto(long codiceBarre){

		if(codiceBarre <= 0)
			throw new RuntimeException("idprodotto non valido");

		Session session = factory.getCurrentSession();
		Transaction tx = null;
		Prodotto prodotto = mappaProdotti.get(codiceBarre);
		boolean eliminazioneInserzione = false;
		Set<Inserzione> inserzioni = new HashSet<Inserzione>();
		if(prodotto!=null){
			try{
				tx=session.beginTransaction();
				session.delete(prodotto);
				
				for(Inserzione i : (Set<Inserzione>)prodotto.getInserziones()){
					session.delete(i);
					mappaInserzioni.remove(i.getIdInserzione());
					inserzioni.add(i);
					
					if(!eliminazioneInserzione)
						eliminazioneInserzione = true;
				}				
				tx.commit();

			}catch(Throwable ex){
				if(tx!=null)
					tx.rollback();
				if(eliminazioneInserzione){
					for(Inserzione i : inserzioni){
						mappaInserzioni.put(i.getIdInserzione(),i);
					}
				}				
				
				throw new RuntimeException(ex);
			}finally{
				if(session!=null && session.isOpen()){
					session.close();
				}
				session=null;
			}
		}else{
			throw new RuntimeException("elemento non trovato");
		}

	}

	/**Metodo get della mappa prodotti
	 * @return
	 */
	public Map<Long,Prodotto> getProdotti(){

		HashMap<Long,Prodotto> prodotti = new HashMap<Long,Prodotto>();
		prodotti.putAll(mappaProdotti);
		
		return prodotti;
	}

	/**Metodo get della mappa Inserzioni
	 * @return
	 */
	public Map<Integer,Inserzione> getInserzioni(){
		Map<Integer,Inserzione> inserzioni = new HashMap<Integer,Inserzione>();
		
		inserzioni.putAll(mappaInserzioni);
		
		return inserzioni;
	}

	/**metodo get della mappa profili
	 * @return
	 */
	public Map<Integer,Profilo> getProfili(){
		HashMap<Integer,Profilo> profili = new HashMap<Integer,Profilo>();
		
		profili.putAll(mappaProfili);

		return profili;

	}

	/**Inserimento di un Profilo
	 * @param utente
	 * @param creditiAcquisiti
	 * @param creditiPendenti
	 * @param reputazione
	 * @param premium
	 * @param contatoreInfrazioni
	 */
	public void inserisciProfilo(Utente utente,int creditiAcquisiti,int creditiPendenti,int reputazione,boolean premium,int contatoreInfrazioni){
		if(utente == null || creditiAcquisiti<0 || creditiPendenti<0  || contatoreInfrazioni<0)
			throw new RuntimeException("parametro/i non validi");
		
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		Profilo profilo = new Profilo(utente, creditiAcquisiti, creditiPendenti, reputazione, premium, contatoreInfrazioni,0,0,0,0);
		try{
			tx=session.beginTransaction();
			Integer idProfilo=(Integer)session.save(profilo);
			mappaUtente.get(utente.getMail()).getProfilos().add(profilo);	
			mappaProfili.put(idProfilo,profilo);
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}

	/**Modifica di un profilo
	 * @param idProfilo
	 * @param utente
	 * @param creditiAcquisiti
	 * @param creditiPendenti
	 * @param reputazione
	 * @param premium
	 * @param contatoreInfrazioni
	 */
	public void modificaProfilo(int idProfilo,int creditiAcquisiti,int creditiPendenti,int reputazione,boolean premium,int contatoreInfrazioni,int numeroInserzioniPositive,int numeroInserzioniTotali,int numeroValutazioniPositive,int numeroValutazioniTotali){
		if(idProfilo <0 || creditiAcquisiti<0 || creditiPendenti<0 ||  contatoreInfrazioni<0)
			throw new RuntimeException("parametro/i non validi");
		
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		Profilo profiloVecchio = mappaProfili.get(idProfilo);

		if(profiloVecchio != null){
			Profilo profilo = new Profilo(profiloVecchio.getUtente(), creditiAcquisiti, creditiPendenti, reputazione, premium, contatoreInfrazioni,numeroInserzioniPositive,numeroInserzioniTotali,numeroValutazioniPositive, numeroValutazioniTotali);
			profilo.setIdProfilo(idProfilo);

			try{
				tx=session.beginTransaction();
				session.update(profilo);
				profiloVecchio.setContatoreInfrazioni(contatoreInfrazioni);
				profiloVecchio.setCreditiAcquisiti(creditiAcquisiti);
				profiloVecchio.setCreditiPendenti(creditiPendenti);
				profiloVecchio.setPremium(premium);
				profiloVecchio.setReputazione(reputazione);
				tx.commit();
			}catch(Throwable ex){
				if(tx!=null)
					tx.rollback();
				throw new RuntimeException(ex);
			}finally{
				if(session!=null && session.isOpen()){
					session.close();
				}
				session=null;
			}
		}else{
			throw new RuntimeException("elemento non trovato");
		}
	}

	/**metodo per eliminare un profilo
	 * @param idProfilo
	 */
	public void eliminaProfilo(int idProfilo){
		if(idProfilo < 0)
			throw new RuntimeException("id non valido");
		
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		Profilo profiloVecchio = mappaProfili.get(idProfilo);

		if(profiloVecchio == null)
			throw new RuntimeException("elemento non trovato");

		try{
			tx=session.beginTransaction();
			session.delete(profiloVecchio);
			mappaUtente.get(profiloVecchio.getUtente().getMail()).getProfilos().remove(profiloVecchio);
			mappaProfili.remove(idProfilo);
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}

	}

	/**Inserimento di una SottoCategoria
	 * @param categoria
	 * @param nome
	 * @param prodotti
	 */
	public void inserisciSottocategoria(Categoria categoria,String nome){
		if(categoria == null || nome == null)
			throw new RuntimeException("tutti i parametri devono essere non nulli");
		
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		Sottocategoria sottoCategoria = new Sottocategoria(categoria, nome,new HashSet<Prodotto>());

		try{
			tx=session.beginTransaction();
			Integer idSottoCategoria = (Integer)session.save(sottoCategoria);
			mappaCategorie.get(categoria.getIdCategoria()).getSottocategorias().add(sottoCategoria);			
			mappaSottocategorie.put(nome,sottoCategoria);
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}

	/**modifica SottoCategoria
	 * @param idSottoCategoria
	 * @param categoria
	 * @param nome
	 */
	public void modificaSottocategoria(int idSottoCategoria,Categoria categoria,String nome){
		if(idSottoCategoria <=0 || categoria == null || nome == null)
			throw new RuntimeException("parametri non corretti");
		
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		Sottocategoria sottoCategoriaVecchia = mappaSottocategorie.get(idSottoCategoria);
		
		if(sottoCategoriaVecchia == null)
			throw new RuntimeException("elemento non trovato");
		
		Sottocategoria sottoCategoria = new Sottocategoria(categoria, nome,sottoCategoriaVecchia.getProdottos());
		sottoCategoria.setIdSottocategoria(idSottoCategoria);

		try{
			tx=session.beginTransaction();
			session.update(sottoCategoria);
			sottoCategoriaVecchia.setCategoria(categoria);
			sottoCategoriaVecchia.setNome(nome);	
			if(!categoria.equals(sottoCategoriaVecchia.getCategoria()))
				mappaCategorie.get(categoria.getIdCategoria()).getSottocategorias().add(sottoCategoriaVecchia);
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}

	/**Elimina una SottoCategoria (mette nei prodotti associati un oggetto vuoto)
	 * @param idSottoCategoria
	 */
	public void eliminaSottocategoria(int idSottoCategoria){
		if(idSottoCategoria<=0)
			throw new RuntimeException("id non valido");
		
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		Sottocategoria sottoCategoriaVecchia = mappaSottocategorie.get(idSottoCategoria);	

		if(sottoCategoriaVecchia == null)
			throw new RuntimeException("elemento non trovato");

		try{
			tx=session.beginTransaction();
			session.delete(sottoCategoriaVecchia);
			mappaCategorie.get(sottoCategoriaVecchia.getCategoria().getIdCategoria()).getSottocategorias().remove(sottoCategoriaVecchia);
			for(Prodotto p : (Set<Prodotto>)sottoCategoriaVecchia.getProdottos()){
				p.setSottocategoria(new Sottocategoria());
			}
			mappaSottocategorie.remove(sottoCategoriaVecchia.getNome());

			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}

	public Map<String,Sottocategoria> getSottocategorie(){

		HashMap<String,Sottocategoria> sottoCategorie = new HashMap<String,Sottocategoria>();
		sottoCategorie.putAll(mappaSottocategorie);
		return sottoCategorie;

	}

	/**Inserimento di SuperMercato
	 * @param nome
	 * @param latitudine
	 * @param longitudine
	 * @return
	 */
	public int inserisciSupermercato(String nome,float latitudine,float longitudine){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		if(nome == null )
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");

		Supermercato supermercato = new Supermercato(nome, latitudine, longitudine, new HashSet<Inserzione>());
		Integer idSuperMercato ;

		try{
			tx=session.beginTransaction();
			idSuperMercato = (Integer)session.save(supermercato);
			mappaSupermercati.put(nome,supermercato);

			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.flush();
				session.close();
			}
			session=null;
		}
		return idSuperMercato;
	}

	/**Modifica di un SuperMercato
	 * @param idSuperMercato
	 * @param nome
	 * @param latitudine
	 * @param longitudine
	 */
	public void modificaSupermercato(int idSuperMercato,String nome,float latitudine,float longitudine){
		if(idSuperMercato <=0 || nome == null)
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");
		
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		Supermercato superMercatoVecchio = mappaSupermercati.get(idSuperMercato);			

		if(superMercatoVecchio==null)
			throw new RuntimeException("elemento non trovato");

		Supermercato superMercato = new Supermercato(nome, latitudine, longitudine, new HashSet<Inserzione>());
		superMercato.setIdSupermercato(idSuperMercato);
		superMercato.getInserziones().addAll(superMercatoVecchio.getInserziones());

		try{
			tx=session.beginTransaction();			
			session.update(superMercato);
			mappaSupermercati.remove(idSuperMercato);
			mappaSupermercati.put(nome,superMercato);
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}


	}

	/**Eliminazione di un Supermercato
	 * @param nomeSuperMercato
	 */
	public void eliminaSupermercato(String nomeSuperMercato){

		Session session = factory.getCurrentSession();
		Transaction tx = null;
		Supermercato superMercatoVecchio = mappaSupermercati.get(nomeSuperMercato);		

		if(superMercatoVecchio==null)
			throw new RuntimeException("elemento non trovato");

		try{
			tx=session.beginTransaction();
			session.delete(superMercatoVecchio);
			
			for(Inserzione i : (Set<Inserzione>)superMercatoVecchio.getInserziones()){
				eliminaInserzione(i.getIdInserzione());
			}
			
			mappaSupermercati.remove(superMercatoVecchio);
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}

	}

	/**Metodo get della mappa dei Supermercati
	 * @return
	 */
	public Map<String,Supermercato> getSupermercati(){
		Map<String,Supermercato> supermercati = new HashMap<String,Supermercato>();
		supermercati.putAll(mappaSupermercati);			
		return supermercati;
	}

	public int inserisciUtente(String email,String nickname,String password,Date dataRegistrazione,String numeroCasuale){
		if(email == null || nickname == null || password == null || dataRegistrazione == null || numeroCasuale == null)
			throw new RuntimeException("i parametri devono essere non nulli");
		
		Session session = factory.getCurrentSession();
		Transaction tx = null;	
		Utente utente = new Utente(email, nickname, password, dataRegistrazione,false,numeroCasuale,new HashSet<ValutazioneInserzione>(), new HashSet<ValutazioneInserzione>(), new HashSet<ListaSpesa>(), new HashSet<Inserzione>(), new HashSet<Profilo>(), new HashSet<ListaDesideri>());		
		int idUtente,idProfilo;
		try{
			tx=session.beginTransaction();
			idUtente = (Integer)session.save(utente);
			inserisciProfilo(utente, 0, 0, 0, false, 0);
			mappaUtente.put(email,utente);
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
		return idUtente;
	}

	/**Modifica di un utente
	 * @param idUtente
	 * @param email
	 * @param nickname
	 * @param password
	 * @param dataRegistrazione
	 * @param confermato
	 * @param numeroCasuale
	 */
	public void modificaUtente(int idUtente,String email,String nickname,String password,Date dataRegistrazione,boolean confermato,String numeroCasuale){
		if(email == null || nickname == null || password == null || dataRegistrazione == null || numeroCasuale == null)
			throw new RuntimeException("i parametri devono essere non nulli");
		
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		Utente utenteVecchio = mappaUtente.get(idUtente);

		if(utenteVecchio==null)
			throw new RuntimeException("elemento non trovato");		

		Utente utente = new Utente(email, nickname, password, dataRegistrazione,confermato,numeroCasuale, 
				utenteVecchio.getValutazioneInserzionesForIdUtenteInserzionista(),
				utenteVecchio.getValutazioneInserzionesForIdUtenteValutatore(), 
				utenteVecchio.getListaSpesas(), utenteVecchio.getInserziones(), 
				utenteVecchio.getProfilos(), utenteVecchio.getListaDesideris());
		utente.setIdUtente(idUtente);
		
		try{
			tx=session.beginTransaction();
			session.update(utente);
			utenteVecchio.setConfermato(confermato);
			utenteVecchio.setDataRegistrazione(dataRegistrazione);
			utenteVecchio.setMail(email);
			utenteVecchio.setNickname(nickname);
			utenteVecchio.setNumeroCasuale(numeroCasuale);
			utenteVecchio.setPassword(password);
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}

	}


	/**eliminazione Utente
	 * @param idUtente
	 */
	public void eliminaUtente(int idUtente){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		Utente utenteVecchio = mappaUtente.get(idUtente);
		
		if(utenteVecchio==null)
			throw new RuntimeException("elemento non trovato");

		try{
			tx=session.beginTransaction();
			session.delete(utenteVecchio);

			for(Inserzione i : (Set<Inserzione>)utenteVecchio.getInserziones()){
				mappaInserzioni.get(i.getIdInserzione()).setUtente(null);
			}

			for(ValutazioneInserzione v : (Set<ValutazioneInserzione>) utenteVecchio.getValutazioneInserzionesForIdUtenteInserzionista()){
				v.setUtenteByIdUtenteInserzionista(null);
			}
			
			for(ValutazioneInserzione v : (Set<ValutazioneInserzione>) utenteVecchio.getValutazioneInserzionesForIdUtenteValutatore()){
				v.setUtenteByIdUtenteValutatore(null);
			}

			for(ListaSpesa ls : (Set<ListaSpesa>) utenteVecchio.getListaSpesas()){
				mappaListaSpesa.remove(ls);
			}

			for(ListaDesideri ld : (Set<ListaDesideri>) utenteVecchio.getListaDesideris()){
				mappaListaDesideri.remove(ld);
			}

			for(Profilo p : (Set<Profilo>) utenteVecchio.getProfilos()){
				mappaProfili.remove(p);
			}

			mappaUtente.remove(utenteVecchio);

			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}

	/**metodo get degli Utenti
	 * @return
	 */
	public Map<String,Utente> getUtenti(){
		HashMap<String,Utente> utenti = new HashMap<String,Utente>();
		utenti.putAll(mappaUtente);
		return utenti;
	}
	
	/** inserimento di una valutazione di un'inserzione
	 * @param inserzione
	 * @param inserzionista
	 * @param valutatore
	 * @param valutazione
	 * @param data
	 */
	public void inserimentoValutazioneInserzione(Inserzione inserzione,Utente inserzionista, Utente valutatore, int valutazione, Date data){
		Session session = factory.getCurrentSession();
		Transaction tx = null;

		if(inserzionista == null || valutatore == null || valutazione<0 || data == null)
			throw new RuntimeException("parametri non corretti");
		
		ValutazioneInserzione valutazioneInserzione = new ValutazioneInserzione(inserzione,inserzionista, valutatore, valutazione, data);
		try{
			tx=session.beginTransaction();
			Integer idValutazioneInserzione = (Integer)session.save(valutazioneInserzione);
			mappaValutazioneInserzione.put(idValutazioneInserzione,valutazioneInserzione);
			mappaUtente.get(inserzionista.getMail()).getValutazioneInserzionesForIdUtenteInserzionista().add(valutazioneInserzione);
			mappaUtente.get(valutatore.getMail()).getValutazioneInserzionesForIdUtenteValutatore().add(valutazioneInserzione);
			mappaInserzioni.get(inserzione.getIdInserzione()).getValutazioneInserziones().add(valutazioneInserzione);
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}
	

	/**modifica di una valutazione di una inserzione
	 * @param idValutazione
	 * @param inserzione
	 * @param inserzionista
	 * @param valutatore
	 * @param data
	 * @param valutazione
	 */
	public void modificaValutazioneInserzione(int idValutazione,Inserzione inserzione,Utente inserzionista,Utente valutatore,Date data,int valutazione){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		ValutazioneInserzione valutazioneInserzioneVecchia = mappaValutazioneInserzione.get(idValutazione);

		if(idValutazione <=0 || inserzionista == null || valutatore == null || valutazione<0 || data == null)
			throw new RuntimeException("parametri non corretti");

		if(valutazioneInserzioneVecchia==null)
			throw new RuntimeException("elemento non trovato");

		ValutazioneInserzione valutazioneinserzione = new ValutazioneInserzione(inserzione,inserzionista, valutatore, valutazione, data);
		valutazioneinserzione.setIdValutazioneInserzione(idValutazione);

		try{
			tx=session.beginTransaction();
			session.update(valutazioneinserzione);
			if(!inserzionista.equals(valutazioneInserzioneVecchia.getUtenteByIdUtenteInserzionista())){
				mappaUtente.get(valutazioneInserzioneVecchia.getUtenteByIdUtenteInserzionista().getMail()).getValutazioneInserzionesForIdUtenteInserzionista().remove(valutazioneInserzioneVecchia);
				mappaUtente.get(inserzionista.getMail()).getValutazioneInserzionesForIdUtenteInserzionista().add(valutazioneInserzioneVecchia);
			}
			
			if(!valutatore.equals(valutazioneInserzioneVecchia.getUtenteByIdUtenteValutatore())){
				mappaUtente.get(valutazioneInserzioneVecchia.getUtenteByIdUtenteValutatore().getMail()).getValutazioneInserzionesForIdUtenteValutatore().remove(valutazioneInserzioneVecchia);
				mappaUtente.get(valutatore.getMail()).getValutazioneInserzionesForIdUtenteValutatore().add(valutazioneInserzioneVecchia);
			}

			if(!inserzione.equals(valutazioneInserzioneVecchia.getInserzione())){
				mappaInserzioni.get(valutazioneInserzioneVecchia.getInserzione().getIdInserzione()).getValutazioneInserziones().remove(valutazioneInserzioneVecchia);
				mappaInserzioni.get(inserzione.getIdInserzione()).getValutazioneInserziones().add(valutazioneInserzioneVecchia);
			}
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}

	}

	/**eliminazione di una valutazione di una inserzione
	 * @param idValutazione
	 */
	public void eliminaValutazioneInserzione(int idValutazione){

		Session session = factory.getCurrentSession();
		Transaction tx = null;
		ValutazioneInserzione valutazioneVecchia = mappaValutazioneInserzione.get(idValutazione);

		if(idValutazione <=0 )
			throw new RuntimeException("id non valido");

		if(valutazioneVecchia==null)
			throw new RuntimeException("elemento non trovato");

		try{
			tx=session.beginTransaction();
			session.delete(valutazioneVecchia);
			mappaUtente.get(valutazioneVecchia.getUtenteByIdUtenteInserzionista().getMail()).getValutazioneInserzionesForIdUtenteInserzionista().remove(valutazioneVecchia);
			mappaUtente.get(valutazioneVecchia.getUtenteByIdUtenteValutatore().getMail()).getValutazioneInserzionesForIdUtenteValutatore().remove(valutazioneVecchia);
			mappaInserzioni.get(valutazioneVecchia.getInserzione().getIdInserzione()).getValutazioneInserziones().remove(valutazioneVecchia);
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}

	}


	/**metodo get della mappa ValutazioniInserzioni
	 * @return
	 */
	public Map<Integer,ValutazioneInserzione> getValutazioniInserzioni(){
		HashMap<Integer,ValutazioneInserzione> valutazioni = new HashMap<Integer,ValutazioneInserzione>();
		valutazioni.putAll(mappaValutazioneInserzione);
		return valutazioni;
	}
}
