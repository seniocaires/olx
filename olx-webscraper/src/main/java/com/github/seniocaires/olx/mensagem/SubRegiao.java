package com.github.seniocaires.olx.mensagem;

public class SubRegiao {

	private String nome;

	private String link;

	/*--------------------------------------------
	 * Construtores
	 * --------------------------------------------
	 */

	public SubRegiao(String nome, String link) {
		this.nome = nome;
		this.link = link;
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

	/*--------------------------------------------
	 * ToString/Equals/HashCode
	 * --------------------------------------------
	 */

	@Override
	public String toString() {
		return "Sub Região [link=" + link + "]";
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
		SubRegiao other = (SubRegiao) obj;
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		return true;
	}
}
