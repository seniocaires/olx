package com.github.seniocaires.olx;

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

public class Webscraper {

	private static final Logger LOGGER = Logger.getLogger(Webscraper.class.getName());

	private Regiao regiao;

	public Webscraper(Regiao regiao) {
		this.regiao = regiao;
	}

	public List<SubRegiao> processar() {

		List<SubRegiao> subRegioes = new ArrayList<SubRegiao>();

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

			final HtmlPage pagina = webClient.getPage(regiao.getLink());

			final List<HtmlDivision> htmlNavegacaoSubRegioes = pagina.getByXPath("//div[@class='linkshelf-tabs-content zones']");
			for (HtmlElement htmlLink : htmlNavegacaoSubRegioes.get(0).getElementsByTagName("a")) {

				htmlLink = (HtmlAnchor) htmlLink;

				if (!StringUtils.isEmpty(htmlLink.getAttribute("data-id"))) {
					String nome = htmlLink.asText();
					String link = htmlLink.getAttribute("href");
					SubRegiao subRegiao = new SubRegiao(nome, link, regiao.getUf());
					LOGGER.fine(subRegiao.toString());
					subRegioes.add(subRegiao);
				}
			}

		} catch (FailingHttpStatusCodeException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		} catch (MalformedURLException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}

		return subRegioes;
	}
}
