package hibernate;

// Generated 10-mag-2014 10.34.39 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ListaDesideriProdottiId generated by hbm2java
 */
@Embeddable
public class ListaDesideriProdottiId implements java.io.Serializable {

	private int idElemento;
	private int idListaDesideri;

	public ListaDesideriProdottiId() {
	}

	public ListaDesideriProdottiId(int idElemento, int idListaDesideri) {
		this.idElemento = idElemento;
		this.idListaDesideri = idListaDesideri;
	}

	@Column(name = "ID_Elemento", nullable = false)
	public int getIdElemento() {
		return this.idElemento;
	}

	public void setIdElemento(int idElemento) {
		this.idElemento = idElemento;
	}

	@Column(name = "ID_ListaDesideri", nullable = false)
	public int getIdListaDesideri() {
		return this.idListaDesideri;
	}

	public void setIdListaDesideri(int idListaDesideri) {
		this.idListaDesideri = idListaDesideri;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ListaDesideriProdottiId))
			return false;
		ListaDesideriProdottiId castOther = (ListaDesideriProdottiId) other;

		return (this.getIdElemento() == castOther.getIdElemento())
				&& (this.getIdListaDesideri() == castOther.getIdListaDesideri());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getIdElemento();
		result = 37 * result + this.getIdListaDesideri();
		return result;
	}

}
