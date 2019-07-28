package com.github.seniocaires.olx.mensagem;

public class Mensagem {

	private Estado estado;

	private Regiao regiao;

	private SubRegiao subRegiao;

	private Categoria categoria;

	private SubCategoria subCategoria;

	private Anuncio anuncio;

	private StackTraceElement[] stackTrace;

	/*--------------------------------------------
	 * Construtores
	 * --------------------------------------------
	 */

	public Mensagem() {
	}

	public Mensagem(Mensagem mensagem) {
		this.estado = mensagem.getEstado();
		this.regiao = mensagem.getRegiao();
		this.subRegiao = mensagem.getSubRegiao();
		this.categoria = mensagem.getCategoria();
		this.subCategoria = mensagem.getSubCategoria();
		this.anuncio = mensagem.getAnuncio();
	}

	/*--------------------------------------------
	 * Get/Set
	 * --------------------------------------------
	 */

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Regiao getRegiao() {
		return regiao;
	}

	public void setRegiao(Regiao regiao) {
		this.regiao = regiao;
	}

	public SubRegiao getSubRegiao() {
		return subRegiao;
	}

	public void setSubRegiao(SubRegiao subRegiao) {
		this.subRegiao = subRegiao;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public SubCategoria getSubCategoria() {
		return subCategoria;
	}

	public void setSubCategoria(SubCategoria subCategoria) {
		this.subCategoria = subCategoria;
	}

	public Anuncio getAnuncio() {
		return anuncio;
	}

	public void setAnuncio(Anuncio anuncio) {
		this.anuncio = anuncio;
	}

	public StackTraceElement[] getStackTrace() {
		return this.stackTrace;
	}

	public void setStackTrace(StackTraceElement[] stackTrace) {
		this.stackTrace = stackTrace;
	}
}
