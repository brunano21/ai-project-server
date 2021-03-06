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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.joda.time.DateTime;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
/**
 * @author ciakky
 *
 */

/**
 * @author IgnazioChp
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
	private volatile Map<Integer,Supermercato> mappaSupermercati = new ConcurrentHashMap<Integer, Supermercato>();
	private volatile Map<Integer,ValutazioneInserzione> mappaValutazioneInserzione = new ConcurrentHashMap<Integer, ValutazioneInserzione>();

	/***
	 * Questa � session factory
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
				mappaSupermercati.put(s.getIdSupermercato(), s);
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
	 * Questo metodo inizializza il timer che scatter� per la prima volta alla mezzanotte del giorno in cui viene lanciato
	 * e successivamente ogni 24 ore. Inoltre inizializza un oggetto della classe TimerSistemaCrediti che contiene il metodo (run) che
	 * verr� eseguito quando il timer scatter�.
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
			Inserzione inserzione = new Inserzione(utente, supermercato, prodotto, prezzo, dataInizio, dataFine, descrizione, foto,0,(float)0.0,new HashSet<ListaDesideriProdotti>(),new HashSet<ListaSpesaProdotti>(),new HashSet<ValutazioneInserzione>(),new HashSet<ArgomentiInserzione>());
			idInserzione=(Integer)session.save(inserzione);

			if(argomenti != null && valori != null) {
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
			}
			session.update(inserzione);
			mappaInserzioni.put(idInserzione,inserzione);			
			mappaUtente.get(utente.getMail()).getInserziones().add(inserzione);
			mappaSupermercati.get(supermercato.getIdSupermercato()).getInserziones().add(inserzione);
			mappaProdotti.get(prodotto.getCodiceBarre()).getInserziones().add(inserzione);

			// Aggiugo all'utente i crediti pendenti che gli spettano dall'inserimento dell'inserzione, ossia +10
			Profilo profilo = (Profilo) utente.getProfilos().iterator().next();
			profilo.setCreditiPendenti(profilo.getCreditiPendenti() + 10);
			session.update(profilo);
			mappaProfili.put(profilo.getIdProfilo(), profilo);
			mappaUtente.get(utente.getMail()).getProfilos().add(profilo);
			
			// Assegniamo degli utenti valutatori
			assegnaValutazioni(utente,  supermercato, idInserzione);

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
	 * @param valori - valori degli argomenti
	 */
	public void modificaInserzione(int idInserzione,Utente utente,Supermercato supermercato,Prodotto prodotto,float prezzo,Date dataInizio,Date dataFine,String descrizione,String foto,Set<Argomenti> argomenti,List<String> valori){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		boolean eliminazioneArgomentiInserzione = false;
		boolean inserimentoArgomentiInserzione = false;

		if(idInserzione <= 0 || utente == null || supermercato == null || prodotto == null || prezzo <= 0 || dataInizio == null || dataFine == null || descrizione == null || foto == null)
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");

		Inserzione inserzioneVecchia = null;
		inserzioneVecchia=mappaInserzioni.get(idInserzione);
		if(inserzioneVecchia == null)
			throw new RuntimeException("elemento non trovato");
		Set<ArgomentiInserzione> argomentiInserzione = new HashSet<ArgomentiInserzione>();
		Set<ArgomentiInserzione> argomentiInserzioneEliminati = new HashSet<ArgomentiInserzione>();
		Set<ArgomentiInserzione> argomentiInserzioneInseriti = new HashSet<ArgomentiInserzione>();


		if(argomenti != null && valori != null) {
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
		}
		Inserzione inserzione = new Inserzione(utente, supermercato, prodotto, prezzo, dataInizio, dataFine, descrizione, foto, 0, (float) 0.0,inserzioneVecchia.getListaDesideriProdottis(),inserzioneVecchia.getListaSpesaProdottis(),inserzioneVecchia.getValutazioneInserziones(),argomenti);
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
			/*
			if(!utente.equals(inserzioneVecchia.getUtente())){
				inserzioneVecchia.setUtente(utente);
				mappaUtente.get(inserzioneVecchia.getUtente().getMail()).getInserziones().remove(inserzioneVecchia);
				mappaUtente.get(utente.getMail()).getInserziones().add(inserzioneVecchia);
			}*/

			if(!supermercato.equals(inserzioneVecchia.getSupermercato())){				
				inserzioneVecchia.setSupermercato(supermercato);
				mappaSupermercati.get(inserzioneVecchia.getSupermercato().getIdSupermercato()).getInserziones().remove(inserzioneVecchia);
				mappaSupermercati.get(supermercato.getIdSupermercato()).getInserziones().add(inserzioneVecchia);
			}
			if(!prodotto.equals(inserzioneVecchia.getProdotto())){
				inserzioneVecchia.setProdotto(prodotto);
				mappaProdotti.get(inserzioneVecchia.getProdotto().getCodiceBarre()).getInserziones().remove(inserzioneVecchia);
				mappaProdotti.get(prodotto.getCodiceBarre()).getInserziones().add(inserzioneVecchia);		
			}

			// added by bruno
			mappaInserzioni.put(idInserzione,inserzione);

			tx.commit();
		}catch(Throwable ex){
			ex.printStackTrace();
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
			//throw new RuntimeException(ex);
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
			mappaSupermercati.get(inserzioneDaEliminare.getSupermercato().getIdSupermercato()).getInserziones().remove(inserzioneDaEliminare);
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

	/**
	 * Inserisce una nuova todo list per l'utente loggato.
	 * @param idListaDesideri Id generato lato client tramite un hashcode applicato su un timestamp.
	 * @param utente utente loggato
	 * @param nomeListaDesideri nome assegnato alla lista desideri.
	 */
	public void inserisciListaDesideri(int idListaDesideri, Utente utente, String nomeListaDesideri) {
		if(idListaDesideri == 0 || utente == null || nomeListaDesideri == null)
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");

		Session session = factory.getCurrentSession();
		Transaction tx = null;
		try {
			tx=session.beginTransaction();		

			ListaDesideri listaDesideri = new ListaDesideri(idListaDesideri, utente, nomeListaDesideri);
			session.save(listaDesideri);
			mappaListaDesideri.put(idListaDesideri, listaDesideri);
			mappaUtente.get(utente.getMail()).getListaDesideris().add(listaDesideri);

			tx.commit();
		} catch(Throwable ex) {
			if(tx != null) {
				tx.rollback();
				throw new RuntimeException(ex);
			}
		} finally {
			if(session != null && session.isOpen()) 
				session.close();
			session = null;
		}
	}

	/**
	 * Modifica il nome di una lista desideri
	 * @param idListaDesideri
	 * @param utente
	 * @param nuovoNomeListaDesideri
	 */
	public void modificaNomeListaDesideri(int idListaDesideri, Utente utente, String nuovoNomeListaDesideri) {
		if(idListaDesideri == 0 || utente == null || nuovoNomeListaDesideri == null)
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");

		Session session = factory.getCurrentSession();
		Transaction tx = null;
		try {
			tx=session.beginTransaction();		

			if(!mappaListaDesideri.containsKey(idListaDesideri))
				throw new RuntimeException("Id lista desideri non trovato: " + idListaDesideri);

			if(!mappaUtente.get(utente.getMail()).getListaDesideris().contains(mappaListaDesideri.get(idListaDesideri)))
				throw new RuntimeException("Id lista desideri non appartiene all'utente: " + idListaDesideri);

			ListaDesideri tmpListaDesideri = mappaListaDesideri.get(idListaDesideri);
			tmpListaDesideri.setNomeListaDesideri(nuovoNomeListaDesideri);
			session.update(tmpListaDesideri);

			mappaListaDesideri.put(idListaDesideri, tmpListaDesideri);

			for(Iterator ldIterator = mappaUtente.get(utente.getMail()).getListaDesideris().iterator(); ldIterator.hasNext();) {
				ListaDesideri ld = (ListaDesideri) ldIterator.next();
				if(ld.getIdListaDesideri() != idListaDesideri)
					continue;

				ld.setNomeListaDesideri(nuovoNomeListaDesideri);
				break;
			}

			tx.commit();
		} catch(Throwable ex) {
			if(tx != null) {
				tx.rollback();
				throw new RuntimeException(ex);
			}
		} finally {
			if(session != null && session.isOpen()) 
				session.close();
			session=null;
		}
	}

	/**
	 * Elimina una lista dei desideri dal database, con i relativi elementi eventualmente inseriti
	 * @param idListaDesideri
	 * @param utente
	 */
	public void eliminaListaDesideri(int idListaDesideri, Utente utente) {
		if(idListaDesideri == 0 || utente == null)
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");

		Session session = factory.getCurrentSession();
		Transaction tx = null;
		try {
			tx=session.beginTransaction();		

			if(!mappaListaDesideri.containsKey(idListaDesideri))
				throw new RuntimeException("Id lista desideri non trovato: " + idListaDesideri);

			if(!mappaUtente.get(utente.getMail()).getListaDesideris().contains(mappaListaDesideri.get(idListaDesideri)))
				throw new RuntimeException("Id lista desideri non appartiene all'utente: " + idListaDesideri);

			//rimozione anche dalla lista_desideri_prodotti
			Query query = session.createQuery("FROM ListaDesideriProdotti WHERE ID_ListaDesideri = :id");
			query.setParameter("id", idListaDesideri);
			List prodotti = query.list();

			for (Iterator iterator = prodotti.iterator(); iterator.hasNext();) {
				ListaDesideriProdotti prodotto = (ListaDesideriProdotti) iterator.next();
				mappaListaDesideriProdotti.remove(prodotto.getId());
				session.delete(prodotto);
			}

			session.delete(mappaListaDesideri.get(idListaDesideri));

			mappaUtente.get(utente.getMail()).getListaDesideris().remove(mappaListaDesideri.get(idListaDesideri));
			mappaListaDesideri.remove(idListaDesideri);

			tx.commit();
		} catch(Throwable ex) {
			if(tx != null) {
				tx.rollback();
				throw new RuntimeException(ex);
			}
		} finally {
			if(session != null && session.isOpen()) 
				session.close();
			session=null;
		}
	}

	/**
	 * Inserisci un elemento all'interno di una lista dei desideri.
	 * @param idListaDesideri
	 * @param idElemento
	 * @param descrizione
	 * @param quantita
	 * @param utente
	 * @param idInserzione
	 */
	public void inserisciElementoListaDesideri(int idListaDesideri, int idElemento, String descrizione, int quantita, Utente utente, Integer idInserzione) {
		if(idListaDesideri == 0 || idElemento == 0 || descrizione == null || quantita <=0 || utente == null)
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");

		System.out.println("inserisciElementoListaDesideri");
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		try {
			tx=session.beginTransaction();		

			if(!mappaListaDesideri.containsKey(idListaDesideri))
				throw new RuntimeException("Id lista desideri non trovato: " + idListaDesideri);

			if(!mappaUtente.get(utente.getMail()).getListaDesideris().contains(mappaListaDesideri.get(idListaDesideri)))
				throw new RuntimeException("Id lista desideri non appartiene all'utente: " + idListaDesideri);
			//l'inserzione suggerita dal server � messa a null all'inizio, fare attenzione
			ListaDesideriProdotti elemento = new ListaDesideriProdotti(new ListaDesideriProdottiId(idElemento, idListaDesideri), mappaInserzioni.get(idInserzione), mappaListaDesideri.get(idListaDesideri), descrizione, quantita);

			mappaListaDesideriProdotti.put(new ListaDesideriProdottiId(idElemento, idListaDesideri), elemento);
			session.save(elemento);

			for(Iterator ldIterator = mappaUtente.get(utente.getMail()).getListaDesideris().iterator(); ldIterator.hasNext();) {
				ListaDesideri ld = (ListaDesideri) ldIterator.next();
				if(ld.getIdListaDesideri() != idListaDesideri)
					continue;

				ld.getListaDesideriProdottis().add(elemento);
				break;
			}

			tx.commit();
		} catch(Throwable ex) {
			if(tx != null) {
				tx.rollback();
				throw new RuntimeException(ex);
			}
		} finally {
			if(session != null && session.isOpen()) 
				session.close();
			session = null;
		}
	}

	/**
	 * Modifica il testo di un elemento della lista dei desideri
	 * @param idListaDesideri
	 * @param idElemento
	 * @param descrizione
	 * @param utente
	 */
	public void modificaDescrizioneElementoListaDesideri(int idListaDesideri, int idElemento, String descrizione, Utente utente) {
		if(idListaDesideri == 0 || idElemento == 0 || descrizione == null || utente == null)
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");

		Session session = factory.getCurrentSession();
		Transaction tx = null;
		try {
			tx=session.beginTransaction();		

			if(!mappaListaDesideri.containsKey(idListaDesideri))
				throw new RuntimeException("Id lista desideri non trovato: " + idListaDesideri);

			if(!mappaUtente.get(utente.getMail()).getListaDesideris().contains(mappaListaDesideri.get(idListaDesideri)))
				throw new RuntimeException("Id lista desideri non appartiene all'utente: " + idListaDesideri);

			if(!mappaListaDesideriProdotti.containsKey(new ListaDesideriProdottiId(idElemento, idListaDesideri)))
				throw new RuntimeException("Id elemento non trovato: " + idElemento);

			ListaDesideriProdotti elemento = (ListaDesideriProdotti) mappaListaDesideriProdotti.get(new ListaDesideriProdottiId(idElemento, idListaDesideri));

			elemento.setDescrizione(descrizione);
			session.update(elemento);

			// vedere inoltre se aggiornare il set ListaDesideri dell'utente

			doubleloop:
				for(Iterator ldIterator = mappaUtente.get(utente.getMail()).getListaDesideris().iterator(); ldIterator.hasNext();) {
					ListaDesideri ld = (ListaDesideri) ldIterator.next();
					if(ld.getIdListaDesideri() != idListaDesideri)
						continue;

					for(Iterator ldpIterator = ld.getListaDesideriProdottis().iterator(); ldpIterator.hasNext();) {
						ListaDesideriProdotti ldp = (ListaDesideriProdotti) ldpIterator.next();
						if(ldp.getId().getIdElemento() != idElemento)
							continue;

						ldp.setDescrizione(descrizione);
						//mappaListaDesideriProdotti.put(new ListaDesideriProdottiId(idElemento, idListaDesideri), ldp);
						//mappaUtente.get(utente.getMail()).getListaDesideris().add(ldp);

						break doubleloop;
					}
				}

			tx.commit();
		} catch(Throwable ex) {
			if(tx != null) {
				tx.rollback();
				throw new RuntimeException(ex);
			}
		} finally {
			if(session != null && session.isOpen()) 
				session.close();
			session = null;
		}
	}

	/**
	 * * Modifica la quantit� di un elemento della lista dei desideri
	 * @param idListaDesideri
	 * @param idElemento
	 * @param quantita
	 * @param utente
	 */
	public void modificaQuantitaElementoListaDesideri(int idListaDesideri, int idElemento, int quantita, Utente utente) {
		if(idListaDesideri == 0 || idElemento == 0 || quantita == 0 || utente == null)
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");

		Session session = factory.getCurrentSession();
		Transaction tx = null;
		try {
			tx=session.beginTransaction();		

			if(!mappaListaDesideri.containsKey(idListaDesideri))
				throw new RuntimeException("Id lista desideri non trovato: " + idListaDesideri);

			if(!mappaUtente.get(utente.getMail()).getListaDesideris().contains(mappaListaDesideri.get(idListaDesideri)))
				throw new RuntimeException("Id lista desideri non appartiene all'utente: " + idListaDesideri);

			if(!mappaListaDesideriProdotti.containsKey(new ListaDesideriProdottiId(idElemento, idListaDesideri)))
				throw new RuntimeException("Id elemento non trovato: " + idElemento);

			ListaDesideriProdotti elemento = (ListaDesideriProdotti) mappaListaDesideriProdotti.get(new ListaDesideriProdottiId(idElemento, idListaDesideri));

			elemento.setQuantit�(quantita);

			session.update(elemento);

			doubleloop:
				for(Iterator ldIterator = mappaUtente.get(utente.getMail()).getListaDesideris().iterator(); ldIterator.hasNext();) {
					ListaDesideri ld = (ListaDesideri) ldIterator.next();
					if(ld.getIdListaDesideri() != idListaDesideri)
						continue;

					for(Iterator ldpIterator = ld.getListaDesideriProdottis().iterator(); ldpIterator.hasNext();) {
						ListaDesideriProdotti ldp = (ListaDesideriProdotti) ldpIterator.next();
						if(ldp.getId().getIdElemento() != idElemento)
							continue;

						ldp.setQuantit�(quantita);
						break doubleloop;
					}
				}

			tx.commit();
		} catch(Throwable ex) {
			if(tx != null) {
				tx.rollback();
				throw new RuntimeException(ex);
			}
		} finally {
			if(session != null && session.isOpen()) 
				session.close();
			session = null;
		}
	}

	/**
	 * * Modifica la quantit� di un elemento della lista dei desideri
	 * @param idListaDesideri
	 * @param idElemento
	 * @param quantita
	 * @param utente
	 */
	public void modificaIDInserzioneElementoListaDesideri(int idListaDesideri, int idElemento, int idInserzione, Utente utente) {
		if(idListaDesideri == 0 || idElemento == 0 || idInserzione == 0 || utente == null)
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");

		Session session = factory.getCurrentSession();
		Transaction tx = null;
		try {
			tx=session.beginTransaction();		

			if(!mappaListaDesideri.containsKey(idListaDesideri))
				throw new RuntimeException("Id lista desideri non trovato: " + idListaDesideri);

			if(!mappaUtente.get(utente.getMail()).getListaDesideris().contains(mappaListaDesideri.get(idListaDesideri)))
				throw new RuntimeException("Id lista desideri non appartiene all'utente: " + idListaDesideri);

			if(!mappaListaDesideriProdotti.containsKey(new ListaDesideriProdottiId(idElemento, idListaDesideri)))
				throw new RuntimeException("Id elemento non trovato: " + idElemento);

			if(!mappaInserzioni.containsKey(idInserzione))
				throw new RuntimeException("Id Inserzione non trovato: " + idInserzione);

			ListaDesideriProdotti elemento = (ListaDesideriProdotti) mappaListaDesideriProdotti.get(new ListaDesideriProdottiId(idElemento, idListaDesideri));
			elemento.setInserzione(mappaInserzioni.get(idInserzione));
			session.update(elemento);

			doubleloop:
				for(Iterator ldIterator = mappaUtente.get(utente.getMail()).getListaDesideris().iterator(); ldIterator.hasNext();) {
					ListaDesideri ld = (ListaDesideri) ldIterator.next();
					if(ld.getIdListaDesideri() != idListaDesideri)
						continue;

					for(Iterator ldpIterator = ld.getListaDesideriProdottis().iterator(); ldpIterator.hasNext();) {
						ListaDesideriProdotti ldp = (ListaDesideriProdotti) ldpIterator.next();
						if(ldp.getId().getIdElemento() != idElemento)
							continue;

						ldp.setInserzione(mappaInserzioni.get(idInserzione));
						break doubleloop;
					}
				}

			tx.commit();
		} catch(Throwable ex) {
			if(tx != null) {
				tx.rollback();
				throw new RuntimeException(ex);
			}
		} finally {
			if(session != null && session.isOpen()) 
				session.close();
			session = null;
		}
	}

	/**
	 * Elimina un elemento all'interno della lista dei desideri
	 * @param idListaDesideri
	 * @param idElemento
	 * @param utente
	 */
	public void eliminaElementoListaDesideri(int idListaDesideri, int idElemento, Utente utente) {
		if(idListaDesideri == 0 || idElemento == 0 || utente == null)
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");

		Session session = factory.getCurrentSession();
		Transaction tx = null;
		try {
			tx=session.beginTransaction();		

			if(!mappaListaDesideri.containsKey(idListaDesideri))
				throw new RuntimeException("Id lista desideri non trovato: " + idListaDesideri);

			if(!mappaUtente.get(utente.getMail()).getListaDesideris().contains(mappaListaDesideri.get(idListaDesideri)))
				throw new RuntimeException("Id lista desideri non appartiene all'utente: " + idListaDesideri);

			if(!mappaListaDesideriProdotti.containsKey(new ListaDesideriProdottiId(idElemento, idListaDesideri)))
				throw new RuntimeException("Id elemento non trovato: " + idElemento);

			ListaDesideriProdotti elemento = (ListaDesideriProdotti) mappaListaDesideriProdotti.get(new ListaDesideriProdottiId(idElemento, idListaDesideri));

			session.delete(elemento);
			mappaListaDesideriProdotti.remove(new ListaDesideriProdottiId(idElemento, idListaDesideri));

			doubleloop:
				for(Iterator ldIterator = mappaUtente.get(utente.getMail()).getListaDesideris().iterator(); ldIterator.hasNext();) {
					ListaDesideri ld = (ListaDesideri) ldIterator.next();
					if(ld.getIdListaDesideri() != idListaDesideri)
						continue;

					for(Iterator ldpIterator = ld.getListaDesideriProdottis().iterator(); ldpIterator.hasNext();) {
						ListaDesideriProdotti ldp = (ListaDesideriProdotti) ldpIterator.next();
						if(ldp.getId().getIdElemento() != idElemento)
							continue;

						ldpIterator.remove();
						break doubleloop;
					}
				}

			tx.commit();
		} catch(Throwable ex) {
			if(tx != null) {
				tx.rollback();
				throw new RuntimeException(ex);
			}
		} finally {
			if(session != null && session.isOpen()) 
				session.close();
			session = null;
		}
	}

	/**
	 * A seconda del valore acquistato, questa funzione contrassegna come acquistato o no. In particolare se acquistato = true, allora la tupla relativa all'elemento viene spostato dalla tabella lista_desideri alla tabella lista_spesa, aggiungendo anche un time stamp. Se il parametro acquistato = false allora viene eseguito il processo inverso, scartando ovviamente il timestamp. 
	 * @param idListaDesideri
	 * @param idElemento
	 * @param acquistato
	 * @param utente
	 */
	public void modificaAcquistatoElementoListaDesideri(int idListaDesideri, int idElemento, boolean acquistato, Utente utente) {
		if(idListaDesideri == 0 || idElemento == 0 || utente == null)
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");

		Session session = factory.getCurrentSession();
		Transaction tx = null;
		try {
			if(!mappaListaDesideri.containsKey(idListaDesideri))
				throw new RuntimeException("Id lista desideri non trovato: " + idListaDesideri);

			if(!mappaUtente.get(utente.getMail()).getListaDesideris().contains(mappaListaDesideri.get(idListaDesideri)))
				throw new RuntimeException("Id lista desideri non appartiene all'utente: " + idListaDesideri);

			if(acquistato) {
				tx=session.beginTransaction();
				// sposto la tupla dalla lista desideri alla lista spesa

				ListaDesideriProdottiId prodottoId =  new ListaDesideriProdottiId(idElemento, idListaDesideri);

				if(!mappaListaDesideriProdotti.containsKey(prodottoId))
					throw new RuntimeException("Id elemento non trovato: " + idElemento);

				ListaDesideriProdotti prodotto = (ListaDesideriProdotti) mappaListaDesideriProdotti.get(prodottoId);

				if(!mappaListaSpesa.containsKey(idListaDesideri)) {
					ListaDesideri listaDesideri = (ListaDesideri) mappaListaDesideri.get(idListaDesideri);
					ListaSpesa listaSpesa = new ListaSpesa(idListaDesideri, utente, listaDesideri.getNomeListaDesideri());
					session.save(listaSpesa);
					mappaListaSpesa.put(idListaDesideri, listaSpesa);
					mappaUtente.get(utente.getMail()).getListaSpesas().add(listaSpesa);
				}

				ListaSpesaProdotti prodottoSpesa = new ListaSpesaProdotti(new ListaSpesaProdottiId(idElemento, idListaDesideri),prodotto.getInserzione(), mappaListaSpesa.get(idListaDesideri), prodotto.getDescrizione(), new Date(), prodotto.getQuantit�());

				mappaListaSpesaProdotti.put(new ListaSpesaProdottiId(idElemento, idListaDesideri), prodottoSpesa);
				session.save(prodottoSpesa);
				mappaListaDesideriProdotti.remove(prodottoId);

				doubleloop:
					for(Iterator ldIterator = mappaUtente.get(utente.getMail()).getListaDesideris().iterator(); ldIterator.hasNext();) {
						ListaDesideri ld = (ListaDesideri) ldIterator.next();
						if(ld.getIdListaDesideri() != idListaDesideri)
							continue;

						for(Iterator ldpIterator = ld.getListaDesideriProdottis().iterator(); ldpIterator.hasNext();) {
							ListaDesideriProdotti ldp = (ListaDesideriProdotti) ldpIterator.next();
							if(ldp.getId().getIdElemento() != idElemento)
								continue;

							ldpIterator.remove();
							break doubleloop;
						}
					}

				session.delete(prodotto);
				tx.commit();
			} else { 
				tx=session.beginTransaction();
				// sposto la tupla dalla lista spesa alla lista desideri

				ListaSpesaProdottiId prodottoId =  new ListaSpesaProdottiId(idElemento, idListaDesideri);

				if(!mappaListaSpesaProdotti.containsKey(prodottoId))
					throw new RuntimeException("Id elemento non trovato: " + idElemento);

				ListaSpesaProdotti prodottoSpesa = (ListaSpesaProdotti) mappaListaSpesaProdotti.get(prodottoId);

				/*if(!mappaListaDesideri.containsKey(idListaDesideri)) {
					ListaSpesa listaSpesa = (ListaSpesa) mappaListaSpesa.get(idListaDesideri);
					Lista
				}*/

				ListaDesideriProdotti prodotto = new ListaDesideriProdotti(new ListaDesideriProdottiId(idElemento, idListaDesideri), prodottoSpesa.getInserzione(), mappaListaDesideri.get(idListaDesideri),prodottoSpesa.getDescrizione(), prodottoSpesa.getQuantit�());
				mappaListaDesideriProdotti.put(new ListaDesideriProdottiId(idElemento, idListaDesideri), prodotto);

				mappaListaSpesaProdotti.remove(prodottoSpesa);
				session.delete(prodottoSpesa);
				session.save(prodotto);
				tx.commit();
			}
		} catch(Throwable ex) {

			if(tx != null) {
				tx.rollback();
				throw new RuntimeException(ex);
			}
		} finally {
			if(session != null && session.isOpen()) 
				session.close();
			session = null;
		}
	}



	/**Inserimento di una lista desideri
	 * @param utente
	 * @param listaElementi 
	 * Set di ListaDesideriProdotti dove ogni suo elemento ha come id composto, l'id elemento e l'id listadesideri (Hashcodes) da voi creati
	 * @param nomeListaDesideri
	 * @param descrizione
	 */

	/*
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
	 */

	/**modifica di una lista desideri
	 * @param idListaDesideri 
	 * id della listadesideri vecchia da modificare
	 * @param utente
	 * @param nomeListaDesideri
	 * @param listaDesideriElementi 
	 * Set di ListaDesideriProdotti nuova, dove ogni suo elemento ha come id composto, l'id elemento e l'id listadesideri (Hashcodes) da voi creati
	 * 
	 */
	/*
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
	 */

	/**Eliminazione di una lista desideri
	 * @param idListaDesideri
	 */

	/*
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
	 */
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
	/*
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
	 */
	/**Modifica di una ListaSpesa
	 * @param idSpesa
	 * @param utente
	 * @param nomeListaSpesa
	 * @param prodottiQuantita
	 * Set di ListaSpesaProdotti nuova, dove ogni suo elemento ha come id composto, l'id elemento e l'id listaspesa (Hashcodes) da voi creati
	 */
	/*
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
	 */
	/**eliminazione di una ListaSpesa
	 * @param idSpesa
	 */
	/*
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
	 */
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

	/**
	 * 
	 * @param idProfilo
	 * @param creditiAcquisiti
	 * @param creditiPendenti
	 * @param reputazione
	 * @param premium
	 * @param contatoreInfrazioni
	 * @param numInserzioniPositive
	 * @param numInserzioniTotali
	 * @param numValutazioniPositive
	 * @param numValutazioniTotali
	 */
	public void modificaProfilo(int idProfilo,int creditiAcquisiti,int creditiPendenti,int reputazione,boolean premium,int contatoreInfrazioni, int numInserzioniPositive, int numInserzioniTotali, int numValutazioniPositive, int numValutazioniTotali){
		if(idProfilo <0 || creditiAcquisiti<0 || creditiPendenti<0 ||  contatoreInfrazioni<0 || numInserzioniPositive<0 || numInserzioniTotali<0 || numValutazioniPositive<0 || numValutazioniTotali<0)
			throw new RuntimeException("parametro/i non validi");

		Session session = factory.getCurrentSession();
		Transaction tx = null;
		Profilo profiloVecchio = mappaProfili.get(idProfilo);

		if(profiloVecchio != null) {

			Profilo profilo = new Profilo(profiloVecchio.getUtente(), creditiAcquisiti, creditiPendenti, reputazione, premium, contatoreInfrazioni, numInserzioniPositive, numInserzioniTotali, numValutazioniPositive, numValutazioniTotali);
			profilo.setIdProfilo(idProfilo);

			try{
				tx=session.beginTransaction();
				session.update(profilo);
				profiloVecchio.setContatoreInfrazioni(contatoreInfrazioni);
				profiloVecchio.setCreditiAcquisiti(creditiAcquisiti);
				profiloVecchio.setCreditiPendenti(creditiPendenti);
				profiloVecchio.setPremium(premium);
				profiloVecchio.setReputazione(reputazione);
				profiloVecchio.setNumeroInserzioniPositive(numInserzioniPositive);
				profiloVecchio.setNumeroInserzioniTotali(numInserzioniTotali);
				profiloVecchio.setNumeroValutazioniPositive(numValutazioniPositive);
				profiloVecchio.setNumeroValutazioniTotali(numValutazioniTotali);
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
	public int inserisciSupermercato(String nome,String indirizzo, String comune, String provincia, float latitudine,float longitudine){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		if(nome == null )
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");
		Supermercato supermercato = new Supermercato(nome, indirizzo, comune, provincia, latitudine, longitudine, new HashSet<Inserzione>());
		Integer idSuperMercato ;

		try{
			tx=session.beginTransaction();
			idSuperMercato = (Integer)session.save(supermercato);
			mappaSupermercati.put(idSuperMercato,supermercato);

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
	public void modificaSupermercato(int idSuperMercato,String nome,String indirizzo, String comune, String provincia, float latitudine,float longitudine){
		if(idSuperMercato <=0 || nome == null)
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");

		Session session = factory.getCurrentSession();
		Transaction tx = null;
		Supermercato superMercatoVecchio = mappaSupermercati.get(idSuperMercato);			

		if(superMercatoVecchio==null)
			throw new RuntimeException("elemento non trovato");

		Supermercato superMercato = new Supermercato(nome, indirizzo, comune, provincia, latitudine, longitudine, new HashSet<Inserzione>());
		superMercato.setIdSupermercato(idSuperMercato);
		superMercato.getInserziones().addAll(superMercatoVecchio.getInserziones());

		try{
			tx=session.beginTransaction();			
			session.update(superMercato);
			mappaSupermercati.remove(idSuperMercato);
			mappaSupermercati.put(idSuperMercato,superMercato);
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
	public void eliminaSupermercato(Integer idSupermercato){

		Session session = factory.getCurrentSession();
		Transaction tx = null;
		Supermercato superMercatoVecchio = mappaSupermercati.get(idSupermercato);		

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
	public Map<Integer,Supermercato> getSupermercati(){
		Map<Integer,Supermercato> supermercati = new HashMap<Integer,Supermercato>();
		supermercati.putAll(mappaSupermercati);			
		return supermercati;
	}

	public int inserisciUtente(String email,String nickname,String password,Date dataRegistrazione,String numeroCasuale) {
		if(email == null || nickname == null || password == null || dataRegistrazione == null || numeroCasuale == null)
			throw new RuntimeException("i parametri devono essere non nulli");

		Session session = factory.getCurrentSession();
		Transaction tx = null;	
		Utente utente = new Utente(email, nickname, password, dataRegistrazione,false,numeroCasuale,new HashSet<ValutazioneInserzione>(), new HashSet<ValutazioneInserzione>(), new HashSet<ListaSpesa>(), new HashSet<Inserzione>(), new HashSet<Profilo>(), new HashSet<ListaDesideri>());		
		int idUtente = 0, idProfilo;
		try{
			tx = session.beginTransaction();
			idUtente = (Integer)session.save(utente);
			mappaUtente.put(email,utente);
			tx.commit();

			inserisciProfilo(utente, 10, 0, 100, false, 0);
			
			//Inseriamo la prima lista di default
			DateTime time = DateTime.now();
			String valore = time.toString() + String.valueOf(idUtente);
			System.out.println("ID PRIMA LISTA DESIDERI: " + valore.hashCode());
			
			inserisciListaDesideri(valore.hashCode(), utente, "Lista Prova");
		}
		catch(ConstraintViolationException e) {
			System.out.println(e.getSQLException().getLocalizedMessage());
			System.out.println("error" + e.getMessage() + " - " + e.getStackTrace());
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(e.getSQLException().getLocalizedMessage());
		}
		catch(Throwable ex){
			System.out.println("error" + ex.getMessage() + " - " + ex.getStackTrace());
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
		Utente utenteVecchio = mappaUtente.get(email);

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
	public void inserimentoValutazioneInserzione(Inserzione inserzione, Utente inserzionista, Utente valutatore, int valutazione, Date data){
		Session session = factory.getCurrentSession();
		Transaction tx = null;

		if(inserzionista == null || valutatore == null)
			throw new RuntimeException("parametri non corretti");

		ValutazioneInserzione valutazioneInserzione = new ValutazioneInserzione(inserzione, inserzionista, valutatore, valutazione, data);
		try {
			tx = session.beginTransaction();
			Integer idValutazioneInserzione = (Integer)session.save(valutazioneInserzione);
			mappaValutazioneInserzione.put(idValutazioneInserzione,valutazioneInserzione);
			mappaUtente.get(inserzionista.getMail()).getValutazioneInserzionesForIdUtenteInserzionista().add(valutazioneInserzione);
			mappaUtente.get(valutatore.getMail()).getValutazioneInserzionesForIdUtenteValutatore().add(valutazioneInserzione);
			mappaInserzioni.get(inserzione.getIdInserzione()).getValutazioneInserziones().add(valutazioneInserzione);

			// Aggiugo all'utente i crediti pendenti che gli spettano dall'inserimento dell'inserzione, ossia +2
			Profilo profilo = ((Profilo) valutatore.getProfilos().iterator().next());
			profilo.setCreditiPendenti(profilo.getCreditiPendenti() + 2);
			session.update(profilo);
			mappaProfili.put(profilo.getIdProfilo(), profilo);
			mappaUtente.get(valutatore.getMail()).getProfilos().add(profilo);

			tx.commit();
		} catch(Throwable ex) {
			if(tx != null)
				tx.rollback();
			throw new RuntimeException(ex);
		} finally {
			if(session != null && session.isOpen()) {
				session.close();
			}
			session = null;
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
	
	/***
	 * 
	 * @param supermercato
	 * @param utente
	 */
	private void assegnaValutazioni(Utente utente, Supermercato supermercato, Integer idInserzione) {
		System.out.println("assegnaValutazioni");
		
		factory = buildSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		ArrayList<Integer> utentiValutatori = null;

		session = factory.openSession();
		try{
			tx=session.beginTransaction();

			Query q = session.createSQLQuery(
					"select i.ID_Utente " +
					"from supermercato s, inserzione i, lista_desideri_prodotti ldp, lista_spesa_prodotti lsp " +
					"where s.ID_Supermercato = i.ID_Supermercato AND (i.ID_Inserzione = ldp.ID_Inserzione or i.ID_Inserzione = lsp.ID_Inserzione) AND s.ID_Supermercato = :idSupermercato AND i.ID_Utente != :idUtenteInserzionista " +
					"group by i.ID_Utente limit 15 ");
			q.setParameter("idSupermercato", supermercato.getIdSupermercato());
			q.setParameter("idUtenteInserzionista", utente.getIdUtente());
			utentiValutatori = new ArrayList<Integer>(q.list());

			tx.commit();
		} catch(RuntimeException e) {
			if(tx!=null)
				tx.rollback();
			throw e;
		} finally {
			if(session!=null && session.isOpen())
				session.close();
		}
		
		for (Integer idValutatore : utentiValutatori)
			inserimentoValutazioneInserzione(mappaInserzioni.get(idInserzione), utente, mappaUtente.get(idValutatore), 0, null);
	}


	/**metodo get della mappa ValutazioniInserzioni
	 * @return
	 */
	public Map<Integer,ValutazioneInserzione> getValutazioniInserzioni(){
		HashMap<Integer,ValutazioneInserzione> valutazioni = new HashMap<Integer,ValutazioneInserzione>();
		valutazioni.putAll(mappaValutazioneInserzione);
		return valutazioni;
	}
	
	public List getInserzioniDaValutareProposte(String mailUtente) {
		factory = buildSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		List inserzioniDaValutareList;

		session = factory.openSession();
		try{
			tx=session.beginTransaction();

			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = new GregorianCalendar();
			Query q = session.createSQLQuery(
					"select i.ID_Inserzione "  +
					"from valutazione_inserzione vi, inserzione i " +
					"where vi.ID_UtenteValutatore = :idUtente and vi.ID_Inserzione = i.ID_Inserzione and i.DataInizio <= :currDate and i.DataFine > :currDate and vi.Valutazione = 0");
			q.setParameter("idUtente", mappaUtente.get(mailUtente).getIdUtente());
			q.setParameter("currDate", formato.format(cal.getTime()));
			inserzioniDaValutareList = q.list();

			System.out.println("INSERZIONE DA VALUTARE: " + mailUtente);

			tx.commit();
		} catch(RuntimeException e) {
			if(tx!=null)
				tx.rollback();
			throw e;
		} finally {
			if(session!=null && session.isOpen())
				session.close();
		}
		return inserzioniDaValutareList;
	}

	public List getInserzioniDaValutareSuggerite(String mailUtente, String lat, String lng) {
		factory = buildSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		List inserzioniDaValutareList;

		session = factory.openSession();
		try{
			tx=session.beginTransaction();

			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = new GregorianCalendar();
			Query q = session.createSQLQuery(
					"select ins.ID_Inserzione "  +
					"from inserzione ins, " +
					"(select *, (acos( sin(:latitudine*pi()/180)*sin(Latitudine*pi()/180) + cos(:latitudine*pi()/180)*cos(Latitudine*pi()/180)*cos( (:longitudine*pi()/180) - (Longitudine*pi()/180) ) ) * :raggioTerra) as distanza " +
						"from supermercato " +
						"where (acos( sin(:latitudine*pi()/180)*sin(Latitudine*pi()/180) + cos(:latitudine*pi()/180)*cos(Latitudine*pi()/180)*cos( (:longitudine*pi()/180) - (Longitudine*pi()/180) ) ) * :raggioTerra) <= :raggioUtente " +
						") as supTmp " +
					"where ins.ID_Supermercato = supTmp.ID_Supermercato and ins.DataInizio <= :currDate and ins.DataFine > :currDate and ins.ID_Utente != :idUtente and ins.ID_Supermercato in (supTmp.ID_Supermercato) and " +
					"ins.ID_Inserzione not in (select ID_Inserzione " +
												"from valutazione_inserzione " +
												"where ID_UtenteValutatore = :idUtente) " +
					"order by supTmp.distanza ");
			q.setParameter("latitudine", Float.valueOf(lat));
			q.setParameter("longitudine", Float.valueOf(lng));
			q.setParameter("raggioTerra", 6378.137);
			q.setParameter("currDate", formato.format(cal.getTime()));
			q.setParameter("idUtente", mappaUtente.get(mailUtente).getIdUtente());
			q.setParameter("raggioUtente", 100);
			/*
			Query q = session.createSQLQuery(
					"select ID_Inserzione " +
					"from inserzione ins");
			*/
			inserzioniDaValutareList = q.list();

			System.out.println("INSERZIONE DA VALUTARE: " + mailUtente);
			System.out.println("INSERZIONE DA VALUTARE: " + lat);
			System.out.println("INSERZIONE DA VALUTARE: " + lng);

			tx.commit();
		} catch(RuntimeException e) {
			if(tx!=null)
				tx.rollback();
			throw e;
		} finally {
			if(session!=null && session.isOpen())
				session.close();
		}
		return inserzioniDaValutareList;
	}

	public List getInserzioniInScadenza(String mailUtente, String lat, String lng) {
		factory = buildSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		List inserzioniDaValutareList;

		session = factory.openSession();
		try{
			tx=session.beginTransaction();

			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = new GregorianCalendar();
			
			// Eliminato dalla query il controllo per cui le inserzioni dell'utente stesso non sono ritornate come suggerimenti
			Query q = session.createSQLQuery(
					"select ins.ID_Inserzione " +
					"from inserzione ins, " +
					"(select *, (acos( sin(:latitudine*pi()/180)*sin(Latitudine*pi()/180) + cos(:latitudine*pi()/180)*cos(Latitudine*pi()/180)*cos( (:longitudine*pi()/180) - (Longitudine*pi()/180) ) ) * :raggioTerra) as distanza " +
						"from supermercato " +
						"where (acos( sin(:latitudine*pi()/180)*sin(Latitudine*pi()/180) + cos(:latitudine*pi()/180)*cos(Latitudine*pi()/180)*cos( (:longitudine*pi()/180) - (Longitudine*pi()/180) ) ) * :raggioTerra) <= :raggioUtente " +
						") as supTmp " +
					"where ins.ID_Supermercato = supTmp.ID_Supermercato and ins.DataInizio <= :currDate and ins.DataFine > :currDate and DATEDIFF(ins.DataFine, :currDate) <= 2 and ins.ID_Utente != :idUtente and ins.ID_Supermercato in (supTmp.ID_Supermercato) " +
					"order by supTmp.distanza, DATEDIFF(ins.DataFine, :currDate) ");
			/*Query q = session.createSQLQuery(
					"select ins.ID_Inserzione " +
					"from inserzione ins, " +
					"(select *, (acos( sin(:latitudine*pi()/180)*sin(Latitudine*pi()/180) + cos(:latitudine*pi()/180)*cos(Latitudine*pi()/180)*cos( (:longitudine*pi()/180) - (Longitudine*pi()/180) ) ) * :raggioTerra) as distanza " +
						"from supermercato " +
						"where (acos( sin(:latitudine*pi()/180)*sin(Latitudine*pi()/180) + cos(:latitudine*pi()/180)*cos(Latitudine*pi()/180)*cos( (:longitudine*pi()/180) - (Longitudine*pi()/180) ) ) * :raggioTerra) <= :raggioUtente " +
						") as supTmp " +
					"where ins.ID_Supermercato = supTmp.ID_Supermercato and ins.DataInizio <= :currDate and ins.DataFine > :currDate and DATEDIFF(ins.DataFine, :currDate) <= 2 and ins.ID_Utente != :idUtente and ins.ID_Supermercato in (supTmp.ID_Supermercato) and " +
					"ins.ID_Inserzione not in (select ID_Inserzione " +
												"from valutazione_inserzione " +
												"where ID_UtenteValutatore = :idUtente) " +
					"order by supTmp.distanza, DATEDIFF(ins.DataFine, :currDate) ");*/
			q.setParameter("latitudine", Float.valueOf(lat));
			q.setParameter("longitudine", Float.valueOf(lng));
			q.setParameter("raggioTerra", 6378.137);
			q.setParameter("currDate", formato.format(cal.getTime()));
			q.setParameter("idUtente", mappaUtente.get(mailUtente).getIdUtente());
			q.setParameter("raggioUtente", 50000);
			/*
			Query q = session.createSQLQuery(
					"select ID_Inserzione " +
					"from inserzione ins");
					*/
			inserzioniDaValutareList = q.list();

			tx.commit();
		} catch(RuntimeException e) {
			if(tx!=null)
				tx.rollback();
			throw e;
		} finally {
			if(session!=null && session.isOpen())
				session.close();
		}
		return inserzioniDaValutareList;
	}

	public Integer[] getNumeroValutazioniByIdInserzione(Integer idInserzione, Integer idUtente) {
		factory = buildSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		List l = null;
		Integer valutazioni[] = {0, 0};
		try{
			tx = session.beginTransaction();
			Query q = session.createSQLQuery("SELECT " + 
					"sum(case when valutazione=1 then 1 else 0 end) as 'valutazioni_positive', " + 
					"sum(case when valutazione=-1 then 1 else 0 end) as 'valutazioni_negative'  " +
					"FROM valutazione_inserzione WHERE valutazione_inserzione.ID_Inserzione = :idInserzione AND valutazione_inserzione.ID_UtenteInserzionista = :idUtente ");
			q.setParameter("idInserzione", idInserzione);
			q.setParameter("idUtente", idUtente);
			l = q.list();
			for (Object obj : l) {
				Object[] fields = (Object[]) obj;
				if(fields[0] != null) 
					valutazioni[0] = ((Number) fields[0]).intValue();
				if(fields[1] != null)
					valutazioni[1] = ((Number) fields[1]).intValue();
				System.out.println(valutazioni.toString());
			}

		} catch(RuntimeException e) {
			if(tx!=null)
				tx.rollback();
			throw e;
		} finally {
			if(session!=null && session.isOpen())
				session.close();
		}
		return valutazioni;
	}

	public ArrayList<Integer> getSuggerimentiProdotto(String mailUtente, String lat, String lng, String descrizione) {
		factory = buildSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		ArrayList<Integer> inserzioniDaSuggerireList = null;

		session = factory.openSession();
		try{
			tx=session.beginTransaction();

			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = new GregorianCalendar();

			System.out.println("Data Richiesta: " + formato.format(cal.getTime()) + " Stringa da ricercare: " + '%'+descrizione.replace(' ', '%')+'%');

			// Eliminato dalla query il controllo per cui le inserzioni dell'utente stesso non sono ritornate come suggerimenti
			Query q = session.createSQLQuery(
					"select ins.ID_Inserzione " +
					"from inserzione ins, " +
					"prodotto p, " +
					"(select *, (acos( sin(:latitudine*pi()/180)*sin(Latitudine*pi()/180) + cos(:latitudine*pi()/180)*cos(Latitudine*pi()/180)*cos( (:longitudine*pi()/180) - (Longitudine*pi()/180) ) ) * :raggioTerra) as distanza " +
						"from supermercato " +
						"where (acos( sin(:latitudine*pi()/180)*sin(Latitudine*pi()/180) + cos(:latitudine*pi()/180)*cos(Latitudine*pi()/180)*cos( (:longitudine*pi()/180) - (Longitudine*pi()/180) ) ) * :raggioTerra) <= :raggioUtente " +
						") as supTmp " +
					"where ins.ID_Supermercato = supTmp.ID_Supermercato and ins.ID_Prodotto = p.ID_Prodotto and ins.DataInizio < :currDate and ins.DataFine > :currDate and p.Descrizione like :stringToMatch " +
					"order by supTmp.distanza ");
			/*
			Query q = session.createSQLQuery(
											"select ins.ID_Inserzione " +
											"from inserzione ins, " +
											"prodotto p, " +
											"(select *, (acos( sin(:latitudine*pi()/180)*sin(Latitudine*pi()/180) + cos(:latitudine*pi()/180)*cos(Latitudine*pi()/180)*cos( (:longitudine*pi()/180) - (Longitudine*pi()/180) ) ) * :raggioTerra) as distanza " +
												"from supermercato " +
												"where (acos( sin(:latitudine*pi()/180)*sin(Latitudine*pi()/180) + cos(:latitudine*pi()/180)*cos(Latitudine*pi()/180)*cos( (:longitudine*pi()/180) - (Longitudine*pi()/180) ) ) * :raggioTerra) <= :raggioUtente " +
												") as supTmp " +
											"where ins.ID_Supermercato = supTmp.ID_Supermercato and ins.ID_Prodotto = p.ID_Prodotto and ins.DataInizio < :currDate and ins.DataFine > :currDate and ins.ID_Utente != :idUtente and p.Descrizione like :stringToMatch " +
											"order by supTmp.distanza ");*/
			/*
			Query q = session.createSQLQuery(
					"select ins.ID_Inserzione " +
							"from inserzione ins, " +
							"prodotto p, " +
							"(select *, (acos( sin(:latitudine*pi()/180)*sin(Latitudine*pi()/180) + cos(:latitudine*pi()/180)*cos(Latitudine*pi()/180)*cos( (:longitudine*pi()/180) - (Longitudine*pi()/180) ) ) * :raggioTerra) as distanza " +
							"from supermercato " +
							"where (acos( sin(:latitudine*pi()/180)*sin(Latitudine*pi()/180) + cos(:latitudine*pi()/180)*cos(Latitudine*pi()/180)*cos( (:longitudine*pi()/180) - (Longitudine*pi()/180) ) ) * :raggioTerra) <= :raggioUtente " +
							") as supTmp " +
							"where ins.ID_Supermercato = supTmp.ID_Supermercato and ins.ID_Prodotto = p.ID_Prodotto and ins.DataInizio <= :currDate and ins.DataFine > :currDate and p.Descrizione like :stringToMatch " +
					"order by supTmp.distanza ");*/
			q.setParameter("latitudine", Float.valueOf(lat));
			q.setParameter("longitudine", Float.valueOf(lng));
			q.setParameter("raggioTerra", 6378.137);
			q.setParameter("raggioUtente", 100);
			q.setParameter("currDate", formato.format(cal.getTime()));
			//q.setParameter("idUtente", mappaUtente.get(mailUtente).getIdUtente());
			q.setParameter("stringToMatch", '%'+descrizione.replace(' ', '%')+'%');
			inserzioniDaSuggerireList = new ArrayList<Integer>(q.list());

			tx.commit();
		} catch(RuntimeException e) {
			if(tx!=null)
				tx.rollback();
			throw e;
		} finally {
			if(session!=null && session.isOpen())
				session.close();
		}
		for (Integer integer : inserzioniDaSuggerireList)
			System.out.println("Id_Inserzione: " + integer);

		return inserzioniDaSuggerireList;
	}

	/**
	 * Questa funzione ritorna le inserzioni valide.
	 * E' generica, cio� non usa informazioni dell'utente, l'idea � avere delle inserizioni da mostrare agli utenti non ancora registrati.
	 * @return ArrayList<Integer> contenenti gli id delle inserzioni trovate.
	 */
	public ArrayList<Integer> getInserzioniValide() {
		factory = buildSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		ArrayList<Integer> inserzioniDaSuggerireList = null;

		session = factory.openSession();
		try{
			tx=session.beginTransaction();

			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = new GregorianCalendar();

			Query q = session.createSQLQuery(
					"select ID_Inserzione " +
							"from inserzione ins " +
							"where DataInizio <= :currDate and ins.DataFine > :currDate " +
					"order by DATEDIFF(DataFine, :currDate) ");
			q.setParameter("currDate", formato.format(cal.getTime()));
			inserzioniDaSuggerireList = new ArrayList<Integer>(q.list());

			tx.commit();
		} catch(RuntimeException e) {
			if(tx!=null)
				tx.rollback();
			throw e;
		} finally {
			if(session!=null && session.isOpen())
				session.close();
		}
		for (Integer integer : inserzioniDaSuggerireList)
			System.out.println("Id_Inserzione: " + integer);

		return inserzioniDaSuggerireList;
	}
	
	public List getAbitudiniSpesa(String mailUtente) {
		factory = buildSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		List abitudiniList;

		session = factory.openSession();
		try{
			tx=session.beginTransaction();

			Query q = session.createSQLQuery(
					"select lsp.Descrizione, count(*) as contatore " +
					"from lista_spesa_prodotti lsp, lista_spesa ls " +
					"where ls.ID_Utente = :idUtente and ls.ID_Spesa = lsp.ID_ListaSpesa " +
					"group by lsp.Descrizione " +
					"order by lsp.DataAcquisto desc limit 20 ");
			q.setParameter("idUtente", mappaUtente.get(mailUtente).getIdUtente());
			abitudiniList = q.list();

			tx.commit();
		} catch(RuntimeException e) {
			if(tx!=null)
				tx.rollback();
			throw e;
		} finally {
			if(session!=null && session.isOpen())
				session.close();
		}
		return abitudiniList;
	}
	
	public List getSuggerimentiAbitudini(String mailUtente, String lat, String lng, String descrizioni) {
		factory = buildSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		String [] descrizioneArray = null;
		List inserzioniDaSuggerireList = null;

		session = factory.openSession();
		try{
			tx=session.beginTransaction();

			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = new GregorianCalendar();
			System.out.println("Data Richiesta: " + formato.format(cal.getTime()) + " Stringa da ricercare: " + descrizioni);

			String txt = "select ins.ID_Inserzione, p.Descrizione, ins.ID_Supermercato, ins.DataFine, ins.Prezzo, ins.Foto " +
					"from inserzione ins, " +
					"prodotto p, " +
					"(select *, (acos( sin(:latitudine*pi()/180)*sin(Latitudine*pi()/180) + cos(:latitudine*pi()/180)*cos(Latitudine*pi()/180)*cos( (:longitudine*pi()/180) - (Longitudine*pi()/180) ) ) * :raggioTerra) as distanza " +
						"from supermercato " +
						"where (acos( sin(:latitudine*pi()/180)*sin(Latitudine*pi()/180) + cos(:latitudine*pi()/180)*cos(Latitudine*pi()/180)*cos( (:longitudine*pi()/180) - (Longitudine*pi()/180) ) ) * :raggioTerra) <= :raggioUtente " +
						") as supTmp " +
					"where ins.ID_Supermercato = supTmp.ID_Supermercato and ins.ID_Prodotto = p.ID_Prodotto and ins.DataInizio < :currDate and ins.DataFine > :currDate ";
			
			descrizioneArray = descrizioni.split(" ");
			
			for(String descrizione : descrizioneArray)
				txt += " or p.Descrizione like " + descrizione + " ";

			txt += " order by supTmp.distanza limit 30 ";
			
			System.out.println("QUERY: " + txt);
			
			Query q = session.createSQLQuery(txt);
			q.setParameter("latitudine", Float.valueOf(lat));
			q.setParameter("longitudine", Float.valueOf(lng));
			q.setParameter("raggioTerra", 6378.137);
			q.setParameter("raggioUtente", 100);
			q.setParameter("currDate", formato.format(cal.getTime()));
			//q.setParameter("idUtente", mappaUtente.get(mailUtente).getIdUtente());
			
			inserzioniDaSuggerireList = q.list();

			tx.commit();
		} catch(RuntimeException e) {
			if(tx!=null)
				tx.rollback();
			throw e;
		} finally {
			if(session!=null && session.isOpen())
				session.close();
		}
		System.out.println("LIST SIZE: " + inserzioniDaSuggerireList.size());
		return inserzioniDaSuggerireList;
	}
}
