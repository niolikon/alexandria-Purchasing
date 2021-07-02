package org.niolikon.alexandria.purchasing.system;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
@EnableMongoAuditing
@ConfigurationProperties("alexandria.purchasing-mongodb")
public class MongoClientConfig extends AbstractMongoClientConfiguration {
	
	private String connectionUrl;
	
	@Bean
	public MongoClient mongoClient() {
		return MongoClients.create(getConnectionUrl());
	}

	@Override
	protected String getDatabaseName() {
		return "alexandriaPurchasing";
	}
	
	@Override
	public boolean autoIndexCreation() {
		return true;
	}
	
	void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}
	
	String getConnectionUrl() {
		return this.connectionUrl;
	}
}
