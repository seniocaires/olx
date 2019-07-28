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
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.github.seniocaires.olx.configuracao.Configuracao;
import com.github.seniocaires.olx.mensagem.Mensagem;
import com.github.seniocaires.olx.mensagem.Anuncio;

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

			do {

				pagina = webClient.getPage(mensagem.getSubCategoria().getLink() + "?" + "o=" + paginaAtual + "&" + Configuracao.ORDENACAO_MAIS_RECENTES);

				htmlNavegacaoAnuncios = pagina.getFirstByXPath("//div[@class='section_OLXad-list ']"); // XPath absoluto
				for (HtmlElement htmlAnuncio : htmlNavegacaoAnuncios.getElementsByTagName("li")) {

					if ("item".equals(htmlAnuncio.getAttribute("class"))) {
						List<HtmlElement> htmlLinks = htmlAnuncio.getElementsByTagName("a");

						if (htmlLinks != null && !htmlLinks.isEmpty()) {
							htmlAnuncio = (HtmlListItem) htmlAnuncio;
							HtmlAnchor linkItem = (HtmlAnchor) htmlAnuncio.getElementsByTagName("a").get(0);
							String nome = linkItem.getAttribute("title");
							String link = linkItem.getAttribute("href");

							BigDecimal preco = null;
							BigDecimal precoAnterior = null;

							HtmlDivision divPrecos = linkItem.getFirstByXPath("div[@class='col-3 ']"); // XPath relativo
							if (divPrecos != null) {

								HtmlParagraph paragraphPreco = (HtmlParagraph) divPrecos.getFirstByXPath("p[@class='OLXad-list-price']"); // XPath relativo
								HtmlParagraph paragraphPrecoAnterior = (HtmlParagraph) divPrecos.getFirstByXPath("p[@class='OLXad-list-old-price']"); // XPath relativo

								if (paragraphPreco != null) {
									try {
										preco = new BigDecimal(paragraphPreco.asText().replaceAll("[^\\d.]", "").trim());
									} catch (NumberFormatException e) {
										preco = null;
									}
								}
								if (paragraphPrecoAnterior != null) {
									try {
										precoAnterior = new BigDecimal(paragraphPrecoAnterior.asText().replaceAll("[^\\d.]", "").trim());
									} catch (NumberFormatException e) {
										precoAnterior = null;
									}
								}
							}
							Mensagem mensagemSaida = new Mensagem(mensagem);
							mensagemSaida.setAnuncio(new Anuncio(nome, link, preco, precoAnterior));
							mensagensSaida.add(mensagemSaida);
						}
					}
				}

				try {
					htmlPaginacao = pagina.getFirstByXPath("//div[@class='module_pagination']");
					htmlProximaPagina = htmlPaginacao.getFirstByXPath("//li[@class='item next']");
					paginaAtual++;
				} catch (NullPointerException e) {
					htmlProximaPagina = null;
				}

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
