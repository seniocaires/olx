package com.github.seniocaires.olx.configuracao;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Configuracao {

	private static final Logger LOGGER = Logger.getLogger(Configuracao.class.getName());

	public static final String UF = (System.getenv("UF") == null || "".equals(System.getenv("UF")) ? "" : System.getenv("UF"));
	public static final String BROKER_URL = (System.getenv("BROKER_URL") == null || "".equals(System.getenv("BROKER_URL")) ? "tcp://localhost:61616?jms.prefetchPolicy.all=1" : System.getenv("BROKER_URL"));
	public static final String BROKER_USUARIO = (System.getenv("BROKER_USUARIO") == null || "".equals(System.getenv("BROKER_USUARIO")) ? "" : System.getenv("BROKER_USUARIO"));
	public static final String BROKER_SENHA = (System.getenv("BROKER_SENHA") == null || "".equals(System.getenv("BROKER_SENHA")) ? "" : System.getenv("BROKER_SENHA"));
	public static final String BROKER_QUEE_REGIAO = "OLX.001.REGIAO";
	public static final String BROKER_QUEE_SUBREGIAO = "OLX.002.SUBREGIAO";
	public static final String BROKER_QUEE_CATEGORIA = "OLX.003.CATEGORIA";
	public static final String BROKER_QUEE_SUBCATEGORIA = "OLX.004.SUBCATEGORIA";
	public static final String BROKER_QUEE_TIMEOUT = "OLX.000.TIMEOUT";
	public static final String BROKER_QUEE_ERROS = "OLX.000.ERROS";
	public static final Integer TIMEOUT_PROCESSAMENTO_SEGUNDOS = (System.getenv("TIMEOUT_PROCESSAMENTO_SEGUNDOS") == null || "".equals(System.getenv("TIMEOUT_PROCESSAMENTO_SEGUNDOS")) ? 30 : Integer.valueOf(System.getenv("TIMEOUT_PROCESSAMENTO_SEGUNDOS")));
	public static final Integer SLEEP_MILISEGUNDOS = (System.getenv("SLEEP_MILISEGUNDOS") == null || "".equals(System.getenv("SLEEP_MILISEGUNDOS")) ? 60000 : Integer.valueOf(System.getenv("SLEEP_MILISEGUNDOS")));

	public Configuracao() {
	}

	{
		LOGGER.log(Level.FINE, "Configurações:");
		LOGGER.log(Level.FINE, "UF " + UF);
		LOGGER.log(Level.FINE, "BROKER_URL " + BROKER_URL);
		LOGGER.log(Level.FINE, "BROKER_QUEE_REGIAO " + BROKER_QUEE_REGIAO);
		LOGGER.log(Level.FINE, "BROKER_QUEE_SUBREGIAO " + BROKER_QUEE_SUBREGIAO);
		LOGGER.log(Level.FINE, "BROKER_QUEE_CATEGORIA " + BROKER_QUEE_CATEGORIA);
		LOGGER.log(Level.FINE, "BROKER_QUEE_SUBCATEGORIA " + BROKER_QUEE_SUBCATEGORIA);
		LOGGER.log(Level.FINE, "BROKER_QUEE_TIMEOUT " + BROKER_QUEE_TIMEOUT);
		LOGGER.log(Level.FINE, "BROKER_QUEE_ERROS " + BROKER_QUEE_ERROS);
		LOGGER.log(Level.FINE, "TIMEOUT_PROCESSAMENTO_SEGUNDOS " + TIMEOUT_PROCESSAMENTO_SEGUNDOS);
		LOGGER.log(Level.FINE, "SLEEP_MILISEGUNDOS " + SLEEP_MILISEGUNDOS);
	}
}
