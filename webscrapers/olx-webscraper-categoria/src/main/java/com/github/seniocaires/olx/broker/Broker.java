package com.github.seniocaires.olx.broker;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Broker {

	private static final Logger LOGGER = Logger.getLogger(Broker.class.getName());
	private static transient ConnectionFactory factory;
	private transient Connection connection;
	private transient Session session;

	public Broker(String brokerURL, String brokerUsuario, String brokerSenha) throws JMSException {
		factory = new ActiveMQConnectionFactory(brokerURL);
		connection = factory.createConnection(brokerUsuario, brokerSenha);
		((ActiveMQConnection) connection).addTransportListener(new MonitorConexao());
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	public void close(MessageProducer messageProducer) throws JMSException {
		try {
			messageProducer.close();
			this.session.close();
			this.connection.close();
		} catch (JMSException e) {
			LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
	}

	public void close(MessageConsumer messageConsumer) throws JMSException {
		try {
			messageConsumer.close();
			this.session.close();
			this.connection.close();
		} catch (JMSException e) {
			LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
	}

	public Session getSession() {
		return this.session;
	}

	public void enviarMensagemEhFecharConexao(String mensagem, String quee) throws JMSException {
		Destination destination = this.getSession().createQueue(quee);
		MessageProducer messageProducer = this.getSession().createProducer(destination);
		TextMessage textMessage = this.getSession().createTextMessage(mensagem);
		messageProducer.send(textMessage);
		this.close(messageProducer);
	}
}
