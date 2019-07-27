package com.github.seniocaires.olx;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Configuracao {

	private static final Logger LOGGER = Logger.getLogger(Configuracao.class.getName());

	public static final String BROKER_URL = (System.getenv("BROKER_URL") == null || "".equals(System.getenv("BROKER_URL")) ? "tcp://localhost:61616?jms.prefetchPolicy.all=1" : System.getenv("BROKER_URL"));
	public static final String BROKER_USUARIO = (System.getenv("BROKER_USUARIO") == null || "".equals(System.getenv("BROKER_USUARIO")) ? "" : System.getenv("BROKER_USUARIO"));
	public static final String BROKER_SENHA = (System.getenv("BROKER_SENHA") == null || "".equals(System.getenv("BROKER_SENHA")) ? "" : System.getenv("BROKER_SENHA"));
	public static final String BROKER_QUEE_CATEGORIA = "OLX.003.CATEGORIA";
	public static final String BROKER_QUEE_SUBCATEGORIA = "OLX.004.SUBCATEGORIA";
	public static final String BROKER_QUEE_TIMEOUT = "OLX.000.TIMEOUT";
	public static final String BROKER_QUEE_ERROS = "OLX.000.ERROS";
	public static final Integer TIMEOUT_PROCESSAMENTO_SEGUNDOS = (System.getenv("TIMEOUT_PROCESSAMENTO_SEGUNDOS") == null || "".equals(System.getenv("TIMEOUT_PROCESSAMENTO_SEGUNDOS")) ? 15 : Integer.valueOf(System.getenv("TIMEOUT_PROCESSAMENTO_SEGUNDOS")));

	public Configuracao() {
	}

	{

		LOGGER.log(Level.FINE, "Configurações:");
		LOGGER.log(Level.FINE, "BROKER_URL " + BROKER_URL);
		LOGGER.log(Level.FINE, "BROKER_QUEE_CATEGORIA " + BROKER_QUEE_CATEGORIA);
		LOGGER.log(Level.FINE, "BROKER_QUEE_SUBCATEGORIA " + BROKER_QUEE_SUBCATEGORIA);
		LOGGER.log(Level.FINE, "BROKER_QUEE_TIMEOUT " + BROKER_QUEE_TIMEOUT);
		LOGGER.log(Level.FINE, "BROKER_QUEE_ERROS " + BROKER_QUEE_ERROS);
		LOGGER.log(Level.FINE, "TIMEOUT_PROCESSAMENTO_SEGUNDOS " + TIMEOUT_PROCESSAMENTO_SEGUNDOS);

	}
}
