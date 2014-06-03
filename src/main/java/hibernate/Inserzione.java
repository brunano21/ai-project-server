package hibernate;

// Generated 2-giu-2014 22.11.49 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Inserzione generated by hbm2java
 */
@Entity
@Table(name = "inserzione", catalog = "supermercati")
public class Inserzione implements java.io.Serializable {

	private Integer idInserzione;
	private Utente utente;
	private Supermercato supermercato;
	private Prodotto prodotto;
	private Float prezzo;
	private Date dataInizio;
	private Date dataFine;
	private String descrizione;
	private String foto;
	private Integer numeroValutazioni;
	private Float totaleVoti;
	private Set listaDesideriProdottis = new HashSet(0);
	private Set listaSpesaProdottis = new HashSet(0);
	private Set valutazioneInserziones = new HashSet(0);
	private Set argomentiInserziones = new HashSet(0);

	public Inserzione() {
	}

	public Inserzione(Utente utente, Supermercato supermercato,
			Prodotto prodotto, Float prezzo, Date dataInizio, Date dataFine,
			String descrizione, String foto, Integer numeroValutazioni,
			Float totaleVoti, Set listaDesideriProdottis,
			Set listaSpesaProdottis, Set valutazioneInserziones,
			Set argomentiInserziones) {
		this.utente = utente;
		this.supermercato = supermercato;
		this.prodotto = prodotto;
		this.prezzo = prezzo;
		this.dataInizio = dataInizio;
		this.dataFine = dataFine;
		this.descrizione = descrizione;
		this.foto = foto;
		this.numeroValutazioni = numeroValutazioni;
		this.totaleVoti = totaleVoti;
		this.listaDesideriProdottis = listaDesideriProdottis;
		this.listaSpesaProdottis = listaSpesaProdottis;
		this.valutazioneInserziones = valutazioneInserziones;
		this.argomentiInserziones = argomentiInserziones;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID_Inserzione", unique = true, nullable = false)
	public Integer getIdInserzione() {
		return this.idInserzione;
	}

	public void setIdInserzione(Integer idInserzione) {
		this.idInserzione = idInserzione;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_Utente")
	public Utente getUtente() {
		return this.utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_Supermercato")
	public Supermercato getSupermercato() {
		return this.supermercato;
	}

	public void setSupermercato(Supermercato supermercato) {
		this.supermercato = supermercato;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_Prodotto")
	public Prodotto getProdotto() {
		return this.prodotto;
	}

	public void setProdotto(Prodotto prodotto) {
		this.prodotto = prodotto;
	}

	@Column(name = "Prezzo", precision = 12, scale = 0)
	public Float getPrezzo() {
		return this.prezzo;
	}

	public void setPrezzo(Float prezzo) {
		this.prezzo = prezzo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DataInizio", length = 10)
	public Date getDataInizio() {
		return this.dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DataFine", length = 10)
	public Date getDataFine() {
		return this.dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	@Column(name = "Descrizione", length = 45)
	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@Column(name = "Foto", length = 200)
	public String getFoto() {
		return this.foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	@Column(name = "NumeroValutazioni")
	public Integer getNumeroValutazioni() {
		return this.numeroValutazioni;
	}

	public void setNumeroValutazioni(Integer numeroValutazioni) {
		this.numeroValutazioni = numeroValutazioni;
	}

	@Column(name = "TotaleVoti", precision = 12, scale = 0)
	public Float getTotaleVoti() {
		return this.totaleVoti;
	}

	public void setTotaleVoti(Float totaleVoti) {
		this.totaleVoti = totaleVoti;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "inserzione")
	public Set getListaDesideriProdottis() {
		return this.listaDesideriProdottis;
	}

	public void setListaDesideriProdottis(Set listaDesideriProdottis) {
		this.listaDesideriProdottis = listaDesideriProdottis;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "inserzione")
	public Set getListaSpesaProdottis() {
		return this.listaSpesaProdottis;
	}

	public void setListaSpesaProdottis(Set listaSpesaProdottis) {
		this.listaSpesaProdottis = listaSpesaProdottis;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "inserzione")
	public Set getValutazioneInserziones() {
		return this.valutazioneInserziones;
	}

	public void setValutazioneInserziones(Set valutazioneInserziones) {
		this.valutazioneInserziones = valutazioneInserziones;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "inserzione")
	public Set getArgomentiInserziones() {
		return this.argomentiInserziones;
	}

	public void setArgomentiInserziones(Set argomentiInserziones) {
		this.argomentiInserziones = argomentiInserziones;
	}

}
