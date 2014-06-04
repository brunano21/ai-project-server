package hibernate;

// Generated Jun 3, 2014 7:29:11 PM by Hibernate Tools 3.6.0

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * ArgomentiInserzione generated by hbm2java
 */
@Entity
@Table(name = "argomenti_inserzione", catalog = "supermercati")
public class ArgomentiInserzione implements java.io.Serializable {

	private ArgomentiInserzioneId id;
	private Inserzione inserzione;
	private Argomenti argomenti;
	private Float argVal;

	public ArgomentiInserzione() {
	}

	public ArgomentiInserzione(ArgomentiInserzioneId id, Inserzione inserzione,
			Argomenti argomenti) {
		this.id = id;
		this.inserzione = inserzione;
		this.argomenti = argomenti;
	}

	public ArgomentiInserzione(ArgomentiInserzioneId id, Inserzione inserzione,
			Argomenti argomenti, Float argVal) {
		this.id = id;
		this.inserzione = inserzione;
		this.argomenti = argomenti;
		this.argVal = argVal;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "idInserzione", column = @Column(name = "ID_Inserzione", nullable = false)),
			@AttributeOverride(name = "argomento", column = @Column(name = "Argomento", nullable = false, length = 30)) })
	public ArgomentiInserzioneId getId() {
		return this.id;
	}

	public void setId(ArgomentiInserzioneId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_Inserzione", nullable = false, insertable = false, updatable = false)
	public Inserzione getInserzione() {
		return this.inserzione;
	}

	public void setInserzione(Inserzione inserzione) {
		this.inserzione = inserzione;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Argomento", nullable = false, insertable = false, updatable = false)
	public Argomenti getArgomenti() {
		return this.argomenti;
	}

	public void setArgomenti(Argomenti argomenti) {
		this.argomenti = argomenti;
	}

	@Column(name = "Arg_Val", precision = 12, scale = 0)
	public Float getArgVal() {
		return this.argVal;
	}

	public void setArgVal(Float argVal) {
		this.argVal = argVal;
	}

}
