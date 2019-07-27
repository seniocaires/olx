package com.github.seniocaires.olx.broker;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.activemq.transport.TransportListener;

public class MonitorConexao implements TransportListener {

	private Logger LOGGER = Logger.getLogger(MonitorConexao.class.getName());

	public void onCommand(Object command) {
	}

	public void onException(IOException error) {
		LOGGER.log(Level.SEVERE, error.getMessage(), error);
		System.exit(137);
	}

	public void transportInterupted() {
	}

	public void transportResumed() {
	}
}