package it.valeriovaudi.cqrs.config;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.convert.CassandraConverter;
import org.springframework.data.cassandra.core.CassandraTemplate;

/**
 * Created by vvaudi on 22/04/17.
 */

@Slf4j
//@Configuration
//@EnableConfigurationProperties(CassandraProperties.class)
public class CassandraConfig {

    private final CassandraProperties properties;

    private final PropertyResolver propertyResolver;


    public CassandraConfig(Environment environment,CassandraProperties properties) {
        this.properties = properties;
        this.propertyResolver = new RelaxedPropertyResolver(environment,
                "spring.data.cassandra.");
    }

    @Bean("commandSession")
    public CassandraSessionFactoryBean commandSession(Cluster cluster, CassandraConverter converter)
            throws Exception {
        String[] split = properties.getKeyspaceName().split(",");
        log.info("********************************************");
        log.info("********************************************");
        log.info("********************************************");
        log.info("********************************************");
        log.info("********************************************");
        log.info("********************************************");
        log.info(split[0]);

        CassandraSessionFactoryBean session = new CassandraSessionFactoryBean();
        session.setCluster(cluster);
        session.setConverter(converter);
        session.setKeyspaceName(split[0]);
        String name = this.propertyResolver.getProperty("schemaAction", SchemaAction.NONE.name());
        SchemaAction schemaAction = SchemaAction.valueOf(name.toUpperCase());
        session.setSchemaAction(schemaAction);
        return session;
    }


    @Bean("commandTemplate")
    public CassandraTemplate commandQueryCassandraTemplate(@Qualifier("commandSession") Session session,
                                                    CassandraConverter converter) throws Exception {
        return new CassandraTemplate(session, converter);
    }




    @Bean("querySession")
    public CassandraSessionFactoryBean querySession(Cluster cluster, CassandraConverter converter)
            throws Exception {
        String[] split = properties.getKeyspaceName().split(",");
        log.info("********************************************");
        log.info("********************************************");
        log.info("********************************************");
        log.info("********************************************");
        log.info("********************************************");
        log.info("********************************************");
        log.info(split[1]);

        CassandraSessionFactoryBean session = new CassandraSessionFactoryBean();
        session.setCluster(cluster);
        session.setConverter(converter);
        session.setKeyspaceName(split[1]);
        String name = this.propertyResolver.getProperty("schemaAction", SchemaAction.NONE.name());
        SchemaAction schemaAction = SchemaAction.valueOf(name.toUpperCase());
        session.setSchemaAction(schemaAction);
        return session;
    }


    @Bean("queryTemplate")
    public CassandraTemplate queryCassandraTemplate(@Qualifier("querySession") Session session,
                                                           CassandraConverter converter) throws Exception {
        return new CassandraTemplate(session, converter);
    }


}
