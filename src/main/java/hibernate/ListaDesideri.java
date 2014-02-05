package hibernate;

// Generated 3-feb-2014 16.30.21 by Hibernate Tools 3.4.0.CR1

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

/**
 * ListaDesideri generated by hbm2java
 */
@Entity
@Table(name = "lista_desideri", catalog = "supermercati")
public class ListaDesideri implements java.io.Serializable {

	private Integer idListaDesideri;
	private Utente utente;
	private String nomeListaDesideri;
	private Set listaDesideriProdottis = new HashSet(0);

	public ListaDesideri() {
	}

	public ListaDesideri(Utente utente, String nomeListaDesideri) {
		this.utente = utente;
		this.nomeListaDesideri = nomeListaDesideri;
	}

	public ListaDesideri(Utente utente, String nomeListaDesideri,
			Set listaDesideriProdottis) {
		this.utente = utente;
		this.nomeListaDesideri = nomeListaDesideri;
		this.listaDesideriProdottis = listaDesideriProdottis;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID_ListaDesideri", unique = true, nullable = false)
	public Integer getIdListaDesideri() {
		return this.idListaDesideri;
	}

	public void setIdListaDesideri(Integer idListaDesideri) {
		this.idListaDesideri = idListaDesideri;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_Utente", nullable = false)
	public Utente getUtente() {
		return this.utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	@Column(name = "NomeListaDesideri", nullable = false, length = 45)
	public String getNomeListaDesideri() {
		return this.nomeListaDesideri;
	}

	public void setNomeListaDesideri(String nomeListaDesideri) {
		this.nomeListaDesideri = nomeListaDesideri;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "listaDesideri")
	public Set getListaDesideriProdottis() {
		return this.listaDesideriProdottis;
	}

	public void setListaDesideriProdottis(Set listaDesideriProdottis) {
		this.listaDesideriProdottis = listaDesideriProdottis;
	}

}
