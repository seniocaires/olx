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

	private SubRegiao subRegiao;

	public Webscraper(SubRegiao subRegiao) {
		this.subRegiao = subRegiao;
	}

	public List<Categoria> processar() {

		List<Categoria> categorias = new ArrayList<Categoria>();

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

			final HtmlPage pagina = webClient.getPage(subRegiao.getLink());

			final List<HtmlDivision> htmlNavegacaoCategorias = pagina.getByXPath("//div[@class='search-category-nav']");
			for (HtmlElement htmlCategoria : htmlNavegacaoCategorias.get(0).getElementsByTagName("li")) {

				List<HtmlElement> htmlLinks = htmlCategoria.getElementsByTagName("a");

				if (!htmlLinks.isEmpty()) {
					htmlCategoria = (HtmlListItem) htmlCategoria;
					String nome = htmlCategoria.asText();
					String link = htmlCategoria.getElementsByTagName("a").get(0).getAttribute("href");
					Categoria categoria = new Categoria(nome, link, subRegiao.getUf());
					LOGGER.fine(categoria.toString());
					categorias.add(categoria);
				}
			}

		} catch (FailingHttpStatusCodeException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		} catch (MalformedURLException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}

		return categorias;
	}
}
