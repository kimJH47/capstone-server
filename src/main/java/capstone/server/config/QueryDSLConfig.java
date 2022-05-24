package capstone.server.config;


import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@EnableJpaAuditing
@Configuration
public class QueryDSLConfig {

    @PersistenceContext
    private EntityManager em;
    @Bean
    public JPAQueryFactory jpqlQueryFactory() {
        return new JPAQueryFactory(em);
    }

}
