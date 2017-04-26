package it.valeriovaudi.cqrs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@SpringBootApplication
@EnableCassandraRepositories
public class DemocqrsDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemocqrsDemoApplication.class, args);
	}
}
