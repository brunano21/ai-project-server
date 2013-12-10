package hibernate;

// Generated 10-dic-2013 16.19.44 by Hibernate Tools 3.4.0.CR1

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
	private Integer creditiAcquisiti;
	private Integer creditiPendenti;
	private Integer reputazione;
	private Boolean premium;
	private Integer contatoreInfrazioni;

	public Profilo() {
	}

	public Profilo(Utente utente, Integer creditiAcquisiti,
			Integer creditiPendenti, Integer reputazione, Boolean premium,
			Integer contatoreInfrazioni) {
		this.utente = utente;
		this.creditiAcquisiti = creditiAcquisiti;
		this.creditiPendenti = creditiPendenti;
		this.reputazione = reputazione;
		this.premium = premium;
		this.contatoreInfrazioni = contatoreInfrazioni;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_Utente", unique = true)
	public Utente getUtente() {
		return this.utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	@Column(name = "CreditiAcquisiti")
	public Integer getCreditiAcquisiti() {
		return this.creditiAcquisiti;
	}

	public void setCreditiAcquisiti(Integer creditiAcquisiti) {
		this.creditiAcquisiti = creditiAcquisiti;
	}

	@Column(name = "CreditiPendenti")
	public Integer getCreditiPendenti() {
		return this.creditiPendenti;
	}

	public void setCreditiPendenti(Integer creditiPendenti) {
		this.creditiPendenti = creditiPendenti;
	}

	@Column(name = "Reputazione")
	public Integer getReputazione() {
		return this.reputazione;
	}

	public void setReputazione(Integer reputazione) {
		this.reputazione = reputazione;
	}

	@Column(name = "Premium")
	public Boolean getPremium() {
		return this.premium;
	}

	public void setPremium(Boolean premium) {
		this.premium = premium;
	}

	@Column(name = "ContatoreInfrazioni")
	public Integer getContatoreInfrazioni() {
		return this.contatoreInfrazioni;
	}

	public void setContatoreInfrazioni(Integer contatoreInfrazioni) {
		this.contatoreInfrazioni = contatoreInfrazioni;
	}

}
