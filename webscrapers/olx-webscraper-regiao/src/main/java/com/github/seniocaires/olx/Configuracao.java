package com.github.seniocaires.olx;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Configuracao {

	private static final Logger LOGGER = Logger.getLogger(Configuracao.class.getName());

	public static final String BROKER_URL = (System.getenv("BROKER_URL") == null || "".equals(System.getenv("BROKER_URL")) ? "tcp://localhost:61616?jms.prefetchPolicy.all=1" : System.getenv("BROKER_URL"));
	public static final String BROKER_USUARIO = (System.getenv("BROKER_USUARIO") == null || "".equals(System.getenv("BROKER_USUARIO")) ? "" : System.getenv("BROKER_USUARIO"));
	public static final String BROKER_SENHA = (System.getenv("BROKER_SENHA") == null || "".equals(System.getenv("BROKER_SENHA")) ? "" : System.getenv("BROKER_SENHA"));
	public static final String BROKER_QUEE_REGIAO = "OLX.001.REGIAO";
	public static final String UF = (System.getenv("UF") == null || "".equals(System.getenv("UF")) ? "" : System.getenv("UF"));
	public static final Integer SLEEP_MILISEGUNDOS = (System.getenv("SLEEP_MILISEGUNDOS") == null || "".equals(System.getenv("SLEEP_MILISEGUNDOS")) ? 60000 : Integer.valueOf(System.getenv("SLEEP_MILISEGUNDOS")));

	public Configuracao() {
	}

	{
		LOGGER.log(Level.FINE, "Configurações:");
		LOGGER.log(Level.FINE, "BROKER_URL " + BROKER_URL);
		LOGGER.log(Level.FINE, "BROKER_QUEE_REGIAO " + BROKER_QUEE_REGIAO);
		LOGGER.log(Level.FINE, "UF " + UF);
		LOGGER.log(Level.FINE, "SLEEP_MILISEGUNDOS " + SLEEP_MILISEGUNDOS);
	}
}
