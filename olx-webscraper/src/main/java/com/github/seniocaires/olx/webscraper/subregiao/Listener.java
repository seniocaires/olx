package com.github.seniocaires.olx.webscraper.subregiao;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;

import com.github.seniocaires.olx.broker.Broker;
import com.github.seniocaires.olx.configuracao.Configuracao;
import com.github.seniocaires.olx.mensagem.Mensagem;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class Listener implements MessageListener {

	private static final Logger LOGGER = Logger.getLogger(Listener.class.getName());

	public void onMessage(Message message) {
		try {
			Gson gson = new Gson();
			Mensagem mensagem = gson.fromJson(((TextMessage) message).getText(), Mensagem.class);
			processarMensagem(mensagem);
		} catch (JsonSyntaxException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		} catch (JMSException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	private void processarMensagem(Mensagem mensagemEntrada) throws JMSException {

		Broker broker = new Broker(Configuracao.BROKER_URL, Configuracao.BROKER_USUARIO, Configuracao.BROKER_SENHA);
		Destination destination = broker.getSession().createQueue(Configuracao.BROKER_QUEE_SUBREGIAO);
		MessageProducer messageProducer = broker.getSession().createProducer(destination);

		Gson gson = new Gson();
		Webscraper scraper = new Webscraper(mensagemEntrada);
		List<Mensagem> mensagensSaida;
		ExecutorService executor = Executors.newCachedThreadPool();
		Callable<Object> task = new Callable<Object>() {
			public Object call() {
				return scraper.processar();
			}
		};
		Future<Object> future = executor.submit(task);
		try {
			mensagensSaida = (List<Mensagem>) future.get(Configuracao.TIMEOUT_PROCESSAMENTO_SEGUNDOS, TimeUnit.SECONDS);
			for (Mensagem mensagemSaida : mensagensSaida) {
				TextMessage textMessage = broker.getSession().createTextMessage(gson.toJson(mensagemSaida));
				messageProducer.send(textMessage);
			}

		} catch (TimeoutException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			mensagemEntrada.setStackTrace(e.getStackTrace());
			broker.enviarMensagemEhFecharConexao(gson.toJson(mensagemEntrada), Configuracao.BROKER_QUEE_TIMEOUT);
		} catch (InterruptedException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			mensagemEntrada.setStackTrace(e.getStackTrace());
			broker.enviarMensagemEhFecharConexao(gson.toJson(mensagemEntrada), Configuracao.BROKER_QUEE_ERROS);
		} catch (ExecutionException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			mensagemEntrada.setStackTrace(e.getStackTrace());
			broker.enviarMensagemEhFecharConexao(gson.toJson(mensagemEntrada), Configuracao.BROKER_QUEE_ERROS);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			mensagemEntrada.setStackTrace(e.getStackTrace());
			broker.enviarMensagemEhFecharConexao(gson.toJson(mensagemEntrada), Configuracao.BROKER_QUEE_ERROS);
		} finally {
			broker.close(messageProducer);
			future.cancel(true);
		}
	}
}
