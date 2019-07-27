package com.github.seniocaires.olx;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.JMSException;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.github.seniocaires.olx.broker.Broker;

public class App {

	private static final Logger LOGGER = Logger.getLogger(App.class.getName());

	public static void main(String[] args) {

		try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {

			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setDoNotTrackEnabled(true);
			webClient.getOptions().setGeolocationEnabled(false);
			webClient.getOptions().setAppletEnabled(false);
			webClient.getOptions().setDownloadImages(false);
			webClient.getOptions().setJavaScriptEnabled(false);
			webClient.getOptions().setPopupBlockerEnabled(true);
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Include.NON_NULL);

			while (true) {

				final HtmlPage pagina = webClient.getPage("https://" + Configuracao.UF + ".olx.com.br/");

				final List<HtmlDivision> htmlNavegacaoRegioes = pagina.getByXPath("//div[@class='linkshelf-tabs-content state']");
				for (HtmlElement htmlRegiao : htmlNavegacaoRegioes.get(0).getElementsByTagName("li")) {

					List<HtmlElement> htmlLinks = htmlRegiao.getElementsByTagName("a");

					if (!htmlLinks.isEmpty()) {
						htmlRegiao = (HtmlListItem) htmlRegiao;
						String nome = htmlRegiao.getElementsByTagName("a").get(0).asText();
						String link = htmlRegiao.getElementsByTagName("a").get(0).getAttribute("href");
						Regiao regiao = new Regiao(nome, link, Configuracao.UF);
						LOGGER.fine(regiao.toString());
						Broker producer = new Broker(Configuracao.BROKER_URL, Configuracao.BROKER_USUARIO, Configuracao.BROKER_SENHA);
						producer.enviarMensagemEhFecharConexao(mapper.writeValueAsString(regiao), Configuracao.BROKER_QUEE_REGIAO);
					}
				}

				System.gc();
				Thread.sleep(Configuracao.SLEEP_MILISEGUNDOS);
			}

		} catch (FailingHttpStatusCodeException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		} catch (MalformedURLException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		} catch (JMSException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		} catch (InterruptedException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
