package com.github.seniocaires.olx.webscraper.anuncio;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.github.seniocaires.olx.configuracao.Configuracao;
import com.github.seniocaires.olx.mensagem.Mensagem;
import com.github.seniocaires.olx.mensagem.Produto;

public class Webscraper {

	private static final Logger LOGGER = Logger.getLogger(Webscraper.class.getName());

	private Mensagem mensagem;

	public Webscraper(Mensagem mensagem) {
		this.mensagem = mensagem;
	}

	public List<Mensagem> processar() {

		List<Mensagem> mensagensSaida = new ArrayList<Mensagem>();

		try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {

			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setDoNotTrackEnabled(true);
			webClient.getOptions().setGeolocationEnabled(false);
			webClient.getOptions().setAppletEnabled(false);
			webClient.getOptions().setDownloadImages(false);
			webClient.getOptions().setJavaScriptEnabled(false);
			webClient.getOptions().setPopupBlockerEnabled(true);
			webClient.getOptions().setCssEnabled(false);
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Include.NON_NULL);

			Integer paginaAtual = 1;
			HtmlListItem htmlProximaPagina = null;
			HtmlPage pagina = null;
			HtmlDivision htmlPaginacao = null;
			HtmlDivision htmlNavegacaoAnuncios;

//			final HtmlListItem htmlPaginaAtual = htmlPaginacao.getFirstByXPath("//div[@class='item number active']");
//			paginaAtual = Integer.valueOf(htmlPaginaAtual.asText());

			do {

				pagina = webClient.getPage(mensagem.getSubCategoria().getLink() + "?" + "o=" + paginaAtual + "&" + Configuracao.ORDENACAO_MAIS_RECENTES);

				htmlNavegacaoAnuncios = pagina.getFirstByXPath("//div[@class='section_OLXad-list ']");
				for (HtmlElement htmlAnuncio : htmlNavegacaoAnuncios.getElementsByTagName("li")) {

					if ("item".equals(htmlAnuncio.getAttribute("class"))) {
						List<HtmlElement> htmlLinks = htmlAnuncio.getElementsByTagName("a");

						if (!htmlLinks.isEmpty()) {
							htmlAnuncio = (HtmlListItem) htmlAnuncio;
							HtmlElement itemLista = htmlAnuncio.getElementsByTagName("a").get(0);
							String nome = itemLista.getAttribute("title");
							String link = itemLista.getAttribute("href");

							BigDecimal preco = null;
							BigDecimal precoAnterior = null;

							HtmlDivision divPrecos = itemLista.getFirstByXPath("//div[@class='col-3 ']");

							if (divPrecos != null) {

								HtmlDivision divPreco = (HtmlDivision) divPrecos.getFirstByXPath("//div[@class='OLXad-list-price']");
								HtmlDivision divPrecoAnterior = (HtmlDivision) divPrecos.getFirstByXPath("//div[@class='OLXad-list-old-price']");
								if (divPreco != null) {
									preco = new BigDecimal(divPrecos.asText().replaceAll("[^\\d.]", "").trim());
								}
								if (divPrecoAnterior != null) {
									precoAnterior = new BigDecimal(divPrecoAnterior.asText().replaceAll("[^\\d.]", "").trim());
								}
							}
							Mensagem mensagemSaida = new Mensagem(mensagem);
							mensagemSaida.setProduto(new Produto(nome, link, preco, precoAnterior));
							mensagensSaida.add(mensagemSaida);
						}
					}
				}

				htmlPaginacao = pagina.getFirstByXPath("//div[@class='module_pagination']");
				htmlProximaPagina = htmlPaginacao.getFirstByXPath("//li[@class='item next']");
				paginaAtual++;

			} while (htmlProximaPagina != null);

		} catch (FailingHttpStatusCodeException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		} catch (MalformedURLException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}

		return mensagensSaida;
	}
}
