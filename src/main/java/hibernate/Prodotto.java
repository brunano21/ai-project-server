package hibernate;

// Generated Jun 3, 2014 7:29:11 PM by Hibernate Tools 3.6.0

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
import javax.persistence.UniqueConstraint;

/**
 * Prodotto generated by hbm2java
 */
@Entity
@Table(name = "prodotto", catalog = "supermercati", uniqueConstraints = @UniqueConstraint(columnNames = "CodiceBarre"))
public class Prodotto implements java.io.Serializable {

	private Integer idProdotto;
	private Sottocategoria sottocategoria;
	private long codiceBarre;
	private String descrizione;
	private Set inserziones = new HashSet(0);

	public Prodotto() {
	}

	public Prodotto(Sottocategoria sottocategoria, long codiceBarre,
			String descrizione) {
		this.sottocategoria = sottocategoria;
		this.codiceBarre = codiceBarre;
		this.descrizione = descrizione;
	}

	public Prodotto(Sottocategoria sottocategoria, long codiceBarre,
			String descrizione, Set inserziones) {
		this.sottocategoria = sottocategoria;
		this.codiceBarre = codiceBarre;
		this.descrizione = descrizione;
		this.inserziones = inserziones;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID_Prodotto", unique = true, nullable = false)
	public Integer getIdProdotto() {
		return this.idProdotto;
	}

	public void setIdProdotto(Integer idProdotto) {
		this.idProdotto = idProdotto;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SottoCategoria", nullable = false)
	public Sottocategoria getSottocategoria() {
		return this.sottocategoria;
	}

	public void setSottocategoria(Sottocategoria sottocategoria) {
		this.sottocategoria = sottocategoria;
	}

	@Column(name = "CodiceBarre", unique = true, nullable = false)
	public long getCodiceBarre() {
		return this.codiceBarre;
	}

	public void setCodiceBarre(long codiceBarre) {
		this.codiceBarre = codiceBarre;
	}

	@Column(name = "Descrizione", nullable = false, length = 45)
	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "prodotto")
	public Set getInserziones() {
		return this.inserziones;
	}

	public void setInserziones(Set inserziones) {
		this.inserziones = inserziones;
	}

}
