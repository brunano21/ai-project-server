package hibernate;

// Generated 28-apr-2014 22.53.51 by Hibernate Tools 3.4.0.CR1

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
 * Sottocategoria generated by hbm2java
 */
@Entity
@Table(name = "sottocategoria", catalog = "supermercati", uniqueConstraints = @UniqueConstraint(columnNames = "Nome"))
public class Sottocategoria implements java.io.Serializable {

	private Integer idSottocategoria;
	private Categoria categoria;
	private String nome;
	private Set prodottos = new HashSet(0);

	public Sottocategoria() {
	}

	public Sottocategoria(Categoria categoria, String nome, Set prodottos) {
		this.categoria = categoria;
		this.nome = nome;
		this.prodottos = prodottos;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID_Sottocategoria", unique = true, nullable = false)
	public Integer getIdSottocategoria() {
		return this.idSottocategoria;
	}

	public void setIdSottocategoria(Integer idSottocategoria) {
		this.idSottocategoria = idSottocategoria;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_Categoria")
	public Categoria getCategoria() {
		return this.categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	@Column(name = "Nome", unique = true, length = 45)
	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sottocategoria")
	public Set getProdottos() {
		return this.prodottos;
	}

	public void setProdottos(Set prodottos) {
		this.prodottos = prodottos;
	}

}
