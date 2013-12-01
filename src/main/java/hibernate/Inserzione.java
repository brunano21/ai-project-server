package hibernate;

// Generated 21-nov-2013 16.24.11 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

	public Inserzione() {
	}

	public Inserzione(Utente utente, Supermercato supermercato,
			Prodotto prodotto, Float prezzo, Date dataInizio, Date dataFine,
			String descrizione, String foto) {
		this.utente = utente;
		this.supermercato = supermercato;
		this.prodotto = prodotto;
		this.prezzo = prezzo;
		this.dataInizio = dataInizio;
		this.dataFine = dataFine;
		this.descrizione = descrizione;
		this.foto = foto;
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

	@Column(name = "Foto", length = 30)
	public String getFoto() {
		return this.foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

}