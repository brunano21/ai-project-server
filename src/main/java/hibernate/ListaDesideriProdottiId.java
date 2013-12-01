package hibernate;

// Generated 21-nov-2013 16.24.11 by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ListaDesideriProdottiId generated by hbm2java
 */
@Embeddable
public class ListaDesideriProdottiId implements java.io.Serializable {

	private int idListaDesideri;
	private int idProdotto;
	private String descrizione;

	public ListaDesideriProdottiId() {
	}

	public ListaDesideriProdottiId(int idListaDesideri, int idProdotto,
			String descrizione) {
		this.idListaDesideri = idListaDesideri;
		this.idProdotto = idProdotto;
		this.descrizione = descrizione;
	}

	@Column(name = "ID_ListaDesideri", nullable = false)
	public int getIdListaDesideri() {
		return this.idListaDesideri;
	}

	public void setIdListaDesideri(int idListaDesideri) {
		this.idListaDesideri = idListaDesideri;
	}

	@Column(name = "ID_Prodotto", nullable = false)
	public int getIdProdotto() {
		return this.idProdotto;
	}

	public void setIdProdotto(int idProdotto) {
		this.idProdotto = idProdotto;
	}

	@Column(name = "Descrizione", nullable = false, length = 45)
	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ListaDesideriProdottiId))
			return false;
		ListaDesideriProdottiId castOther = (ListaDesideriProdottiId) other;

		return (this.getIdListaDesideri() == castOther.getIdListaDesideri())
				&& (this.getIdProdotto() == castOther.getIdProdotto())
				&& ((this.getDescrizione() == castOther.getDescrizione()) || (this
						.getDescrizione() != null
						&& castOther.getDescrizione() != null && this
						.getDescrizione().equals(castOther.getDescrizione())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getIdListaDesideri();
		result = 37 * result + this.getIdProdotto();
		result = 37
				* result
				+ (getDescrizione() == null ? 0 : this.getDescrizione()
						.hashCode());
		return result;
	}

}