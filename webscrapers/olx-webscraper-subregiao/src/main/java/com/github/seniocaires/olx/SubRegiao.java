package com.github.seniocaires.olx;

public class SubRegiao {

	private String nome;

	private String link;

	private String uf;

	private StackTraceElement[] stackTrace;

	/*--------------------------------------------
	 * Construtores
	 * --------------------------------------------
	 */

	public SubRegiao(String nome, String link, String uf) {
		this.nome = nome;
		this.link = link;
		this.uf = uf;
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

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public StackTraceElement[] getStackTrace() {
		return this.stackTrace;
	}

	public void setStackTrace(StackTraceElement[] stackTrace) {
		this.stackTrace = stackTrace;
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
