package hibernate;

// Generated Jun 3, 2014 7:29:11 PM by Hibernate Tools 3.6.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Profilo generated by hbm2java
 */
@Entity
@Table(name = "profilo", catalog = "supermercati", uniqueConstraints = @UniqueConstraint(columnNames = "ID_Utente"))
public class Profilo implements java.io.Serializable {

	private Integer idProfilo;
	private Utente utente;
	private int creditiAcquisiti;
	private int creditiPendenti;
	private int reputazione;
	private Boolean premium;
	private int contatoreInfrazioni;
	private int numeroInserzioniPositive;
	private int numeroInserzioniTotali;
	private int numeroValutazioniPositive;
	private int numeroValutazioniTotali;

	public Profilo() {
	}

	public Profilo(Utente utente, int creditiAcquisiti, int creditiPendenti,
			int reputazione, int contatoreInfrazioni,
			int numeroInserzioniPositive, int numeroInserzioniTotali,
			int numeroValutazioniPositive, int numeroValutazioniTotali) {
		this.utente = utente;
		this.creditiAcquisiti = creditiAcquisiti;
		this.creditiPendenti = creditiPendenti;
		this.reputazione = reputazione;
		this.contatoreInfrazioni = contatoreInfrazioni;
		this.numeroInserzioniPositive = numeroInserzioniPositive;
		this.numeroInserzioniTotali = numeroInserzioniTotali;
		this.numeroValutazioniPositive = numeroValutazioniPositive;
		this.numeroValutazioniTotali = numeroValutazioniTotali;
	}

	public Profilo(Utente utente, int creditiAcquisiti, int creditiPendenti,
			int reputazione, Boolean premium, int contatoreInfrazioni,
			int numeroInserzioniPositive, int numeroInserzioniTotali,
			int numeroValutazioniPositive, int numeroValutazioniTotali) {
		this.utente = utente;
		this.creditiAcquisiti = creditiAcquisiti;
		this.creditiPendenti = creditiPendenti;
		this.reputazione = reputazione;
		this.premium = premium;
		this.contatoreInfrazioni = contatoreInfrazioni;
		this.numeroInserzioniPositive = numeroInserzioniPositive;
		this.numeroInserzioniTotali = numeroInserzioniTotali;
		this.numeroValutazioniPositive = numeroValutazioniPositive;
		this.numeroValutazioniTotali = numeroValutazioniTotali;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID_profilo", unique = true, nullable = false)
	public Integer getIdProfilo() {
		return this.idProfilo;
	}

	public void setIdProfilo(Integer idProfilo) {
		this.idProfilo = idProfilo;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_Utente", unique = true, nullable = false)
	public Utente getUtente() {
		return this.utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	@Column(name = "CreditiAcquisiti", nullable = false)
	public int getCreditiAcquisiti() {
		return this.creditiAcquisiti;
	}

	public void setCreditiAcquisiti(int creditiAcquisiti) {
		this.creditiAcquisiti = creditiAcquisiti;
	}

	@Column(name = "CreditiPendenti", nullable = false)
	public int getCreditiPendenti() {
		return this.creditiPendenti;
	}

	public void setCreditiPendenti(int creditiPendenti) {
		this.creditiPendenti = creditiPendenti;
	}

	@Column(name = "Reputazione", nullable = false)
	public int getReputazione() {
		return this.reputazione;
	}

	public void setReputazione(int reputazione) {
		this.reputazione = reputazione;
	}

	@Column(name = "Premium")
	public Boolean getPremium() {
		return this.premium;
	}

	public void setPremium(Boolean premium) {
		this.premium = premium;
	}

	@Column(name = "ContatoreInfrazioni", nullable = false)
	public int getContatoreInfrazioni() {
		return this.contatoreInfrazioni;
	}

	public void setContatoreInfrazioni(int contatoreInfrazioni) {
		this.contatoreInfrazioni = contatoreInfrazioni;
	}

	@Column(name = "NumeroInserzioniPositive", nullable = false)
	public int getNumeroInserzioniPositive() {
		return this.numeroInserzioniPositive;
	}

	public void setNumeroInserzioniPositive(int numeroInserzioniPositive) {
		this.numeroInserzioniPositive = numeroInserzioniPositive;
	}

	@Column(name = "NumeroInserzioniTotali", nullable = false)
	public int getNumeroInserzioniTotali() {
		return this.numeroInserzioniTotali;
	}

	public void setNumeroInserzioniTotali(int numeroInserzioniTotali) {
		this.numeroInserzioniTotali = numeroInserzioniTotali;
	}

	@Column(name = "NumeroValutazioniPositive", nullable = false)
	public int getNumeroValutazioniPositive() {
		return this.numeroValutazioniPositive;
	}

	public void setNumeroValutazioniPositive(int numeroValutazioniPositive) {
		this.numeroValutazioniPositive = numeroValutazioniPositive;
	}

	@Column(name = "NumeroValutazioniTotali", nullable = false)
	public int getNumeroValutazioniTotali() {
		return this.numeroValutazioniTotali;
	}

	public void setNumeroValutazioniTotali(int numeroValutazioniTotali) {
		this.numeroValutazioniTotali = numeroValutazioniTotali;
	}

}
