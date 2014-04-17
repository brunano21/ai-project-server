package hibernate;

// Generated 17-apr-2014 0.21.19 by Hibernate Tools 3.4.0.CR1

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
 * ValutazioneInserzione generated by hbm2java
 */
@Entity
@Table(name = "valutazione_inserzione", catalog = "supermercati")
public class ValutazioneInserzione implements java.io.Serializable {

	private Integer idValutazioneInserzione;
	private Inserzione inserzione;
	private Utente utenteByIdUtenteValutatore;
	private Utente utenteByIdUtenteInserzionista;
	private Integer valutazione;
	private Date data;

	public ValutazioneInserzione() {
	}

	public ValutazioneInserzione(Inserzione inserzione,
			Utente utenteByIdUtenteValutatore,
			Utente utenteByIdUtenteInserzionista, Integer valutazione, Date data) {
		this.inserzione = inserzione;
		this.utenteByIdUtenteValutatore = utenteByIdUtenteValutatore;
		this.utenteByIdUtenteInserzionista = utenteByIdUtenteInserzionista;
		this.valutazione = valutazione;
		this.data = data;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID_ValutazioneInserzione", unique = true, nullable = false)
	public Integer getIdValutazioneInserzione() {
		return this.idValutazioneInserzione;
	}

	public void setIdValutazioneInserzione(Integer idValutazioneInserzione) {
		this.idValutazioneInserzione = idValutazioneInserzione;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_Inserzione")
	public Inserzione getInserzione() {
		return this.inserzione;
	}

	public void setInserzione(Inserzione inserzione) {
		this.inserzione = inserzione;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_UtenteValutatore")
	public Utente getUtenteByIdUtenteValutatore() {
		return this.utenteByIdUtenteValutatore;
	}

	public void setUtenteByIdUtenteValutatore(Utente utenteByIdUtenteValutatore) {
		this.utenteByIdUtenteValutatore = utenteByIdUtenteValutatore;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_UtenteInserzionista")
	public Utente getUtenteByIdUtenteInserzionista() {
		return this.utenteByIdUtenteInserzionista;
	}

	public void setUtenteByIdUtenteInserzionista(
			Utente utenteByIdUtenteInserzionista) {
		this.utenteByIdUtenteInserzionista = utenteByIdUtenteInserzionista;
	}

	@Column(name = "Valutazione")
	public Integer getValutazione() {
		return this.valutazione;
	}

	public void setValutazione(Integer valutazione) {
		this.valutazione = valutazione;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "Data", length = 10)
	public Date getData() {
		return this.data;
	}

	public void setData(Date data) {
		this.data = data;
	}

}
