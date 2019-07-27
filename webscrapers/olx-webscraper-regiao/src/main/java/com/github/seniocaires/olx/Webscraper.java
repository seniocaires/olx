package com.github.seniocaires.olx;

import java.io.IOException;
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

public class Webscraper {

	private static final Logger LOGGER = Logger.getLogger(Webscraper.class.getName());

	private Categoria categoria;

	public Webscraper(Categoria categoria) {
		this.categoria = categoria;
	}

	public List<Regiao> processar() {

		List<Regiao> regioes = new ArrayList<Regiao>();

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

			final HtmlPage pagina = webClient.getPage(categoria.getLink());

			final List<HtmlDivision> htmlNavegacaoRegioes = pagina.getByXPath("//div[@class='linkshelf-tabs-content state']");
			for (HtmlElement htmlRegiao : htmlNavegacaoRegioes.get(0).getElementsByTagName("li")) {

				List<HtmlElement> htmlLinks = htmlRegiao.getElementsByTagName("a");

				if (!htmlLinks.isEmpty()) {
					htmlRegiao = (HtmlListItem) htmlRegiao;
					String nome = htmlRegiao.getElementsByTagName("a").get(0).asText();
					String link = htmlRegiao.getElementsByTagName("a").get(0).getAttribute("href");
					Regiao regiao = new Regiao(nome, link, categoria.getUf());
					LOGGER.fine(regiao.toString());
					regioes.add(regiao);
				}
			}

		} catch (FailingHttpStatusCodeException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		} catch (MalformedURLException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}

		return regioes;
	}
}
