package hibernate;

// Generated 1-gen-2014 19.41.13 by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ListaSpesaProdottiId generated by hbm2java
 */
@Embeddable
public class ListaSpesaProdottiId implements java.io.Serializable {

	private int idSpesa;
	private int idProdotto;
	private String descrizione;

	public ListaSpesaProdottiId() {
	}

	public ListaSpesaProdottiId(int idSpesa, int idProdotto, String descrizione) {
		this.idSpesa = idSpesa;
		this.idProdotto = idProdotto;
		this.descrizione = descrizione;
	}

	@Column(name = "ID_Spesa", nullable = false)
	public int getIdSpesa() {
		return this.idSpesa;
	}

	public void setIdSpesa(int idSpesa) {
		this.idSpesa = idSpesa;
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
		if (!(other instanceof ListaSpesaProdottiId))
			return false;
		ListaSpesaProdottiId castOther = (ListaSpesaProdottiId) other;

		return (this.getIdSpesa() == castOther.getIdSpesa())
				&& (this.getIdProdotto() == castOther.getIdProdotto())
				&& ((this.getDescrizione() == castOther.getDescrizione()) || (this
						.getDescrizione() != null
						&& castOther.getDescrizione() != null && this
						.getDescrizione().equals(castOther.getDescrizione())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getIdSpesa();
		result = 37 * result + this.getIdProdotto();
		result = 37
				* result
				+ (getDescrizione() == null ? 0 : this.getDescrizione()
						.hashCode());
		return result;
	}

}
