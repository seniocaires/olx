package com.github.seniocaires.olx.mensagem;

import java.math.BigDecimal;

public class Produto {

	private String nome;

	private String link;

	private BigDecimal preco;

	private BigDecimal precoAnterior;

	/*--------------------------------------------
	 * Construtores
	 * --------------------------------------------
	 */

	public Produto(String nome, String link, BigDecimal preco, BigDecimal precoAnterior) {
		this.nome = nome;
		this.link = link;
		this.preco = preco;
		this.precoAnterior = precoAnterior;
	}

	/*--------------------------------------------
	 * Get/Set
	 * --------------------------------------------
	 */

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public BigDecimal getPrecoAnterior() {
		return precoAnterior;
	}

	public void setPrecoAnterior(BigDecimal precoAnterior) {
		this.precoAnterior = precoAnterior;
	}

	/*--------------------------------------------
	 * ToString/Equals/HashCode
	 * --------------------------------------------
	 */

	@Override
	public String toString() {
		return "Produto [link=" + link + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Produto other = (Produto) obj;
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		return true;
	}
}
