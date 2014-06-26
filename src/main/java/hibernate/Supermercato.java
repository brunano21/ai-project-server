package hibernate;

// Generated 26-giu-2014 18.20.54 by Hibernate Tools 3.6.0

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Supermercato generated by hbm2java
 */
@Entity
@Table(name = "supermercato", catalog = "supermercati")
public class Supermercato implements java.io.Serializable {

	private Integer idSupermercato;
	private String nome;
	private String indirizzo;
	private String comune;
	private String provincia;
	private float latitudine;
	private float longitudine;
	private Set inserziones = new HashSet(0);

	public Supermercato() {
	}

	public Supermercato(String nome, String indirizzo, String comune,
			String provincia, float latitudine, float longitudine) {
		this.nome = nome;
		this.indirizzo = indirizzo;
		this.comune = comune;
		this.provincia = provincia;
		this.latitudine = latitudine;
		this.longitudine = longitudine;
	}

	public Supermercato(String nome, String indirizzo, String comune,
			String provincia, float latitudine, float longitudine,
			Set inserziones) {
		this.nome = nome;
		this.indirizzo = indirizzo;
		this.comune = comune;
		this.provincia = provincia;
		this.latitudine = latitudine;
		this.longitudine = longitudine;
		this.inserziones = inserziones;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID_Supermercato", unique = true, nullable = false)
	public Integer getIdSupermercato() {
		return this.idSupermercato;
	}

	public void setIdSupermercato(Integer idSupermercato) {
		this.idSupermercato = idSupermercato;
	}

	@Column(name = "Nome", nullable = false, length = 45)
	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "Indirizzo", nullable = false, length = 50)
	public String getIndirizzo() {
		return this.indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	@Column(name = "Comune", nullable = false, length = 30)
	public String getComune() {
		return this.comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	@Column(name = "Provincia", nullable = false, length = 45)
	public String getProvincia() {
		return this.provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	@Column(name = "Latitudine", nullable = false, precision = 12, scale = 0)
	public float getLatitudine() {
		return this.latitudine;
	}

	public void setLatitudine(float latitudine) {
		this.latitudine = latitudine;
	}

	@Column(name = "Longitudine", nullable = false, precision = 12, scale = 0)
	public float getLongitudine() {
		return this.longitudine;
	}

	public void setLongitudine(float longitudine) {
		this.longitudine = longitudine;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "supermercato")
	public Set getInserziones() {
		return this.inserziones;
	}

	public void setInserziones(Set inserziones) {
		this.inserziones = inserziones;
	}

}
