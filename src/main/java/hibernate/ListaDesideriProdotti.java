package hibernate;

// Generated 19-dic-2013 19.14.54 by Hibernate Tools 3.4.0.CR1

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
 * ListaDesideriProdotti generated by hbm2java
 */
@Entity
@Table(name = "lista_desideri_prodotti", catalog = "supermercati")
public class ListaDesideriProdotti implements java.io.Serializable {

	private ListaDesideriProdottiId id;
	private Prodotto prodotto;
	private ListaDesideri listaDesideri;

	public ListaDesideriProdotti() {
	}

	public ListaDesideriProdotti(ListaDesideriProdottiId id, Prodotto prodotto,
			ListaDesideri listaDesideri) {
		this.id = id;
		this.prodotto = prodotto;
		this.listaDesideri = listaDesideri;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "idListaDesideri", column = @Column(name = "ID_ListaDesideri", nullable = false)),
			@AttributeOverride(name = "idProdotto", column = @Column(name = "ID_Prodotto", nullable = false)),
			@AttributeOverride(name = "descrizione", column = @Column(name = "Descrizione", nullable = false, length = 45)) })
	public ListaDesideriProdottiId getId() {
		return this.id;
	}

	public void setId(ListaDesideriProdottiId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_Prodotto", nullable = false, insertable = false, updatable = false)
	public Prodotto getProdotto() {
		return this.prodotto;
	}

	public void setProdotto(Prodotto prodotto) {
		this.prodotto = prodotto;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ListaDesideri", nullable = false, insertable = false, updatable = false)
	public ListaDesideri getListaDesideri() {
		return this.listaDesideri;
	}

	public void setListaDesideri(ListaDesideri listaDesideri) {
		this.listaDesideri = listaDesideri;
	}

}
