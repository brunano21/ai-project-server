package hibernate;

// Generated 26-giu-2014 10.57.23 by Hibernate Tools 3.6.0

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

	private int idListaDesideri;
	private Utente utente;
	private String nomeListaDesideri;
	private Set listaDesideriProdottis = new HashSet(0);

	public ListaDesideri() {
	}

	public ListaDesideri(int idListaDesideri, Utente utente,
			String nomeListaDesideri) {
		this.idListaDesideri = idListaDesideri;
		this.utente = utente;
		this.nomeListaDesideri = nomeListaDesideri;
	}

	public ListaDesideri(int idListaDesideri, Utente utente,
			String nomeListaDesideri, Set listaDesideriProdottis) {
		this.idListaDesideri = idListaDesideri;
		this.utente = utente;
		this.nomeListaDesideri = nomeListaDesideri;
		this.listaDesideriProdottis = listaDesideriProdottis;
	}

	@Id
	@Column(name = "ID_ListaDesideri", unique = true, nullable = false)
	public int getIdListaDesideri() {
		return this.idListaDesideri;
	}

	public void setIdListaDesideri(int idListaDesideri) {
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
