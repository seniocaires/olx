package com.github.seniocaires.olx.mongo;

import java.util.Arrays;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.github.seniocaires.olx.configuracao.Configuracao;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

public class MongoDB {

	private static final MongoDB INSTANCE = new MongoDB();

	private final Datastore datastore;

	private MongoDB() {

		MongoCredential credential = MongoCredential.createScramSha1Credential(Configuracao.MONGO_USUARIO, Configuracao.MONGO_DATA_BASE, Configuracao.MONGO_SENHA.toCharArray());

		MongoClientOptions mongoOptions = MongoClientOptions.builder().socketTimeout(60000) // Wait 1m for a query to finish, https://jira.mongodb.org/browse/JAVA-1076
				.connectTimeout(15000) // Try the initial connection for 15s, http://blog.mongolab.com/2013/10/do-you-want-a-timeout/
				.maxConnectionIdleTime(600000) // Keep idle connections for 10m, so we discard failed connections quickly
				.readPreference(ReadPreference.primaryPreferred()) // Read from the primary, if not available use a secondary
				.build();
		MongoClient mongoClient;
		mongoClient = new MongoClient(new ServerAddress(Configuracao.MONGO_URL, Configuracao.MONGO_PORTA), Arrays.asList(credential), mongoOptions);

		mongoClient.setWriteConcern(WriteConcern.SAFE);
		datastore = new Morphia().mapPackage("com.github.seniocaires.olx.entidades").createDatastore(mongoClient, Configuracao.MONGO_DATA_BASE);
		datastore.ensureIndexes();
		datastore.ensureCaps();
	}

	public static MongoDB instance() {
		return INSTANCE;
	}

	// Creating the mongo connection is expensive - (re)use a singleton for performance reasons.
	// Both the underlying Java driver and Datastore are thread safe.
	public Datastore getDatabase() {
		return datastore;
	}
}
