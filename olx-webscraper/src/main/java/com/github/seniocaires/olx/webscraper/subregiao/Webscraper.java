package com.github.seniocaires.olx.webscraper.subregiao;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.github.seniocaires.olx.mensagem.Mensagem;
import com.github.seniocaires.olx.mensagem.SubRegiao;

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

			final HtmlPage pagina = webClient.getPage(mensagem.getRegiao().getLink());

			final List<HtmlDivision> htmlNavegacaoSubRegioes = pagina.getByXPath("//div[@class='linkshelf-tabs-content zones']");
			for (HtmlElement htmlLink : htmlNavegacaoSubRegioes.get(0).getElementsByTagName("a")) {

				htmlLink = (HtmlAnchor) htmlLink;

				if (!StringUtils.isEmpty(htmlLink.getAttribute("data-id"))) {
					String nome = htmlLink.asText();
					String link = htmlLink.getAttribute("href");
					Mensagem mensagemSaida = new Mensagem(mensagem);
					mensagemSaida.setSubRegiao(new SubRegiao(nome, link));
					mensagensSaida.add(mensagemSaida);
				}
			}

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
