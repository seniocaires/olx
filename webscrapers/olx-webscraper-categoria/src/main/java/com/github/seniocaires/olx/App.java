package com.github.seniocaires.olx;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;

import com.github.seniocaires.olx.broker.Broker;

public class App {

	public static void main(String[] args) throws JMSException {
		Broker consumer = new Broker(Configuracao.BROKER_URL, Configuracao.BROKER_USUARIO, Configuracao.BROKER_SENHA);
		Destination destination = consumer.getSession().createQueue(Configuracao.BROKER_QUEE_SUBREGIAO);
		MessageConsumer messageConsumer = consumer.getSession().createConsumer(destination);
		messageConsumer.setMessageListener(new Listener());
	}

}
